package com.huiketong.cofpasgers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: 左飞
 * @Date: 2018/12/28 10:40
 * @Version 1.0
 */
@Entity
public class Systemenu {
    @Id
    @GeneratedValue()
    @Column(columnDefinition = "int(11) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(255) default ''")
    String name;
    @Column(columnDefinition = "varchar(255) default ''")
    String url;
    @Column(columnDefinition = "int(11) default 0 COMMENT '主菜单'")
    Integer menuId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '子菜单'")
    Integer menuPid;
}
