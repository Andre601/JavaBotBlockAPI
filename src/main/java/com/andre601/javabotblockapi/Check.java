package com.andre601.javabotblockapi;

import java.util.Map;

public class Check {

    public static void notNull(Object target, String msg){
        if(target == null)
            throw new NullPointerException(msg);
    }

    public static void notEmpty(CharSequence arguments, String msg){
        notNull(arguments, msg);
        if(arguments.length() == 0)
            throw new NullPointerException(msg);
    }

    public static void notEmpty(Map map, String msg){
        notNull(map, msg);
        if(map.isEmpty())
            throw new NullPointerException(msg);
    }
}
