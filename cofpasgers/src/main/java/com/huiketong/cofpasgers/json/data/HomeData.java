package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

@Data
/**
 * 首页响应数据
 */
public class HomeData {
    List<NoticeData> notice;
    List<StoreData> store;
    List<BannerData> banner;
    String brokerage;
    HomeInfoData info;
}
