package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 版本管理表
 */
@Entity
@Data
public class Version {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(10) default 0")
    Integer Id;
    @Column(columnDefinition = "int(3) default 0 COMMENT '0无更新,1更新,2强制更新'")
    Integer status;
    @Column(columnDefinition = "varchar(255) COMMENT '平台 android ios'")
    String platform;
    @Column(columnDefinition = "varchar(255) COMMENT '更新说明'")
    String descipt;
    @Column(columnDefinition = "varchar(255) COMMENT '下载地址'")
    String url;
    @Column(columnDefinition = "varchar(12) COMMENT '版本号'")
    String version;
}
