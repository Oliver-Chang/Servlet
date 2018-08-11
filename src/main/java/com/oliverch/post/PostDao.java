package com.oliverch.post;

import com.google.gson.Gson;
import com.oliverch.common.annotation.Table;

import com.oliverch.common.dao.InterfaceDAO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Table(clazz = Post.class, table = "post", primaryKey = "rId")
public class PostDao implements InterfaceDAO<Post> {

    public static void main(String args[]) {

    }
}