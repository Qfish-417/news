package web.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import web.ssm.dto.KaptchaDTO;
import web.ssm.dto.LoginDTO;
import web.ssm.dto.OpResultDTO;
import web.ssm.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * @Description: 获取验证码
     * @Author: Song
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public OpResultDTO getKaptcha() {
        OpResultDTO opResult = new OpResultDTO();
        try {
            KaptchaDTO kaptchaDTO = loginService.getKaptcha();
            opResult.setMsgResult("success");
            opResult.setObjResult(kaptchaDTO);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 用户账号登录验证
     * @Author: Song
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public OpResultDTO loginCheck(@RequestBody LoginDTO loginDTO, @RequestHeader(value = "device-id", required = false) String deviceId) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult = loginService.loginCheck(
                    loginDTO.getLoginCode(),
                    loginDTO.getLoginPwd(),
                    loginDTO.getRandomUuid(),
                    loginDTO.getAuthCode(),
                    loginDTO.getRememberMe(),
                    deviceId);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return opResult;
    }

    /**
     * @Description: 用户登出
     * @Author: Song
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public OpResultDTO logout(@RequestHeader(value = "token") String token, @RequestHeader(value = "device-id", required = false) String deviceId) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 从token中解析userId
            web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
            web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
            if (tokenDTO != null && tokenDTO.getLoginId() != null) {
                opResult = loginService.logout(tokenDTO.getLoginId(), deviceId);
            } else {
                opResult.setMsgResult("error");
                opResult.setObjResult("Token无效");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: Token验证
     * @Author: Song
     */
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public OpResultDTO tokenCheck(@RequestHeader String token) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 演示使用，返回token，拦截器已经处理过了。
            opResult.setMsgResult(token);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        opResult.setObjResult("success");;
        return opResult;
    }
}
