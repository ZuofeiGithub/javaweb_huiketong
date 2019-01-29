package com.huiketong.cofpasgers.service;

import com.huiketong.cofpasgers.entity.Customer;
import com.huiketong.cofpasgers.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2018/12/24 14:58
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements IPageQueryService<Customer> {
    @Autowired
    CustomerRepository customerRepository;
    @Override
    public Page<Customer> findPageNoCriteria(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.Direction.ASC,"id");
        return customerRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> findPageCriteria(Integer page, Integer size, Object obj) {
        System.out.println(obj.toString());
        Pageable pageable = PageRequest.of(page,size,Sort.Direction.ASC);
        Page<Customer> ustomerPage = customerRepository.findAll((Specification<Customer>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(criteriaBuilder.equal(root.get("verify_status").as(String.class),obj));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        },pageable);
        return ustomerPage;
    }
}
