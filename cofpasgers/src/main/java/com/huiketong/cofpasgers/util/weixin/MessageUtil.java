package com.huiketong.cofpasgers.util.weixin;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MessageUtil {

    //消息常量
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    /**
     * xml 转集合
     * @param request
     * @return
     */
    public static Map<String,String> xmlToMap(HttpServletRequest request) throws DocumentException {
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream ins = null;
        try {
            ins = request.getInputStream();
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element e:list){
                map.put(e.getName(),e.getText());
            }
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *将文本消息对象转为xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    public static String initText(String toUserName,String fromUserName,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent("您发送的消息是:" + content);
        return textMessageToXml(text);
    }

    /**
     * 菜单消息
     * @return
     */
    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注，请按照菜单提示进行操作:\n\n");
        sb.append("1,课程介绍\n");
        sb.append("2,慕课网介绍\n");
        sb.append("3,回复?调出此菜单\n\n");
        return sb.toString();
    }

    public static String firstMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("经过7个多小时讨论，欧洲联盟除英国以外的27国领导人21日同意，无论退出欧盟协议下周能否在英国议会下院获通过，欧盟都不会在原先商定的29日与英国“分手”。\n" +
                "\n" +
                "英国首相特雷莎·梅接受上述安排，同时获得额外两周，以确定下一步“脱欧”进程。\n" +
                "\n" +
                "【新“大限”】\n" +
                "\n" +
                "欧盟春季峰会当天在比利时布鲁塞尔开幕。一名欧盟官员在全天会议结束后告诉法新社：“就今晚而言，4月12日是新的‘3月29日’。”\n" +
                "\n" +
                "美联社报道，欧盟领导人经过从下午持续到晚饭后的会谈，一致决定给英国两种选择：如果英国议会下院下周表决通过“脱欧”协议，英国可以把“脱欧”正式日期从3月29日推迟至5月22日、即欧洲议会选举开始前一天；如果“脱欧”协议第三次遭否决，英国下周不会“无协议脱欧”，但需要在4月12日以前“指明前行方向”。\n" +
                "\n" +
                "主持峰会的欧洲理事会主席唐纳德·图斯克说，4月12日以前“全部选项保持开放”，英国政府依然需要在“有协议脱欧”、“无协议脱欧”、大幅延期“脱欧”或不“脱欧”之间作出选择。");
        return sb.toString();
    }

    public static String secondMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("6月30日是特雷莎·梅先前致信图斯克时为英国申请延期“脱欧”的期限。图斯克20日回复说，可能同意英国短暂延期“脱欧”，前提是“脱欧”协议必须获英国议会下院通过。欧盟方面同时希望，尽力避免“脱欧”事宜与欧洲议会竞选相互干扰。\n" +
                "\n" +
                "综合路透社和彭博社报道，特雷莎·梅21日向欧盟27国领导人发表90分钟的讲话后离开会议室。其他领导人随后做7个多小时“头脑风暴”讨论，最终作出上述决定。\n" +
                "\n" +
                "一贯立场强硬的法国总统埃马纽埃尔·马克龙在英首相离开后提议，不管英国能否“有协议脱欧”，都把5月7日设为最后期限。这一“最后通牒”引发激烈讨论，欧盟领导人“罕见地”群策群力，商讨应给英国设定何种条件。不少人反对马克龙的过激提议，德国总理安格拉·默克尔居中调解。\n" +
                "\n" +
                "一些官员披露，欧盟领导人最终没有能就大幅延期“脱欧”的具体期限达成一致，可能至少会持续到今年底。\n" +
                "\n" +
                "马克龙在会商结束后说，“不是所有牌都在欧盟手中”，“责任现在英国人一方……我认为这是今天的大成就”。");
        return sb.toString();
    }

    /**
     * 图文消息转换成xml
     * @param imageTextMsg
     * @return
     */
    public static String imageTextMessageToXml(ImageTextMsg imageTextMsg){
        XStream xStream = new XStream();
        xStream.alias("xml",imageTextMsg.getClass());
        xStream.alias("item",new ImageTextBean().getClass());
        return xStream.toXML(imageTextMsg);
    }

    /**
     * 发送图文消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initImageTextMessage(String toUserName,String fromUserName){
        String message = null;
        List<ImageTextBean> imageTextBeans = new ArrayList<>();
        ImageTextMsg imageTextMsg = new ImageTextMsg();

        ImageTextBean imageTextBean = new ImageTextBean();
        imageTextBean.setTitle("慕课网介绍");
        imageTextBean.setDescription("描述");
        imageTextBean.setPicUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3140833740,2428704371&fm=26&gp=0.jpg");
        imageTextBean.setUrl("www.imooc.com");
        imageTextBeans.add(imageTextBean);
        imageTextMsg.setToUserName(fromUserName);
        imageTextMsg.setFromUserName(toUserName);
        imageTextMsg.setCreateTime(new Date().getTime());
        imageTextMsg.setMsgType(MESSAGE_NEWS);
        imageTextMsg.setArticles(imageTextBeans);
        imageTextMsg.setArticleCount(imageTextBeans.size());
        message = imageTextMessageToXml(imageTextMsg);
        return message;
    }
}
