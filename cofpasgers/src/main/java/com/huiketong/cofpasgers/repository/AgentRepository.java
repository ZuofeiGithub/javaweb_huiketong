package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Agent;
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
public interface AgentRepository extends JpaRepository<Agent, Integer>, JpaSpecificationExecutor<Agent> {
    @Override
    List<Agent> findAll();

    @Override
    Page<Agent> findAll(Pageable pageable);

    Agent findAgentByTelphoneOrLoginUsername(String telphone, String LoginUsername);

    Agent findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(String telphone, Integer comId, String LoginUsername, Integer companyId);

    Agent findAgentByTelphoneAndInitCode(String telphone, String inviteCode);

    Agent findAgentByTelphoneAndCompanyId(String telphone, Integer companyId);

    Agent save(Agent s);

    @Query(value = "update agent set access_token = ?1,device_id = (CASE when ?2 IS NOT NULL and ?2 != ''THEN ?2 ELSE '' END),client_token = (CASE when ?3 IS NOT NULL and ?3 != ''THEN ?3 ELSE '' END) where telphone = ?4", nativeQuery = true)
    @Modifying
    @Transactional
    void updateAccesstokenAndDeviceId(String accessToken, String deviceId, String clientToken, String telphone);

    @Query(value = "update agent set init_agent_nam = init_agent_nam + 1 where telphone = ?1 and company_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void addAgentNum(String telphone, Integer company_id);

    @Query(value = "update agent set init_agent_nam = init_agent_nam - 1 where telphone = ?1", nativeQuery = true)
    @Modifying
    @Transactional
    void reduceAgentNum(String telphone);

    @Query(value = "update agent set password = ?1 where telphone = ?2 or login_username = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void modifyPwd(String pwd, String telphone, String login_user);

    @Query(value = "update agent set card_photo = ?1,id_card = ?2,real_name=?3 where telphone = ?4 and company_id = ?5 or login_username = ?6 and company_id = ?7", nativeQuery = true)
    @Modifying
    @Transactional
    void certiAgent(String cardPhone, String idCard, String realName, String telphone, Integer comid, String login_username, Integer compayid);

    List<Agent> findAgentsByTelphoneOrLoginUsername(String supername, String login_id);

    List<Agent> findAgentsBySuperIdOrTopId(Integer superId, Integer topId);

    @Query(value = "SELECT * FROM agent WHERE  id  = (SELECT super_id FROM agent WHERE id = ?1 AND company_id = ?2)", nativeQuery = true)
    Agent findSuperById(Integer userid, Integer companyid);

    List<Agent> findAgentsByCompanyId(Integer companyid);


    @Query(value = "select * from agent where company_id = ?1 ", nativeQuery = true)
    List<Agent> findAgentList(Integer companyid);


    @Query(value = "select * from agent where company_id = ?1 and type != 9", nativeQuery = true)
    List<Agent> findNotCusAgents(Integer comId);

    @Query(value = "select * from agent where company_id = ?1 and type = 9", nativeQuery = true)
    Agent findIsCusAgents(Integer comId);

    Agent findAgentByInitCode(String code);

    @Query(value = "update agent set account_balance = account_balance + ?1 where id = ?2 and company_id = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updateScore(BigDecimal money, Integer userid, Integer comId);

    @Query(value = "update agent set deal_custom_num = deal_custom_num + 1 where id = ?1 and company_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateDealNum(Integer agentId, Integer comId);

    @Query(value = "update agent set rank_account = rank_account+?1 where id = ?2 and company_id = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updateAccount(BigDecimal money, Integer userid, Integer comId);


    @Query(value = "update agent set account_balance = account_balance - ?1 where id = ?2 and company_id = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updateProfit(BigDecimal money, Integer userid, Integer comId);


    @Query(value = "update agent set recon_custom_nam = recon_custom_nam+1 where id = ?1 and company_id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCustomeNum(Integer userId, Integer comId);


    @Query(value = "select * from agent where super_id = ?1 and company_id = ?2", nativeQuery = true)
    List<Agent> findNextAgents(Integer superid, Integer comId);

    @Query(value = "select * from agent where super_id = ?1 and company_id = ?2 order by reg_datetime desc limit ?3,?4", nativeQuery = true)
    List<Agent> findPageNextAgents(Integer superid, Integer comId, Integer page, Integer limit);

    @Query(value = "update agent set b_certification = ?1 where init_code = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateCertfication(Integer bcert, String inviteCode);

    @Query(value = "update agent set avatar = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateUserPhoto(String avatar, Integer user_id);

    @Query(value = "update agent set agent_name = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateUserNickName(String nickname, Integer user_id);

    /**
     * 周排行榜
     */
    @Query(value = "SELECT * FROM agent WHERE company_id=?1 and YEARWEEK(DATE_FORMAT(reg_datetime,'%Y-%m-%d')) = YEARWEEK(NOW())  ORDER BY rank_account DESC LIMIT ?2,?3", nativeQuery = true)
    List<Agent> findWeekRank(Integer company_id, Integer page, Integer limit);

    /**
     * 总排行
     */
    @Query(value = "SELECT * FROM agent where company_id = ?1 ORDER BY rank_account DESC LIMIT ?2,?3", nativeQuery = true)
    List<Agent> findAllRank(Integer company_id, Integer page, Integer limit);

    @Query(value = "select GROUP_CONCAT(id) from agent where company_id = ?1", nativeQuery = true)
    String findUserIds(int companId);

    @Query(value = "select * from agent where company_id = ?1  limit ?2,?3", nativeQuery = true)
    List<Agent> findPagesAgent(Integer companyId, Integer page, Integer limit);

    @Query(value = "select count(1) from agent where company_id = ?1", nativeQuery = true)
    long counts(Integer companyId);

    @Override
    long count();

    Agent findAgentById(Integer id);

    @Query(value = "update agent set drawl_pad = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateDralPwd(String pwd, Integer userId);

    @Query(value = "update agent set password = ?1 where login_username = ?2 or telphone = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void forgetPwd(String pwd, String login_name, String telphone);

    @Query(value = "update agent set telphone = ?1 where login_username = ?2 or telphone = ?3", nativeQuery = true)
    @Modifying
    @Transactional
    void changeUserPhone(String phone, String login_name, String telphone);

    @Query(value = "update agent set forbid = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void delAgent(Integer forbid, Integer id);

    //authuser
    @Query(value = "update agent set b_certification = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void authUser(Integer b_certification, Integer id);

    @Query(value = "update agent set points = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updatePoint(Integer point, Integer user_id);

    @Query(value = "update agent set account_balance = ?1 where id = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateAccount(BigDecimal money, Integer user_id);
}
