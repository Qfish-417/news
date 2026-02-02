package web.ssm.service;

import web.ssm.dto.*;
import web.ssm.entity.NewsEntity;

import java.util.List;
import java.util.Map;

public interface NewsService {
    /**
     * @Description: 获取新闻列表（分页）
     * @Author: Song
     */
    TableResponseDTO list4Table(TableRequestDTO tableReqDTO, String status, String category, Integer publisherId, Integer deptId) throws Exception;

    /**
     * @Description: 根据ID获取新闻
     * @Author: Song
     */
    NewsDTO getById(Integer newsId) throws Exception;

    /**
     * @Description: 添加新闻
     * @Author: Song
     */
    OpResultDTO add(NewsDTO newsDTO, Integer userId, String userName) throws Exception;

    /**
     * @Description: 更新新闻
     * @Author: Song
     */
    OpResultDTO update(NewsDTO newsDTO, Integer userId) throws Exception;

    /**
     * @Description: 删除新闻
     * @Author: Song
     */
    OpResultDTO remove(Integer newsId, Integer userId, String userType) throws Exception;

    /**
     * @Description: 提交新闻（草稿提交审核）
     * @Author: Song
     */
    OpResultDTO submit(Integer newsId, Integer userId) throws Exception;

    /**
     * @Description: 撤回提交
     * @Author: Song
     */
    OpResultDTO withdraw(Integer newsId, Integer userId) throws Exception;

    /**
     * @Description: 审核新闻（管理员操作）
     * @Author: Song
     */
    OpResultDTO review(NewsReviewDTO reviewDTO, Integer reviewerId, String reviewerName) throws Exception;

    /**
     * @Description: 获取已发布的新闻列表（公开接口，动态调用数据库）
     * @Author: Song
     */
    List<NewsDTO> listPublished(Integer start, Integer length) throws Exception;

    /**
     * @Description: 根据栏目获取已发布的新闻（二级页面，动态调用数据库）
     * @Author: Song
     */
    List<NewsDTO> listByCategory(String category, Integer start, Integer length) throws Exception;

    /**
     * @Description: 获取已发布新闻总数
     * @Author: Song
     */
    Integer countPublished() throws Exception;

    /**
     * @Description: 根据栏目获取已发布新闻总数
     * @Author: Song
     */
    Integer countByCategory(String category) throws Exception;

    /**
     * @Description: 获取新闻统计数据
     * @Author: Song
     */
    NewsStatisticsDTO getStatistics() throws Exception;
}



