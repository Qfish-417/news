package web.ssm.service;

import web.ssm.dto.KaptchaDTO;
import web.ssm.dto.OpResultDTO;
import web.ssm.dto.UserDTO;
import web.ssm.entity.UserEntity;

public interface LoginService {

    /**
     * @Description: 获取验证码信息
     * @Author: Song
     */
    KaptchaDTO getKaptcha() throws Exception;

    /**
     * @Description: 登录认证
     * @Author: Song
     */
    OpResultDTO loginCheck(String loginCode, String loginPwd, String randomId, String authCode, Boolean rememberMe, String deviceId) throws Exception;

    /**
     * @Description: 根据登录账号获取用户信息
     * @Author: Song
     */
    UserDTO getByLoginCode(String loginCode) throws Exception;

    /**
     * @Description: 登出，清除设备登录信息
     * @Author: Song
     */
    OpResultDTO logout(Integer userId, String deviceId) throws Exception;
}
