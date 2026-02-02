package web.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import web.ssm.dto.UserDTO;
import web.ssm.service.LoginService;
import web.ssm.utils.ExportWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping(value = "/api/template")
public class ThymeleafController {

    private final LoginService loginService;

    private final ExportWord exportWord;

    @Autowired
    public ThymeleafController(LoginService loginService, ExportWord exportWord) {
        this.loginService = loginService;
        this.exportWord = exportWord;
    }

    private final Logger logger = LoggerFactory.getLogger(ThymeleafController.class);

    /**
     * @Description: 模板示例
     * @Author: Song
     */
    @RequestMapping(path = "/thymeleaf", method = RequestMethod.GET)
    public ModelAndView getThymeleaf(Model model) {
        try {
            UserDTO userDto = loginService.getByLoginCode("13454788583");
            model.addAttribute("user", userDto);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ModelAndView("default", "userModel", model);
    }

    /**
     * @Description: Word文档导出（单个用户）
     * @Author: Song
     */
    @RequestMapping(value = "/exportWord", method = RequestMethod.POST)
    public void exportWord(HttpServletResponse response, @RequestParam(required = false) Integer userId) {
        try {
            // 得到导出文档
            File outFile = exportWord.createTemplate(userId);
            InputStream fin = new FileInputStream(outFile);

            // 设置响应头
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");

            // 设置文件名
            String fileName = outFile.getName();
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ";filename*=UTF-8''" + encodedFileName);

            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            out.flush();
            out.close();
            fin.close();

            // 删除临时文件
            if (!outFile.delete()) {
                logger.warn(outFile.getName() + "删除失败");
            }
        } catch (Exception e) {
            logger.error("导出Word失败: " + e.getMessage(), e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败: " + e.getMessage());
            } catch (IOException ioException) {
                logger.error("发送错误响应失败", ioException);
            }
        }
    }

    /**
     * @Description: Word文档导出（用户列表）
     * @Author: Song
     */
    @RequestMapping(value = "/exportWordList", method = RequestMethod.POST)
    public void exportWordList(HttpServletResponse response) {
        try {
            // 得到导出文档
            File outFile = exportWord.createUserListTemplate();
            InputStream fin = new FileInputStream(outFile);

            // 设置响应头
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");

            // 设置文件名
            String fileName = outFile.getName();
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ";filename*=UTF-8''" + encodedFileName);

            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
            out.flush();
            out.close();
            fin.close();

            // 删除临时文件
            if (!outFile.delete()) {
                logger.warn(outFile.getName() + "删除失败");
            }
        } catch (Exception e) {
            logger.error("导出Word列表失败: " + e.getMessage(), e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败: " + e.getMessage());
            } catch (IOException ioException) {
                logger.error("发送错误响应失败", ioException);
            }
        }
    }
}