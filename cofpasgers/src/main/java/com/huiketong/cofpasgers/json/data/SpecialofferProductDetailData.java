package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

@Data
public class SpecialofferProductDetailData {
    List<String> image;
    String title;
    String id;
    String unit;
    String sale_price;
    String raw_price;
    String concern;
    String activity;
    String detail;
    String deposit;
    String is_focus;
    String logo;
}
