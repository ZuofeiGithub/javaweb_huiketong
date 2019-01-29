package com.huiketong.cofpasgers.json.data;

import lombok.Data;

/**
 * 认证返回数据
 */
@Data
public class CertificationInfoData {
    String real_name;
    String id_card;
    String id_card_photo;
    String reason;
}
