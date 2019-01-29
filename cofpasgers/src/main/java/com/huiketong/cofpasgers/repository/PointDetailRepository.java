package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/17 16:35
 * @Version 1.0
 */
@Repository
public interface PointDetailRepository extends JpaRepository<PointDetail,Integer> {
    @Override
    <S extends PointDetail> S save(S entity);

    /**
     * 获取今天的积分
     * @param userId
     * @param comId
     * @return
     */
    @Query(value = "SELECT SUM(POINT) FROM point_detail WHERE user_id = ?1 and company_id = ?2 and status = 1",nativeQuery = true)
    Integer findAllGetPoints(Integer userId,Integer comId);


    @Query(value = "SELECT SUM(POINT) FROM point_detail WHERE user_id = ?1 and company_id = ?2 and status = 2",nativeQuery = true)
    Integer findAllResumPoints(Integer userId,Integer comId);


    @Query(value = "select sum(POINT) from point_detail where status = 1 AND TO_DAYS(add_time) = TO_DAYS(NOW()) and user_id = ?1 and company_id = ?2",nativeQuery = true)
    Integer findCurGetPoint(Integer userId,Integer companyId);

    @Query(value = "select sum(point) from point_detail where status = 1 and type = ?1 and user_id = ?2 and company_id = ?3 ",nativeQuery = true)
    Integer findAllPoints(Integer type,Integer user_id,Integer companyId);
    /**
     * 获取当天签到积分个数
     * @param user_id
     * @param company_id
     * @return
     */
    @Query(value = "select count(1) from point_detail where user_id = ?1 and company_id = ?2 AND type = 2 AND TO_DAYS(add_time) = TO_DAYS(NOW())",nativeQuery = true)
    Integer findCurSignPoint(Integer user_id,Integer company_id);


    @Query(value = "select * from point_detail where user_id = ?1 and company_id = ?2 and case WHEN ?3 != 0 then status = ?3 else 1=1 end order by add_time desc limit ?4,?5",nativeQuery = true)
    List<PointDetail> findDetailPoints(Integer userId,Integer comId,Integer status,Integer page,Integer limit);

    @Query(value = "select count(point) from point_detail where type = 1",nativeQuery = true)
    Integer findCurLoginPointCount();


}
