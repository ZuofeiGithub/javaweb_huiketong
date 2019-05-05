package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.entity.Commission;
import com.huiketong.cofpasgers.entity.Enterprise;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.CommissionRepository;
import com.huiketong.cofpasgers.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 所有页面的视图部分
 */
@Controller
public class PageViewController {
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;

    /**
     * 佣金规则视图
     *
     * @return
     */
    @GetMapping(value = "/commission_rule")
    public Object CommissionRule() {
        ModelAndView mv = new ModelAndView("commission_rule");
        return mv;
    }

    @PostMapping(value = "/getcommission")
    @ResponseBody
    public Commission GetCommission(HttpServletRequest request) {
        String login_id = request.getParameter("login_id");
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_id);
        if (enterprise != null) {
            Commission commission = commissionRepository.findCommissionByCompanyId(enterprise.getId());
            if (commission != null) {
                return commission;
            } else {
                Commission newcommission = new Commission();
                newcommission.setCompanyId(enterprise.getId());
                newcommission.setScore(new BigDecimal(20.0));
                newcommission.setUpdateTime(new Date());
                return newcommission;
            }
        } else {
            return null;
        }
    }

//    @PostMapping(value = "/modifycommission")
//    @ResponseBody
//    public BaseJsonResponse ModifyCommission(HttpServletRequest request){
//        String login_id = request.getParameter("login_id");
//        String score = request.getParameter("score");
//        BaseJsonResponse response = new BaseJsonResponse();
//        if(login_id !=null&&!login_id.isEmpty()){
//          Enterprise enterprise = enterpriseRepository.findEnterpriseByEnterLoginName(login_id);
//          if(enterprise != null&&score!=null&&!score.isEmpty()){
//              Commission commission = commissionRepository.findCommissionByCompanyId(enterprise.getId());
//              if(commission != null)
//              {
//                  commissionRepository.updateComByComId(score,enterprise.getId());
//                  response.setCode("1");
//                  response.setMsg("修改成功");
//              }else{
//                  Commission newcommission = new Commission();
//                  newcommission.setCompanyId(enterprise.getId());
//                  newcommission.setUpdateTime(new Date());
//                  newcommission.setScore(new BigDecimal(score));
//                  commissionRepository.save(newcommission);
//                  response.setCode("1");
//                  response.setMsg("修改成功");
//              }
//          }else{
//              response.setCode("2");
//              response.setMsg("修改失败");
//          }
//        }else{
//            response.setCode("2");
//            response.setMsg("修改失败");
//        }
//        return response;
//    }

    @GetMapping(value = "changeuser")
    public ModelAndView ChangeUser() {
        ModelAndView mv = new ModelAndView("changeuser");
        return mv;
    }
}
