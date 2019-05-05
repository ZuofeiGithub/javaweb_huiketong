package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.ReportCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCustomerRepository extends JpaRepository<ReportCustomer, Integer> {
}
