package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.AttentionGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @Author: 左飞
 * @Date: 2019/1/10 9:59
 * @Version 1.0
 */
public interface AttentionGoodsRepository extends JpaRepository<AttentionGoods, Integer> {
    @Override
    <S extends AttentionGoods> S save(S entity);

    @Query(value = "update attention_goods set status = ?1 where goods_id = ?2 and user_id = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatus(Integer status, Integer goodid, Integer user_id);

    AttentionGoods findAttentionGoodsByGoodsIdAndUserId(Integer goodid, Integer userId);
}
