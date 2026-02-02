package web.ssm.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import web.ssm.dto.TableRequestDTO;
import web.ssm.dto.TableResponseDTO;
import web.ssm.dto.UserDTO;
import web.ssm.service.UserService;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExportWord {

    @Value("${spring.upload.folder}")
    private String uploadFolder;

    @Autowired
    private UserService userService;

    /**
     * 创建单个用户Word文档
     * @param userId 用户ID，如果为null则使用第一个用户
     * @return Word文件
     */
    public File createTemplate(Integer userId) throws Exception {
        String wordName = "demo.ftl";

        // 获取用户数据
        UserDTO user;
        if (userId != null) {
            user = userService.getById(userId);
        } else {
            // 如果没有指定用户ID，获取第一个用户
            TableRequestDTO tableRequestDTO = new TableRequestDTO(1, 1, "");
            TableResponseDTO tableResponse = userService.list4Table(tableRequestDTO);
            @SuppressWarnings("unchecked")
            List<UserDTO> userList = (List<UserDTO>) tableResponse.getListTable();
            if (userList == null || userList.isEmpty()) {
                throw new Exception("没有可导出的用户数据");
            }
            user = userList.get(0);
        }

        if (user == null) {
            throw new Exception("用户不存在");
        }

        // 用户的数据封装
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("userCode", user.getLoginCode() != null ? user.getLoginCode() : "");
        dataMap.put("userName", user.getUserName() != null ? user.getUserName() : "");
        dataMap.put("userPhone", user.getUserPhone() != null ? user.getUserPhone() : "");
        dataMap.put("userEmail", user.getUserEmail() != null ? user.getUserEmail() : "");
        dataMap.put("deptName", user.getDeptName() != null ? user.getDeptName() : "");
        dataMap.put("userType", user.getUserType() != null && "admin".equals(user.getUserType()) ? "管理员" : "记者");

        // 格式化创建时间
        if (user.getGmtCreate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataMap.put("gmtCreate", dateFormat.format(user.getGmtCreate()));
        } else {
            dataMap.put("gmtCreate", "");
        }

        // 生成FTL模板文档
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        Template t = configuration.getTemplate(wordName, "UTF-8");

        // 生成Word文档
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        // 判断路径是否存在，不存在则创建
        File file = new File(uploadFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = uploadFolder + "用户信息_" + df.format(new Date()) + ".doc";
        File outFile = new File(name);
        Writer w = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
        t.process(dataMap, w);
        w.close();
        return outFile;
    }

    /**
     * 创建用户列表Word文档
     * @return Word文件
     */
    public File createUserListTemplate() throws Exception {
        // 获取所有用户数据
        TableRequestDTO tableRequestDTO = new TableRequestDTO(10000, 1, "");
        TableResponseDTO tableResponse = userService.list4Table(tableRequestDTO);
        @SuppressWarnings("unchecked")
        List<UserDTO> userList = (List<UserDTO>) tableResponse.getListTable();

        if (userList == null || userList.isEmpty()) {
            throw new Exception("没有可导出的用户数据");
        }

        // 准备数据
        List<Map<String, Object>> users = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (UserDTO user : userList) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getUserId() != null ? user.getUserId().toString() : "");
            userMap.put("loginCode", user.getLoginCode() != null ? user.getLoginCode() : "");
            userMap.put("userName", user.getUserName() != null ? user.getUserName() : "");
            userMap.put("userEmail", user.getUserEmail() != null ? user.getUserEmail() : "");
            userMap.put("userPhone", user.getUserPhone() != null ? user.getUserPhone() : "");
            userMap.put("userType", user.getUserType() != null && "admin".equals(user.getUserType()) ? "管理员" : "记者");
            userMap.put("deptName", user.getDeptName() != null ? user.getDeptName() : "");
            userMap.put("gmtCreate", user.getGmtCreate() != null ? dateFormat.format(user.getGmtCreate()) : "");
            users.add(userMap);
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("users", users);
        dataMap.put("totalCount", userList.size());
        dataMap.put("exportDate", dateFormat.format(new Date()));

        // 生成FTL模板文档（使用demo.ftl模板，但可以创建新的模板）
        String wordName = "demo.ftl";
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        Template t = configuration.getTemplate(wordName, "UTF-8");

        // 生成Word文档
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File(uploadFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = uploadFolder + "用户列表_" + df.format(new Date()) + ".doc";
        File outFile = new File(name);
        Writer w = new OutputStreamWriter(new FileOutputStream(outFile), "utf-8");
        t.process(dataMap, w);
        w.close();
        return outFile;
    }

    /**
     * 兼容旧方法
     */
    public File createTemplate() throws Exception {
        return createTemplate(null);
    }
}
