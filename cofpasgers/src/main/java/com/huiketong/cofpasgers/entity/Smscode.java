package com.huiketong.cofpasgers.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 短信验证码表
 */
@Entity
@Data
public class Smscode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int(10) default 0")
    private Integer id;
    private String type;
    private String code;
    private Integer userId;
    private String telphone;
    private Date sendTime;
}
