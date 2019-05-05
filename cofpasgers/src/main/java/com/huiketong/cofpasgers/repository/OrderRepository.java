package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @Author: 左飞
 * @Date: 2019/1/14 14:35
 * @Version 1.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Override
    <S extends Order> S save(S entity);

    @Query(value = "update t_order set status = ?1 where order_id = ?2", nativeQuery = true)
    void updateOrderStatus(Integer status, String out_trade_no);

    Order findOrderByOrderId(String orderId);
}
