package com.huiketong.cofpasgers.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: ￣左飞￣
 * @Date: 2018/12/23 13:51
 * @Version 1.0
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocket {
    //用来存放每一个客户端对应的Websocket对象
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    //用来记录sessionId和该session进行绑定
    private static Map<String, Session> map = new HashMap<String, Session>();
    //与某个客户端的连接绘画，需要通过它来给客户端发送数据
    private Session session;
    private String nickname;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
//        this.nickname = nickname;
        map.put(session.getId(), session);
        webSockets.add(this);
        System.out.println("有新连接加入!当前在线人数为:" + webSockets.size());
        this.session.getAsyncRemote().sendText(this.nickname + "上线了" + "(他的频道号是" + session.getId() + ")");
    }

    @OnClose
    public void onClose(Session session) {
        webSockets.remove(this);
        map.remove(session.getId());
        System.out.println("有一连接关闭！当前在线人数为" + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("nickname") String nickname) {
        System.out.println("来自客户端的消息:" + message);
        ObjectMapper objectMapper = new ObjectMapper();

    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void broadcast(String nickname, String socketMsg) {
        for (WebSocket item : webSockets) {
            item.session.getAsyncRemote().sendText("群发消息");
        }
    }
}
