package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Banner;
import com.huiketong.cofpasgers.entity.Earnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 14:59
 * @Version 1.0
 */
@Repository
public interface EarningsRepository extends JpaRepository<Earnings, Integer> {
    @Override
    <S extends Earnings> S save(S s);

    @Query(value = "SELECT * FROM  earnings  WHERE user_id = ?1  ORDER BY add_time DESC LIMIT ?2,?3", nativeQuery = true)
    List<Earnings> FindEarningsByAgentId(int userId, int page, int limit);


    @Query(value = "select count(1) from earnings where user_id = ?1 and type=?2 and  date_format(add_time,'%Y-%m-%d')= date_format(now(),'%Y-%m-%d')", nativeQuery = true)
    int shareTimes(Integer user_id, Integer type);


    @Query(value = "select * from earnings t where t.com_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.angentname like CONCAT('%',?2,'%') else 1=1  end) and (case when ?3!='' and ?3 is not null then t.cusname like CONCAT('%',?3,'%') else 1=1 end) " +
            " order by add_time desc limit ?4,?5", nativeQuery = true)
    List<Earnings> findPagesByLimit(Integer comId, String angentname, String cusname, int page, int limit);


    @Query(value = "select count(1) from earnings t where t.com_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.angentname like CONCAT('%',?2,'%') else 1=1  end) and (case when ?3!='' and ?3 is not null then t.cusname like CONCAT('%',?3,'%') else 1=1 end) ", nativeQuery = true)
    long count(Integer comId, String angentname, String cusname);


    @Query(value = "select sum(money) from earnings where user_id = ?1 and type = 2 and com_id = ?2 ", nativeQuery = true)
    Integer getShareInviteMoneys(Integer user_id, Integer com_id);
}
