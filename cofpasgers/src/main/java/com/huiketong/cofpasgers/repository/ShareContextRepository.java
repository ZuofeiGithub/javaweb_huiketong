package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Merchants;
import com.huiketong.cofpasgers.entity.ShareContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface ShareContextRepository extends JpaRepository<ShareContext, Integer> {
    @Override
    <S extends ShareContext> S save(S s);

    List<ShareContext> findAllByCompanyId(Integer comId);

    ShareContext findShareContextByCompanyId(Integer comId);

    @Query(value = "select * from share_context t where company_id = ?1 ORDER BY t.datetime desc  limit ?2,?3", nativeQuery = true)
    List<ShareContext> findPagesByLimit(Integer comId, int page, int limit);

    ShareContext findShareContextById(Integer id);


    @Query(value = "select count(1) from share_context where company_id = ?1 and title like  CONCAT('%',?2,'%')", nativeQuery = true)
    long count(Integer comId, String searchTitle);

    @Query(value = "delete from share_context where id=?1 ", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteYongjin(Integer id);


    @Query(value = "update share_context  set context=?1 ,title=?2,money=?3,url=?4,img_url=?5 where id=?6 ", nativeQuery = true)
    @Modifying
    @Transactional
    void updateYongjin(String context, String title, BigDecimal money, String url, String imgUrl, Integer id);


    @Query(value = "select * from share_context t where company_id = ?1 and  title like CONCAT('%',?2,'%')  ORDER BY datetime desc  limit ?3,?4 ", nativeQuery = true)
    List<ShareContext> findPagesByLikeName(Integer comId, String searchTitle, int page, int limit);
}
