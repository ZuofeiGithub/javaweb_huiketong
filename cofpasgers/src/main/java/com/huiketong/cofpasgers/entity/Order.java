package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2019/1/14 14:16
 * @Version 1.0
 * 订单表
 */
@Entity
@Data
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(columnDefinition = "datetime COMMENT '下单时间'")
    Date preorderTime;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '订单号'")
    String orderId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '用户ID'")
    Integer userId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品ID'")
    Integer goodsId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '交易类型'")
    String type;
    @Column(columnDefinition = "int(11) default 0 COMMENT '交易状态'")
    Integer status;
    @Column(columnDefinition = "decimal(11,2) default 0.00 COMMENT '订单金额'")
    BigDecimal money;
}
