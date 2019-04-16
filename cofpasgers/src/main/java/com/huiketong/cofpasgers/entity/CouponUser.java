package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 领券的用户
 */

@Entity
@Data
public class CouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer companyId;
    String shareUser;
    String sharePhone;
    String couponPhone;
    Date getTime;
}
