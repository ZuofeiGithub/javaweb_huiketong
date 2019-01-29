package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 积分规则
 */
@Data
@Entity
public class IntegralRule {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 1 COMMENT '积分id'")
    Integer id;

    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;

    @Column(columnDefinition = "int(11) default 0 COMMENT '最低兑换金额'")
    Integer minPrice ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '1元=多少积分'")
    Integer  rmbForPoint;

    @Column(columnDefinition = "int(4) default 1 COMMENT '是否开启积分兑换 1 不开启 2开启'")
    Integer isPoint ;

    @Column(columnDefinition = "int(4) default 1 COMMENT '是否开启抽奖功能 1 不开启 2 开启'")
    Integer isOpenLotto ;

    @Column(columnDefinition = "varchar(1024) default '' COMMENT '8个奖项'")
    String dials ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '消费积分 多少积分抽奖一次'")
    Integer consume ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '登录奖励'")
    Integer loginIntegral ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '签到奖励'")
    Integer signIntegral ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '推荐客户奖励'")
    Integer recomIntegral ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '认证奖励'")
    Integer authIntegral ;

    @Column(columnDefinition = "int(11) default 0 COMMENT '邀请奖励'")
    Integer inviteIntegral ;




}
