package com.huiketong.cofpasgers.json.chat;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/16 10:30
 * @Version 1.0
 */
@Data
public class InitResp {
    Integer code;
    String msg;
    InitData data;

}
