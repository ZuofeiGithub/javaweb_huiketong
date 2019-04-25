package com.huiketong.cofpasgers.controller.wx;

import com.huiketong.cofpasgers.util.weixin.CheckUtil;
import com.huiketong.cofpasgers.util.weixin.MessageUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RestController
public class WxController {
    private static Logger logger = LoggerFactory.getLogger(WxController.class);

    @GetMapping("/weinxin")
    public String Wx(String signature, String timestamp, String nonce, String echostr) {
        logger.info("weixin请求");
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return signature + timestamp + nonce + echostr;
    }

    /**
     * 微信消息类型有:text,image,voice,video,link,location
     * 事件推送消息:event:包括:关注subscribe,取消关注unsubscribe,菜单点击CLICK,VIEW
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/weinxin")
    public void Wx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter  out = response.getWriter();
        try {

            Map<String, String> map = MessageUtil.xmlToMap(request);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");

            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
                if("1".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.firstMenu());
                }else if("2".equals(content)){
                    message = MessageUtil.initImageTextMessage(toUserName,fromUserName);
                }else if("?".equals(content) || "？".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                }
            }else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
                String eventType = map.get("Event");
                if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){

                }
            }
            out.print(message);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
