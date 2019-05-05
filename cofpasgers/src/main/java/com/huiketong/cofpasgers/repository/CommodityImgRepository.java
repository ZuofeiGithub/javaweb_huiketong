package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Agent;
import com.huiketong.cofpasgers.entity.Commodity;
import com.huiketong.cofpasgers.entity.CommodityImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommodityImgRepository extends JpaRepository<CommodityImg, Integer>, JpaSpecificationExecutor<CommodityImg> {

    @Override
    <S extends CommodityImg> S save(S s);


    @Query(value = "select * from commodity_img t where t.commodityd = ?1  limit ?2,?3", nativeQuery = true)
    List<CommodityImg> findPagesByLimit(Integer commodityd, int page, int limit);

    @Query(value = "select count(1) from commodity_img t where t.commodityd = ?1 ", nativeQuery = true)
    long count(Integer commodityd);

    @Query(value = "delete  from  commodity_img  where id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void delCommodityImg(Integer id);

    CommodityImg findFirstByCommodityd(Integer id);


    List<CommodityImg> findCommodityImgsByCommodityd(Integer id);
}
