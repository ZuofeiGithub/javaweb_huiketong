package com.huiketong.cofpasgers.json.chat;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/16 10:23
 * @Version 1.0
 */
@Data
public class FriendData {
    String groupname;
    Integer id;
    List<FriendListData> list;
}
