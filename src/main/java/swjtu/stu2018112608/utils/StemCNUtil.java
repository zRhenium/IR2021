package swjtu.stu2018112608.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StemCNUtil {

    static public int CN = 0;
    static public int EN = 1;

    public int mode = CN;

    private DBUtil dbUtil = new DBUtil();

    private ArrayList<String> wordList = new ArrayList<>();

    private JiebaSegmenter segmenter = new JiebaSegmenter();

    private String sourseText = " ";

    public StemCNUtil(String sourse) {
        sourseText = sourse;
    }

    public void stemmingAll() {
        // 创造字典
        WordDictionary.getInstance().init(Paths.get("conf"));
        // 中文分词
        List<String> list = segmenter.sentenceProcess(sourseText);
        for (String word : list) {
            wordList.add(word);
        }

        sourseText = "";
        StringWriter builder = new StringWriter();
        for (String s : wordList) {
            builder.append(s);
            builder.append(" ");
        }
        sourseText = builder.toString();
    }

    /**
     * 导出为 txt
     * @param targetFileName 导出的文件名，注意，不是路径名，文件名即可
     */
    public void exportAsTXT(String targetFileName) {
        File file = new File("result\\" + "CN" + "\\" + targetFileName);

        // 不存在就创建一个
        if (!file.exists()) {
            try {
                if(!file.createNewFile()) {
                    throw new IOException("创建文件失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(file, true));
            fw.write(sourseText);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除停用词
     */
    public void removeStopWord() {
        ArrayList<String> newList = new ArrayList<String>();

        // wordList.clear();
        for (String word : wordList) {
            if (dbUtil.isStopCNWord(word) == false) {
                newList.add(word);
            }
        }
        wordList = newList;

        // 更新 sourseText
        sourseText = "";
        for (String s : wordList) {
            sourseText += s + " ";
        }
    }

}
