package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 合作商家
 */
@Data
@Entity
public class Merchants {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer merId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '合作商家名字'")
    String merName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '合作商家地址'")
    String merAddress;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '合作商家LOGO'")
    String merLogo;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '合作商家跳转地址'")
    String merUrl;
    @Column(columnDefinition = "int(11) default 0 COMMENT '合作商家排序'")
    Integer merOrder;
    @Column(columnDefinition = "int(11) default 0 COMMENT '企业登录ID'")
    Integer enterId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '商家标语'")
    String descript;

}
