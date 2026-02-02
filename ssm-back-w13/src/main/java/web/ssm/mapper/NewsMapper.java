package web.ssm.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import web.ssm.dto.NewsDTO;
import web.ssm.entity.NewsEntity;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface NewsMapper {

    /**
     * @Description: 获取新闻列表（分页，支持状态筛选）
     * @Author: Song
     */
    @Select({"<script>",
            "SELECT n.news_id, n.news_title, n.news_category, n.publisher_id, n.publisher_name, ",
            "n.dept_id, n.dept_name, n.publish_time, n.news_content, n.news_image, n.news_attachment, ",
            "n.news_status, n.return_reason, n.reviewer_id, n.reviewer_name, n.review_time, ",
            "n.gmt_create, n.gmt_modified ",
            "FROM tb_news n ",
            "WHERE 1=1 ",
            "<if test='status != null and status != \"\"'> AND n.news_status = #{status} </if>",
            "<if test='category != null and category != \"\"'> AND n.news_category = #{category} </if>",
            "<if test='publisherId != null'> AND n.publisher_id = #{publisherId} </if>",
            "<if test='deptId != null'> AND n.dept_id = #{deptId} </if>",
            "<if test='queryText != null and queryText != \"\"'> AND (n.news_title LIKE CONCAT('%', #{queryText}, '%') OR n.news_content LIKE CONCAT('%', #{queryText}, '%')) </if>",
            "ORDER BY n.gmt_create DESC ",
            "LIMIT #{start}, #{length}",
            "</script>"})
    List<NewsDTO> list4Table(@Param("start") Integer start,
                             @Param("length") Integer length,
                             @Param("status") String status,
                             @Param("category") String category,
                             @Param("publisherId") Integer publisherId,
                             @Param("deptId") Integer deptId,
                             @Param("queryText") String queryText) throws Exception;

    /**
     * @Description: 获取新闻总数
     * @Author: Song
     */
    @Select({"<script>",
            "SELECT COUNT(*) FROM tb_news n WHERE 1=1 ",
            "<if test='status != null and status != \"\"'> AND n.news_status = #{status} </if>",
            "<if test='category != null and category != \"\"'> AND n.news_category = #{category} </if>",
            "<if test='publisherId != null'> AND n.publisher_id = #{publisherId} </if>",
            "<if test='deptId != null'> AND n.dept_id = #{deptId} </if>",
            "<if test='queryText != null and queryText != \"\"'> AND (n.news_title LIKE CONCAT('%', #{queryText}, '%') OR n.news_content LIKE CONCAT('%', #{queryText}, '%')) </if>",
            "</script>"})
    Integer count4Table(@Param("status") String status,
                       @Param("category") String category,
                       @Param("publisherId") Integer publisherId,
                       @Param("deptId") Integer deptId,
                       @Param("queryText") String queryText) throws Exception;

    /**
     * @Description: 根据ID获取新闻
     * @Author: Song
     */
    @Select("SELECT news_id, news_title, news_category, publisher_id, publisher_name, " +
            "dept_id, dept_name, publish_time, news_content, news_image, news_attachment, " +
            "news_status, return_reason, reviewer_id, reviewer_name, review_time, " +
            "gmt_create, gmt_modified " +
            "FROM tb_news WHERE news_id = #{newsId}")
    NewsDTO getById(@Param("newsId") Integer newsId) throws Exception;

    /**
     * @Description: 添加新闻
     * @Author: Song
     */
    @Insert("INSERT INTO tb_news (news_title, news_category, publisher_id, publisher_name, dept_id, dept_name, " +
            "publish_time, news_content, news_image, news_attachment, news_status, gmt_create, gmt_modified) " +
            "VALUES (#{newsEntity.newsTitle}, #{newsEntity.newsCategory}, #{newsEntity.publisherId}, " +
            "#{newsEntity.publisherName}, #{newsEntity.deptId}, #{newsEntity.deptName}, #{newsEntity.publishTime}, " +
            "#{newsEntity.newsContent}, #{newsEntity.newsImage}, #{newsEntity.newsAttachment}, " +
            "#{newsEntity.newsStatus}, #{newsEntity.gmtCreate}, #{newsEntity.gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "newsEntity.newsId", keyColumn = "news_id")
    Integer add(@Param("newsEntity") NewsEntity newsEntity) throws Exception;

    /**
     * @Description: 更新新闻
     * @Author: Song
     */
    @Update("UPDATE tb_news SET news_title = #{newsEntity.newsTitle}, news_category = #{newsEntity.newsCategory}, " +
            "news_content = #{newsEntity.newsContent}, news_image = #{newsEntity.newsImage}, " +
            "news_attachment = #{newsEntity.newsAttachment}, news_status = #{newsEntity.newsStatus}, " +
            "gmt_modified = #{newsEntity.gmtModified} " +
            "WHERE news_id = #{newsEntity.newsId}")
    Integer update(@Param("newsEntity") NewsEntity newsEntity) throws Exception;

    /**
     * @Description: 提交新闻（状态改为submitted）
     * @Author: Song
     */
    @Update("UPDATE tb_news SET news_status = 'submitted', gmt_modified = NOW() WHERE news_id = #{newsId}")
    Integer submit(@Param("newsId") Integer newsId) throws Exception;

    /**
     * @Description: 审核新闻（通过或退回）
     * @Author: Song
     */
    @Update("UPDATE tb_news SET news_status = #{status}, return_reason = #{returnReason}, " +
            "reviewer_id = #{reviewerId}, reviewer_name = #{reviewerName}, review_time = NOW(), gmt_modified = NOW() " +
            "WHERE news_id = #{newsId}")
    Integer review(@Param("newsId") Integer newsId,
                   @Param("status") String status,
                   @Param("returnReason") String returnReason,
                   @Param("reviewerId") Integer reviewerId,
                   @Param("reviewerName") String reviewerName) throws Exception;

    /**
     * @Description: 删除新闻
     * @Author: Song
     */
    @Delete("DELETE FROM tb_news WHERE news_id = #{newsId}")
    Integer remove(@Param("newsId") Integer newsId) throws Exception;

    /**
     * @Description: 获取已发布的新闻列表（公开接口，动态调用数据库）
     * @Author: Song
     */
    @Select("SELECT news_id, news_title, news_category, publisher_name, dept_name, publish_time, " +
            "news_image, LEFT(news_content, 200) as news_content, news_content as full_content " +
            "FROM tb_news WHERE news_status = 'published' " +
            "ORDER BY publish_time DESC LIMIT #{start}, #{length}")
    List<NewsDTO> listPublished(@Param("start") Integer start, @Param("length") Integer length) throws Exception;

    /**
     * @Description: 根据栏目获取已发布的新闻（二级页面，动态调用数据库）
     * @Author: Song
     */
    @Select("SELECT news_id, news_title, news_category, publisher_name, dept_name, publish_time, " +
            "news_image, LEFT(news_content, 200) as news_content, news_content as full_content " +
            "FROM tb_news WHERE news_status = 'published' AND news_category = #{category} " +
            "ORDER BY publish_time DESC LIMIT #{start}, #{length}")
    List<NewsDTO> listByCategory(@Param("category") String category,
                                @Param("start") Integer start,
                                @Param("length") Integer length) throws Exception;

    /**
     * @Description: 获取已发布新闻总数（用于分页）
     * @Author: Song
     */
    @Select("SELECT COUNT(*) FROM tb_news WHERE news_status = 'published'")
    Integer countPublished() throws Exception;

    /**
     * @Description: 根据栏目获取已发布新闻总数（用于分页）
     * @Author: Song
     */
    @Select("SELECT COUNT(*) FROM tb_news WHERE news_status = 'published' AND news_category = #{category}")
    Integer countByCategory(@Param("category") String category) throws Exception;

    /**
     * @Description: 每日新闻发布数量统计
     * @Author: Song
     */
    @Select("SELECT DATE(publish_time) as date, COUNT(*) as count " +
            "FROM tb_news WHERE news_status = 'published' " +
            "AND publish_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(publish_time) ORDER BY date")
    List<Map<String, Object>> getDailyNewsCount() throws Exception;

    /**
     * @Description: 新闻栏目包含新闻数量统计
     * @Author: Song
     */
    @Select("SELECT news_category as category, COUNT(*) as count " +
            "FROM tb_news WHERE news_status = 'published' " +
            "GROUP BY news_category ORDER BY count DESC")
    List<Map<String, Object>> getCategoryNewsCount() throws Exception;

    /**
     * @Description: 记者发布新闻数量统计
     * @Author: Song
     */
    @Select("SELECT publisher_name as reporter, COUNT(*) as count " +
            "FROM tb_news WHERE news_status = 'published' " +
            "GROUP BY publisher_id, publisher_name ORDER BY count DESC LIMIT 20")
    List<Map<String, Object>> getReporterNewsCount() throws Exception;

    /**
     * @Description: 部门发布新闻数量统计
     * @Author: Song
     */
    @Select("SELECT dept_name as dept, COUNT(*) as count " +
            "FROM tb_news WHERE news_status = 'published' " +
            "GROUP BY dept_id, dept_name ORDER BY count DESC")
    List<Map<String, Object>> getDeptNewsCount() throws Exception;
    /**
     * @Description: 撤回提交（状态改为草稿）
     * @Author: Song
     */
    @Update("UPDATE tb_news SET news_status = 'draft', gmt_modified = NOW() WHERE news_id = #{newsId}")
    Integer withdraw(@Param("newsId") Integer newsId) throws Exception;
}



