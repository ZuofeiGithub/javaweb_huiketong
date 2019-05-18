package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.OnlineCollege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnlineCollegeRepository extends JpaRepository<OnlineCollege,Integer> {

    List<OnlineCollege> findAllByCompanyId(Integer companyId);

    @Query(value = "select * from online_college where company_id = ?1 LIMIT ?2,?3",nativeQuery = true)
    List<OnlineCollege> findListByCompanyIdLimit(Integer companyId,Integer page, Integer limit);


    /**
     * 视频分享列表
     * @param companyId
     * @param page
     * @param limit
     * @return
     */
    @Query(value = "select * from online_college where company_id = ?1 and article_type = 2 LIMIT ?2,?3",nativeQuery = true)
    List<OnlineCollege> findVideoShareListByCompanyIdLimit(Integer companyId,Integer page, Integer limit);


    /**
     * 拓客技巧列表
     * @param companyId
     * @param page
     * @param limit
     * @return
     */
    @Query(value = "select * from online_college where company_id = ?1 and article_type = 1 LIMIT ?2,?3",nativeQuery = true)
    List<OnlineCollege> findTalkSkillListByCompanyIdLimit(Integer companyId,Integer page, Integer limit);

    /**
     * 公司介绍列表
     * @param companyId
     * @param page
     * @param limit
     * @return
     */
    @Query(value = "select * from online_college where company_id = ?1 and article_type = 3 LIMIT ?2,?3",nativeQuery = true)
    List<OnlineCollege> findCompanyIntolListByCompanyIdLimit(Integer companyId,Integer page, Integer limit);
    OnlineCollege findOnlineCollegeByIdAndArticleType(Integer id,Integer type);
}
