package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.DealFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 17:20
 * @Version 1.0
 */
@Repository
public interface DealFlowRepository extends JpaRepository<DealFlow, Integer> {
    @Override
    <S extends DealFlow> S save(S s);
}
