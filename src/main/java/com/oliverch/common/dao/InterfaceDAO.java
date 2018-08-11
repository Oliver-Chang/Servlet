package com.oliverch.common.dao;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.oliverch.common.annotation.Table;

import com.oliverch.utils.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public interface InterfaceDAO<T> {
    /**
     * @return
     */
    private List<List> getFieldKeyValue(T model) {
        List<List> lists = new ArrayList<>();
        List<String> placeHolder = new ArrayList<>();
        Field[] fields = model.getClass().getDeclaredFields();
        List<String> fieldName = new ArrayList<>();
        List<Object> fieldValue = new ArrayList<>();
        String name = null;

        String priKey = this.getClass().getAnnotation(Table.class).primaryKey();


        for (Field field : fields) {
            if ((name = field.getName()).equals(priKey)) {
                continue;
            }
            fieldName.add(name);

            try {
                String value = BeanUtils.getProperty(model, name);
                fieldValue.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (Integer i = 0; i < fieldValue.size(); i++) {
            placeHolder.add("?");
        }

        lists.add(fieldName);
        lists.add(fieldValue);
        lists.add(placeHolder);
        return lists;
    }

    private Object getFieldValue(T model, String name) {
        Object property = null;
        try {
            property = BeanUtils.getProperty(model, name);
//            Field field = model.getClass().getDeclaredField(name);
//            field.setAccessible(true);
//            ret = field.get(model);
//            field.setAccessible(false);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

        }
        return property;
    }

    default Integer getRecordsCount(Map<String, String[]> condition) {
        List<String> sqlList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        Class clazz = this.getClass().getAnnotation(Table.class).clazz();
        String table = this.getClass().getAnnotation(Table.class).table();
        String sql = String.format("SELECT COUNT(*) FROM %s", table);


        Iterator<Map.Entry<String, String[]>> iterator = condition.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();
            String key = next.getKey();
            String[] values = next.getValue();

            for (Integer i = 0; i < values.length; i++) {

                if (i < values.length - 1) {
                    params.add(values[i]);
                    sqlList.add(String.format("%s=? or", key));
                } else {
                    params.add(values[i]);
                    sqlList.add(String.format("%s=?", key));
                }
            }

            if (iterator.hasNext()) {
                sqlList.add("and");
            }
        }

        if (sqlList.size() > 0) {
            sqlList.add(0, "WHERE");
        }

        sqlList.add(0, sql);

        sql = StringUtils.join(sqlList, " ");
        return BaseDao.recordsCount(sql, params);
    }


    default List<T> getALL() {
        Class clazz = this.getClass().getAnnotation(Table.class).clazz();
        String table = this.getClass().getAnnotation(Table.class).table();
        String sql = null;
        sql = String.format("SELECT * FROM %s", table);
        List<T> query = BaseDao.query(sql, null, clazz);
        return query;
    }

    default T getById(Integer id) {
        T t = null;
        String sql;

        Class clazz = this.getClass().getAnnotation(Table.class).clazz();
        String table = this.getClass().getAnnotation(Table.class).table();
        String prikey = this.getClass().getAnnotation(Table.class).primaryKey();
        List<Object> params = new ArrayList<>();

        params.add(id);

        sql = String.format("SELECT * FROM %s WHERE %s=?", table, prikey);
        List<T> ts = BaseDao.query(sql, params, clazz);
        ;

        if (ts.size() == 0) {
            return null;
        } else
            t = ts.get(0);
        return t;
    }

    default List<T> getBy(Map<String, String[]> condition) {
        List<String> sqlList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        Class clazz = this.getClass().getAnnotation(Table.class).clazz();
        String table = this.getClass().getAnnotation(Table.class).table();
        String sql = String.format("SELECT * FROM %s", table);
        String limit = null;
        Integer pageNum = null;
        Integer pageSize = null;


        Iterator<Map.Entry<String, String[]>> iterator = condition.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String[]> next = iterator.next();
            String key = next.getKey();
            String[] values = next.getValue();
            if (key.equals("page")) {
                pageNum = Integer.valueOf(values[0]);
                continue;
            }
            if (key.equals("page_size")) {
                pageSize = Integer.valueOf(values[0]);
                continue;
            }
            for (Integer i = 0; i < values.length; i++) {

                if (i < values.length - 1) {
                    params.add(values[i]);
                    sqlList.add(String.format("%s=? or", key));
                } else {
                    params.add(values[i]);
                    sqlList.add(String.format("%s=?", key));
                }
            }

            if (iterator.hasNext()) {
                sqlList.add("and");
            }
        }
        int lastIndex = sqlList.size() - 1;
        if (lastIndex >= 0 && sqlList.get(lastIndex).equals("and")) {
            sqlList.remove(lastIndex);
        }

        if (sqlList.size() > 0) {
            sqlList.add(0, "WHERE");
        }

        if (pageNum != null && pageSize != null) {
            Integer index = (pageNum - 1) * pageSize;
            limit = String.format("limit ?,?");
            sqlList.add(limit);
            params.add(index);
            params.add(pageSize);
        }

        sqlList.add(0, sql);

        sql = StringUtils.join(sqlList, " ");
        return BaseDao.query(sql, params, clazz);
    }

    default Integer addOne(T model) {
        Integer ret = -1;
        String sql;
        int[] priKeys = new int[1];
        String table = this.getClass().getAnnotation(Table.class).table();
        List<List> lists = getFieldKeyValue(model);
        List<String> sqlList = new ArrayList<>();

        List<String> fieldName = lists.get(0);
        List<Object> fieldValue = lists.get(1);
        List<String> placeHolder = lists.get(2);

        sqlList.add(String.format("INSERT INTO %s", table));
        sqlList.add("(");
        sqlList.add(StringUtils.join(fieldName, ","));
        sqlList.add(")");
        sqlList.add("VALUES");
        sqlList.add("(");
        sqlList.add(StringUtils.join(placeHolder, ","));
        sqlList.add(")");

        sql = StringUtils.join(sqlList, " ");
        if (BaseDao.update(sql, fieldValue, priKeys)) {
            ret = priKeys[0];
        }
        return ret;
    }

    default Boolean delOne(T model) {

        String sql = null;
        String table = this.getClass().getAnnotation(Table.class).table();
        String priKey = this.getClass().getAnnotation(Table.class).primaryKey();
        List params = new ArrayList();


        Object priKeyValue = getFieldValue(model, priKey);

        if (priKeyValue == null)
            return null;
        else {
            params.add(priKeyValue);
        }

        sql = String.format("DELETE FROM %s WHERE %s=?", table, priKey);
        return BaseDao.update(sql, params);
    }

    default Boolean updateOne(T model) {
        String sql = null;
        String table = this.getClass().getAnnotation(Table.class).table();
        String priKey = this.getClass().getAnnotation(Table.class).primaryKey();
        List<String> sqlList = new ArrayList<>();
        List<List> lists = getFieldKeyValue(model);

        Object priKeyValue = getFieldValue(model, priKey);
        List fieldName = lists.get(0);
        List fieldValue = lists.get(1);
        Integer sizeField = fieldName.size();


        sqlList.add(String.format("UPDATE %s SET", table));
        for (Integer i = 0; i < sizeField; i++) {
            if (i < sizeField - 1) {
                sqlList.add(String.format("%s=?,", fieldName.get(i)));
            } else {
                sqlList.add(String.format("%s=?", fieldName.get(i)));
            }
        }
        sqlList.add(String.format("WHERE %s=?", priKey));

        sql = StringUtils.join(sqlList, " ");
        fieldValue.add(priKeyValue);
        return BaseDao.update(sql, fieldValue);
    }

}
