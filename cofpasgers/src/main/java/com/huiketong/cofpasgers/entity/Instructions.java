package com.huiketong.cofpasgers.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 邀请好友说明
 */
@Entity
@Data
public class Instructions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String benefits; //邀请好友有奖
    String ruleDesc; //规则说明
    String rewardDesc; //奖励说明
    Date createTime; //创建时间
    Integer companyId; //公司ID
}
