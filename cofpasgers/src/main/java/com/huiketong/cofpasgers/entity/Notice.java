package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    Integer id;
    @Column(columnDefinition = "VARCHAR(255) default '恭喜' COMMENT '通知内容'")
    String context;
    @Column(columnDefinition = "VARCHAR(255) default '' COMMENT '代理的名字'")
    String agentName;
    @Column(columnDefinition = "datetime  COMMENT '通知时间'")
    Date addTime;
    @Column(columnDefinition = "decimal(10,2) default 0.00 COMMENT '佣金，积分'")
    BigDecimal score;
    @Column(columnDefinition = "int(11) default 0 COMMENT '公司ID'")
    Integer companyId;
}
