package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 轮播图能容
 */
@Data
@Entity
public class BannerContext {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '轮播图id'")
    Integer bannerId;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '轮播图片'")
    String pic_url;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '轮播图片描述'")
    String pic_descript;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '轮播图跳转链接'")
    String pic_link_url;
    @Column(columnDefinition = "int(11) default 0 COMMENT '轮播图排序'")
    Integer pic_order_num;
}
