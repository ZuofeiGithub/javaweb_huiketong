package com.huiketong.cofpasgers.config;

import com.huiketong.cofpasgers.entity.Commission;
import com.huiketong.cofpasgers.repository.CommissionRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author: 左飞
 * @Date: 2018/12/24 12:54
 * @Version 1.0
 */
@Configuration
@EnableScheduling
public class TaskConfig {
    @Autowired
    EnterpriseRepository enterpriseRepository;
    /**
     * 秒（0~59）
     * 分钟（0~59）
     * 小时（0~23）
     * 天（0~31）
     * 月（0~11）
     * 星期（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
     * 年份（1970－2099）
     * '/':每隔
     * ',':取值
     * '-':区间内
     * '*':所有可能值
     * “？”字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值
     * 当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“？”
     *
     * “L” 字符仅被用于天（月）和天（星期）两个子表达式，它是单词“last”的缩写
     * 如果在“L”前有具体的内容，它就具有其他的含义了。
     */
    @Scheduled(cron = "0 0 0/2 * * *")
    public void runTask(){
        System.out.println("开始执行任务");
        enterpriseRepository.updateBrokerage();
    }
}
