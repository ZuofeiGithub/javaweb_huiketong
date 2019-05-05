package com.huiketong.cofpasgers.service;

import com.huiketong.cofpasgers.entity.Agent;
import com.huiketong.cofpasgers.entity.SystemUser;
import com.huiketong.cofpasgers.repository.AgentRepository;
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

@Service
public class AgentQueryServiceImpl implements IPageQueryService<Agent> {
    @Resource
    AgentRepository agentRepository;

    @Override
    public Page<Agent> findPageNoCriteria(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return agentRepository.findAll(pageable);
    }

    @Override
    public Page<Agent> findPageCriteria(Integer page, Integer size, Object obj) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC);
        Page<Agent> agentPage = agentRepository.findAll((Specification<Agent>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            //list.add(criteriaBuilder.equal(root.get("").as(String.class),));
            System.out.println(root.get("agent_name"));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);
        return agentPage;
    }
}
