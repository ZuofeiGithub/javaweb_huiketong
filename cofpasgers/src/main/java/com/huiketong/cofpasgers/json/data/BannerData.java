package com.huiketong.cofpasgers.json.data;

import lombok.Data;

@Data
public class BannerData {
    String image;
    String url;
    String title;
    String type; //1.外部url,2.内部view
}
