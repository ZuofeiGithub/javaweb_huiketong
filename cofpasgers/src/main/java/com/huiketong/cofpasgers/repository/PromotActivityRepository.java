package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.PromotActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface PromotActivityRepository extends JpaRepository<PromotActivity,Integer> {


    @Query(value = "select * from promot_activity where company_id = ?1 limit ?2,?3",nativeQuery = true)
    List<PromotActivity> findAllByCompanyIdLimit(Integer companyId,Integer page,Integer limit);

    List<PromotActivity> findAllByCompanyId(Integer companyId);


    PromotActivity findPromotActivityByActAlias(String actAlias);

    PromotActivity findPromotActivityById(Integer id);

    @Query(value = "update promot_activity set title = ?1,detail = ?2,begin_time = ?3,end_time = ?4,activity_type = ?5,cover_img = ?6 where id = ?7",nativeQuery = true)
    @Modifying
    @Transactional
    void updatePromotAcvitiyById(String title, String detail, Date begintime,Date endtime,Integer type,String coverImg,Integer id);
}
