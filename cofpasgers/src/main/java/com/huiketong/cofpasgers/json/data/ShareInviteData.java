package com.huiketong.cofpasgers.json.data;

import lombok.Data;

@Data
public class ShareInviteData {
    String code;
    String url;
    String title;
    String text;
    String image;
    String invite_num;
    String invite_money;
}
