package com.duan.common;

import java.util.HashMap;

/**
 * Created by DuanJiaNing on 2017/4/26.
 */
public class StringUtil {
    private HashMap<String, String> IIntMap;

    public StringUtil() {
        IIntMap = new HashMap<>();
        IIntMap.put("Z", "boolean");
        IIntMap.put("B", "byte");
        IIntMap.put("S", "short");
        IIntMap.put("C", "char");
        IIntMap.put("I", "int");
        IIntMap.put("J", "long");
        IIntMap.put("F", "float");
        IIntMap.put("D", "double");
        IIntMap.put("V", "void");
    }

    public static boolean isVZBS(char ch) {
        return ch == 'Z' ||
                ch == 'B' ||
                ch == 'S' ||
                ch == 'C' ||
                ch == 'I' ||
                ch == 'J' ||
                ch == 'F' ||
                ch == 'D' ||
                ch == 'V' ;
    }

    public String getType(String str) {
        return IIntMap.get(str);
    }

}
