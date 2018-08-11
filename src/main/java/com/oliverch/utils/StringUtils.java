package com.oliverch.utils;

import java.util.List;

public class StringUtils {
    public static String join(List<String> list, String placeHolder) {
        Integer listSize = list.size();
        StringBuilder sbd = new StringBuilder();
        for (Integer i = 0; i < listSize; i++) {
            if (i < listSize -1) {
                sbd.append(list.get(i));
                sbd.append(placeHolder);
            }
            else {
                sbd.append(list.get(i));
            }
        }
        return sbd.toString();
    }
}
