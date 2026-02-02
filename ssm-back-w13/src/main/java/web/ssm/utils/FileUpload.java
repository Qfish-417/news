package web.ssm.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import web.ssm.dto.OpResultDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/upload")
public class FileUpload {

    @Value("${spring.upload.folder}")
    private String uploadFolder;

    private final Logger logger = LoggerFactory.getLogger(FileUpload.class);

    /**
     * @Description: 图片上传，同时支持Base64和MultipartFile方式
     * @Author: Song
     */
    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public OpResultDTO uploadImage(
            @RequestParam(value = "imgBase64", required = false) String imgBase64,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        OpResultDTO opResult = new OpResultDTO();
        StringBuffer fileName = new StringBuffer();
        StringBuffer directoryName = new StringBuffer();
        StringBuffer pathName = new StringBuffer();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        fileName.append(df.format(new Date()));
        pathName.append(uploadFolder);

        try {
            // 检查是否有文件上传
            if (file != null && !file.isEmpty()) {
                logger.info("接收MultipartFile方式上传的图片");
                return uploadMultipartImage(file);
            }

            // 检查是否有Base64数据
            if (imgBase64 != null && !imgBase64.isEmpty()) {
                logger.info("接收Base64方式上传的图片");
                return uploadBase64Image(imgBase64);
            }

            opResult.setMsgResult("error");
            opResult.setObjResult("未接收到图片数据");

        } catch (Exception e) {
            logger.error("图片上传失败: {}", e.getMessage(), e);
            opResult.setMsgResult("error");
            opResult.setObjResult("上传失败: " + e.getMessage());
        }

        return opResult;
    }

    /**
     * 处理MultipartFile上传
     */
    private OpResultDTO uploadMultipartImage(MultipartFile file) throws IOException {
        OpResultDTO opResult = new OpResultDTO();

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            opResult.setMsgResult("error");
            opResult.setObjResult("只支持图片文件");
            return opResult;
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = df.format(new Date()) + suffixName;

        // 创建日期目录
        df = new SimpleDateFormat("yyyyMMdd");
        String directoryName = df.format(new Date()) + "/";

        // 完整路径
        String fullPath = uploadFolder + directoryName;
        File desFile = new File(fullPath + fileName);

        // 创建目录
        if (!desFile.getParentFile().exists()) {
            desFile.getParentFile().mkdirs();
        }

        // 保存文件
        file.transferTo(desFile);

        opResult.setMsgResult("success");
        // 返回相对路径
        opResult.setObjResult("file/" + directoryName + fileName);

        logger.info("MultipartFile图片上传成功: {}", opResult.getObjResult());
        return opResult;
    }

    /**
     * 处理Base64上传
     */
    private OpResultDTO uploadBase64Image(String imgBase64) throws IOException {
        OpResultDTO opResult = new OpResultDTO();
        StringBuffer fileName = new StringBuffer();
        StringBuffer directoryName = new StringBuffer();
        StringBuffer pathName = new StringBuffer();
        StringBuffer imgPrefix = new StringBuffer();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        fileName.append(df.format(new Date()));
        pathName.append(uploadFolder);

        // 提取Base64数据和图片格式
        String base64Data;
        String fileExtension;

        if (imgBase64.startsWith("data:image/png;base64,")) {
            imgPrefix.append("data:image/png;base64,");
            fileExtension = ".png";
            base64Data = imgBase64.substring(imgPrefix.length());
        } else if (imgBase64.startsWith("data:image/jpeg;base64,")) {
            imgPrefix.append("data:image/jpeg;base64,");
            fileExtension = ".jpeg";
            base64Data = imgBase64.substring(imgPrefix.length());
        } else if (imgBase64.startsWith("data:image/jpg;base64,")) {
            imgPrefix.append("data:image/jpg;base64,");
            fileExtension = ".jpg";
            base64Data = imgBase64.substring(imgPrefix.length());
        } else if (imgBase64.startsWith("data:image/bmp;base64,")) {
            imgPrefix.append("data:image/bmp;base64,");
            fileExtension = ".bmp";
            base64Data = imgBase64.substring(imgPrefix.length());
        } else {
            // 如果没有前缀，假设是普通的Base64字符串
            fileExtension = ".png";
            base64Data = imgBase64;
        }

        fileName.append(fileExtension);

        // 创建日期目录
        df = new SimpleDateFormat("yyyyMMdd");
        directoryName.append(df.format(new Date()) + "/");

        // 创建文件
        File file = new File(pathName.toString() + directoryName.toString() + fileName.toString());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 解码并保存Base64数据
        byte[] fileBytes = Base64.getDecoder().decode(base64Data);
        FileUtils.writeByteArrayToFile(file, fileBytes);

        opResult.setMsgResult("success");
        opResult.setObjResult("file/" + directoryName.toString() + fileName.toString());

        logger.info("Base64图片上传成功: {}", opResult.getObjResult());
        return opResult;
    }

    /**
     * @Description: 文件上传，采用FormData方式
     * 修改参数名为"file"以匹配前端
     * @Author: Song
     */
    @ResponseBody
    @RequestMapping(value = "/accessory", method = RequestMethod.POST)
    public OpResultDTO saveAccessory(@RequestParam("file") MultipartFile[] files) {
        OpResultDTO opResult = new OpResultDTO();
        StringBuffer fileName = new StringBuffer();
        StringBuffer directoryName = new StringBuffer();
        StringBuffer pathName = new StringBuffer();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        pathName.append(uploadFolder);

        try {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                if (originalName == null || originalName.isEmpty()) {
                    continue;
                }

                String suffixName = originalName.substring(originalName.lastIndexOf("."));
                fileName.append(df.format(new Date()) + suffixName);

                df = new SimpleDateFormat("yyyyMMdd");
                directoryName.append(df.format(new Date()) + "/");

                File desFile = new File(pathName.toString() + directoryName.toString() + fileName.toString());
                if (!desFile.exists()) {
                    desFile.getParentFile().mkdirs();
                }

                file.transferTo(desFile);
                logger.info("附件上传成功: {}", originalName);
            }

            opResult.setMsgResult("success");
            opResult.setObjResult("file/" + directoryName.toString() + fileName.toString());

        } catch (IOException e) {
            logger.error("附件上传失败: {}", e.getMessage(), e);
            opResult.setMsgResult("error");
            opResult.setObjResult("上传失败: " + e.getMessage());
        }

        return opResult;
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public OpResultDTO healthCheck() {
        OpResultDTO opResult = new OpResultDTO();
        opResult.setMsgResult("success");
        opResult.setObjResult("Upload service is running");
        return opResult;
    }
    // 在 FileUpload.java 中增加以下方法

    /**
     * 文件下载接口
     */
    @GetMapping("/file/{date}/{filename}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String date,
            @PathVariable String filename) throws IOException {

        String filePath = uploadFolder + date + "/" + filename;
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileContent = FileUtils.readFileToByteArray(file);

        // 获取文件扩展名
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        String contentType = getContentType(extension);

        // 设置下载头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    /**
     * 根据文件扩展名获取ContentType
     */
    private String getContentType(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 获取文件列表（可选）
     */
    @GetMapping("/files")
    public OpResultDTO getFileList() {
        OpResultDTO opResult = new OpResultDTO();
        List<Map<String, Object>> fileList = new ArrayList<>();

        File folder = new File(uploadFolder);
        if (folder.exists() && folder.isDirectory()) {
            for (File dateFolder : folder.listFiles()) {
                if (dateFolder.isDirectory()) {
                    for (File file : dateFolder.listFiles()) {
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("name", file.getName());
                        fileInfo.put("size", file.length());
                        fileInfo.put("date", new Date(file.lastModified()));
                        fileInfo.put("path", "file/" + dateFolder.getName() + "/" + file.getName());
                        fileList.add(fileInfo);
                    }
                }
            }
        }

        opResult.setMsgResult("success");
        opResult.setObjResult(fileList);
        return opResult;
    }
}