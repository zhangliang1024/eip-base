package com.eip.sample.gray.zuul.comm;

import java.util.Map;

public class RibbonFilterMapHolder {

    private static final ThreadLocal<Map<String, String>> ribbonFilterMap = new ThreadLocal<Map<String, String>>();

    public static void add(Map<String, String> map) {
        ribbonFilterMap.set(map);
    }

    public static Map<String, String> getRibbonFilterMap() {
        return ribbonFilterMap.get();
    }

    public static void remove() {
        ribbonFilterMap.remove();
    }
}
