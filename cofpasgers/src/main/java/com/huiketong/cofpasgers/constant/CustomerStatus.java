package com.huiketong.cofpasgers.constant;

/**
 * @Author: 左飞
 * @Date: 2018/12/25 9:06
 * @Version 1.0
 * 客户状态
 * 0全部客户1待审核2期房3暂时不装4已量房5已进店6跟踪中7已交定金8已签合同9无效客户
 */
public class CustomerStatus {
    static String status;
    static String ALL_CUSTOMER = "全部客户";
    static String WAIT_CUSTOMER = "待审核";
    static String WEEK_CUSTOMER = "期房";
    static String NOBUILD_CUSTOMER = "暂时不装";
    static String HAVE_CUSTOMER = "已量房";
    static String IN_CUSTOMER = "已进店";
    static String FLOW_CUSTOMER = "跟踪中";
    static String PAY_CUSTOMER = "已交定金";
    static String SIGN_CUSTOMER = "已签合同";
    static String NO_CUSTOMER = "无效客户";

    public static String STATUS(int id) {
        switch (id) {
            case 1:
                status = "1";
                break;
            case 2:
                status = "2";
                break;
            case 3:
                status = "3";
                break;
            case 4:
                status = "4";
                break;
            case 5:
                status = "5";
                break;
            case 6:
                status = "6";
                break;
            case 7:
                status = "7";
                break;
            case 8:
                status = "8";
                break;
            case 9:
                status = "9";
                break;
        }
        return status;
    }
}
