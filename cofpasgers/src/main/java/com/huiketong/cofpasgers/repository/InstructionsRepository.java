package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Instructions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionsRepository extends JpaRepository<Instructions, Integer> {
}
