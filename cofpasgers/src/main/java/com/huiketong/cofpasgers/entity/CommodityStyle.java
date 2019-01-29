package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 商品风格表
 */
@Entity
@Data
public class CommodityStyle {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品风格id'")
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "varchar(100) default '' COMMENT '商品风格名称'")
    String typeName ;

}
