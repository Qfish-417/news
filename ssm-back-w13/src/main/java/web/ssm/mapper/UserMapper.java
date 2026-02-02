package web.ssm.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import web.ssm.dto.UserDTO;
import web.ssm.entity.UserEntity;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    /**
     * @Description: 根据用户登录名称获取用户信息
     * @Author: Song
     */
    @Select({"SELECT " +
            "u.user_id, u.login_code, u.login_pwd, u.user_name, u.user_image, " +
            "u.user_email, u.user_phone, u.user_type, u.dept_id, d.dept_name, u.gmt_create " +
            "FROM tb_user u " +
            "LEFT JOIN tb_dept d ON u.dept_id = d.dept_id " +
            "WHERE u.login_code = #{loginCode} OR " +
            "u.user_email = #{loginCode} OR " +
            "u.user_phone = #{loginCode} LIMIT 1"
    })
    UserDTO getByLoginCode(@Param("loginCode") String loginCode) throws Exception;

    /**
     * @Description: 获取表格数据
     * @Author: Song
     */
    @Select({"SELECT " +
            "u.user_id, u.login_code, u.login_pwd, u.user_name, u.user_image, " +
            "u.user_email, u.user_phone, u.user_type, u.dept_id, d.dept_name, u.gmt_create " +
            "FROM tb_user u " +
            "LEFT JOIN tb_dept d ON u.dept_id = d.dept_id " +
            "WHERE u.login_code LIKE CONCAT('%', #{queryText}, '%') OR u.user_name LIKE CONCAT('%', #{queryText}, '%') " +
            "ORDER BY u.user_id desc " +
            "LIMIT #{start}, #{length}"})
    List<UserDTO> list4Table(@Param("start") Integer start,
                             @Param("length") Integer length,
                             @Param("queryText") String queryText) throws Exception;

    /**
     * @Description: 获取表格数据记录的总条数
     * @Author: Song
     */
    @Select({"<script>SELECT COUNT(*) " +
            "FROM tb_user " +
            "WHERE login_code LIKE CONCAT('%', #{queryText}, '%') OR user_name LIKE CONCAT('%', #{queryText}, '%')</script>"})
    Integer count4Table(@Param("queryText") String queryText) throws Exception;

    /**
     * @Description: 添加记录
     * @Author: Song
     */
    @Insert("INSERT INTO tb_user ( " +
            "login_code, login_pwd, user_name, " +
            "user_email, user_phone, " +
            "user_type, dept_id, gmt_create) " +
            "VALUES (#{userEntity.loginCode}, #{userEntity.loginPwd}, #{userEntity.userName}, " +
            "#{userEntity.userEmail}, #{userEntity.userPhone}, " +
            "#{userEntity.userType}, #{userEntity.deptId}, #{userEntity.gmtCreate})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Integer add(@Param("userEntity") UserEntity userEntity) throws Exception;

    /**
     * @Description: 编辑记录
     * @Author: Song
     */
    @Update("UPDATE tb_user " +
            "SET user_name = #{userEntity.userName}, " +
            "user_email = #{userEntity.userEmail}, " +
            "user_phone = #{userEntity.userPhone}, " +
            "dept_id = #{userEntity.deptId} " +
            "WHERE user_id = #{userEntity.userId}")
    Integer edit(@Param("userEntity") UserEntity userEntity) throws Exception;


    /**
     * @Description: 删除记录
     * @Author: Song
     */
    @Select("SELECT u.user_id, u.login_code, u.login_pwd, u.user_name, u.user_image, u.user_email, u.user_phone, u.user_type, u.dept_id, d.dept_name, u.gmt_create " +
            "FROM tb_user u LEFT JOIN tb_dept d ON u.dept_id = d.dept_id WHERE u.user_id = #{userId}")
    UserDTO getById(@Param("userId") Integer userId) throws Exception;

    @Delete({"DELETE FROM tb_user WHERE user_id = #{userId}"})
    Integer remove(@Param("userId") Integer userId) throws Exception;

    /**
     * @Description: 更新用户头像
     * @Author: Song
     */
    @Update("UPDATE tb_user SET user_image = #{imagePath} WHERE user_id = #{userId}")
    Integer updateUserImage(@Param("userId") Integer userId, @Param("imagePath") String imagePath) throws Exception;

    /**
     * @Description: 更新用户密码
     * @Author: Song
     */
    @Update("UPDATE tb_user SET login_pwd = #{password} WHERE user_id = #{userId}")
    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password) throws Exception;
}