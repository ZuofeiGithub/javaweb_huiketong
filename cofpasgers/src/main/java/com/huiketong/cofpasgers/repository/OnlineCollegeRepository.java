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
}
