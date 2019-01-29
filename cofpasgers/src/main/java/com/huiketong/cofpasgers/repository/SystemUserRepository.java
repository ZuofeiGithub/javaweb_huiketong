package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser,Integer>{
    SystemUser findSystemUserByTelphoneOrLoginName(String telphone,String loginName);
    SystemUser save(SystemUser s);

    @Query(value = "update system_user set login_passward = ?1 where telphone = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateUserPwd(String pwd,String telphone);
}
