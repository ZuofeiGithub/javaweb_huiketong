package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.constant.UserType;
import com.huiketong.cofpasgers.entity.SystemUser;
import com.huiketong.cofpasgers.entity.UserRights;
import com.huiketong.cofpasgers.json.data.MenuData;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.SystemUserRepository;
import com.huiketong.cofpasgers.repository.UserRightsRepository;
import com.huiketong.cofpasgers.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
@RequestMapping(value = "/sys")
public class SystemController {
    @Autowired
    private SystemUserRepository userRepository;
    @Autowired
    private UserRightsRepository userRightsRepository;
    @Autowired
    private SystemUserRepository systemUserRepository;

    @GetMapping(value = "/getuser")
    @ResponseBody
    @CrossOrigin
    public Object GetUsers(){
        Map<String, Object> map = new HashMap<>();
        List<SystemUser> userList = systemUserRepository.findAll();
        map.put("data",userList);
        return map;
    }
    /**
     * 添加系统用户
     */
    @PostMapping(value = "/adduser")
    @ResponseBody
    @CrossOrigin
    public String addUser(HttpServletRequest request){
        String user_name = request.getParameter("user_name");
        String telphone = request.getParameter("telphone");
        String user_pwd = request.getParameter("user_pwd");
        String login_name = request.getParameter("login_name");

        SystemUser existuser = userRepository.findSystemUserByTelphoneOrLoginName(telphone,user_name);
        if(existuser != null)
        {
            return "用户已经存在";
        }
        try {
            String pwd  = MD5Util.getEncryptedPwd(user_pwd);
            SystemUser user = new SystemUser();
            user.setUsername(user_name);
            user.setLoginPassward(pwd);
            user.setTelphone(telphone);
            user.setLoginName(login_name);
            user.setReg_date(new Date());
            userRepository.save(user);
            UserRights userRights = new UserRights();
            userRights.setUserTel(telphone);
            userRights.setUserRight(UserType.SYSTEM.ordinal());
            userRights.setUserName(user_name);
            userRights.setLoginName("admin");
            userRights.setRightName("系统超级管理员");
            userRightsRepository.save(userRights);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "添加用户成功";
    }

    @PostMapping(value = "modifypwd")
    @ResponseBody
    public BaseJsonResponse ModifyPwd(String pwd) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        BaseJsonResponse response = new BaseJsonResponse();
        String md5pwd  = MD5Util.getEncryptedPwd(pwd);
        try {
            userRepository.updateUserPwd(md5pwd,"12345678901");
            response.setMsg("修改成功").setCode("2");
        }catch (Exception e){
            response.setMsg("修改失败").setCode("2");
        }

        return response;
    }

    @PostMapping(value = "menus")
    @ResponseBody
    public BaseJsonResponse menuList(){
        BaseJsonResponse response = new BaseJsonResponse();
        List<MenuData> dataList = new ArrayList<>();
        for(int i = 0; i < 4;i++)
        {
            MenuData data = new MenuData();
            data.setId(String.valueOf(i));
            data.setName("菜单项"+i);
            data.setUrl("1111");
            data.setPid(String.valueOf(0));
            dataList.add(data);
        }
        response.setData(dataList);
        response.setMsg("获取菜单成功");
        response.setCode("1");
        return response;
    }
}
