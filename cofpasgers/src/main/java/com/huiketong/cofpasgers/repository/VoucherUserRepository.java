package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.VoucherUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherUserRepository extends JpaRepository<VoucherUser,Integer> {
    VoucherUser findVoucherUserByTelphone(String telphone);

    List<VoucherUser> findAllByCompanyId(Integer companyId);
}
