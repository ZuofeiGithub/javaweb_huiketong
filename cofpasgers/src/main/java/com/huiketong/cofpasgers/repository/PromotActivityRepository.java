package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.PromotActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromotActivityRepository extends JpaRepository<PromotActivity,Integer> {


    @Query(value = "select * from promot_activity where company_id = ?1 limit ?2,?3",nativeQuery = true)
    List<PromotActivity> findAllByCompanyIdLimit(Integer companyId,Integer page,Integer limit);

    List<PromotActivity> findAllByCompanyId(Integer companyId);


    PromotActivity findPromotActivityByActAlias(String actAlias);
}
