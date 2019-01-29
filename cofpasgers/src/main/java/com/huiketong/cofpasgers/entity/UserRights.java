package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

//用户权限表
@Data
@Entity
public class UserRights {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(11) default '' COMMENT '用户联系方式'")
    String userTel;
    @Column(columnDefinition = "varchar(11) default '' COMMENT '登陆名'")
    String loginName;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '用户名'")
    String userName;
    @Column(columnDefinition = "int(11) default 0  COMMENT '权限'")
    Integer userRight;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '权限名'")
    String rightName;
}
