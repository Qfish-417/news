package web.ssm.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;

public class HtmlGenerator {
    public static void main(String[] args) throws IOException {
        // 创建模板解析器
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        // 创建模板引擎
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        // 创建上下文对象
        Context context = new Context();
        context.setVariable("name", "John");
        context.setVariable("age", 25);
        //文件输出的路径及文件名
        FileWriter writer = new FileWriter("D:\\test.html");
        // 生成HTML代码，参数：模板，数据，文件输出流
        engine.process("person", context, writer);
        //关闭文件
        writer.close();
    }
}
