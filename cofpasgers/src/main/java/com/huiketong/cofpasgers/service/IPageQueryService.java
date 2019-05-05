package com.huiketong.cofpasgers.service;

import org.springframework.data.domain.Page;


public interface IPageQueryService<T> {
    Page<T> findPageNoCriteria(Integer page, Integer size);

    Page<T> findPageCriteria(Integer page, Integer size, Object obj);
}
