package web.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import web.ssm.dto.*;
import web.ssm.entity.UserConverter;
import web.ssm.entity.UserEntity;
import web.ssm.mapper.UserMapper;
import web.ssm.service.UserService;
import web.ssm.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public TableResponseDTO list4Table(TableRequestDTO tableRequestDTO) throws Exception {
        Integer count = userMapper.count4Table(tableRequestDTO.getQueryText());
        List<UserDTO> listUserDTOs = userMapper.list4Table(tableRequestDTO.getStart(),
                tableRequestDTO.getPageSize(), tableRequestDTO.getQueryText());
        return new TableResponseDTO(count, listUserDTOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(UserDTO userDTO) throws Exception {
        UserEntity userEntity = UserConverter.INSTANT.entityToDto(userDTO);
        // MD5加密之后的用户初始密码
        userEntity.setLoginPwd("25d55ad283aa400af464c76d713c07ad");
        userEntity.setUserType(userDTO.getUserType() != null ? userDTO.getUserType() : "reporter");
        userEntity.setGmtCreate(new Date());
        return userMapper.add(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer edit(UserDTO userDTO) throws Exception {
        UserEntity userEntity = UserConverter.INSTANT.entityToDto(userDTO);
        return userMapper.edit(userEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer remove(Integer userId) throws Exception {
        return userMapper.remove(userId);
    }
    @Override
    public UserDTO getById(Integer userId) throws Exception {
        return userMapper.getById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserImage(Integer userId, String imagePath) throws Exception {
        return userMapper.updateUserImage(userId, imagePath);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO changePassword(Integer userId, String oldPassword, String newPassword, Integer currentUserId, String currentUserType) throws Exception {
        OpResultDTO result = new OpResultDTO();

        // 检查权限：管理员可以修改所有用户的密码，普通用户只能修改自己的密码
        if (!"admin".equals(currentUserType) && !currentUserId.equals(userId)) {
            result.setMsgResult("error");
            result.setObjResult("无权限修改其他用户的密码");
            return result;
        }

        // 获取用户信息
        UserDTO user = userMapper.getById(userId);
        if (user == null) {
            result.setMsgResult("error");
            result.setObjResult("用户不存在");
            return result;
        }

        // 如果是普通用户修改自己的密码，需要验证旧密码
        if (!"admin".equals(currentUserType)) {
            // 将旧密码转为MD5进行验证
            String encryptedOldPassword = MD5Util.encrypt(oldPassword);
            if (encryptedOldPassword == null || !encryptedOldPassword.equals(user.getLoginPwd())) {
                result.setMsgResult("error");
                result.setObjResult("原密码错误");
                return result;
            }
        }

        // 验证新密码规则（至少6位）
        if (newPassword == null || newPassword.length() < 6) {
            result.setMsgResult("error");
            result.setObjResult("新密码长度不能少于6位");
            return result;
        }

        // 将新密码转为MD5
        String encryptedNewPassword = MD5Util.encrypt(newPassword);
        if (encryptedNewPassword == null) {
            result.setMsgResult("error");
            result.setObjResult("密码加密失败");
            return result;
        }

        // 更新密码
        int rows = userMapper.updatePassword(userId, encryptedNewPassword);
        if (rows > 0) {
            result.setMsgResult("success");
            result.setObjResult("密码修改成功");
        } else {
            result.setMsgResult("error");
            result.setObjResult("密码修改失败");
        }

        return result;
    }
}
