package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.VoucherShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VoucherShareRepository extends JpaRepository<VoucherShare,Integer> {
    VoucherShare findVoucherShareByCompanyIdAndSharetype(Integer companyId,Integer type);
    VoucherShare findVoucherShareByCompanyIdAndGoodsIdAndSharetype(Integer companyId,Integer goodsId,Integer type);

    List<VoucherShare> findVoucherSharesByCompanyId(Integer companyId);


    @Transactional
    @Query(value = "update voucher_share set image = ?2 where goods_id = ?1",nativeQuery = true)
    @Modifying
    void updateImageUrl(Integer goodsId,String imageurl);
}
