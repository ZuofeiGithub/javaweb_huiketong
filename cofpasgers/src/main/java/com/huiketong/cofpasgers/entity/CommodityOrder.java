package com.huiketong.cofpasgers.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品订单表
 */
@Entity
@Data
public class CommodityOrder {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 0 COMMENT '订单id'")
    Integer id;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '商家订单号'")
    String orderNum;

    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;

    @Column(columnDefinition = "int(11) default 0 COMMENT '客户ID'")
    Integer userId;

    @Column(columnDefinition = "varchar(225) default 0 COMMENT '商品订单图片url'")
    String  commodityOrderImg;

    @Column(columnDefinition = "varchar(50) default '' COMMENT '客户名称'")
    String customerName ;

    @Column(columnDefinition = "varchar(225) default '' COMMENT '商品名称'")
    String commodityName ;

    @Column(columnDefinition = "decimal(11,2) default 0.00 COMMENT '活动特价'")
    BigDecimal activityPrice ;

    @Column(columnDefinition = "int(4) default 1 COMMENT '1 未核销  2 已核销'")
    Integer status ;
    @Column(columnDefinition = "int(4) default 0 COMMENT ''")
    Integer orderStatus;
    @Column(columnDefinition = "DateTime COMMENT '订单生成时间'")
    Date insertTime;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '支付类型'")
    String payType;
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品ID'")
    Integer commodityId;

}
