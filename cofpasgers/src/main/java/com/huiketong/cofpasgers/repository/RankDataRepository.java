package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.RankData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/29 11:15
 * @Version 1.0
 */
public interface RankDataRepository extends JpaRepository<RankData, Integer> {
    /**
     * 周排行榜
     */
    @Query(value = "select SUM(earnings.money) as money,agent.agent_name as nickname,(select count(1) from customer where agent_id = earnings.user_id) as client_num  from earnings LEFT JOIN agent on earnings.user_id = agent.id where earnings.user_id in (?1) and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(earnings.add_time) GROUP BY earnings.user_id order by money desc limit ?2,?3", nativeQuery = true)
    List<RankData> findWeekRanks(List<Integer> users, int limit, int page);

    /**
     * 周排行榜
     */
    @Query(value = "select SUM(earnings.money) as money,agent.agent_name as nickname,(select count(1) from customer where agent_id = earnings.user_id) as client_num  from earnings LEFT JOIN agent on earnings.user_id = agent.id where earnings.user_id in (?1)  GROUP BY earnings.user_id order by money desc limit ?2,?3", nativeQuery = true)
    List<RankData> findAllRanks(List<Integer> users, int limit, int page);
}
