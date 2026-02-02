package web.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.ssm.dto.*;
import web.ssm.entity.NewsEntity;
import web.ssm.mapper.NewsMapper;
import web.ssm.mapper.UserMapper;
import web.ssm.service.NewsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper;
    private final UserMapper userMapper;

    @Autowired
    public NewsServiceImpl(NewsMapper newsMapper, UserMapper userMapper) {
        this.newsMapper = newsMapper;
        this.userMapper = userMapper;
    }

    @Override
    public TableResponseDTO list4Table(TableRequestDTO tableReqDTO, String status, String category, Integer publisherId, Integer deptId) throws Exception {
        Integer count = newsMapper.count4Table(status, category, publisherId, deptId, tableReqDTO.getQueryText());
        List<NewsDTO> list = newsMapper.list4Table(
                tableReqDTO.getStart(),
                tableReqDTO.getPageSize(),
                status,
                category,
                publisherId,
                deptId,
                tableReqDTO.getQueryText()
        );
        return new TableResponseDTO(count, list);
    }

    @Override
    public NewsDTO getById(Integer newsId) throws Exception {
        return newsMapper.getById(newsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO add(NewsDTO newsDTO, Integer userId, String userName) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setNewsTitle(newsDTO.getNewsTitle());
        newsEntity.setNewsCategory(newsDTO.getNewsCategory());
        newsEntity.setPublisherId(userId);
        newsEntity.setPublisherName(userName);
        
        // 获取用户部门信息
        UserDTO user = userMapper.getById(userId);
        if (user != null && user.getDeptId() != null) {
            newsEntity.setDeptId(user.getDeptId());
            newsEntity.setDeptName(user.getDeptName());
        }
        
        newsEntity.setPublishTime(new Date());
        newsEntity.setNewsContent(newsDTO.getNewsContent());
        newsEntity.setNewsImage(newsDTO.getNewsImage());
        newsEntity.setNewsAttachment(newsDTO.getNewsAttachment());
        newsEntity.setNewsStatus(newsDTO.getNewsStatus() != null ? newsDTO.getNewsStatus() : "draft");
        newsEntity.setGmtCreate(new Date());
        newsEntity.setGmtModified(new Date());
        
        newsMapper.add(newsEntity);
        opResult.setMsgResult("success");
        opResult.setObjResult(newsEntity.getNewsId());
        return opResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO update(NewsDTO newsDTO, Integer userId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();

        // 检查权限：记者只能修改自己的新闻
        NewsDTO existingNews = newsMapper.getById(newsDTO.getNewsId());
        if (existingNews == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("新闻不存在");
            return opResult;
        }

        // 检查是否是自己的新闻或者是管理员
        UserDTO user = userMapper.getById(userId);
        if (user == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("用户不存在");
            return opResult;
        }

        if (!"admin".equals(user.getUserType()) && !existingNews.getPublisherId().equals(userId)) {
            opResult.setMsgResult("error");
            opResult.setObjResult("无权限修改此新闻");
            return opResult;
        }

        // 状态转换逻辑
        String newStatus = newsDTO.getNewsStatus();

        // 如果是已发布的新闻被编辑，自动转为待审核状态
        if ("published".equals(existingNews.getNewsStatus())) {
            newStatus = "submitted"; // 已发布 -> 已提交（需要重新审核）
        }

        // 已退回的新闻可以编辑，编辑后转为待审核状态
        else if ("returned".equals(existingNews.getNewsStatus())) {
            newStatus = "submitted"; // 已退回 -> 已提交（重新提交审核）
        }

        // 已审核的新闻编辑后，重新提交审核
        else if ("reviewed".equals(existingNews.getNewsStatus())) {
            newStatus = "submitted"; // 已审核 -> 已提交（重新提交审核）
        }

        // 已提交的新闻可以编辑，编辑后保持已提交状态（需要重新审核）
        else if ("submitted".equals(existingNews.getNewsStatus())) {
            newStatus = "submitted"; // 保持已提交状态
        }

        // 草稿状态可以自由编辑
        else if ("draft".equals(existingNews.getNewsStatus())) {
            // 保持原有状态或使用传入的状态
            if (newStatus == null || newStatus.isEmpty()) {
                newStatus = "draft";
            }
        }

        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setNewsId(newsDTO.getNewsId());
        newsEntity.setNewsTitle(newsDTO.getNewsTitle());
        newsEntity.setNewsCategory(newsDTO.getNewsCategory());
        newsEntity.setNewsContent(newsDTO.getNewsContent());
        newsEntity.setNewsImage(newsDTO.getNewsImage());
        newsEntity.setNewsAttachment(newsDTO.getNewsAttachment());
        newsEntity.setNewsStatus(newStatus);
        newsEntity.setGmtModified(new Date());

        newsMapper.update(newsEntity);
        opResult.setMsgResult("success");
        opResult.setObjResult("更新成功");

        return opResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO remove(Integer newsId, Integer userId, String userType) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        
        NewsDTO news = newsMapper.getById(newsId);
        if (news == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("新闻不存在");
            return opResult;
        }
        
        // 管理员可以删除任何新闻，记者只能删除自己的草稿
        if (!"admin".equals(userType) && !news.getPublisherId().equals(userId)) {
            opResult.setMsgResult("error");
            opResult.setObjResult("无权限删除此新闻");
            return opResult;
        }
        
        if (!"admin".equals(userType) && !"draft".equals(news.getNewsStatus())) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能删除草稿状态的新闻");
            return opResult;
        }
        
        newsMapper.remove(newsId);
        opResult.setMsgResult("success");
        opResult.setObjResult("删除成功");
        return opResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO submit(Integer newsId, Integer userId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        
        NewsDTO news = newsMapper.getById(newsId);
        if (news == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("新闻不存在");
            return opResult;
        }
        
        if (!news.getPublisherId().equals(userId)) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能提交自己的新闻");
            return opResult;
        }
        
        if (!"draft".equals(news.getNewsStatus())) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能提交草稿状态的新闻");
            return opResult;
        }
        
        newsMapper.submit(newsId);
        opResult.setMsgResult("success");
        opResult.setObjResult("提交成功");
        return opResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO withdraw(Integer newsId, Integer userId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        
        NewsDTO news = newsMapper.getById(newsId);
        if (news == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("新闻不存在");
            return opResult;
        }
        
        if (!news.getPublisherId().equals(userId)) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能撤回自己的新闻");
            return opResult;
        }
        
        if (!"submitted".equals(news.getNewsStatus())) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能撤回已提交状态的新闻");
            return opResult;
        }
        
        // 撤回：将状态改回草稿
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setNewsId(newsId);
        newsEntity.setNewsStatus("draft");
        newsEntity.setGmtModified(new Date());
        newsMapper.update(newsEntity);
        
        opResult.setMsgResult("success");
        opResult.setObjResult("撤回成功");
        return opResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OpResultDTO review(NewsReviewDTO reviewDTO, Integer reviewerId, String reviewerName) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        
        NewsDTO news = newsMapper.getById(reviewDTO.getNewsId());
        if (news == null) {
            opResult.setMsgResult("error");
            opResult.setObjResult("新闻不存在");
            return opResult;
        }
        
        if (!"submitted".equals(news.getNewsStatus())) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只能审核已提交状态的新闻");
            return opResult;
        }
        
        String status = "reviewed"; // 默认通过
        String returnReason = null;
        
        if ("return".equals(reviewDTO.getAction())) {
            status = "returned";
            returnReason = reviewDTO.getReturnReason();
            if (returnReason == null || returnReason.trim().isEmpty()) {
                opResult.setMsgResult("error");
                opResult.setObjResult("退回时必须填写退回原因");
                return opResult;
            }
        } else if ("publish".equals(reviewDTO.getAction())) {
            status = "published";
        }
        
        newsMapper.review(reviewDTO.getNewsId(), status, returnReason, reviewerId, reviewerName);
        opResult.setMsgResult("success");
        opResult.setObjResult("审核成功");
        return opResult;
    }

    @Override
    public List<NewsDTO> listPublished(Integer start, Integer length) throws Exception {
        return newsMapper.listPublished(start, length);
    }

    @Override
    public List<NewsDTO> listByCategory(String category, Integer start, Integer length) throws Exception {
        return newsMapper.listByCategory(category, start, length);
    }

    @Override
    public Integer countPublished() throws Exception {
        return newsMapper.countPublished();
    }

    @Override
    public Integer countByCategory(String category) throws Exception {
        return newsMapper.countByCategory(category);
    }

    @Override
    public NewsStatisticsDTO getStatistics() throws Exception {
        NewsStatisticsDTO statistics = new NewsStatisticsDTO();
        statistics.setDailyNewsCount(newsMapper.getDailyNewsCount());
        statistics.setCategoryNewsCount(newsMapper.getCategoryNewsCount());
        statistics.setReporterNewsCount(newsMapper.getReporterNewsCount());
        statistics.setDeptNewsCount(newsMapper.getDeptNewsCount());
        return statistics;
    }
}



