package com.huiketong.cofpasgers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 注入ServerEndpointExporter之后
 * 这个bean会自动注册使用@ServerEndpoit注解声明的websocket
 *
 * @Author: ￣左飞￣
 * @Date: 2018/12/23 14:12
 * @Version 1.0
 */
//@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
