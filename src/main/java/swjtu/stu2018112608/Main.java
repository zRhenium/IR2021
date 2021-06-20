package swjtu.stu2018112608;

import org.junit.jupiter.api.Test;
import swjtu.stu2018112608.doc.NewsDoc;
import swjtu.stu2018112608.html.HtmlPage;
import swjtu.stu2018112608.utils.*;

import java.util.*;


public class Main {

    static String SOURSE_EN = "resources\\news_EN.xlsx";
    static String SOURSE_CN = "resources\\news_CN.xlsx";

    static String DEFAULT_SHEET = "sheet1";

    // 处理所有英文的文件
    @Test
    static void handleEN() {
        int nDone = 0;
        // 从爬取的原始数据（excel表）
        ExcelUtil excelUtil = new ExcelUtil(SOURSE_EN, DEFAULT_SHEET);
        // 新闻的数量
        int nNews = excelUtil.getRowCount();
        // 将原始数据导出为txt文档
        excelUtil.exportAllENNewsAsTXT();

        for (int i = 1; i < nNews; i++) {
            // 提取 txt 文档进行分词处理
            SegmentationUtil segmentationUtil = new SegmentationUtil("output\\EN\\News_"+ i +"_Org.txt", SegmentationUtil.EN);
            // 删除特殊字符
            segmentationUtil.removeSpecialCharacter();

            // 删除停用词
            segmentationUtil.removeStopWord();

            // 词干提取
            StemUtil stemUtil = new StemUtil(segmentationUtil.getText());
            stemUtil.stemmingAll();
            // 导出为文本
            stemUtil.exportAsTXT("News_" + i + "_E.txt");
            nDone += 1;
            System.out.println("News_" + i + "_E.txt 已经生成!");
        }

        for (int i = 3760 ; i <= 3763; i++) {
            // 提取 txt 文档进行分词处理
            SegmentationUtil segmentationUtil = new SegmentationUtil("output\\EN\\News_"+ i +"_Org.txt", SegmentationUtil.EN);
            // 删除特殊字符
            segmentationUtil.removeSpecialCharacter();

            // 删除停用词
            segmentationUtil.removeStopWord();

            // 词干提取
            StemUtil stemUtil = new StemUtil(segmentationUtil.getText());
            stemUtil.stemmingAll();
            // 导出为文本
            stemUtil.exportAsTXT("News_" + i + "_E.txt");
            nDone += 1;
            System.out.println("News_" + i + "_E.txt 已经生成!");
        }

    }

    // 处理所有的中文文件
    static void handleCN() {
        int nDone = 0;
        // 从爬取的原始数据（excel表）
        ExcelUtil excelUtil = new ExcelUtil(SOURSE_CN, DEFAULT_SHEET);
        // 新闻的数量
        int nNews = excelUtil.getRowCount();
        // 将原始数据导出为txt文档
        excelUtil.exportAllCNNewsAsTXT();

        for (int i = 1; i < nNews; i++) {
            // 提取 txt 文档进行分词处理
            SegmentationUtil segmentationUtil = new SegmentationUtil("output\\CN\\News_"+ i +"_Org.txt", SegmentationUtil.EN);

            // 创造中文分词工具对象
            StemCNUtil stemCNUtil = new StemCNUtil(segmentationUtil.getText());

            // 中文进行分词
            stemCNUtil.stemmingAll();

            // 删除中文停用词
            stemCNUtil.removeStopWord();

            stemCNUtil.exportAsTXT("News_" + i + "_C.txt");
            nDone += 1;
            System.out.println("News_" + i + "_C.txt 已经生成!");
        }


    }


    // 聚类
    static void clustering() {


    }


    public static void main(String[] args) {

        ArrayList<ArrayList<Double>> cosineMatrix = new ArrayList<>();

        // 聚类中心
        ArrayList<NewsDoc> centerList;

        // 聚类中心的id
        ArrayList<Integer> centerIdList = new ArrayList<>();

        try {
            // 处理英文的数据
            // handleEN();
            // 处理中文数据
            // handleCN();

            IndexingUtil indexingUtil = new IndexingUtil();

            // indexingUtil.createIndex();

            // 检索部分

            while (true) {

                System.out.println("输入检索的关键词：");

                Scanner input = new Scanner(System.in);

                String keyWord = input.next();
                keyWord= keyWord.toLowerCase();

                // 检索关键词，结果输出到 searchResults 中
                indexingUtil.searchIndex(null, keyWord, 999999, false);

                /*
                for (Map.Entry<String, NewsDoc> entry1 : indexingUtil.searchResultSet.entrySet()) {
                    ArrayList<Double> consineList = new ArrayList<>();

                    for (Map.Entry<String, NewsDoc> entry2 : indexingUtil.searchResultSet.entrySet()) {
                        // 获取第 0 篇文档的词频表
                        HashMap<String, Integer> freq0 = AnalyzeUtil.analyzeByTokenStream(entry1.getValue());
                        // 获取第 1 篇文档的词频表
                        HashMap<String, Integer> freq1 = AnalyzeUtil.analyzeByTokenStream(entry2.getValue());

                        double cosd = AnalyzeUtil.cosineDistance(freq0, freq1);
                        consineList.add(cosd);

                        System.out.println(entry1.getKey() + " 与 " + entry2.getKey() + "的余弦距离是：" + cosd);
                    }
                    cosineMatrix.add(consineList);
                }*/

                AnalyzeUtil.exportAsExcel(cosineMatrix, "cos.xls");




                System.out.println("检索到 " + indexingUtil.searchResultSet.size() + " 个文档包含关键词 " + keyWord);
            }




        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
