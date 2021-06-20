package swjtu.stu2018112608.utils;

import java.io.*;
import java.util.ArrayList;

/**
 * 分词工具类
 */
public class SegmentationUtil {

    // 源文本
    private String sourseText = " ";

    // 数据库工具
    private DBUtil dbUtil = new DBUtil();

    // 单词序列
    public ArrayList<String> wordList = new ArrayList<>();

    public static int EN = 0;
    public static int CN = 1;

    int mode = EN;

    public String getText() {
        return sourseText;
    }

    public SegmentationUtil(String text) {
        sourseText = text;
    }

    /**
     * 构造函数
     * @param soursePath 文件路径
     * @param lan 语言（CN 中文，EN 英文）
     */
    public SegmentationUtil(String soursePath, int lan) {
        String temp = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(soursePath));
            while ((temp = reader.readLine()) != null) {
                sourseText += temp;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mode = lan;
    }

    public static boolean isALetter(char ch) {
        return (ch <= 'Z' && ch >='A') || (ch <= 'z' && ch >='a') || (ch <= '9' && ch >='0');
    }

    public void removeSpecialCharacter() {
        String token = "";
        for (int i = 0; i < sourseText.length() - 1; i++) {
            char temp = sourseText.charAt(i);

            // 如果字母数字，就在后面追加
            if (isALetter(temp)) {
                token += String.valueOf(temp);
            }

            // 如果是特殊字符，则跳过，并将 token 加入单词序列表中
            else {
                if (!token.isEmpty()) {
                    wordList.add(token);
                    // token 清空
                    token = "";
                }
            }

        }

        if (!token.isEmpty()) {
            wordList.add(token);
            // token 清空
            token = "";
        }

        sourseText = "";
        for (String s : wordList) {
            sourseText += s + " ";
        }

    }

    public void exportAsTXT(String targetFileName) {
        File file;
        if (mode == CN)
            file = new File("output\\" + "CN" + "\\" + targetFileName);
        else
            file = new File("output\\" + "EN" + "\\" + targetFileName);

        // 不存在就创建一个
        if (file.exists() == false) {
            try {
                file.createNewFile();
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

    public void printWordList() {
        for (String s : wordList) {
            System.out.println(s);
        }
    }

    /**
     * 删除停用词
     */
    public void removeStopWord() {
        ArrayList<String> newList = new ArrayList<String>();
        for (String word : wordList) {
            if (dbUtil.isStopENWord(word) == false) {
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
