package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class VoucherDetail {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer id;
    String price;
    String title;
    String context;
    Date startTime;
    Date endTime;
    Integer companyId;
}
