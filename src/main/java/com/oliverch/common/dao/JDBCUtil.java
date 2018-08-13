package com.oliverch.common.dao;


import com.oliverch.staff.Staff;
import com.oliverch.utils.StringUtils;
import org.mariadb.jdbc.MariaDbPoolDataSource;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtil {

    // 驱动包名和数据库url
    private static String url = null;
    private static String driverClass = null;
    // 数据库用户名和密码
    private static String userName = null;
    private static String passWord = null;
    private static Integer maxPoolSize = null;
    private static Integer port = null;
    private static String dataBaseName = null;

    private static MariaDbPoolDataSource poolDataSource = null;

    /**
     * 初始化驱动程序
     * 静态代码块中（只加载一次）
     */
    static {
        try {
            //读取db.properties文件
            Properties prop = new Properties();

            /**
             * 使用类路径的读取方式
             *  / : 斜杠表示classpath的根目录
             *     在java项目下，classpath的根目录从bin目录开始
             *     在web项目下，classpath的根目录从WEB-INF/classes目录开始
             */
            InputStream in = JDBCUtil.class.getResourceAsStream("/jdbc.properties");

            //加载文件
            prop.load(in);
            //读取信息
            url = prop.getProperty("url");
            driverClass = prop.getProperty("driverClass");
            userName = prop.getProperty("userName");
            passWord = prop.getProperty("passWord");
            maxPoolSize = Integer.valueOf(prop.getProperty("maxPoolSize"));


            //注册驱动程序
            Class.forName(driverClass);
            poolDataSource = new MariaDbPoolDataSource();
            poolDataSource.setUrl(url);
            poolDataSource.setUser(userName);
            poolDataSource.setPassword(passWord);
            poolDataSource.setMaxPoolSize(maxPoolSize);
            poolDataSource.initialize();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("驱程程序注册出错");
        }
    }


    /**
     * 打开数据库驱动连接
     */
    public static Connection getConnection() {
        Connection conn = null;
//        try {
//
//            conn = DriverManager.getConnection(url, userName, passWord);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
        try {
            conn = poolDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return conn;
    }


    /**
     * 清理环境，关闭连接(顺序:后打开的先关闭)
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        rs = null;
        stmt = null;
        conn = null;
    }


    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        conn = JDBCUtil.getConnection();
        try {
            stmt = (Statement) conn.createStatement();
            //准备sql操作语句
            String sql = "SELECT * FROM websites";
            rs = stmt.executeQuery(sql);

            //从结果集中提取数据
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String url = rs.getString("url");
                String alexa = rs.getString("alexa");
                String country = rs.getString("country");


                System.out.println("ID: " + id);
                System.out.println(", name: " + name);
                System.out.println(", url: " + url);
                System.out.println(", alexa: " + alexa);
                System.out.println(", alexa: " + country);


            }
            Staff staff = null;
            staff = new Staff();


            Field[] fields = staff.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    System.out.println(field.get(staff));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            List<String> list = new ArrayList<>();
            list.add("hello");
            list.add("asd");
            System.out.println(StringUtils.join(list, ","));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

}