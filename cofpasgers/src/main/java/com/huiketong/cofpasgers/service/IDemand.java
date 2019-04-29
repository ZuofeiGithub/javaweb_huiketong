package com.huiketong.cofpasgers.service;

import com.alipay.api.AlipayApiException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

/**
 * @Author: 左飞
 * @Date: 2019/1/9 10:19
 * @Version 1.0
 * 业务接口
 */
public interface IDemand {
    //业务函数
    void bussiness(Object o) throws ParseException, AlipayApiException;
}
