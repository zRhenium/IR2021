package swjtu.stu2018112608.utils;

import org.tartarus.snowball.ext.PorterStemmer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 词干提取工具类
 * 调用第三方库
 */

public class StemUtil {

    static public int CN = 0;
    static public int EN = 1;

    public int mode = EN;

    private ArrayList<String> wordList = new ArrayList<>();

    private String sourseText = " ";

    public StemUtil(String sourseText) {
        String[] words = sourseText.split(String.valueOf(' '));
        Collections.addAll(wordList, words);
    }


    /**
     * stemming一个单词
     * @param word 原来的单词
     * @return stemming 后的单词
     */
    public static String stemmingWord(String word) {
        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(word);
        if(stemmer.stem()) {
            return stemmer.getCurrent();
        }
        return word;
    }

    public void stemmingAll() {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> newList = new ArrayList<>();
        for (String word : wordList) {
            newList.add(stemmingWord(word));
        }
        wordList = newList;

        sourseText = "";
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
        File file;
        if (mode == CN)
            file = new File("result\\" + "CN" + "\\" + targetFileName);
        else
            file = new File("result\\" + "EN" + "\\" + targetFileName);

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
}
