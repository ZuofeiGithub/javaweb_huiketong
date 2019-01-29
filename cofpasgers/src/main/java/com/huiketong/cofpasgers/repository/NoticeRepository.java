package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    @Override
    <S extends Notice> S save(S s);

    @Query(value = "SELECT * FROM notice WHERE company_id  = ?1 ORDER BY add_time DESC LIMIT 0,20", nativeQuery = true)
    List<Notice> findAllOrderBy(int companyId);
}
