package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * 推广文章
 */
@Data
@Entity
@Table(name = "extension_article")
public class ExtensionArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    String title;
    Integer commission;
    Integer extension_order;
    String detail_des;
    String img_url;
    String extension_telphone;
}
