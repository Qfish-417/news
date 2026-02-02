package web.ssm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;
    private String loginCode;
    private String loginPwd;
    private String userName;
    private String userImage;
    private String userEmail;
    private String userPhone;
    private String userType; // 角色：admin管理员, reporter记者
    private Integer deptId; // 部门ID
    private String deptName; // 部门名称（关联查询）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;
}
