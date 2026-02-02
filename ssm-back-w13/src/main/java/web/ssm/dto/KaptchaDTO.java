package web.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaptchaDTO {

    private String uuid;
    private String image;
    private int expire;
}
