package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "share_context")
public class ShareContext {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int(10) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '分享标题'")
    String title;
    @Column(columnDefinition = "varchar(4096) default '' COMMENT '分享内容'")
    String context;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '分享图片'")
    String imgUrl;
    @Column(columnDefinition = "decimal(20,2) default 0000.00 COMMENT '分享佣金'")
    BigDecimal money;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '跳转地址'")
    String url;
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    String datetime;
}
