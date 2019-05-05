package com.huiketong.cofpasgers.jgim;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.common.model.message.MessageBody;
import cn.jmessage.api.message.SendMessageResult;
import cn.jmessage.api.user.UserStateResult;
import com.huiketong.cofpasgers.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 左飞
 * @Date: 2019/1/16 11:05
 * @Version 1.0
 */
public class JPUserService {
    String appkey = "1cb4077c3dc3741a279a9a25";
    String masterSecret = "ed6ef3b9bc7835095e31f97c";

    private JMessageClient client;

    protected static final Logger LOG = LoggerFactory.getLogger(JPUserService.class);

    public JPUserService() {
        this.client = new JMessageClient(appkey, masterSecret);
    }

    public void registerUser(String username, String userpassword) {
        try {
            List<RegisterInfo> users = new ArrayList<>();
            RegisterInfo user = RegisterInfo.newBuilder()
                    .setUsername(username)
                    .setPassword(userpassword)
                    .build();
            users.add(user);
            RegisterInfo[] regUsers = new RegisterInfo[users.size()];
            String res = client.registerUsers(users.toArray(regUsers));
            LOG.info(res);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void sendSingleTextByAdmin(String fromId, String targetId, String context) {
        try {
            MessageBody body = MessageBody.text(context);
            SendMessageResult result = client.sendSingleTextByAdmin(targetId, fromId, body);
            LOG.info(String.valueOf(result.getMsg_id()));
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void getUserState(String username) {
        JMessageClient client = new JMessageClient(appkey, masterSecret);
        try {
            UserStateResult result = client.getUserState(username);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }
}
