package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金表
 */
@Entity
@Data
public class Commission {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int(10) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "decimal(10,2) default 0.00 COMMENT '量房佣金'")
    BigDecimal score;
    @Column(columnDefinition = "int(10) default 0 COMMENT '是否设置提现功能'")
    Integer bWithdrawOpen;
    @Column(columnDefinition = "decimal(10,2) default 0.00 COMMENT '提现最低限额'")
    BigDecimal minWithdraw;
    @Column(columnDefinition = "Date COMMENT '修改时间'")
    Date updateTime;
    @Column(columnDefinition = "float(10,2) default 0.00 COMMENT '一级佣金比例'")
    Float firstPercentage;
    @Column(columnDefinition = "float(10,2) default 0.00 COMMENT '二级佣金比例'")
    Float secondPercentage;

    @Column(columnDefinition = "float(10,2) default 0.00 COMMENT '其他一级佣金比例'")
    Float ofirstPercentage;
    @Column(columnDefinition = "float(10,2) default 0.00 COMMENT '其他二级佣金比例'")
    Float osecondPercentage;

    @Column(columnDefinition = "int(11) default 5 COMMENT '分享次数'")
    Integer shareCount;
    @Column(columnDefinition = "decimal(10,2) default 5.00 COMMENT '邀请佣金'")
    BigDecimal invitScore;

    @Column(columnDefinition = " text COMMENT '内部备注'")
    String nbRemark;

    @Column(columnDefinition = "  text COMMENT '其他备注'")
    String qtRemark;
}
