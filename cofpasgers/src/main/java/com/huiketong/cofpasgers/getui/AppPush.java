package com.huiketong.cofpasgers.getui;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/23 14:12
 * @Version 1.0
 */
public class AppPush extends PushBase {
    public static void pushToSigle(String deviceId,String title,String context) {
        //显示每个用户的用户状态，false:不显示，true：显示
        System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");

        // 推送主类
        IIGtPush push = new IGtPush(API, APPKEY, MASTERSECRET);

        try {
            ListMessage message = new ListMessage();

            //通知模版：支持TransmissionTemplate、LinkTemplate、NotificationTemplate，此处以NotificationTemplate为例
            //在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用
            NotificationTemplate template = new NotificationTemplate();

            template.setAppId(APPID);                           //应用APPID
            template.setAppkey(APPKEY);                         //应用APPKEY

            //通知属性设置：如通知的标题，内容
            template.setTitle(title);                    // 通知标题
            template.setText(context);                 // 通知内容
//            template.setLogo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548235349596&di=e6e4bc26f3af0b228515f04d752d5c8d&imgtype=0&src=http%3A%2F%2Fpic31.nipic.com%2F20130622%2F10558908_111329508000_2.jpg");               // 通知图标，需要客户端开发时嵌入
            template.setIsRing(false);                  // 收到通知是否响铃，可选，默认响铃
//          template.setIsVibrate(true);                    // 收到通知是否震动，可选，默认振动
            template.setIsClearable(true);              // 通知是否可清除，可选，默认可清除

            template.setTransmissionType(2);                // 收到消息是否立即启动应用，1为立即启动，2则广播等待客户端自启动

            JSONObject json = new JSONObject();
            json.put("type","report_notify");
            template.setTransmissionContent(json.toJSONString());  // 透传内容（点击通知后SDK将透传内容传给你的客户端，需要客户端做相应开发）

            message.setData(template);
//          message.setOffline(true);       //用户当前不在线时，是否离线存储，可选，默认不存储
//          message.setOfflineExpireTime(72 * 3600 * 1000);     //离线有效时间，单位为毫秒，可选

            // 接收者
            List<Target> targets = new ArrayList<Target>();
            Target target1 = new Target();
//          Target target2 = new Target();                      //如果需要可设置多个接收者
            target1.setAppId(APPID);                            //接收者安装的应用的APPID
            target1.setClientId(deviceId);                      //接收者的ClientID

            //如需，可设置多个接收者
//          target2.setAppId(APPID2);                           //接收者2安装应用的APPID
//          target2.setClientId(CLIENTID2);                     //接收者2的ClientID

            targets.add(target1);
//          targets.add(target2);

            //推送前通过该接口申请“ContentID”
            String contentId = push.getContentId(message);
            IPushResult ret = push.pushMessageToList(contentId, targets);

            System.out.println(ret.getResponse().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
