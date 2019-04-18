package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.VoucherShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherShareRepository extends JpaRepository<VoucherShare,Integer> {
    VoucherShare findVoucherShareByCompanyIdAndSharetype(Integer companyId,Integer type);
    VoucherShare findVoucherShareByCompanyIdAndGoodsIdAndSharetype(Integer companyId,Integer goodsId,Integer type);

    List<VoucherShare> findVoucherSharesByCompanyId(Integer companyId);
}
