package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/26 9:50
 * @Version 1.0
 * 银行表
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    @Override
    <S extends Bank> S save(S s);

    Bank findBankById(Integer id);

    @Override
    @Transactional
    void deleteById(Integer integer);

    @Override
    List<Bank> findAll();

    List<Bank> findBanksByUserId(Integer userId);

    Bank findFirstByUserId(Integer userId);

    Bank findBankByBankNum(String bankNum);
}
