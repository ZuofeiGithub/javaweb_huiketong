package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.CompanyBindUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyBindUser,Integer> {
    @Override
    CompanyBindUser save(CompanyBindUser s);

    CompanyBindUser findCompanyBindUserByUserTelAndCompanyId(String user_id,Integer companyid);

    CompanyBindUser findCompanyBindUserByCompanyId(Integer id);

    CompanyBindUser findCompanyBindUserByInviteCode(String inviteCode);
}
