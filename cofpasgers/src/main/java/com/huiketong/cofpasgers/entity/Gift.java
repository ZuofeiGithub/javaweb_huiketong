package com.huiketong.cofpasgers.entity;

import lombok.Data;

/**
 * @Author: 左飞
 * @Date: 2019/1/18 16:17
 * @Version 1.0
 * 奖品
 */
@Data
public class Gift {

    private Integer id;         //奖品Id
    private Integer point;    //奖积分
    private Double prob;    //获奖概率
}