package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods,Integer> {
    @Query(value = "select * from goods where company_id = ?1 limit ?2,?3",nativeQuery = true)
    List<Goods> findAllByCompanyIdLimit(Integer companyId,Integer page,Integer limit);
}
