package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户
 */
@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int default 0 COMMENT '经纪人ID'")
    Integer agentId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '经纪人姓名'")
    String agentName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '客户名字'")
    String customerName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '客户手机'")
    String telphone;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '客户性别'")
    String sex;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '客户地址'")
    String detailAddress;
    @Column(columnDefinition = "int default 0 COMMENT '客户装修类型'")
    Integer style;
    @Column(columnDefinition = "VARCHAR(10) default 0 not null COMMENT '装修方案'")
    Integer scheme;
    @Column(columnDefinition = "int(10) default 0 COMMENT '客户状态'")
    Integer verifyStatus;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '装修备注'")
    String renovRemark;
    @Column(columnDefinition = "int default 0 COMMENT '装修面积'")
    Integer houseArea;
    @Column(columnDefinition = "int default 0 COMMENT '装修预算'")
    Integer renovBudget;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '撤销原因'")
    String revokeReason;
    @Column(columnDefinition = "datetime  COMMENT '推荐时间'")
    Date recomDatetime;
    @Column(columnDefinition = "int default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "decimal(10,2) default 0 COMMENT '签约总价'")
    BigDecimal signPrice;
}
