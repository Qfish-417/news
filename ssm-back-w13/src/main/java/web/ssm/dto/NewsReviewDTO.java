package web.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsReviewDTO {
    private Integer newsId;
    private String action; // review通过, return退回
    private String returnReason; // 退回原因（退回时必填）
    private Integer reviewerId; // 审核人ID
}



