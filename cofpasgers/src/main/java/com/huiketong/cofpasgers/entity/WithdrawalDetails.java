package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 15:01
 * @Version 1.0
 * 提现明细表
 */
@Entity
@Data
@Table(name = "withdrawal_details")
public class WithdrawalDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) default 0")
    Integer id;
    BigDecimal money;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '提现描述'")
    String descript;
    @Column(columnDefinition = "datetime COMMENT '提现时间'")
    Date drawalTime;
    @Column(columnDefinition = "datetime COMMENT '操作时间'")
    Date updateTime;
    @Column(columnDefinition = "int(10) default 0 COMMENT '用户ID'")
    Integer user_id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer comid;
    @Column(columnDefinition = "int(4) default 0 COMMENT '提现状态 1 审核中 2 已发放 3 已核销'")
    Integer status;
    @Column(columnDefinition = "varchar(20) default '' COMMENT '经纪人姓名'")
    String angentname;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '银行卡号码'")
    String bankNum;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '银行卡名字'")
    String bankName;


}
