package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class VoucherShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String context;
    String image;
    String linkUrl;
    Integer companyId;
    Integer sharetype;
    Integer goodsId;
    Date createTime;
}
