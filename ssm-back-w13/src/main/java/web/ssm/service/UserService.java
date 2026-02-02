package web.ssm.service;

import web.ssm.dto.OpResultDTO;
import web.ssm.dto.TableRequestDTO;
import web.ssm.dto.TableResponseDTO;
import web.ssm.dto.UserDTO;

public interface UserService {

    /**
     * @Description: 获取表格数据
     * @Author: Song
     */
    TableResponseDTO list4Table(TableRequestDTO tableRequestDTO) throws Exception;

    /**
     * @Description: 添加记录
     * @Author: Song
     */
    Integer add(UserDTO userDTO) throws Exception;

    /**
     * @Description: 编辑记录
     * @Author: Song
     */
    Integer edit(UserDTO userDTO) throws Exception;

    /**
     * @Description: 删除记录
     * @Author: Song
     */
    Integer remove(Integer userDTO) throws Exception;
    UserDTO getById(Integer userId) throws Exception;
    Integer updateUserImage(Integer userId, String imagePath) throws Exception;
    /**
     * @Description: 修改用户密码
     * @Author: Song
     */
    OpResultDTO changePassword(Integer userId, String oldPassword, String newPassword, Integer currentUserId, String currentUserType) throws Exception;
}
