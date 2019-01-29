package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    @Transactional
    @Override
    Customer save(Customer customer);

    @Modifying
    @Query(value = "update customer set verify_status=?1 where id=?2", nativeQuery = true)
    @Transactional
    void updateVerifyStatus(Integer status,Integer id);

    Customer findCustomerById(Integer id);

    List<Customer> findCustomersByAgentIdAndVerifyStatus(Integer agentId,Integer status);

    @Query(value = "select * from customer where verify_status = ?1 and agent_id = ?2 order by recom_datetime desc LIMIT ?3,?4",nativeQuery = true)
    List<Customer> findPageCustomers(Integer status,Integer agent_id,Integer limit,Integer page);

    @Query(value = "select * from customer where agent_id = ?1 order by recom_datetime desc LIMIT ?2,?3",nativeQuery = true)
    List<Customer> findAllCustomers(Integer agent_id,Integer limit,Integer page);

    @Query(value = "select count(1) from customer where agent_id = ?1 and company_id=?2 and verify_status = '8'",nativeQuery = true)
    int selectCountDealCustomer(Integer agent_id,Integer comid);

    @Query(value = "select count(1) from customer where agent_id = ?1 and company_id=?2 and verify_status = '1'",nativeQuery = true)
    int selectCountWaitCustomer(Integer agent_id,Integer comid);

    @Query(value = "select * from customer where company_id = ?1 order by recom_datetime desc limit ?2,?3",nativeQuery = true)
    List<Customer> findCustomerPages(Integer company_id,Integer page,Integer limit);

    @Query(value = "select count(1) from customer where company_id = ?1",nativeQuery = true)
    long counts(Integer companyId);

    Customer findCustomerByCustomerNameAndTelphone(String name,String telphone);
    @Override
    long count();

    @Query(value = "update customer set sign_price = ?1 where id = ?2 ",nativeQuery = true)
    @Modifying
    @Transactional
    void updateSignPrice(BigDecimal money,Integer customerId);

    @Query(value = "update customer set revoke_reason = ?1 where id = ?2",nativeQuery = true)
    @Modifying
    @Transactional
    void updateReason(String reason,Integer customerId);

    @Query(value = "select count(1) from customer where  agent_id = ?1 and company_id = ?2 and date_format(recom_datetime,'%Y-%m')=date_format(now(),'%Y-%m')",nativeQuery = true)
    Integer findMonthCustomers(Integer agentId,Integer compnayId);

    @Query(value = "select count(1) from customer where  agent_id = ?1 and company_id = ?2 and date_format(recom_datetime,'%Y-%m')=date_format(now(),'%Y-%m') and verify_status not in (1,9)",nativeQuery = true)
    Integer findMonthvisitCustomers(Integer agentId,Integer compnayId);

    @Query(value = "select count(1) from customer where  agent_id = ?1 and company_id = ?2 and date_format(recom_datetime,'%Y-%m')=date_format(now(),'%Y-%m') and verify_status = 8",nativeQuery = true)
    Integer findMonthmabCustomers(Integer agentId,Integer compnayId);
}