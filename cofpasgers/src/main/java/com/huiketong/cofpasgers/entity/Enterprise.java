package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 企业信息
 */
@Entity
@Data
@Table(name = "enterprise")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业名字'")
    String enterName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业登录账号'")
    String enterLoginName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业登录密码'")
    String enterLoginPwd;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业电话'")
    String enterTelphone;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业法人'")
    String enterLegalperson;
    @Column(columnDefinition = "date COMMENT '企业成立日期'")
    Date establelishDate;
    @Column(columnDefinition = "int(11) default 0 COMMENT '企业状态'")
    Integer enterStatus;
    @Column(columnDefinition = "int(11) default 0 COMMENT '人数上限'")
    Integer personOnline;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业地址'")
    String enterAddress;
    @Column(columnDefinition = "int(11) default 0 COMMENT '企业排序'")
    Integer enterOrder;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业LOGO'")
    String enterLogo;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '企业介绍'")
    String entercomment;
    @Column(columnDefinition = "int(255) default 30000 COMMENT '当前可赚佣金'")
    Integer brokerage;
}
