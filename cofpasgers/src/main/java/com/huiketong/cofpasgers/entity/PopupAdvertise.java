package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 弹屏公告
 */
@Data
@Entity
public class PopupAdvertise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "varchar(10) default ''")
    String name;
    @Column(columnDefinition = "varchar(10) default ''")
    String descript;
    @Column(columnDefinition = "varchar(10) default ''")
    String pic_url;
    @Column(columnDefinition = "varchar(10) default ''")
    String pic_descript;
    @Column(columnDefinition = "varchar(10) default ''")
    String pic_link_url;
}
