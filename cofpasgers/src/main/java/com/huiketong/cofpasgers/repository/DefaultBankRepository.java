package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.DefaultBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 9:58
 * @Version 1.0
 */
@Repository
public interface DefaultBankRepository extends JpaRepository<DefaultBank, Integer> {
    @Override
    <S extends DefaultBank> S save(S s);

    @Query(value = "update default_bank set bank_num = ?1 where user_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateDefaultBank(String bankNum, Integer userId);

    DefaultBank findDefaultBankByBankNum(String bankNum);

    DefaultBank findDefaultBankByUserId(Integer userId);

    @Override
    @Transactional
    void delete(DefaultBank defaultBank);
}
