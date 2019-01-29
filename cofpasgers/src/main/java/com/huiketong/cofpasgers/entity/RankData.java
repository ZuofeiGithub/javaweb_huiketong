package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @Author: 左飞
 * @Date: 2018/12/29 10:23
 * @Version 1.0
 */
@Data
@Entity
public class RankData {
    @Id
    BigDecimal money;
    String nickname;
    Integer client_num;
}
