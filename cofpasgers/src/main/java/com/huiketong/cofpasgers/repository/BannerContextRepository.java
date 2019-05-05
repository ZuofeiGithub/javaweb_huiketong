package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.BannerContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BannerContextRepository extends JpaRepository<BannerContext, Integer> {
    @Query(value = "delete from banner_context where banner_id = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByBannerId(Integer bannerid);
}
