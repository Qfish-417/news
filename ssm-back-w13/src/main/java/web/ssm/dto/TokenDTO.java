package web.ssm.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private Integer loginId;
    private String loginCode;
    private String loginName;

    @Override
    public String toString() {
        return "{'loginId':'" + loginId +
                "', 'loginCode':'" + loginCode +
                "', 'loginName':'" + loginName +
                "'}";
    }
}
