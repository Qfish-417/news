package web.ssm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsStatisticsDTO {
    // 每日新闻发布数量（用于曲线图）
    private List<Map<String, Object>> dailyNewsCount;
    // 新闻栏目包含新闻数量（用于柱状图）
    private List<Map<String, Object>> categoryNewsCount;
    // 记者发布新闻数量统计（用于统计图）
    private List<Map<String, Object>> reporterNewsCount;
    // 部门发布新闻数量统计（用于统计图）
    private List<Map<String, Object>> deptNewsCount;
}



