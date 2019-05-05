package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * 佣金表
 */
@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    @Override
    Commission save(Commission commission);

    Commission findCommissionByCompanyId(Integer id);

    @Modifying
    @Query(value = "update commission set score = ?1,b_withdraw_open=?2,first_percentage=?3,second_percentage=?4," +
            "ofirst_percentage=?5,osecond_percentage=?6,share_count=?7,update_time=now(),invit_score=?8," +
            "nb_remark=?9,qt_remark=?10,min_withdraw = ?11 where company_id = ?12", nativeQuery = true)
    @Transactional
    void updateComByComId(BigDecimal score, Integer bopen, Float firstp, Float secondp, Float ofirst, Float osecond, Integer count, BigDecimal invitescore
            , String nremark, String qremark, BigDecimal minWithdraw, Integer id);
}
