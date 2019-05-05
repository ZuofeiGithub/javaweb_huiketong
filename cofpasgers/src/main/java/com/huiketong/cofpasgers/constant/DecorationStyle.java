package com.huiketong.cofpasgers.constant;

import org.springframework.stereotype.Controller;

/**
 * @Author: 左飞
 * @Date: 2018/12/24 17:12
 * @Version 1.0
 * 客户装修风格
 */
public class DecorationStyle {
    static String STYLE_NOW = "现代";
    static String STYLE_EUROPEAN = "欧式";
    static String STYLE_AMERICAN = "美式";
    static String STYLE_CHINA = "中式";
    static String STYLE_JAPAN = "日式";
    static String STYLE_NORDIC = "北欧";
    static String STYLE_OTHER = "其他";

    private static String style;

    public static String STYLE(int id) {
        switch (id) {
            case 0:
                style = STYLE_NOW;
                break;
            case 1:
                style = STYLE_EUROPEAN;
                break;
            case 2:
                style = STYLE_AMERICAN;
                break;
            case 3:
                style = STYLE_CHINA;
                break;
            case 4:
                style = STYLE_JAPAN;
                break;
            case 5:
                style = STYLE_NORDIC;
                break;
            case 6:
                style = STYLE_OTHER;
                break;
        }
        return style;
    }
}
