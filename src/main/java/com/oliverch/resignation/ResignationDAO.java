package com.oliverch.resignation;

import com.google.gson.Gson;
import com.oliverch.common.annotation.Table;

import com.oliverch.common.dao.InterfaceDAO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Table(clazz = Resignation.class, table = "resignation", primaryKey = "rId")
public class ResignationDAO implements InterfaceDAO<Resignation> {

    public static void main(String args[]) {


    }
}
