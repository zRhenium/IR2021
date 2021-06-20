package swjtu.stu2018112608.utils;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import swjtu.stu2018112608.doc.NewsDoc;
import swjtu.stu2018112608.html.HtmlPage;

import java.io.*;
import java.util.*;

public class AnalyzeUtil {

    static class WordTuple implements Comparable<WordTuple> {
        public String word;
        public Integer freq;
        @Override
        public int compareTo(AnalyzeUtil.WordTuple o) {
            return this.word.compareTo(o.word);
        }
        public WordTuple(String w, Integer f) {
            word = w;
            freq = f;
        }
    }

    // 返回文档的词频表
    public static HashMap<String, Integer> analyzeByTokenStream(NewsDoc doc) throws IOException {
        // 词频列表
        HashMap<String, Integer> wordFreqList = new HashMap<>();

        // 创建 Analyzer 对象
        Analyzer analyzer = new StandardAnalyzer();

        TokenStream tokenStream = analyzer.tokenStream("", doc.content);

        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();

        while (tokenStream.incrementToken()) {
            String line = charTermAttribute.toString();
            if (wordFreqList.containsKey(line)) {
                Integer freq = wordFreqList.get(line);
                wordFreqList.put(line, freq + 1);

            } else {
                wordFreqList.put(line, 1);
            }
        }
        return wordFreqList;
    }

    // 将词频表按照字母表（排序）
    public static ArrayList<WordTuple> toArrayList(HashMap<String, Integer> freqList) {
        ArrayList<WordTuple> wordFreqArray = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : freqList.entrySet()) {
            wordFreqArray.add(new WordTuple(entry.getKey(), entry.getValue()));
        }
        // 排序
        Collections.sort(wordFreqArray);
        return wordFreqArray;
    }

    // 返回两个词频向量的余弦距离
    public static double cosineDistance(HashMap<String, Integer> freqList1, HashMap<String, Integer> freqList2) {
        // 遍历 向量1， 找到向量2 中含有相同单词的词频
        double result = 0.0;
        double denominator1 = 0.0;
        double denominator2 = 0.0;

        for (Map.Entry<String, Integer> entry : freqList1.entrySet()) {
            String wordThis = entry.getKey();
            double feq1 = entry.getValue();

            denominator1 += feq1*feq1;

            // 如果向量2 包含这个单词, 二者频率相乘
            if (freqList2.containsKey(wordThis)) {
                double feq2 = freqList2.get(wordThis);

                denominator2 += feq2*feq2;

                result += feq1 * feq2;

                // 向量2 移除这个单词, 因为之后要遍历向量2，那些在向量1 中没有出现过的单词
                freqList2.remove(wordThis);
            }
            // 如果不含 那么就相当于 向量2 的分量为0
            else {
                result += feq1 * 0;
            }
        }
        // 向量1 遍历结束后，剩余向量二全部加入计算，算出分母
        for (Map.Entry<String, Integer> entry : freqList2.entrySet()) {
            double feq1 = entry.getValue();
            denominator2 += feq1*feq1;
        }

        // 最后开方
        return Math.sqrt(result/( Math.sqrt(denominator1) * Math.sqrt(denominator2)));
    }


    // 导出为excel
    public static void exportAsExcel(ArrayList<ArrayList<Double>> matrix, String fileName) throws IOException {
        File file = new File("result\\" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter("result\\" + fileName);
        for (ArrayList<Double> vec : matrix) {
            for (Double val : vec) {
                fw.write(String.valueOf(val));
                fw.write("\t");
            }
            fw.write("\n");
        }
        fw.close();
    }

}
