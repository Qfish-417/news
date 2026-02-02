package web.ssm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import web.ssm.dto.NewsDTO;
import web.ssm.service.NewsService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class StaticPageGenerator {

    private final NewsService newsService;
    private final String uploadFolder;

    @Autowired
    public StaticPageGenerator(NewsService newsService, @Value("${spring.upload.folder}") String uploadFolder) {
        this.newsService = newsService;
        this.uploadFolder = uploadFolder;
    }

    /**
     * 生成新闻首页静态页面
     */
    public String generateNewsIndex() throws Exception {
        // 创建模板解析器
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        
        // 创建模板引擎
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        
        // 获取已发布的新闻列表（最新20条）
        List<NewsDTO> newsList = newsService.listPublished(0, 20);
        
        // 创建上下文对象
        Context context = new Context();
        context.setVariable("newsList", newsList);
        context.setVariable("title", "新闻首页");
        
        // 生成静态文件路径
        String staticFolder = uploadFolder + "static/";
        java.io.File folder = new java.io.File(staticFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        String filePath = staticFolder + "index.html";
        FileWriter writer = new FileWriter(filePath);
        
        // 生成HTML代码
        engine.process("news_index", context, writer);
        writer.close();
        
        return filePath;
    }
}

