package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.UserRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository
public interface UserRightsRepository extends JpaRepository<UserRights,String> {
    @Override
    UserRights save(UserRights userRights);
    UserRights findUserRightsByUserTel(String telphone);
    UserRights findUserRightsByUserTelAndUserRightOrLoginNameAndUserRight(String telphone,Integer rights,String login_name,Integer right);
}
