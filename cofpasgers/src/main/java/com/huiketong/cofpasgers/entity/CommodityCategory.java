package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品品类表
 */
@Entity
@Data
public class CommodityCategory {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品品类id'")
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "varchar(100) default '' COMMENT '商品品类名称'")
    String categoryName ;
}
