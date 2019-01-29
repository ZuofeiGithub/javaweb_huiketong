package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Earnings;
import com.huiketong.cofpasgers.entity.WithdrawalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 15:05
 * @Version 1.0
 */
@Repository
public interface WithdrawalDetailsRepository extends JpaRepository<WithdrawalDetails,Integer> {
    @Override
    <S extends WithdrawalDetails> S save(S s);

    @Query(value = "select * from  withdrawal_details  where user_id = ?1 order by drawal_time desc limit ?2,?3",nativeQuery = true)
    List<WithdrawalDetails> findWidthdrawalByAgentId(int userId,int page, int limit);


    @Query(value = "select * from withdrawal_details t where t.comid = ?1 and (CASE when  ?2!='' and ?2 is not null then t.angentname like CONCAT('%',?2,'%') else 1=1  end) and (case when ?3!='' and ?3 is not null then t.user_id=?3 else 1=1 end)" +
            " and (case when ?4!='' and ?4 is not null then t.`status`=?4 else 1=1 end) " +
            " order by drawal_time desc limit ?5,?6",nativeQuery = true)
    List<WithdrawalDetails> findPagesByLimit(Integer comId, String angentname ,Integer userid, String status, int page , int limit);



    @Query(value = "select count(1) from withdrawal_details t where t.comid = ?1 and (CASE when  ?2!='' and ?2 is not null then t.angentname like CONCAT('%',?2,'%') else 1=1  end) and (case when ?3!='' and ?3 is not null then t.user_id=?3 else 1=1 end) and (case when ?4!='' and ?4 is not null then t.`status`=?4 else 1=1 end) ",nativeQuery = true)
    long count (Integer comId, String angentname ,Integer userid, String status);

    @Query(value = "update withdrawal_details t set  t.descript=?2 ,t.`status`=?3,t.update_time=?4 where t.id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateTixian (Integer id, String descript , String status, Date date);



}
