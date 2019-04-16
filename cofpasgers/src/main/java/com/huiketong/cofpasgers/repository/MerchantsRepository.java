package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Merchants;
import com.huiketong.cofpasgers.entity.ShareContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MerchantsRepository extends JpaRepository<Merchants,Integer> {
    @Override
    <S extends Merchants> S save(S s);

    @Override
    List<Merchants> findAll();

    List<Merchants> findMerchantsByEnterId(Integer enter_id);

   // List<Merchants> findMerchantsByCompanyId(Integer id);


    @Query(value = "select * from merchants t where t.enter_id = ?1 ORDER BY t.mer_order desc  limit ?2,?3",nativeQuery = true)
    List<Merchants> findPagesByLimit(Integer comId, int page , int limit);


    @Query(value = "select count(1) from merchants where enter_id = ?1 and mer_name like  CONCAT('%',?2,'%') ",nativeQuery = true)
    long count (Integer comId,String merName);


    @Query(value = "update merchants  set mer_name=?1 ,mer_address=?2,mer_url=?3,mer_logo=?4,mer_order=?5,descript=?6 where mer_id=?7 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateYongjin(String merName, String merAddress, String merUrl, String imgSrc, Integer merOrder,String descript, Integer id);


    @Query(value = "delete from merchants where mer_id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteYongjin(Integer id);


    @Query(value = "select *  from merchants where enter_id = ?1 and  mer_name like  CONCAT('%',?2,'%')  ORDER BY mer_order desc limit ?3,?4 ",nativeQuery = true)
    List<Merchants> findPagesByLikeName(Integer comId,String merName, int page , int limit);
}
