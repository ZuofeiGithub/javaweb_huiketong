package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.CommodityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 商品品类
 */
public interface CommodityCategoryRepository extends JpaRepository<CommodityCategory, Integer>, JpaSpecificationExecutor<CommodityCategory> {

    @Override
    <S extends CommodityCategory> S save(S s);


    @Query(value = "select * from commodity_category t where t.company_id = ?1  limit ?2,?3", nativeQuery = true)
    List<CommodityCategory> findPagesByLimit(Integer comId, int page, int limit);


    @Query(value = "select count(1) from commodity_category t where t.company_id = ?1 ", nativeQuery = true)
    long count(Integer comId);


    @Query(value = "update commodity_category t set  t.category_name=?2  where t.id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCommodityCategoryList(Integer id, String categoryName);


    @Query(value = "delete  from  commodity_category  where id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void delCommodityCategoryList(Integer id);

    /**
     * 判断公司商品品类是否已经存在
     *
     * @param comId
     * @param categoryName
     * @return
     */
    @Query(value = "select * from commodity_category t where t.company_id = ?1 and  t.category_name=?2 ", nativeQuery = true)
    CommodityCategory findPagesByCategoryName(Integer comId, String categoryName);

    @Query(value = "select * from commodity_category where company_id = ?1", nativeQuery = true)
    List<CommodityCategory> findAllByCompanyId(Integer companyId);
}
