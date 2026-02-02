package web.ssm.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Integer userId;
    private String loginCode;
    private String loginPwd;
    private String userName;
    private String userImage;
    private String userEmail;
    private String userPhone;
    private String userType; // 角色：admin管理员, reporter记者
    private Integer deptId; // 部门ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;
}