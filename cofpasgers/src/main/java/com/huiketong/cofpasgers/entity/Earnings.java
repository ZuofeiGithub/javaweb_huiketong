package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 14:54
 * @Version 1.0
 * <p>
 * 收益明细表
 */
@Entity
@Data
@Table(name = "earnings")
public class Earnings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(11) default 0")
    Integer id;
    BigDecimal money;
    @Column(columnDefinition = "varchar(11) default ''")
    String descript;
    @Column(columnDefinition = "datetime COMMENT '获取收益时间'")
    Date addTime;
    @Column(columnDefinition = "int(11) default 0")
    Integer userId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '佣金类型 1.分享赚佣金 2.注册赚佣金3.量房4.已签合同5.兑换积分'")
    Integer type;
    @Column(columnDefinition = "varchar(11) COMMENT '经纪人姓名'")
    String angentname;
    @Column(columnDefinition = "varchar(11) COMMENT '客户姓名'")
    String cusname;
    @Column(columnDefinition = "varchar(11) COMMENT '客户联系电话'")
    String cusnamePhone;
    @Column(columnDefinition = "varchar(11) COMMENT '客户id'")
    String cusId;
    @Column(columnDefinition = "varchar(11)  COMMENT '佣金层级'")
    String commissionlevel;
    @Column(columnDefinition = "varchar(11)  COMMENT '对应佣金占比'")
    String commissionpercentage;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer comId;
}
