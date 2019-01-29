package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.PopupAdvertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PopupAdvertiseRepository extends JpaRepository<PopupAdvertise,Integer> {
    public List<PopupAdvertise> findAllById(Integer id);
}
