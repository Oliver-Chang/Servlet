package com.oliverch.department;

import com.google.gson.Gson;
import com.oliverch.common.annotation.Table;

import com.oliverch.common.dao.InterfaceDAO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Table(clazz = Department.class, table = "department", primaryKey = "rId")
public class DepartmentDAO implements InterfaceDAO<Department> {

    public static void main(String args[]) {


    }
}
