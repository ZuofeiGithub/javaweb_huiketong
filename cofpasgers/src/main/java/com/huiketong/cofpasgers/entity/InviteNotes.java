package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 邀请说明
 */
@Data
@Entity
public class InviteNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer compayId;
    String benefits;//邀请好友奖励
    String rewardDesc; //奖励说明
    String ruleDesc; //规则说明
}
