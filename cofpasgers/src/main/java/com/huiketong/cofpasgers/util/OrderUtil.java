package com.huiketong.cofpasgers.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class OrderUtil {

    private static OrderUtil instance;

    private OrderUtil() {

    }

    public static OrderUtil getInstance() {
        if (instance == null) {
            synchronized (OrderUtil.class) {
                if (instance == null) {
                    instance = new OrderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @Desc 获取订单号
     */
    public synchronized String getOrderCode() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(calendar.getTime());
    }

    /**
     * @Desc 获取订单号后缀
     */
    public static String getOrderCodePostfix() {
        Random R = new Random();
        return String.valueOf(R.nextInt(9000) + 1000);
    }
}
