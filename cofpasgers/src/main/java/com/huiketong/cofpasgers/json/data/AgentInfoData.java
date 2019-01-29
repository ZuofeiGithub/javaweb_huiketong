package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/25 14:53
 * @Version 1.0
 */
@Data
public class AgentInfoData {
    List<BannerData> banner;
//    String my_report_client;
//    String line_report_client;
//    String my_invite_broker;
//    String line_invite_broker;
//    String my_make;
//    String line_make;
    //本月报备
    String report;
    //本月到访
    String visit;
    //本月成交
    String mab;
    String wait_client;
    String make_client;
}
