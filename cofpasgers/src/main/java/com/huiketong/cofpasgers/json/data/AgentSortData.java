package com.huiketong.cofpasgers.json.data;

import com.huiketong.cofpasgers.entity.Agent;
import lombok.Data;

import java.util.List;

@Data
public class AgentSortData {
    List<Agent> agentName; //经纪人姓名
    List<Integer> agentNum;//邀请的经纪人总数
    List<Integer> customerNum;//报备的客户总数
    List<Integer> dealCustomerNum;//成交的客户总数
    List<Double> dealMoney;//成交的合同总金额
}
