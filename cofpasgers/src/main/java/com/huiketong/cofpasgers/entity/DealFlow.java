package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 17:10
 * @Version 1.0
 */
@Table(name = "deal_flow")
@Entity
@Data
public class DealFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) default 0")
    Integer id;
    Date time;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "decimal COMMENT '金额'")
    BigDecimal money;
    @Column(columnDefinition = "int(11) default 0 COMMENT '用户ID'")
    Integer userId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '银行卡号码'")
    String bankNum;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '银行卡名字'")
    String bankName;
}
