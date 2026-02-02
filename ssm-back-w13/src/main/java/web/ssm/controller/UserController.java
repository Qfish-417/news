package web.ssm.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import web.ssm.dto.*;
import web.ssm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * @Description: 获取数据列表
     * @Author: Song
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public TableResponseDTO list4Table(@RequestBody TableRequestDTO tableReqDTO, HttpServletRequest request) {
        TableResponseDTO tableResponseDTO = new TableResponseDTO();
        try {
            // 检查权限：记者只能查看自己的信息
            web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
            String token = request.getHeader("token");
            if (token != null) {
                web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
                if (tokenDTO != null) {
                    web.ssm.dto.UserDTO currentUser = userService.getById(tokenDTO.getLoginId());
                    if (currentUser != null && "reporter".equals(currentUser.getUserType())) {
                        // 记者只能查看自己的信息，这里可以添加过滤逻辑
                        // 暂时允许查看，但编辑时会限制
                    }
                }
            }
            tableResponseDTO = userService.list4Table(tableReqDTO);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return tableResponseDTO;
    }

    /**
     * @Description: 添加记录
     * @Author: Song
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public OpResultDTO add(@RequestBody UserDTO userDTO) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult.setMsgResult("success");
            opResult.setObjResult(userService.add(userDTO));
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 编辑记录
     * @Author: Song
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public OpResultDTO update(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 检查权限：记者只能修改自己的信息
            web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
            String token = request.getHeader("token");
            if (token != null) {
                web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
                if (tokenDTO != null) {
                    web.ssm.dto.UserDTO currentUser = userService.getById(tokenDTO.getLoginId());
                    if (currentUser != null && "reporter".equals(currentUser.getUserType())) {
                        // 记者只能修改自己的信息
                        if (!tokenDTO.getLoginId().equals(userDTO.getUserId())) {
                            opResult.setMsgResult("error");
                            opResult.setObjResult("无权限修改其他用户信息");
                            return opResult;
                        }
                        // 记者不能修改角色和部门
                        userDTO.setUserType(null);
                        userDTO.setDeptId(null);
                    }
                }
            }
            System.out.println(userDTO.toString());
            opResult.setMsgResult("success");
            opResult.setObjResult(userService.edit(userDTO));
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 删除记录
     * @Author: Song
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public OpResultDTO remove(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 检查权限：只有管理员可以删除用户
            web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
            String token = request.getHeader("token");
            if (token != null) {
                web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
                if (tokenDTO != null) {
                    web.ssm.dto.UserDTO currentUser = userService.getById(tokenDTO.getLoginId());
                    if (currentUser == null || !"admin".equals(currentUser.getUserType())) {
                        opResult.setMsgResult("error");
                        opResult.setObjResult("无权限删除用户，只有管理员可以删除");
                        return opResult;
                    }
                }
            }
            opResult.setMsgResult("success");
            opResult.setObjResult(userService.remove(userDTO.getUserId()));
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public OpResultDTO getUserInfo(HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 从Token中获取用户ID（根据实际项目的Token解析方式实现）
            Integer userId = getUserIdFromToken(request);
            UserDTO user = userService.getById(userId);
            opResult.setMsgResult("success");
            opResult.setObjResult(user);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 更新用户头像
     * @Author: Song
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public OpResultDTO updateAvatar(@RequestBody UserDTO userDTO) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult.setMsgResult("success");
            opResult.setObjResult(userService.updateUserImage(userDTO.getUserId(), userDTO.getUserImage()));
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    private Integer getUserIdFromToken(HttpServletRequest request) {
        // 从Token中获取登录用户ID
        String token = request.getHeader("token");
        if (token == null) {
            throw new RuntimeException("用户未登录");
        }
        web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
        web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
        if (tokenDTO == null || tokenDTO.getLoginId() == null) {
            throw new RuntimeException("登录信息失效");
        }
        return tokenDTO.getLoginId();
    }
    /**
     * @Description: 修改密码
     * @Author: Song
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public OpResultDTO changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            // 从Token中获取当前用户信息
            web.ssm.utils.JWTUtil jwtUtil = new web.ssm.utils.JWTUtil();
            String token = request.getHeader("token");
            if (token == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("用户未登录");
                return opResult;
            }

            web.ssm.dto.TokenDTO tokenDTO = jwtUtil.verifyToken(token);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("登录信息失效");
                return opResult;
            }

            // 获取当前用户的详细信息（包括角色）
            web.ssm.dto.UserDTO currentUser = userService.getById(tokenDTO.getLoginId());
            if (currentUser == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("用户不存在");
                return opResult;
            }

            // 调用修改密码服务
            opResult = userService.changePassword(
                    changePasswordDTO.getUserId(),
                    changePasswordDTO.getOldPassword(),
                    changePasswordDTO.getNewPassword(),
                    tokenDTO.getLoginId(),
                    currentUser.getUserType()
            );

        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
            opResult.setObjResult("修改密码失败");
        }
        return opResult;
    }


}
