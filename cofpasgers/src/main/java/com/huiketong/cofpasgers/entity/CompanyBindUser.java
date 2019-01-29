package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 公司用户绑定表
 */
@Entity
@Data
public class CompanyBindUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '用户ID'")
    Integer userId;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '用户电话号码'")
    String userTel;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "varchar(125) default '' COMMENT '邀请码'")
    String inviteCode;
}
