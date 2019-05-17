package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 推广活动
 */


@Data
@Entity
public class PromotActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer companyId;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '活动标题'")
    String title;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '活动封面图'")
    String coverImg;
    @Column(columnDefinition = "text  COMMENT '活动详情'")
    String detail;
    @Column(columnDefinition = "int(11) default 0 COMMENT '活动类型'")
    Integer type;
    @Column(columnDefinition = "datetime COMMENT '开始时间'")
    Date beginTime;
    @Column(columnDefinition = "datetime COMMENT '结束时间'")
    Date endTime;
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    Date createTime;
    @Column(columnDefinition = "varchar(32) COMMENT '唯一序列号'")
    String actAlias;
}
