package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.CommodityCategory;
import com.huiketong.cofpasgers.entity.CommodityStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommodityStyleRepository extends JpaRepository<CommodityStyle, Integer>, JpaSpecificationExecutor<CommodityStyle> {
    @Override
    <S extends CommodityStyle> S save(S s);


    @Query(value = "select * from commodity_style t where t.company_id = ?1  limit ?2,?3", nativeQuery = true)
    List<CommodityStyle> findPagesByLimit(Integer comId, int page, int limit);


    @Query(value = "select count(1) from commodity_style t where t.company_id = ?1 ", nativeQuery = true)
    long count(Integer comId);


    @Query(value = "update commodity_style t set  t.type_name=?2  where t.id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCommodityStyle(Integer id, String typeName);


    @Query(value = "delete  from  commodity_style  where id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void delCommodityStyle(Integer id);

    /**
     * 判断公司商品风格是否已经存在
     *
     * @param comId
     * @param typeName
     * @return
     */
    @Query(value = "select * from commodity_style t where t.company_id = ?1 and  t.type_name=?2 ", nativeQuery = true)
    CommodityStyle findPagesByCategoryName(Integer comId, String typeName);


    @Query(value = "select * from commodity_style t where t.company_id = ?1 ", nativeQuery = true)
    List<CommodityStyle> findAllCommodityStyle(Integer comId);
}
