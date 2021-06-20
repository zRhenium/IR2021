package swjtu.stu2018112608.doc;

import java.util.HashMap;

// 新闻类
public class NewsDoc {
    public int id;
    public String name;
    public String path;
    public long   size;
    public String content;

    // 词频表
    HashMap<String, Integer> wordFreq;

    public NewsDoc(int _id, String _name, String _path, String _content) {
        id = _id;
        name = _name;
        path = _path;
        content = _content;
    }

    public void display() {
        System.out.println("[Name]\n" + name);
        System.out.println("[Path]\n" + path);
        System.out.println("[Content]\n" + content);
    }


}
