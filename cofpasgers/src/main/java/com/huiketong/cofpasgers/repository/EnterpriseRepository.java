package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise,Integer>{
    Enterprise save(Enterprise s);
    Enterprise findEnterpriseByEnterLoginName(String loginname);
    Enterprise findEnterpriseByEnterName(String name);
    Enterprise findEnterpriseById(Integer id);

    @Query(value = "select * from enterprise limit ?1,?2",nativeQuery = true)
    List<Enterprise> findPagesEnter(Integer page,Integer limit);
    @Override
    List<Enterprise> findAll();

    @Override
    long count();

    @Query(value = "update enterprise set brokerage = ?1,enter_address=?2,enter_legalperson=?3," +
            "enter_name=?4,enter_telphone=?5,establelish_date=?6 where enter_login_name = ?7",nativeQuery = true)
    @Modifying
    @Transactional
    void updateEnterInfo(Integer brok, String address, String leader, String name, String tel, Date date, String loginname);

    @Query(value = "update enterprise set enter_login_pwd = ?1 where enter_login_name = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateEnterPwd(String pwd,String login_name);

    @Query(value = "update enterprise set brokerage = brokerage - (brokerage / 100)",nativeQuery = true)
    @Modifying
    @Transactional
    void updateBrokerage();
}
