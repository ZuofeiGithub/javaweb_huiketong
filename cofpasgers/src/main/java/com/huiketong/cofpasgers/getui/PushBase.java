package com.huiketong.cofpasgers.getui;

import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2019/1/23 14:12
 * @Version 1.0
 */
public class PushBase {
    //此参数为测试参数，正式需要屏蔽
    protected static final String APPID = "fLv8QdsOwY566gHa6FNPa4";
    protected static final String APPKEY = "6uwPabllLP9QBBXTzBtJHA";
    protected static final String MASTERSECRET = "FlMyrULt0m5G4qPaID1F81";
    protected static final String AppSecret = "v4338SxWQC6PMF1snD7sz2";
    protected static final String API = "http://sdk.open.api.igexin.com/apiex.htm";     //OpenService接口地址
    protected static final String CLIENTID = "618ab3bb984959e218dadfe82f5da767";
    protected static String getDate(){
        Date date = new Date();
        return date.toString();
    }
}
