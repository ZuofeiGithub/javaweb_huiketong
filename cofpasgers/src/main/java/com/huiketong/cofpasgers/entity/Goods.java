package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition = "varchar(32) default '' COMMENT '标题'")
    String title;
    @Column(columnDefinition = "varchar(32) default '' COMMENT '子标题'")
    String subtitle;
    @Column(columnDefinition = "varchar(32) default '' COMMENT '图片'")
    String image;
    @Column(columnDefinition = "varchar(32) default '' COMMENT '推荐按钮名字'")
    String linkname;
    @Column(columnDefinition = "varchar(32) default '' COMMENT '标签字符串  按|分割'")
    String label;
    @Column(columnDefinition = "int(11) COMMENT '公司ID'")
    Integer companyId;
}
