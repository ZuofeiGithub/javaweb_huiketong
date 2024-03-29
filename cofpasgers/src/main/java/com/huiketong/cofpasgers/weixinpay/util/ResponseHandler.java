package com.huiketong.cofpasgers.weixinpay.util;

import com.huiketong.cofpasgers.util.MD5Util;
import com.huiketong.cofpasgers.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
public class ResponseHandler {
    @Value("${pay.wxpay.key}")
    private String key;
    private SortedMap parameters;
    private String debugInfo;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String uriEncoding;

    /**
     * * @param request	 * @param response
     */
    public ResponseHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.parameters = new TreeMap();
        this.debugInfo = "";
        this.uriEncoding = "";
    }

    /**
     *
     */
    public String getKey() {
        return key;
    }

    /**
     *
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * ֵ	 * @param parameter 	 * @return String
     */
    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }

    /**
     * @param parameter * @param parameterValueֵ
     */
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    /**
     * * @return SortedMap
     */
    public SortedMap getAllParameters() {
        return this.parameters;
    }

    public void setAllparamenters(SortedMap map) {
        this.parameters = map;
    }

    /**
     * 微信异步回调签名	 * @return boolean
     */
    public boolean isTenpaySign() {
        StringBuffer sb = new StringBuffer();
        if (ObjectUtils.isNotNull(this.parameters)) {
            Set es = this.parameters.entrySet();
            if (ObjectUtils.isNotNull(es)) {
                Iterator it = es.iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    String k = (String) entry.getKey();
                    String v = (String) entry.getValue();
                    if (!"sign".equals(k) && null != v && !"".equals(v)) {
                        sb.append(k + "=" + v + "&");
                    }
                }
                sb.append("key=" + "423d8d3db6887e97a6054b34ee29e985");
                String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
                String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();
                String tenpaySign = this.getParameter("sign").toLowerCase();
                System.out.println("sign:" + sign + "      tenpaysign:" + tenpaySign);
                return tenpaySign.equals(sign);
            }
            return false;
        }
        return false;
    }

    /**
     * * @throws IOException
     */
    public void sendToCFT(String msg) throws IOException {
        String strHtml = msg;
        PrintWriter out = this.getHttpServletResponse().getWriter();
        out.println(strHtml);
        out.flush();
        out.close();
    }

    /**
     * * @return String
     */
    public String getUriEncoding() {
        return uriEncoding;
    }

    /**
     * @param uriEncoding * @throws UnsupportedEncodingException
     */
    public void setUriEncoding(String uriEncoding) throws UnsupportedEncodingException {
        if (!"".equals(uriEncoding.trim())) {
            this.uriEncoding = uriEncoding;
            String enc = TenpayUtil.getCharacterEncoding(request, response);
            Iterator it = this.parameters.keySet().iterator();
            while (it.hasNext()) {
                String k = (String) it.next();
                String v = this.getParameter(k);
                v = new String(v.getBytes(uriEncoding.trim()), enc);
                this.setParameter(k, v);
            }
        }
    }

    /**
     *
     */
    public String getDebugInfo() {
        return debugInfo;
    }

    /**
     *
     */
    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    protected HttpServletResponse getHttpServletResponse() {
        return this.response;
    }

}
