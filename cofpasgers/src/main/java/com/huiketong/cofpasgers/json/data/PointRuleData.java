package com.huiketong.cofpasgers.json.data;

import lombok.Data;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/28 15:30
 * @Version 1.0
 */
@Data
public class PointRuleData {
    String min_money;
    String rmb_for_point;
    String point;
    String day_point;
    String is_point;
    String is_open_lotto;
    List<PointDialsData> dials;
    String consume;
    TaskData task;
}
