package com.huiketong.cofpasgers.json.chat;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/16 13:07
 * @Version 1.0
 */
@Data
public class InitData {
    MineData mine;
    List<FriendData> friend;
    List<GroupData> group;
}
