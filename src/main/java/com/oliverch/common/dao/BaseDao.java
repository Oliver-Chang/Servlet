package com.oliverch.common.dao;



import org.apache.commons.beanutils.BeanUtils;



import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的dao，自己写的所有的dao都继承此类;
 * 此类定义了2个通用的方法：
 * 1. 更新
 * 2. 查询
 *
 * @author Charlie.chen
 */

public class BaseDao {


    /**
     * 查询的通用方法
     *
     * @param sql
     * @param params
     * @param clazz
     */

    static public <T> List<T> query(String sql, List<Object> params, Class<T> clazz) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            // 返回的集合
            list = new ArrayList<T>();
            // 对象
            T t = null;

            // 1. 获取连接
            conn = JDBCUtil.getConnection();
            // 2. 创建stmt对象
            pstmt = conn.prepareStatement(sql);
            // 3. 获取占位符参数的个数， 并设置每个参数的值
            Integer count = pstmt.getParameterMetaData().getParameterCount();
            if (params != null && params.size() > 0) {
                for (Integer i = 0; i < count; i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
            }
            System.out.println(sql);
            // 4. 执行查询
            rs = pstmt.executeQuery();
            // 5. 获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // ---> 获取列的个数
            Integer columnCount = rsmd.getColumnCount();

            // 6. 遍历rs
            while (rs.next()) {
                // 要封装的对象
                t = clazz.getConstructor().newInstance();

                // 7. 遍历每一行的每一列, 封装数据
                for (Integer i = 0; i < columnCount; i++) {
                    // 获取每一列的列名称
                    String columnName = rsmd.getColumnName(i + 1);
                    // 获取每一列的列名称, 对应的值
                    Object value = rs.getObject(columnName);
                    // 封装： 设置到t对象的属性中  【BeanUtils组件】
                    BeanUtils.copyProperty(t, columnName, value);
                }

                // 把封装完毕的对象，添加到list集合中
                list.add(t);
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }


    /**
     * 更新的通用方法
     *
     * @param sql    更新的sql语句(update/insert/delete)
     * @param params sql语句中占位符对应的值(如果没有占位符，传入null)
     */
    static public Boolean update(String sql, List<Object> params, int[]... priKey) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Boolean ret = null;
        try {
            // 获取连接
            conn = JDBCUtil.getConnection();
            // 创建执行命令的stmt对象
            pstmt = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // 参数元数据： 得到占位符参数的个数
            Integer count = pstmt.getParameterMetaData().getParameterCount();

            // 设置占位符参数的值
            if (params != null && params.size() > 0) {
                // 循环给参数赋值
                for (Integer i = 0; i < count; i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
            }
            System.out.println(sql);
            // 执行更新
            if (pstmt.executeUpdate() == 0) {
                ret = false;
            } else {
                ret = true;
            }
            if (priKey != null) {
                rs = pstmt.getGeneratedKeys();
                while (rs.next()) {
                    priKey[0][0] = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return ret;
    }

    static public Integer recordsCount(String sql, List<Object>params) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Integer recordsCount = 0;
        // 2. 创建stmt对象
        try {
            conn = JDBCUtil.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            // 3. 获取占位符参数的个数， 并设置每个参数的值
            Integer count = pstmt.getParameterMetaData().getParameterCount();
            if (params != null && params.size() > 0) {
                for (Integer i = 0; i < count; i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
            }

            System.out.println(sql);
            // 4. 执行查询
            rs = pstmt.executeQuery();

            while (rs.next()) {
                recordsCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return recordsCount;
    }
}
