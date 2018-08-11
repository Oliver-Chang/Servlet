package com.oliverch.common.requestparse;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.commons.beanutils.BeanUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RequestParse {
    static public <T> T parseJsonRequesTo(InputStreamReader reader, Class<T> clazz){

        String line;
        String json = null;
        T dest = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(reader);
        try {
            line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        json = sb.toString();
        Gson gson = new Gson();
        T t = gson.fromJson(json, clazz);
        return t;
    }

}
