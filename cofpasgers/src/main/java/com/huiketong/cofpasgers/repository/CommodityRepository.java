package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Commodity;
import com.huiketong.cofpasgers.entity.CommodityImg;
import com.huiketong.cofpasgers.entity.CommodityStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface CommodityRepository extends JpaRepository<Commodity, Integer>, JpaSpecificationExecutor<Commodity> {
    @Transactional
    @Override
    <S extends Commodity> S save(S s);

    Commodity findCommodityById(Integer id);

    List<Commodity> findAllByCompanyId(Integer companyId);

    @Query(value = "select * from commodity t where t.company_id = ?1 and case when ?4 != '' then t.commodity_name like CONCAT('%',?4,'%') else 1=1 end order by add_time desc  limit ?2,?3", nativeQuery = true)
    List<Commodity> findPagesByLimit(Integer comId, int page, int limit, String commodityName);


    @Query(value = "SELECT * FROM commodity  WHERE  CASE WHEN ?1 != 0 AND ?1 IS NOT NULL THEN category_id = ?1 else 1=1 END AND CASE WHEN ?2 != 0 AND ?2 IS NOT NULL THEN \n" +
            "style_id = ?2 else 1=1 END AND company_id = ?3 ORDER BY  CASE WHEN  ?4=0 THEN add_time  WHEN ?4 = 1  THEN original_price else 1=1 END ASC limit ?5,?6\n", nativeQuery = true)
    List<Commodity> findPageCommodityDESC(Integer categoryId, Integer styleId, Integer companyId, Integer sort, Integer page, Integer limit);

    @Query(value = "SELECT * FROM commodity  WHERE  CASE WHEN ?1 != 0 AND ?1 IS NOT NULL THEN category_id = ?1 else 1=1 END AND CASE WHEN ?2 != 0 AND ?2 IS NOT NULL THEN \n" +
            "style_id = ?2 else 1=1 END AND company_id = ?3 ORDER BY  CASE WHEN  ?4=0 THEN add_time  WHEN ?4 = 2  THEN original_price else 1=1 END DESC limit ?5,?6\n", nativeQuery = true)
    List<Commodity> findPageCommodityASC(Integer categoryId, Integer styleId, Integer companyId, Integer sort, Integer page, Integer limit);

//    @Query(nativeQuery = true,value = "select * from commodity")
//    List<Commodity> findCommoditiesByCategoryIdAndCompanyIdOrderByAddTimeDesc(Integer id,Integer companyId);
//
//    List<Commodity> findCommoditiesByCategoryIdAndStyleIdAndCompanyId(Integer cateid,Integer styleid,Integer comId);
//
//    List<Commodity> findCommoditiesByStyleIdAndCompanyIdOrderByAddTimeDesc(Integer styleId,Integer comId);


    @Query(value = "select count(1) from commodity t where t.company_id = ?1 ", nativeQuery = true)
    long count(Integer comId);

    @Query(value = "delete  from  commodity  where id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void delCommodity(Integer id);


    @Query(value = "select * from commodity t where t.company_id = ?1 and t.commodity_name=?2", nativeQuery = true)
    Commodity findPagesByCommodityName(Integer comId, String commodityName);


    @Query(value = "update commodity t set t.commodity_name=?2,t.original_price=?3,t.activity_price=?4,t.deposit_money=?5,t.activity_description=?6,t.product_details=?7,t.danwei=?8,t.style_id=?9,t.category_id=?10,label=?11 where t.id=?1  ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCommodity(Integer id, String commodityName, Integer originalPrice, Integer activityPrice, BigDecimal depositMoney, String activityDescription, String productDetails, String danwei, Integer commodityStyleId, Integer commodityCategoryId, String label);

    Commodity findCommoditieByIdAndCompanyId(Integer id, Integer companyId);

    @Query(value = "update commodity set concerned_people = concerned_people + 1 where id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void addConcerned(Integer id);

    @Query(value = "update commodity set concerned_people = case when (concerned_people - 1)!=0 then concerned_people - 1 else 0 end where id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void reduceConcerned(Integer id);
}
