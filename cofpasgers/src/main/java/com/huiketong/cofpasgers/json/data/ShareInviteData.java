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
    String benefits; //邀请好友有奖
    String ruleDesc; //规则说明
    String rewardDesc; //奖励说明
}
