package web.ssm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import web.ssm.dto.KaptchaDTO;
import web.ssm.dto.OpResultDTO;
import web.ssm.dto.TokenDTO;
import web.ssm.dto.UserDTO;
import web.ssm.mapper.UserMapper;
import web.ssm.service.LoginService;
import web.ssm.utils.JWTUtil;
import web.ssm.utils.RedisUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class LoginServiceImpl implements LoginService {

    private final Producer kaptchaProducer;
    private final RedisUtil redisUtil;
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;

    @Autowired
    public LoginServiceImpl(UserMapper userMapper, Producer kaptchaProducer, RedisUtil redisUtil, JWTUtil jwtUtil) {
        this.userMapper = userMapper;
        this.kaptchaProducer = kaptchaProducer;
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
    }

    // token有效时长，60 * 4，单位分钟，共4小时（普通登录）
    private Integer expire = 240;
    // 记住我功能token有效时长，7天
    private Integer rememberMeExpire = 7 * 24 * 60;

    @Override
    public KaptchaDTO getKaptcha() throws Exception {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage bufferedImage = kaptchaProducer.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        // 生成base64字符串
        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        String captchaBase64 = "data:image/png;base64," + base64.replaceAll("\r\n", "");
        // 存入Redis数据库
        String randomId = RandomStringUtils.random(15, true, true);
        redisUtil.set(randomId, text, 60);
        // 生成验证码返回数据
        KaptchaDTO kaptchaDTO = new KaptchaDTO();
        kaptchaDTO.setUuid(randomId);
        kaptchaDTO.setImage(captchaBase64);
        kaptchaDTO.setExpire(60);
        System.out.println(kaptchaDTO.getImage());
        return kaptchaDTO;
    }

    @Override
    public OpResultDTO loginCheck(String loginCode, String loginPwd, String randomId, String authCode, Boolean rememberMe, String deviceId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        TokenDTO tokenDTO = new TokenDTO();
        
        // 验证验证码
        if (randomId == null || redisUtil.get(randomId) == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult(JSONObject.parseObject("{ 'info': '验证码已过期，请单击刷新' }"));
            return opResult;
        } else if (!((String) redisUtil.get(randomId)).equalsIgnoreCase(authCode)) {
            opResult.setMsgResult("error");
            opResult.setObjResult(JSONObject.parseObject("{ 'info': '验证码错误' }"));
            return opResult;
        }
        
        // 检查登录失败次数（3次锁定）
        String lockKey = "login:lock:" + loginCode;
        String failCountKey = "login:fail:" + loginCode;
        Object lockStatus = redisUtil.get(lockKey);
        if (lockStatus != null) {
            opResult.setMsgResult("error");
            opResult.setObjResult(JSONObject.parseObject("{ 'info': '账号已被锁定，请稍后再试' }"));
            return opResult;
        }
        
        // 验证用户名和密码
        UserDTO userBean = this.getByLoginCode(loginCode);
        if (userBean == null || !userBean.getLoginPwd().equals(loginPwd)) {
            // 登录失败，增加失败次数
            Object failCountObj = redisUtil.get(failCountKey);
            int failCount = failCountObj == null ? 0 : Integer.parseInt(failCountObj.toString());
            failCount++;
            
            if (failCount >= 3) {
                // 锁定账号30分钟
                redisUtil.set(lockKey, "locked", 30 * 60);
                redisUtil.set(failCountKey, "0", 30 * 60);
                opResult.setMsgResult("error");
                opResult.setObjResult(JSONObject.parseObject("{ 'info': '登录失败3次，账号已锁定30分钟' }"));
            } else {
                redisUtil.set(failCountKey, String.valueOf(failCount), 60 * 60); // 1小时过期
                opResult.setMsgResult("error");
                opResult.setObjResult(JSONObject.parseObject("{ 'info': '用户名或密码错误，剩余尝试次数: " + (3 - failCount) + "' }"));
            }
            return opResult;
        }
        
        // 登录成功，清除失败计数
        redisUtil.set(failCountKey, "0", 0);
        
        // 单设备登录控制：检查是否已有其他设备登录
        String userDeviceKey = "user:device:" + userBean.getUserId();
        Object existingDeviceId = redisUtil.get(userDeviceKey);
        if (existingDeviceId != null && deviceId != null && !existingDeviceId.toString().equals(deviceId)) {
            // 有其他设备登录，踢出旧设备
            String oldTokenKey = "token:device:" + existingDeviceId.toString();
            redisUtil.set(oldTokenKey, "invalidated", 60); // 标记旧token无效
        }
        
        // 保存当前设备ID
        if (deviceId != null) {
            int tokenExpire = (rememberMe != null && rememberMe) ? rememberMeExpire : expire;
            redisUtil.set(userDeviceKey, deviceId, tokenExpire * 60);
            String tokenDeviceKey = "token:device:" + deviceId;
            redisUtil.set(tokenDeviceKey, userBean.getUserId().toString(), tokenExpire * 60);
        }
        
        // 生成签名DTO，包含账号(用户)ID
        tokenDTO.setLoginId(userBean.getUserId());
        tokenDTO.setLoginCode(userBean.getLoginCode());
        tokenDTO.setLoginName(userBean.getUserName());
        
        int tokenExpire = (rememberMe != null && rememberMe) ? rememberMeExpire : expire;
        opResult.setMsgResult("success");
        JSONObject result = new JSONObject();
        result.put("token", jwtUtil.createSign(tokenDTO.toString(), tokenExpire));
        result.put("user", userBean.getUserName());
        result.put("userId", userBean.getUserId());
        result.put("loginCode", userBean.getLoginCode());
        result.put("avatar", userBean.getUserImage());
        result.put("userType", userBean.getUserType() != null ? userBean.getUserType() : "reporter");
        opResult.setObjResult(result);
        
        return opResult;
    }

    @Override
    public OpResultDTO logout(Integer userId, String deviceId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 清除设备登录信息
            if (deviceId != null) {
                String tokenDeviceKey = "token:device:" + deviceId;
                redisUtil.set(tokenDeviceKey, "invalidated", 60);
            }
            String userDeviceKey = "user:device:" + userId;
            redisUtil.set(userDeviceKey, "", 0);
            
            opResult.setMsgResult("success");
            opResult.setObjResult("登出成功");
        } catch (Exception e) {
            opResult.setMsgResult("error");
            opResult.setObjResult("登出失败");
        }
        return opResult;
    }

    @Override
    public UserDTO getByLoginCode(String loginCode) throws Exception {
        return userMapper.getByLoginCode(loginCode);
    }
}
