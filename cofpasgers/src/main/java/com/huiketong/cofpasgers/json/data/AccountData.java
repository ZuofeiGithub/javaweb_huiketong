package com.huiketong.cofpasgers.json.data;

import lombok.Data;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 14:28
 * @Version 1.0
 */
@Data
public class AccountData {
    String account;
    String min_withdraw;
    BankInfoData bank_info;
    String is_real_name;
    String is_withdraw_open;
}
