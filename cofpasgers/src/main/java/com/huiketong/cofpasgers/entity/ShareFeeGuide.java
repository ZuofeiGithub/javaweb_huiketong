package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: 左飞
 * @Date: 2019/1/8 11:15
 * @Version 1.0
 * 分佣指南表
 */
@Entity
@Data
@Table(name = "share_free_guide")
public class ShareFeeGuide {
    @Id
    @GeneratedValue()
    Integer id;
    Integer companyId;
    @Column(columnDefinition = "TEXT COMMENT '分佣指南内容'")
    String guideContext;
}
