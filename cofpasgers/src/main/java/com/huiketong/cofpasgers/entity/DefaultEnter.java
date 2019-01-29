package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 默认企业
 */
@Data
@Entity
public class DefaultEnter {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(10) default 0")
    Integer Id;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '用户登录名'")
    String userId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer compayId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '用户手机'")
    String userTelphone;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '公司名'")
    String compayName;
}
