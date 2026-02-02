package web.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    private Integer userId;        // 要修改密码的用户ID
    private String oldPassword;    // 原密码（普通用户必填）
    private String newPassword;    // 新密码
}