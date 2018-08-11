package com.oliverch.common.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    Class clazz();
    String table();
    String primaryKey();
}
