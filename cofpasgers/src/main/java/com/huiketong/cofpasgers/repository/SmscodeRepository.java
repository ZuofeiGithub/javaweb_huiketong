package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Smscode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmscodeRepository extends JpaRepository<Smscode,Integer> {
    Smscode save(Smscode s);

    Smscode findSmscodeByUserId(Integer id);

    Smscode findSmscodeByTelphoneAndType(String telphone,String type);
}
