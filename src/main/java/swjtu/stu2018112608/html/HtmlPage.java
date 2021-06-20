package swjtu.stu2018112608.html;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import swjtu.stu2018112608.doc.NewsDoc;
import swjtu.stu2018112608.utils.CommandUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HtmlPage {

    public static String TEMP_DIR = "HtmlTemp/";

    public String name = "name";
    public String title = "title";
    public String content = "content";

    private StringBuffer buffer = new StringBuffer();

    public HtmlPage(String _name, String _title, String _content) {
        name = _name;
        title = _title ;
        content = _content;
    }


    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile() {
        boolean flag = false;
        File file = new File(TEMP_DIR);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (TEMP_DIR.endsWith(File.separator)) {
                temp = new File(TEMP_DIR + tempList[i]);
            } else {
                temp = new File(TEMP_DIR + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
        }
        return flag;
    }


    @Test
    public void createNewsHtmlPage() throws IOException {

        buffer.append(
                "<html>\n" +
                "<head>\n" +
                "<title>\n" +
                name + "\n" +
                "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                title + "\n" +
                "</h1>\n" +
                "<p>\n" +
                content + "\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>"
        );
        File dir = new File("HtmlTemp/");

        File file = new File("HtmlTemp/" + name + ".html");
        if (!file.exists()) {
            file.createNewFile();
            // System.out.println("0");
        }

        FileWriter fw = new FileWriter("HtmlTemp/" + name + ".html");
        fw.write(buffer.toString());
        fw.close();

        // CommandUtil.execCommand("C:\\Users\\52590\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe " + file.getAbsolutePath());

    }


    // 创建导航页面
    public void createGuideHtmlPage(HashMap<String, NewsDoc> searchResultSet, String guidePageName) throws IOException {
        int nPage = searchResultSet.size();
        File dir = new File("HtmlTemp/");
        File file = new File("HtmlTemp/"+ guidePageName +".html");
        if (!file.exists()) {
            file.createNewFile();
            // System.out.println("0");
        }

        buffer.append("<html>\n" +
                "<head>\n" +
                "<title>\n" +
                "检索结果\n" +
                "</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                "共检索到 "+ nPage +" 个文档\n" +
                "</h1>\n");

        // 遍历 hashMap
        for (Map.Entry<String, NewsDoc> entry : searchResultSet.entrySet()) {
            buffer.append("<p><a href=\""+ dir.getAbsolutePath() +"\\"+ entry.getKey() +".html" +"\"> \""+ entry.getKey() + "\" </a></p>\n");
        }
        buffer.append("</body>\n" +
                "</html>");

        FileWriter fw = new FileWriter("HtmlTemp/"+ guidePageName +".html");
        fw.write(buffer.toString());
        fw.close();
        CommandUtil.execCommand("C:\\Users\\52590\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe " + file.getAbsolutePath());
    }


}
