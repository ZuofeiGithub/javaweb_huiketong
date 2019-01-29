package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 经纪人
 */
@Data
@Entity
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(20) default '' COMMENT '经纪人姓名'")
    String agentName;
    @Column(columnDefinition = "int(11) default 0 COMMENT '经纪人类型'")
    Integer type;
    @Column(columnDefinition = "TINYINT default 0 COMMENT '是否推送消息'")
    Boolean imposer;
    @Column(columnDefinition = "varchar(20) default '' COMMENT '经纪人电话'")
    String telphone;
    @Column(columnDefinition = "varchar(20) default '' COMMENT '经纪人登录名'")
    String loginUsername;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '经纪人登录密码'")
    String password;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '邀请码'")
    String initCode;
    @Column(columnDefinition = "datetime  COMMENT '注册时间'")
    Date regDatetime;
    @Column(columnDefinition = "int(11) default 0 COMMENT '上级经纪人ID'")
    Integer superId;
    @Column(columnDefinition = "varchar(10) default '' COMMENT '上级经纪人姓名'")
    String superiorName;
    @Column(columnDefinition = "int(11) default 0 COMMENT '上级的上级经纪人ID'")
    Integer topId;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '上级的上级经纪人姓名'")
    String topName;
    @Column(columnDefinition = "int(11) default 0 COMMENT '是否认证 0未认证 1已经认证 2等待认证'")
    Integer bCertification;
    @Column(columnDefinition = "int(12) default 0 COMMENT '邀请客户数量'")
    Integer reconCustomNam;
    @Column(columnDefinition = "int(12) default 0 COMMENT '成交客户总量'")
    Integer dealCustomNum;
    @Column(columnDefinition = "int(12) default 0 COMMENT '邀请经纪人总数'")
    Integer initAgentNam;
    @Column(columnDefinition = "decimal(10,2) default 0.00 COMMENT '账户余额'")
    BigDecimal accountBalance;
    @Column(columnDefinition = "VARCHAR(255) COMMENT '提现密码'")
    String drawlPad;
    @Column(columnDefinition = "varchar(1024) default '' COMMENT '用户token'")
    String accessToken;
    @Column(columnDefinition = "int(12) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(name = "real_name",columnDefinition="VARCHAR(10) COMMENT '真实名字'")
    String realName;
    @Column(name = "id_card",columnDefinition="VARCHAR(18) COMMENT '身份证号码'")
    String idCard;
    @Column(name = "card_photo",columnDefinition="VARCHAR(30) COMMENT '身份证正面'")
    String cardPhoto;
    @Column(columnDefinition = "int(11) default 0 COMMENT '积分'")
    Integer points;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '用户头像'")
    String avatar;
    @Column(columnDefinition = "varchar(255) default '身份信息不正确' COMMENT '拒绝原由'")
    String reason;
    BigDecimal rankAccount;
    @Column(columnDefinition = "int(11) default 0 COMMENT '禁止登陆 0可以登陆，1禁止'")
    Integer forbid;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '设备ID'")
    String deviceId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '客户端Token'")
    String clientToken;
}
