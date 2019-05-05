package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 商品图片关联表
 */
@Entity
@Data
public class CommodityImg {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 0 COMMENT '图片id'")
    Integer id;

    @Column(columnDefinition = "int(11) default 0 COMMENT '商品ID'")
    Integer commodityd;

    @Column(columnDefinition = "varchar(225) default '' COMMENT '商品图片地址'")
    String commodityImgUrl;

}
