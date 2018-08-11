package com.oliverch.common.routeparse;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  RouteParse {

    public static String parse(String route, String regPattern) {

        String ret = null;
        if(route == null) {
            return ret;
        }
        Matcher matcher;
        Pattern pattern = Pattern.compile(regPattern);
        matcher = pattern.matcher(route);
        if (matcher.find()) {
            ret = matcher.group(0);
        }
        return ret;
    }
}
