package web.ssm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Integer newsId;
    private String newsTitle; // 新闻标题
    private String newsCategory; // 新闻栏目
    private Integer publisherId; // 发布人ID
    private String publisherName; // 发布人姓名
    private Integer deptId; // 部门ID
    private String deptName; // 部门名称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime; // 发布时间
    private String newsContent; // 新闻正文（富文本）
    private String newsImage; // 新闻图片
    private String newsAttachment; // 新闻附件
    private String newsStatus; // 新闻状态：draft草稿, submitted已提交, reviewed已审核, published已发布, returned已退回
    private String returnReason; // 退回原因
    private Integer reviewerId; // 审核人ID
    private String reviewerName; // 审核人姓名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime; // 审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;
}



