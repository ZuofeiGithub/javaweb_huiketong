package com.huiketong.cofpasgers.entity;

import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: 左飞
 * @Date: 2019/1/10 9:55
 * @Version 1.0
 * 关注的商品表
 */
@Entity
@Data
public class AttentionGoods {
    @Id
    @GeneratedValue()
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '用户ID'")
    Integer userId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '商品ID'")
    Integer goodsId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '是否关注 1 关注 0 取消关注'")
    Integer status;
}
