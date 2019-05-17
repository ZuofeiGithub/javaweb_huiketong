package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.URL;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.entity.OnlineCollege;
import com.huiketong.cofpasgers.json.layuidata.OnlineCollegeResp;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import com.huiketong.cofpasgers.repository.OnlineCollegeRepository;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.codehaus.jackson.map.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
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
                        dataBean.setId(onlineCollege.getId());
                        dataBean.setTitle(onlineCollege.getTitle() == null ? "":onlineCollege.getTitle());
                        dataBean.setType(onlineCollege.getArticleType() == null ? 0: onlineCollege.getArticleType());
                        dataBean.setCreatetime(onlineCollege.getCreateTime());
                        dataBean.setParticulars(onlineCollege.getParticulars() == null ? "":onlineCollege.getParticulars());
                        dataBean.setVideoIntro(onlineCollege.getVideoIntro() == null ? "":onlineCollege.getVideoIntro());
                        dataBean.setVideoUrl(onlineCollege.getVideoUrl() == null ? "" : onlineCollege.getVideoUrl());
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

    @PostMapping(value = "addShareContext")
    @ResponseBody
    public BaseJsonResponse addShareContext(String login_name,String title,Integer sharetype,String particulars,String videoUrl,String videointro){
        BaseJsonResponse response = new BaseJsonResponse();
        OnlineCollege onlineCollege = new OnlineCollege();
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_name);
        if(ObjectUtils.isNotEmpty(enterprise)){
            onlineCollege.setCompanyId(enterprise.getId());
            onlineCollege.setArticleType(sharetype);
            onlineCollege.setCreateTime(new Date());
            onlineCollege.setParticulars(particulars);
            onlineCollege.setTitle(title);
            onlineCollege.setVideoIntro(videointro);
            onlineCollege.setVideoUrl(videoUrl);
            try {
                onlineCollegeRepository.save(onlineCollege);
                response.setCode("0").setMsg("添加成功").setData(onlineCollege);
            }catch (Exception e){
                response.setCode("1").setMsg("添加失败");
            }
        }else{
            response.setCode("1").setMsg("添加失败");
        }
        return response;
    }

    @PostMapping(value = "delShareContext")
    @ResponseBody
    public BaseJsonResponse delShareContext(Integer id){
        BaseJsonResponse response = new BaseJsonResponse();
        try {
            onlineCollegeRepository.deleteById(id);
            response.setMsg("删除成功").setCode("0");
        }catch (Exception e){
            response.setCode("1").setMsg("删除失败");
        }

        return  response;
    }
}
