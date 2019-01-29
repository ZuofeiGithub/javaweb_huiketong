package com.huiketong.cofpasgers.constant;

/**
 * @Author: ￣左飞￣
 * @Date: 2018/12/24 21:38
 * @Version 1.0
 * 装修方案
 */
public class DecorationPlan {
    static String ALL_PLAN = "全包";
    static String HALF_PLAN = "半包";
    static String OTHER_PLAN = "其他";

    static String plan;

    public static String PLAN(int id){
        switch (id)
        {
            case 0:
                plan = ALL_PLAN;
                break;
            case 1:
                plan = HALF_PLAN;
                break;
            case 2:
                plan = OTHER_PLAN;
                break;
        }
        return plan;
    }
}
