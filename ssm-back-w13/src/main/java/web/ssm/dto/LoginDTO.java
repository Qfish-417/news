package web.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String loginCode;
    private String loginPwd;
    private String randomUuid;
    private String authCode;
    private Boolean rememberMe; // 记住我功能
}
