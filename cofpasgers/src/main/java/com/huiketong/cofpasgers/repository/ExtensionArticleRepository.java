package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.ExtensionArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtensionArticleRepository extends JpaRepository<ExtensionArticle, Integer> {
    public List<ExtensionArticle> findAllById(Integer id);
}
