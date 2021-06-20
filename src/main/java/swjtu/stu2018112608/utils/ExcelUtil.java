package swjtu.stu2018112608.utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


/**
 * Excel工具类，因为网页爬取的原始数据是ecxel数据表，因此编写这个类来提取数据
 *
 */
public class ExcelUtil {
    public static int TITLE = 0;
    public static int ARTICLE = 3;

    // poi库操作excel的数据表
    private XSSFSheet sheet;

    /**
     * 构造函数，初始化excel数据
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    public ExcelUtil(String filePath, String sheetName) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据行和列的索引获取单元格的数据
     *
     * @param row 行索引
     * @param column 列索引
     * @return 指定单元格数据
     */
    public String getExcelDateByIndex(int row, int column) {
        XSSFRow row1 = sheet.getRow(row);
        return row1.getCell(column).toString();
    }


    /**
     * 获取数据表行数
     * @return 数据表行数
     */
    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }


    /**
     * 将某单元格的值转换为 txt 文件
     * @param row 行索引
     * @param col 列索引
     * @param targetFileName 生成的文件名字
     * @param lan 语言模式
     *
     */
    public void exportAsTXTByIndex(int row, int col, String targetFileName, String lan) {
        String text = getExcelDateByIndex(row, col);
        File file = new File("output\\" + lan + "\\" + targetFileName);
        // 不存在就创建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(file, true));
            fw.write(text);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exportAllENNewsAsTXT() {
        for (int i = 1; i < getRowCount(); i++) {
            exportAsTXTByIndex(i, ExcelUtil.TITLE, "News_"+String.valueOf(i)+"_Org.txt", "EN");
            exportAsTXTByIndex(i, ExcelUtil.ARTICLE, "News_"+String.valueOf(i)+"_Org.txt", "EN");
        }
    }

    public void exportAllCNNewsAsTXT() {
        for (int i = 1; i < getRowCount(); i++) {
            exportAsTXTByIndex(i, ExcelUtil.TITLE, "News_"+ i +"_Org.txt", "CN");
            exportAsTXTByIndex(i, ExcelUtil.ARTICLE, "News_"+ i +"_Org.txt", "CN");
        }
    }

}