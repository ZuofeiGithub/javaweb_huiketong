package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 轮播图标题
 */
@Data
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer bannerId;
    @Column(columnDefinition = "varchar(11) default '' COMMENT '轮播图名字'")
    String name;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(11) default 1 COMMENT '轮播图状态'")
    Integer status;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '轮播图描述'")
    String descript;
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    Date createDate;
    @Column(columnDefinition = "varchar(225) default '' COMMENT '轮播图地址'")
    String imgurl;
    @Column(columnDefinition = "varchar(225) default '' COMMENT '轮播图跳转地址'")
    String trankurl;
    @Column(columnDefinition = "int(11) default 0 COMMENT '排序'")
    Integer sort;
}
