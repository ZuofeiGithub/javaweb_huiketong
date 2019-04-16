package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.VoucherShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherShareRepository extends JpaRepository<VoucherShare,Integer> {

    VoucherShare findVoucherShareByCompanyId(Integer companyId);
}
