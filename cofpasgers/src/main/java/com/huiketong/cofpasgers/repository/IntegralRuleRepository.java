package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.IntegralRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface IntegralRuleRepository  extends JpaRepository<IntegralRule,Integer>, JpaSpecificationExecutor<IntegralRule> {

    @Override
    <S extends IntegralRule> S save(S s);

    IntegralRule findIntegralRuleByCompanyId(Integer comId);

    @Query(value = "update integral_rule set auth_integral = ?1,consume = ?2,dials = ?3,invite_integral=?4,is_open_lotto=?5," +
            "is_point = ?6,login_integral=?7,min_price = ?8,recom_integral = ?9,rmb_for_point = ?10,sign_integral=?11 where company_id = ?12",nativeQuery = true)
    @Modifying
    @Transactional
    void updateRule(Integer auth,Integer consum,String dials,Integer invite,Integer islotto,Integer ispoint,Integer login,Integer minprice,
                    Integer recom,Integer rmb,Integer sign,Integer comId);
}
