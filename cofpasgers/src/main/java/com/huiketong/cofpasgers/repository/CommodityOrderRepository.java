package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Commodity;
import com.huiketong.cofpasgers.entity.CommodityOrder;
import com.huiketong.cofpasgers.entity.CommodityStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommodityOrderRepository  extends JpaRepository<CommodityOrder,Integer>, JpaSpecificationExecutor<CommodityOrder> {


    @Override
    <S extends CommodityOrder> S save(S s);

    @Query(value = "select * from commodity_order t where t.company_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.customer_name like CONCAT('%',?2,'%') else 1=1  end) limit ?3,?4",nativeQuery = true)
    List<CommodityOrder> findPagesByLimit(Integer comId, String customerName,int page , int limit);



    @Query(value = "select count(1) from commodity_order t where t.company_id = ?1 and (CASE when  ?2!='' and ?2 is not null then t.customer_name like CONCAT('%',?2,'%')  else 1=1  end) ",nativeQuery = true)
    long count (Integer comId,String customerName);


    @Query(value = "update commodity_order t set  t.status=?2  where t.id=?1 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateCommodityOrder (Integer id, Integer status);

    @Query(value = "update commodity_order set order_status = ?1 where order_num = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatusByOrderStatusNum(Integer status,String orderNum);

    CommodityOrder findCommodityOrderByOrderNum(String num);

    @Query(value = "select * from commodity_order where user_id = ?1 and company_id = ?2 and order_status = 1 order by insert_time desc limit ?3,?4 ",nativeQuery = true)
    List<CommodityOrder> findOrderPage(Integer userId,Integer comId,Integer page,Integer limit);
}
