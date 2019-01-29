package com.huiketong.cofpasgers.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @Author: 左飞
 * @Date: 2019/1/12 10:38
 * @Version 1.0
 */
public class RequestUtil {
    /**
     * 获取用户的ip	 *
     */
    public static String GetIp() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localip = ia.getHostAddress();
            return localip;
        } catch (Exception e) {
            return "";
        }
    }

    //xml解析
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map<String, String> m = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        IsNext(m, it);
        //关闭流
        in.close();
        return m;
    }

    private static void IsNext(Map<String, String> m, Iterator it) {
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            m.put(k, v);
        }
    }

    //xml解析
    public static SortedMap<String, String> doXMLParseWithSorted(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        SortedMap<String, String> m = new TreeMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        IsNext(m, it);
        //关闭流
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * * Map转xml数据
     */
    public static String GetMapToXML(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
}
