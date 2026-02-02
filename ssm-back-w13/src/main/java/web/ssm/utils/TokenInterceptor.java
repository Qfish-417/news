package web.ssm.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.ssm.dto.TokenDTO;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    // 添加日志记录器
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    @Autowired
    public TokenInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        logger.info("TokenInterceptor初始化完成");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录完整的请求信息
        String requestURI = request.getRequestURI();
        String requestURL = request.getRequestURL().toString();
        String method = request.getMethod();
        String token = request.getHeader("token");
        String contentType = request.getContentType();
        String clientIP = request.getRemoteAddr();

        logger.info("========== TokenInterceptor 开始处理请求 ==========");
        logger.info("请求URI: {}", requestURI);
        logger.info("请求URL: {}", requestURL);
        logger.info("请求方法: {}", method);
        logger.info("客户端IP: {}", clientIP);
        logger.info("Content-Type: {}", contentType);
        logger.info("请求头token: {}", token);
        logger.info("设备ID: {}", request.getHeader("device-id"));

        // 检查是否在白名单中
        boolean isWhiteList = checkWhiteList(requestURI, requestURL);
        logger.info("是否在白名单中: {}", isWhiteList);

        // 获取验证码，用户登录不拦截与验证
        if (isWhiteList) {
            logger.info("请求在白名单中，直接放行: {}", requestURI);
            response.setHeader("tokenstatus", "ok");
            response.setHeader("X-Interceptor-Processed", "TokenInterceptor");
            logger.info("设置响应头: tokenstatus=ok");
            logger.info("========== TokenInterceptor 放行请求 ==========\n");
            return true;
        } else {
            logger.info("请求不在白名单中，需要验证token");
            if (token == null) {
                logger.warn("请求头中未找到token，拒绝访问");
                response.setHeader("tokenstatus", "no");
                response.setHeader("X-Interceptor-Processed", "TokenInterceptor");
                response.setHeader("X-Error-Reason", "Missing token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.info("设置响应头: tokenstatus=no, 状态码: {}", HttpServletResponse.SC_UNAUTHORIZED);
                logger.info("========== TokenInterceptor 拒绝请求 ==========\n");
                return false;
            } else {
                logger.info("开始验证token: {}", token);
                // 通过客户端传递的Token参数进行验证，注意header中的属性名要小写
                TokenDTO tokenDTO = jwtUtil.verifyToken(token);
                if (tokenDTO != null && tokenDTO.getLoginId() > 0) {
                    logger.info("Token验证成功，用户ID: {}, 用户名: {}", tokenDTO.getLoginId(), tokenDTO.getLoginName());

                    // 检查单设备登录：验证设备ID
                    String deviceId = request.getHeader("device-id");
                    if (deviceId != null) {
                        logger.info("设备ID验证: {}", deviceId);
                        // 这里可以进一步验证设备ID是否匹配
                        // 暂时只验证token有效性
                    }

                    response.setHeader("tokenstatus", "ok");
                    response.setHeader("X-Interceptor-Processed", "TokenInterceptor");
                    response.setHeader("X-Authenticated-User", String.valueOf(tokenDTO.getLoginId()));
                    logger.info("设置响应头: tokenstatus=ok, 认证用户ID: {}", tokenDTO.getLoginId());
                    logger.info("========== TokenInterceptor 验证通过 ==========\n");
                    return true;
                } else {
                    logger.warn("Token验证失败，token无效或已过期");
                    response.setHeader("tokenstatus", "no");
                    response.setHeader("X-Interceptor-Processed", "TokenInterceptor");
                    response.setHeader("X-Error-Reason", "Invalid or expired token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    logger.info("设置响应头: tokenstatus=no, 状态码: {}", HttpServletResponse.SC_UNAUTHORIZED);
                    logger.info("========== TokenInterceptor 拒绝请求 ==========\n");
                    return false;
                }
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        int status = response.getStatus();
        String tokenstatus = response.getHeader("tokenstatus");

        logger.info("========== TokenInterceptor 请求完成 ==========");
        logger.info("请求URI: {}", requestURI);
        logger.info("响应状态码: {}", status);
        logger.info("tokenstatus响应头: {}", tokenstatus);

        if (ex != null) {
            logger.error("请求处理过程中发生异常: ", ex);
        }

        logger.info("========== TokenInterceptor 完成处理 ==========\n");
    }

    /**
     * 检查请求是否在白名单中
     */
    private boolean checkWhiteList(String requestURI, String requestURL) {
        // 检查URI和URL
        String[] whiteListPatterns = {
                "/api/login/kaptcha",
                "/api/login/check",
                "/api/login/logout",
                "/api/upload/image",
                "/api/upload/accessory",
                "/api/data/export",
                "/api/data/import",
                "/api/template/thymeleaf",
                "/api/news/public",
                "/file/"
        };

        for (String pattern : whiteListPatterns) {
            if (requestURI.contains(pattern) || requestURL.contains(pattern)) {
                logger.debug("匹配到白名单模式: {}", pattern);
                return true;
            }
        }

        return false;
    }
}