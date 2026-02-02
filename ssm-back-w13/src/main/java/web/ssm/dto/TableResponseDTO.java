package web.ssm.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResponseDTO {

    private Integer total;
    private List listTable;
}
