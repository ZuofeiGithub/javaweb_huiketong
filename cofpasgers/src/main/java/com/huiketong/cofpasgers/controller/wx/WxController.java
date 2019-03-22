package com.huiketong.cofpasgers.controller.wx;

import com.huiketong.cofpasgers.util.weixin.CheckUtil;
import com.huiketong.cofpasgers.util.weixin.MessageUtil;
import com.huiketong.cofpasgers.util.weixin.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
public class WxController {
    private static Logger logger = LoggerFactory.getLogger(WxController.class);
    @GetMapping("/weinxin")
    public String Wx(String signature,String timestamp,String nonce,String echostr){
        logger.info("weixin请求");
        if(CheckUtil.checkSignature(signature,timestamp,nonce)){
            return echostr;
        }
        return signature+timestamp+nonce+echostr;
    }

    @PostMapping("/weinxin")
    public String Wx(HttpServletRequest request){
        Map<String,String> map = MessageUtil.xmlToMap(request);
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");
        String  msgType = map.get("MsgType");
        String content = map.get("Content");

        String message = null;
        if("text".equals(msgType)){
            TextMessage text = new TextMessage();
            text.setFromUserName(toUserName);
            text.setToUserName(fromUserName);
            text.setMsgType("text");
            text.setCreateTime(new Date().getTime());
            text.setContent("您发送的消息是:"+content);
            message = MessageUtil.textMessageToXml(text);
        }
        return message;
    }
}
