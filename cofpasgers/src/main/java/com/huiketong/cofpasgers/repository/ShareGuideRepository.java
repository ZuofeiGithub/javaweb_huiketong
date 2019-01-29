package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.ShareFeeGuide;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
