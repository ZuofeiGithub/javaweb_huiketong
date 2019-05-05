package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.DefaultEnter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DefaultEnterRepository extends JpaRepository<DefaultEnter, Integer> {
    DefaultEnter findDefaultEnterByUserId(String id);

    DefaultEnter findDefaultEnterByUserIdOrUserTelphone(String id, String tel);

    @Override
    <S extends DefaultEnter> S save(S s);

    @Query(value = "UPDATE default_enter SET compay_id = ?1,compay_name = ?2 WHERE user_id = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCompanyid(Integer comid, String comName, String user_id);

    @Query(value = "update default_enter set user_telphone = ?1 where user_id = ?2 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateUserTelphone(String telphone, String user_id);
}
