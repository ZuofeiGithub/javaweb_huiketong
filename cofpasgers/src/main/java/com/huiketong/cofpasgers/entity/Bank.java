package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: ￣左飞￣
 * @Date: 2018/12/25 21:24
 * @Version 1.0
 */
@Entity
@Data
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int(10) default 0 COMMENT '用户ID'")
    Integer userId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '持卡人姓名'")
    String name;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '所在城市'")
    String city;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '支行'")
    String branch;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '银行卡号'")
    String bankNum;
    @Column(columnDefinition = "int(10) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(10) default 0 COMMENT '哪家银行1中国银行2中国工商银行3中国建设银行4中国农业银行'")
    Integer bankId;
}
