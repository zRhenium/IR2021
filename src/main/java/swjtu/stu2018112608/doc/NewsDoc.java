package swjtu.stu2018112608.doc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static swjtu.stu2018112608.doc.NewsDoc.APITest.sortRate;

// 新闻类
public class NewsDoc {
    public int id;
    public String name;
    public String path;
    public long   size;
    public String content;

    // 词频表
    Map<String, Integer> wordFreq;

    public NewsDoc(int _id, String _name, String _path, String _content) {
        id = _id;
        name = _name;
        path = _path;
        content = _content;

        String[] contentWord = APITest.splitText(content, " ");

        wordFreq = APITest.countWord(contentWord);

        Map<String, Integer> wordRate = APITest.sortRate(wordFreq,true);

        wordFreq = wordRate;
    }

    public void display() {
        System.out.println("[Name]\n" + name);
        System.out.println("[Path]\n" + path);
        System.out.println("[Content]\n" + content);
    }





    public static class APITest {

        /** 读取对应文件中的内容
         * @param fileName 文件名称
         * @return 返回String类型
         * @throws IOException
         */
        public static String readText(String fileName) throws IOException {
            //读取数据
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                if (line.trim() != "") {//去除先后空格
                    stringBuffer.append(line);
                }
            }
            return stringBuffer.toString();
        }


        /** 将传入进来的String进行切分
         * @param string 原始的String内容
         * @param re 切分的依据
         * @return 返回数组类型
         */
        public static String[] splitText(String string,String re) {
            return string.split(re);
        }


        /** 将单词数组进行清洗
         * @param content 单词数组
         * @return 清洗后的单词数组
         */
        public static String[] cleanWord(String[] content) {
            List<String> words = new ArrayList<>();

            for (String word : content) {
                if (word.trim().length() > 0) {//单词不为空
                    word = cleanWordStart(word);//清除单词其前面的特殊字符
                    word = cleanWordEnd(word);//清除单词后面的特殊字符
                    if (word.length() > 0) {//不为空的添加到结果中
                        words.add(word);
                    }
                }
            }
            return words.toArray(new String[words.size()]);
        }


        /** 用来将word去除前后的特殊字符
         * @param word
         * @return
         */
        public static String cleanWordEnd(String word) {
            if (word.length() == 0) {//递归过程中可能出现空的字符串
                return "";
            }
            char lastCh = word.charAt(word.length() - 1);//拿到最后一个字符
            if ((lastCh>='A' && lastCh <= 'Z') || (lastCh>='a' && lastCh <= 'z') || (lastCh>='0' && lastCh <= '9')) {//证明此时不需要做处理
                return word;
            } else {//需要将最后一个字符去掉
                word = word.substring(0, word.length() - 1);
                return cleanWordEnd(word);//递归处理
            }
        }

        /** 用来将单词的前面特殊符号去除
         * @param word
         * @return
         */
        public static String cleanWordStart(String word) {
            if (word.length() == 0) {//递归过程中可能出现空的字符串
                return "";
            }
            char startCh = word.charAt(0);//拿到第一个字符
            if ((startCh>='A' && startCh <= 'Z') || (startCh>='a' && startCh <= 'z') || (startCh>='0' && startCh <= '9')) {//证明此时不需要做处理
                return word;
            } else {//需要将第一个字符去掉
                word = word.substring(1, word.length());
                return cleanWordStart(word);
            }
        }

        /** 对单词的频率进行统计
         * @param words 单词数组
         * @return 返回Map集合
         */
        public static Map<String, Integer> countWord(String[] words) {
            Map<String, Integer> count = new HashMap<>();
            Integer num;
            for (String word : words) {
                num = count.get(word);
                if (num == null) {
                    num = 1;
                } else {
                    num++;
                }
                count.put(word, num);
            }
            return count;
        }


        /** 根据单词出现的频率进行排序，默认从小到大，可以传入reverse为true来倒序
         * @param words   单词Map
         * @param reverse 是否倒序排列
         * @return
         */
        public static Map<String,Integer> sortRate(Map<String,Integer> words,boolean reverse) {
            List<Map.Entry<String, Integer>> list = new ArrayList<>(words.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    if (reverse) {
                        return o2.getValue() - o1.getValue();
                    } else {
                        return o1.getValue() - o2.getValue();
                    }
                }
            });
//        //Java8 排序
//        if (reverse) {
//            list = words
//                    .entrySet()
//                    .stream()
//                    .sorted(Map.Entry.<String,Integer> comparingByValue().reversed())
//                    .collect(Collectors.toList());
//        } else {
//            list = words
//                    .entrySet()
//                    .stream()
//                    .sorted(Map.Entry.<String,Integer> comparingByValue())
//                    .collect(Collectors.toList());
//        }

            Map<String, Integer> result = new LinkedHashMap<>();//这里要使用LinkedHashMap使传入其中的有序存放

            for (Map.Entry<String, Integer> map : list) {
                result.put(map.getKey(), map.getValue());
            }
            return result;
        }

        /** 重载排序，默认为从小到大
         * @param words
         * @return
         */
        public static Map<String,Integer> sortRate(Map<String,Integer> words) {
            return sortRate(words, false);
        }
    }


}
