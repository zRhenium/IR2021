package swjtu.stu2018112608.utils;

import java.sql.*;


public class DBUtil  {
    Connection conn = null;
    ResultSet resultSet = null;
    Statement statement;

    /*
    * 构造函数 创建数据库工具类对象
    *
    * */
    public DBUtil(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:resources\\IRDB.db");
            statement = conn.createStatement();

            // System.out.println("数据库连接成功！");

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 判断一个英文单词是否是停用词，通过查询数据库的结果返回值
     * @param word 一个英文单词
     * @return 是否是英文的停用词
     */
    public boolean isStopENWord(String word) {
        word = word.toLowerCase();
        String sql = "select count(*) as rs from StopWord where word = '" + word + "'";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getInt("rs") != 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * 判断一个中文单词是否是停用词，通过查询数据库的结果返回值
     * @param word 一个中文单词
     * @return 是否是中文的停用词
     */
    public boolean isStopCNWord(String word) {
        word = word.toLowerCase();
        String sql = "select count(*) as rs from StopWordCN where word = '" + word + "'";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getInt("rs") != 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
