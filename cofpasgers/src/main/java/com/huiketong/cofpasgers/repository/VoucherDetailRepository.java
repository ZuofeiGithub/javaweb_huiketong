package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherDetailRepository extends JpaRepository<VoucherDetail, Integer> {
    VoucherDetail findVoucherDetailByCompanyId(Integer companyId);
}
