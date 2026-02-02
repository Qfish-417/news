package web.ssm.utils;

import jakarta.servlet.http.HttpServletResponse;
import web.ssm.dto.OpResultDTO;
import web.ssm.dto.TableRequestDTO;
import web.ssm.dto.TableResponseDTO;
import web.ssm.dto.UserDTO;
import web.ssm.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;


@RestController
@RequestMapping(value = "/api/data")
public class ImportExport {

    private final Logger logger = LoggerFactory.getLogger(ImportExport.class);

    @Autowired
    private UserService userService;

    /**
     * @Description: 导入模板中的数据，并返回JSON字符串，该方法主要上传Excel模板的版本要与POI版本对应
     * @Author: Song
     */
    @ResponseBody
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public OpResultDTO importData(@RequestParam("excelData") MultipartFile[] excelData) {
        OpResultDTO opResult = new OpResultDTO();
        HSSFWorkbook workbook = null;
        try {
            MultipartFile file = excelData[0];
            //把文件读入
            InputStream inputStream = file.getInputStream();
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            // 取得上传的文件，创建对Excel工作薄文件的引用
            workbook = new HSSFWorkbook(fs);
            // 建立新的sheet对象
            HSSFSheet sheet = workbook.getSheet("sheet1");
            // 获取Excel的所有行
            int rows = sheet.getPhysicalNumberOfRows();
            String importData = "";
            // 遍历行
            for (int i = 0; i < rows; i++) {
                // 读取左上角单元格
                HSSFRow row = sheet.getRow(i);
                // 行不能为空
                if (row != null) {
                    // 获取Excel文件中的所有列
                    int cells = row.getPhysicalNumberOfCells();
                    // 遍历列
                    for (int j = 0; j < cells; j++) {
                        // 获取列的值
                        HSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    importData += cell.getNumericCellValue();
                                    break;
                                case STRING:
                                    importData += cell.getStringCellValue() + ",";
                                    break;
                                case BOOLEAN:
                                    break;
                                case FORMULA:
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            // 应该将模板中的数据解析，然后插入到数据库，这里只是返回一个字符串
            if (importData.length() > 0) {
                importData = importData.substring(0, importData.length() - 1);
            }
            String[] tempValue = importData.split(",");
            opResult.setMsgResult("success");
            opResult.setObjResult(tempValue);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("关闭workbook失败", e);
                }
            }
        }
        return opResult;
    }

    /**
     * @Description: 从数据库中获取数据导出到EXCEL
     * @Author: SongGH
     * @Date: 2024/04/01 18:15
     */
    @ResponseBody
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportData(HttpServletResponse response) {
        try {
            // 获取所有用户数据
            TableRequestDTO tableRequestDTO = new TableRequestDTO(10000, 1, "");
            TableResponseDTO tableResponse = userService.list4Table(tableRequestDTO);
            @SuppressWarnings("unchecked")
            List<UserDTO> userList = (List<UserDTO>) tableResponse.getListTable();

            // 输出流的字符编码处理
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");

            // 生成文件名（带时间戳）
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = "用户信息_" + dateFormat.format(new Date()) + ".xls";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ";filename*=UTF-8''" + encodedFileName);

            OutputStream os = response.getOutputStream();
            // 创建Excel工作薄
            HSSFWorkbook book = new HSSFWorkbook();
            // 在Excel工作薄中建一张工作表
            HSSFSheet sheet = book.createSheet("用户信息");

            // 创建标题样式
            HSSFCellStyle headerStyle = book.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            org.apache.poi.hssf.usermodel.HSSFFont headerFont = book.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);

            // 第一行为标题行
            HSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"用户ID", "登录账号", "用户姓名", "邮箱", "手机号", "用户类型", "部门名称", "创建时间"};

            // 设置标题
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = headerRow.createCell(i);
                cell.setCellType(STRING);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 设置列宽
            sheet.setColumnWidth(0, 3000);   // 用户ID
            sheet.setColumnWidth(1, 4000);   // 登录账号
            sheet.setColumnWidth(2, 4000);   // 用户姓名
            sheet.setColumnWidth(3, 5000);   // 邮箱
            sheet.setColumnWidth(4, 4000);   // 手机号
            sheet.setColumnWidth(5, 3000);   // 用户类型
            sheet.setColumnWidth(6, 4000);   // 部门名称
            sheet.setColumnWidth(7, 5000);   // 创建时间

            // 日期格式化
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 循环导出数据到excel中
            int rowNum = 1;
            for (UserDTO user : userList) {
                HSSFRow row = sheet.createRow(rowNum);

                // 用户ID
                HSSFCell cell0 = row.createCell(0);
                cell0.setCellType(STRING);
                cell0.setCellValue(user.getUserId() != null ? user.getUserId().toString() : "");

                // 登录账号
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellType(STRING);
                cell1.setCellValue(user.getLoginCode() != null ? user.getLoginCode() : "");

                // 用户姓名
                HSSFCell cell2 = row.createCell(2);
                cell2.setCellType(STRING);
                cell2.setCellValue(user.getUserName() != null ? user.getUserName() : "");

                // 邮箱
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellType(STRING);
                cell3.setCellValue(user.getUserEmail() != null ? user.getUserEmail() : "");

                // 手机号
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellType(STRING);
                cell4.setCellValue(user.getUserPhone() != null ? user.getUserPhone() : "");

                // 用户类型
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellType(STRING);
                String userType = user.getUserType() != null ? user.getUserType() : "";
                cell5.setCellValue("admin".equals(userType) ? "管理员" : "记者");

                // 部门名称
                HSSFCell cell6 = row.createCell(6);
                cell6.setCellType(STRING);
                cell6.setCellValue(user.getDeptName() != null ? user.getDeptName() : "");

                // 创建时间
                HSSFCell cell7 = row.createCell(7);
                cell7.setCellType(STRING);
                cell7.setCellValue(user.getGmtCreate() != null ? dateFormatter.format(user.getGmtCreate()) : "");

                rowNum++;
            }

            // 写入数据，把相应的EXCEL工作簿存盘
            book.write(os);
            os.flush();
            os.close();
            book.close();
        } catch (Exception e) {
            logger.error("导出Excel失败: " + e.getMessage(), e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败: " + e.getMessage());
            } catch (IOException ioException) {
                logger.error("发送错误响应失败", ioException);
            }
        }
    }
}
