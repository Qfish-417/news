package web.ssm.controller;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.ssm.dto.*;
import web.ssm.service.NewsService;
import web.ssm.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = "/api/news")
public class NewsController {

    private final NewsService newsService;
    private final JWTUtil jwtUtil;
    private final web.ssm.mapper.UserMapper userMapper;
    private final web.ssm.utils.StaticPageGenerator staticPageGenerator;

    @Autowired
    public NewsController(NewsService newsService, JWTUtil jwtUtil, web.ssm.mapper.UserMapper userMapper, web.ssm.utils.StaticPageGenerator staticPageGenerator) {
        this.newsService = newsService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.staticPageGenerator = staticPageGenerator;
    }

    private final Logger logger = LoggerFactory.getLogger(NewsController.class);

    /**
     * @Description: 获取新闻列表（分页）
     * @Author: Song
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public TableResponseDTO list4Table(@RequestBody TableRequestDTO tableReqDTO,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) Integer publisherId,
                                       @RequestParam(required = false) Integer deptId,
                                       HttpServletRequest request) {
        TableResponseDTO tableResponseDTO = new TableResponseDTO();
        try {
            // 如果是记者，只能查看自己的新闻
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO != null && !"admin".equals(getUserType(tokenDTO.getLoginId()))) {
                publisherId = tokenDTO.getLoginId();
            }
            
            tableResponseDTO = newsService.list4Table(tableReqDTO, status, category, publisherId, deptId);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return tableResponseDTO;
    }

    /**
     * @Description: 根据ID获取新闻
     * @Author: Song
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public OpResultDTO getById(@RequestParam Integer newsId) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            NewsDTO news = newsService.getById(newsId);
            opResult.setMsgResult("success");
            opResult.setObjResult(news);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 添加新闻
     * @Author: Song
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public OpResultDTO add(@RequestBody NewsDTO newsDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            opResult = newsService.add(newsDTO, tokenDTO.getLoginId(), tokenDTO.getLoginName());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 更新新闻
     * @Author: Song
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public OpResultDTO update(@RequestBody NewsDTO newsDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            opResult = newsService.update(newsDTO, tokenDTO.getLoginId());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 删除新闻
     * @Author: Song
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public OpResultDTO remove(@RequestBody NewsDTO newsDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            String userType = getUserType(tokenDTO.getLoginId());
            opResult = newsService.remove(newsDTO.getNewsId(), tokenDTO.getLoginId(), userType);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 提交新闻（草稿提交审核）
     * @Author: Song
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public OpResultDTO submit(@RequestBody NewsDTO newsDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            opResult = newsService.submit(newsDTO.getNewsId(), tokenDTO.getLoginId());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 撤回提交
     * @Author: Song
     */
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public OpResultDTO withdraw(@RequestBody NewsDTO newsDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            opResult = newsService.withdraw(newsDTO.getNewsId(), tokenDTO.getLoginId());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 审核新闻（管理员操作）
     * @Author: Song
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public OpResultDTO review(@RequestBody NewsReviewDTO reviewDTO, HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            // 检查是否是管理员
            String userType = getUserType(tokenDTO.getLoginId());
            if (!"admin".equals(userType)) {
                opResult.setMsgResult("error");
                opResult.setObjResult("无权限，只有管理员可以审核");
                return opResult;
            }
            
            opResult = newsService.review(reviewDTO, tokenDTO.getLoginId(), tokenDTO.getLoginName());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 获取已发布的新闻列表（公开接口，二级页面动态调用数据库）
     * @Author: Song
     */
    @RequestMapping(value = "/public/list", method = RequestMethod.GET)
    public OpResultDTO listPublished(@RequestParam(defaultValue = "0") Integer start,
                                     @RequestParam(defaultValue = "10") Integer length) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            List<NewsDTO> list = newsService.listPublished(start, length);
            Integer total = newsService.countPublished();
            opResult.setMsgResult("success");
            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("total", total);
            opResult.setObjResult(result);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 根据栏目获取已发布的新闻（二级页面，动态调用数据库）
     * @Author: Song
     */
    @RequestMapping(value = "/public/category", method = RequestMethod.GET)
    public OpResultDTO listByCategory(@RequestParam String category,
                                      @RequestParam(defaultValue = "0") Integer start,
                                      @RequestParam(defaultValue = "10") Integer length) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            List<NewsDTO> list = newsService.listByCategory(category, start, length);
            Integer total = newsService.countByCategory(category);
            opResult.setMsgResult("success");
            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("total", total);
            opResult.setObjResult(result);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 获取新闻详情（三级页面，动态调用数据库）
     * @Author: Song
     */
    @RequestMapping(value = "/public/detail", method = RequestMethod.GET)
    public OpResultDTO getPublishedDetail(@RequestParam Integer newsId) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            NewsDTO news = newsService.getById(newsId);
            if (news != null && "published".equals(news.getNewsStatus())) {
                opResult.setMsgResult("success");
                opResult.setObjResult(news);
            } else {
                opResult.setMsgResult("error");
                opResult.setObjResult("新闻不存在或未发布");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 获取新闻统计数据
     * @Author: Song
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public OpResultDTO getStatistics(HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            // 检查是否是管理员
            String userType = getUserType(tokenDTO.getLoginId());
            if (!"admin".equals(userType)) {
                opResult.setMsgResult("error");
                opResult.setObjResult("无权限，只有管理员可以查看统计");
                return opResult;
            }
            
            NewsStatisticsDTO statistics = newsService.getStatistics();
            opResult.setMsgResult("success");
            opResult.setObjResult(statistics);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 生成新闻首页静态页面
     * @Author: Song
     */
    @RequestMapping(value = "/generateIndex", method = RequestMethod.POST)
    public OpResultDTO generateIndex(HttpServletRequest request) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            TokenDTO tokenDTO = getTokenFromRequest(request);
            if (tokenDTO == null) {
                opResult.setMsgResult("error");
                opResult.setObjResult("未登录");
                return opResult;
            }
            
            // 检查是否是管理员
            String userType = getUserType(tokenDTO.getLoginId());
            if (!"admin".equals(userType)) {
                opResult.setMsgResult("error");
                opResult.setObjResult("无权限，只有管理员可以生成静态页面");
                return opResult;
            }
            
            String filePath = staticPageGenerator.generateNewsIndex();
            opResult.setMsgResult("success");
            opResult.setObjResult("静态页面生成成功：" + filePath);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
            opResult.setObjResult("生成失败：" + e.getMessage());
        }
        return opResult;
    }

    private TokenDTO getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            return null;
        }
        return jwtUtil.verifyToken(token);
    }

    private String getUserType(Integer userId) {
        try {
            UserDTO user = userMapper.getById(userId);
            return user != null ? user.getUserType() : "reporter";
        } catch (Exception e) {
            logger.error("获取用户类型失败: " + e.toString());
            return "reporter";
        }
    }
}

