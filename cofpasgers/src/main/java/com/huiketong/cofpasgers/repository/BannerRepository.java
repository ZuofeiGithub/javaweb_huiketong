package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Banner;
import com.huiketong.cofpasgers.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 轮播图表
 */
@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    @Override
    Banner save(Banner s);

    Banner findBannerByName(String name);

    List<Banner> findBannersByCompanyIdAndStatusOrderByCreateDate(Integer company_id,Integer status);

    @Override
    @Transactional
    void deleteById(Integer integer);

    @Query(value = "select count(1) from banner t where t.company_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.`status`=?2 else 1=1  end) and (case when ?3!='' and ?3 is not null then t.`name` like CONCAT('%',?3,'%') else 1=1 end)  ",nativeQuery = true)
    long count (Integer comId,String status ,String searchMerName);


    @Query(value = "select * from banner t where t.company_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.`status`=?2 else 1=1 end) and (case when ?3!='' and ?3 is not null then t.`name` like CONCAT('%',?3,'%') else 1=1 end) order by create_date desc limit ?4,?5",nativeQuery = true)
    List<Banner> findPagesByLimit(Integer comId,String status ,String searchMerName, int page , int limit);



    @Query(value = "update banner  set status=0  where banner_id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateLunBo( Integer id);


    @Query(value = "update banner  set name=?1 ,descript=?2,trankurl=?3,imgurl=?4,sort=?5 where banner_id=?6 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateLunBo(String name, String descript, String trankurl, String imgSrc, Integer sort, Integer id);


}
