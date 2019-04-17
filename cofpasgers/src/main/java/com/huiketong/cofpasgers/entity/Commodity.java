package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 */
@Entity
@Data
public class Commodity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品id'")
    Integer id;

    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;

    @Column(columnDefinition = "int(11) default 0 COMMENT '品类ID'")
    Integer CategoryId;

    @Column(columnDefinition = "int(11) default 0 COMMENT '风格ID'")
    Integer StyleId;

    @Column(columnDefinition = "varchar(225) default '' COMMENT '商品名称'")
    String commodityName ;

    @Column(columnDefinition = "varchar(11) default '' COMMENT '单位'")
    String danwei ;

    @Column(columnDefinition = "decimal(10,2) default 0 COMMENT '商品原价'")
    BigDecimal originalPrice ;

    @Column(columnDefinition = "decimal(10,2) default 0 COMMENT '活动特价'")
    BigDecimal activityPrice ;

    @Column(columnDefinition = "decimal(10,2) default 0.0 COMMENT '预付定金'")
    BigDecimal depositMoney;

    @Column(columnDefinition = "int(11) default 0 COMMENT '关注人数'")
    Integer concernedPeople  ;

    @Column(columnDefinition = "text  COMMENT '活动说明'")
    String  activityDescription ;

    @Column(columnDefinition = "text  COMMENT '产品详情'")
    String  productDetails ;
    @Column(columnDefinition = "varchar(1024) COMMENT '标签'")
    String label;
    @Column(columnDefinition = "varchar(32) COMMENT '推荐按钮的名字'")
    String linkname;
    @Column(columnDefinition = "datetime COMMENT '添加商品时间'")
    Date addTime;

}
