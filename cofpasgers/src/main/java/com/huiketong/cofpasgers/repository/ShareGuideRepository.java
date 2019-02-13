package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.ShareFeeGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/8 11:27
 * @Version 1.0
 */
public interface ShareGuideRepository extends JpaRepository<ShareFeeGuide,Integer> {
    @Override
    <S extends ShareFeeGuide> S save(S entity);

    @Override
    List<ShareFeeGuide> findAll();

    ShareFeeGuide findShareFeeGuideByCompanyId(Integer comId);

    @Query(value = "update share_free_guide set guide_context = ?1  where company_id = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateShareGuide(String context,Integer company_id);
}
