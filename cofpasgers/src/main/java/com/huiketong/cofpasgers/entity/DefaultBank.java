package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 9:54
 * @Version 1.0
 */
@Entity
@Data
public class DefaultBank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(25) default 0 COMMENT '默认银行卡号码'")
    String bankNum;
    @Column(columnDefinition = "int(11) default 0  COMMENT '默认用户id'")
    Integer userId;
}
