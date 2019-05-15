package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 在线学院
 */
@Entity
@Data
public class OnlineCollege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(11) default 0 COMMENT '文章类型'")
    Integer articleType;
    @Column(columnDefinition = "varchar(255) default '' COMMENT '文章标题'")
    String title;
    @Column(columnDefinition = "text comment '文章详情'")
    String  particulars;
    @Column(columnDefinition = "varchar(1024) default '' comment '视频链接地址'")
    String videoUrl;
    @Column(columnDefinition = "varchar(4084) default '' COMMENT '视频简介'")
    String videoIntro;
    @Column(columnDefinition = "datetime COMMENT '创建时间'")
    Date createTime;
}
