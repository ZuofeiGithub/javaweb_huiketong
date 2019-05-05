package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.Constant;
import com.huiketong.cofpasgers.entity.Agent;
import com.huiketong.cofpasgers.entity.DefaultEnter;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.json.chat.*;
import com.huiketong.cofpasgers.json.data.MessageData;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.AgentRepository;
import com.huiketong.cofpasgers.repository.DefaultEnterRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/16 13:17
 * @Version 1.0
 */
@RequestMapping(value = "chat")
@Controller
public class ChatController {
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    DefaultEnterRepository defaultEnterRepository;

    @RequestMapping("chatinit")
    @ResponseBody
    public InitResp chatInit(String user_id) {
        InitResp resp = new InitResp();
        InitData data = new InitData();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(user_id);
        if (ObjectUtils.isNotNull(enterprise)) {
            MineData mineData = new MineData();
            mineData.setAvatar(Constant.IMAGE_URL + enterprise.getEnterLogo());
            Agent cagent = agentRepository.findIsCusAgents(enterprise.getId());
            if (ObjectUtils.isNotNull(cagent)) {
                mineData.setId("c" + cagent.getInitCode());
                mineData.setStatus("online");
                mineData.setSign("我们公司的主旨共享经济时代");
                mineData.setUsername(cagent.getAgentName());
            } else {
                mineData.setId("company" + enterprise.getId());
                mineData.setStatus("online");
                mineData.setSign("我们公司的主旨共享经济时代");
                mineData.setUsername(enterprise.getEnterName());
            }
            data.setMine(mineData);
            List<Agent> agentList = agentRepository.findNotCusAgents(enterprise.getId());
            if (agentList.size() > 0) {
                List<FriendListData> friendListDataList = new ArrayList<>();
                List<FriendData> friendDataList = new ArrayList<>();
                FriendData friendData = new FriendData();
                friendData.setGroupname("我的好友");
                friendData.setId(1);
                for (Agent agent : agentList) {
                    FriendListData friendListData = new FriendListData();
                    if (ObjectUtils.isNotEmpty(agent.getAvatar())) {
                        friendListData.setAvatar(Constant.IMAGE_URL + agent.getAvatar());
                    } else {
                        friendListData.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547699014722&di=8d3f165fec3373fd51db448acbc61070&imgtype=0&src=http%3A%2F%2Fwww.qqzhi.com%2Fuploadpic%2F2015-02-11%2F201956189.jpg");
                    }
                    friendListData.setId("c" + agent.getInitCode());
                    friendListData.setUsername(agent.getAgentName());
                    friendListData.setSign("我是经纪人");
                    friendListData.setStatus("online");
                    friendListDataList.add(friendListData);
                }
                friendData.setList(friendListDataList);
                friendDataList.add(friendData);
                data.setFriend(friendDataList);
                data.setGroup(new ArrayList<>());
                resp.setMsg("");
                resp.setCode(0);
                resp.setData(data);

            } else {
                resp.setCode(1);
                resp.setMsg("");
            }
        } else {
            resp.setCode(1);
            resp.setMsg("");
        }
        return resp;
    }

    @GetMapping("msgbox")
    public ModelAndView msgBox() {
        ModelAndView mv = new ModelAndView(Constant.PREFIX + "msgbox");
        return mv;
    }

    @PostMapping("getusername")
    @ResponseBody
    public BaseJsonResponse getUserId(String user_id) {
        BaseJsonResponse response = new BaseJsonResponse();
        MessageData data = new MessageData();
        if (ObjectUtils.isNotEmpty(user_id)) {
            Agent agent = agentRepository.findAgentByInitCode(user_id);
            if (ObjectUtils.isNotNull(agent)) {
                if (ObjectUtils.isNotEmpty(agent.getAgentName())) {
                    data.setUsername(agent.getAgentName());
                }
                if (ObjectUtils.isNotEmpty(agent.getAvatar())) {
                    data.setAvatar(Constant.IMAGE_URL + agent.getAvatar());
                } else {
                    data.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547699014722&di=8d3f165fec3373fd51db448acbc61070&imgtype=0&src=http%3A%2F%2Fwww.qqzhi.com%2Fuploadpic%2F2015-02-11%2F201956189.jpg");
                }
                response.setMsg("获取发送者信息成功").setCode("1").setData(data);
            } else {
                response.setMsg("获取信息失败").setCode("2");
            }
        } else {
            response.setCode("2").setMsg("ID无效");
        }
        return response;
    }
}
