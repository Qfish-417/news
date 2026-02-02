package web.ssm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableRequestDTO {

    private Integer pageSize;
    private Integer currentPage;
    private String queryText;

    public TableRequestDTO(Integer pageSize, Integer currentPage, String queryText) {
        this.pageSize = pageSize == null ? 5 : pageSize;
        this.currentPage = currentPage == null ? 1 : currentPage;
        this.queryText = queryText == null ? "" : queryText;
    }

    // 根据pageSize和currentPage计算起始条数用户表格查询
    public Integer getStart() {
        return (this.currentPage - 1) * this.pageSize;
    }
}
