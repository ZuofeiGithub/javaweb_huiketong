package com.huiketong.cofpasgers.entity;

import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统用户
 */
@Data
@Entity
@Table(name = "system_user")
public class SystemUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '用户名'")
    String username;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '手机号码'")
    String telphone;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '登陆密码'")
    String loginPassward;
    @Column(columnDefinition = "datetime COMMENT '注册日期'")
    Date reg_date;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '登录名'")
    String loginName;
}
