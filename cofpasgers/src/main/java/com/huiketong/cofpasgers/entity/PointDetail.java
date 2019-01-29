package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: 左飞
 * @Date: 2019/1/17 16:26
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "point_detail")
public class PointDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(columnDefinition = "int(11) default 0 comment '用户ID'")
    Integer userId;
    @Column(columnDefinition = "int(11) default 0 comment '公司ID'")
    Integer companyId;
    @Column(columnDefinition = "int(11) default 0 comment '积分'")
    Integer point;
    @Column(columnDefinition = "int(11) default 0 comment '积分类型 1登陆积分,2签到奖励,3推荐客户奖励,4认证奖励,5邀请奖励,6兑换积分'")
    Integer type;
    @Column(columnDefinition = "datetime comment '获得积分时间'")
    Date addTime;
    @Column(columnDefinition = "varchar(255) default '' comment '积分描述'")
    String descript;
    @Column(columnDefinition = "int(11) default 0 comment '获得还是支出 1增加 2减少'")
    Integer status;
}
