package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.URL;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.OnlineCollege;
import com.huiketong.cofpasgers.json.layuidata.OnlineCollegeResp;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.OnlineCollegeRepository;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OnlineCollegeController {
    @Autowired
    OnlineCollegeRepository onlineCollegeRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @GetMapping("/onlinecollege")
    public String onlineCollege(){
        return URL.ONLINECOLLEGE;
    }

    @GetMapping("/onlineform")
    public String onlineForm(){
        return URL.ONLINEFORM;
    }


    @GetMapping("onlinecollegelist")
    @ResponseBody
    public OnlineCollegeResp onlineCOllegeList(String login_name,Integer page,Integer limit){
        OnlineCollegeResp resp = new OnlineCollegeResp();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_name);
        if(ObjectUtils.isNotEmpty(enterprise)){

            List<OnlineCollege> allCollegeList = onlineCollegeRepository.findAllByCompanyId(enterprise.getId());
            if(allCollegeList.size() > 0){
                resp.setCount(String.valueOf(allCollegeList.size()));
                List<OnlineCollege> collegeList  = onlineCollegeRepository.findListByCompanyIdLimit(enterprise.getId(),page-1,limit);
                if(collegeList.size() > 0){
                    List<OnlineCollegeResp.DataBean> dataBeanList = new ArrayList<>();
                    for(OnlineCollege onlineCollege:collegeList){
                        OnlineCollegeResp.DataBean dataBean = new OnlineCollegeResp.DataBean();
                        dataBean.setTitle(onlineCollege.getTitle());
                        dataBean.setType(onlineCollege.getArticleType());
                        dataBean.setCreatetime(onlineCollege.getCreateTime());
                        dataBean.setParticulars(onlineCollege.getParticulars() );
                        dataBeanList.add(dataBean);
                    }
                    resp.setMsg("");
                    resp.setData(dataBeanList);
                    resp.setCode(0);
                }
            }else{
                resp.setCode(0);
                resp.setCount("0");
                resp.setMsg("");
                resp.setCount("10");
                resp.setData(new ArrayList<>());
            }
        }
        return resp;
    }
}
