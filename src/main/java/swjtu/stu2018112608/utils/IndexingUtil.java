package swjtu.stu2018112608.utils;

import jdk.jfr.StackTrace;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import swjtu.stu2018112608.doc.NewsDoc;
import swjtu.stu2018112608.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 创建索引的工具类
public class IndexingUtil {

    // 每一次调用searchIndex后，检索到的结果存放在 searchResults 里
    public ArrayList<NewsDoc> searchResults = new ArrayList<>();
    public HashMap<String, NewsDoc> searchResultSet = new HashMap<>();

    public void updateSearchResultSet() throws IOException {

        for (NewsDoc doc : searchResults) {
            if (doc.name != null) {
                searchResultSet.put(doc.name, doc);
            }
        }

        HtmlPage.delAllFile();
        for (Map.Entry<String, NewsDoc> entry : searchResultSet.entrySet()) {
            HtmlPage htmlPage = new HtmlPage(entry.getKey(), entry.getKey(), entry.getValue().content);
            htmlPage.createNewsHtmlPage();
        }

        HtmlPage guidePage = new HtmlPage("guide", null,null);
        guidePage.createGuideHtmlPage(searchResultSet, "guide");

    }

    @Test
    public void createIndex() throws Exception {
        // 创建一个 Directory 对象，指定索引库的位置放在项目的index文件夹里
        Directory directory = FSDirectory.open(new File("index").toPath());

        // 基于索引库创建一个 IndexWriter 对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());

        // 读取磁盘上的文件，每个文件创建一个文档对象
        File dir = new File("result\\EN");

        File[] file = dir.listFiles();
        for (File f : file) {
            // 获得文件名
            String fileName = f.getName();

            // 获取文件路径
            String filePath = f.getPath();

            // 获取文件内容，此处调 common-io 库
            String fileContent = FileUtils.readFileToString(f, "utf-8");

            long fileSize = FileUtils.sizeOf(f);

            // 创建Field
            Field fieldType = new TextField("type", "project", Field.Store.YES);
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new StoredField("path", filePath);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            Field fieldSize = new TextField("size", String.valueOf(fileSize), Field.Store.YES);

            // 创建文档对象
            Document document = new Document();

            // 向文档里添加Field
            document.add(fieldType);
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);

            // 文档对象写入索引库
            indexWriter.addDocument(document);

            System.out.println(fileName + "建立索引成功");

            // 关闭 indexWriter

        }
        indexWriter.close();
    }

    public IndexingUtil() {

    }

    // 单元测试
    @Test
    private void testCase() {
        System.out.println("=======================================");
        try {
            searchIndex(null,"visit",10,true) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (NewsDoc sr :searchResults) {
            sr.display();
            System.out.println("=======================================");
        }
    }

    // 返回 News_***_E.txt 中间的内容
    public int GetIdFromNewsName(String fName) throws Exception{
        Pattern pattern = Pattern.compile("News_[0-9]{1,}_E.txt");
        Matcher matcher = pattern.matcher(fName);
        // 匹配上了，返回 News_***_E.txt 中间的内容
        if (matcher.matches())
            return Integer.parseInt((fName.split("_"))[1]);
        else
            return -1;
    }


    /*
        执行一次检索， 查询于 keyWord 有关的 前 nTopRelated 个文档，结果保存到 searchResults 中
        上层程序调用之后，需要再对 searchResults 操作才能取出数据

        origin 表示返回的文档是处理后的文档还是处理前的文档，默认检索结果为没处理前的文档
     */
    public void searchIndex(String fld, String keyWord, int nTopRelated, boolean origin) throws Exception {
        // 创建一个 Directory 对象，指定索引库的位置放在项目的index文件夹里
        Directory directory = FSDirectory.open(new File("index").toPath());

        // 基于索引库创建一个 IndexReader 对象
        IndexReader indexReader = DirectoryReader.open(directory);

        // 创建IndexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 创建一个Query对象, 如果域为空，则默认定向为正文
        if (fld == null)
            fld = "content";
        Query query = new TermQuery(new Term(fld, keyWord));

        // 执行查询，返回TopDoc
        TopDocs topDocs = indexSearcher.search(query, nTopRelated);

        // 取得查询结果
        System.out.println("本次查询到 " + topDocs.totalHits + " 条记录");

        // 取出文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 刷新 结果集合 searchResults
        searchResults.clear();
        searchResultSet.clear();


        for (ScoreDoc doc : scoreDocs) {
            int docId = doc.doc;

            Document document = indexSearcher.doc(docId);

            long dId = GetIdFromNewsName(document.get("name"));

            String fileContent;

            // 读取原始新闻文件
            if (origin == true) {
                File goalFile = new File("output\\EN\\" + "News_" + String.valueOf(dId) + "_Org.txt");
                fileContent = FileUtils.readFileToString(goalFile, "utf-8");
            }
            // 读取索引文件
            else {
                fileContent = document.get("content");
            }

            String newsName = document.get("name");
            String newsPath = document.get("path");


            searchResults.add(new NewsDoc((int) dId, newsName, newsPath, fileContent));
        }

        indexReader.close();
        updateSearchResultSet();
    }



}
