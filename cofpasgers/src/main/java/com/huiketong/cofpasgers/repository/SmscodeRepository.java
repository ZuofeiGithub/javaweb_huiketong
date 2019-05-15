package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Smscode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SmscodeRepository extends JpaRepository<Smscode, Integer> {
    Smscode save(Smscode s);

    Smscode findSmscodeByUserId(Integer id);

    Smscode findSmscodeByTelphoneAndType(String telphone, String type);

    @Modifying
    @Transactional
    @Query(value = "update huiketong.smscode set huiketong.smscode.code = ?1 where huiketong.smscode.telphone = ?2 and huiketong.smscode.type = ?3", nativeQuery = true)
    void updateCodebyTelphone(String code, String telphone, String type);
}
