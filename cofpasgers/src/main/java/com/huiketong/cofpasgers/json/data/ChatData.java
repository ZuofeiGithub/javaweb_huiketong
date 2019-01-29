package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/14 15:28
 * @Version 1.0
 */
@Data
public class ChatData {
    String username;
    String company_username;
    String user_avatar;
    String company_avatar;
    List<AgentListData> user_list;
}
