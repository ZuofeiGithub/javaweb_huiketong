package com.huiketong.cofpasgers.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 左飞
 * @Date: 2019/1/21 16:20
 * @Version 1.0
 */
public class HttpClientUtil {

    private static final Object HTTP_TIMEOUT = 3000;

    /**
     * requestURL  请求地址(含参数)
     * 远程主机响应正文
     */
    public static String sendGetRequest(String reqURL, String encodeCharset) {

        String respContent = "{status:\"0\",result:\"通信失败\"}"; // 响应内容

        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例

        // 设置代理服务器

        // httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
        // new HttpHost("10.0.0.4", 8080));

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT); // 连接超时20s

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT); // 读取超时50s

        HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet

        try {

            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求

            HttpEntity entity = response.getEntity(); // 获取响应实体

            if (null != entity) {

                // respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式

                respContent = EntityUtils.toString(entity, encodeCharset);

                // Consume response content

                EntityUtils.consume(entity);

            }

            System.out.println("-------------------------------------------------------------------------------------------");

            StringBuilder respHeaderDatas = new StringBuilder();

            for (Header header : response.getAllHeaders()) {

                respHeaderDatas.append(header.toString()).append("\r\n");

            }

            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息

            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息

            String respBodyMsg = respContent; // HTTP应答报文体信息

            System.out.println("HTTP请求地址：" + reqURL);

            System.out.println("HTTP应答完整报文\n" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n" + respBodyMsg + "");
//
            System.out.println("-------------------------------------------------------------------------------------------");

        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            // Should catch ConnectTimeoutException, and don`t catch
            // org.apache.http.conn.HttpHostConnectException

            // //LogUtil.getLogger().error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下",
            // cte);

        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下",
            // ste);

        } catch (ClientProtocolException cpe) {
            cpe.printStackTrace();
            // 该异常通常是协议错误导致:比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合HTTP协议要求等

            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下",
            // cpe);

        } catch (ParseException pe) {
            pe.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下",
            // pe);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            // 该异常通常是网络原因引起的,如HTTP服务器未启动等

            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下",
            // ioe);

        } catch (Exception e) {
            e.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);

        } finally {

            // 关闭连接,释放资源

            httpClient.getConnectionManager().shutdown();

        }
        return respContent;
    }

    /**
     * @param reqURL        请求地址
     * @param reqData       请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
     * @param encodeCharset 编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
     */
    public static String sendPostRequest(String reqURL, String reqData, String encodeCharset, String contentType) {

        String respContent = "通信失败";

        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT);

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT);

        HttpPost httpPost = new HttpPost(reqURL);

        // 由于下面使用的是new
        // StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain;
        // charset=ISO-8859-1

        // 这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息

        httpPost.setHeader(HTTP.CONTENT_TYPE, contentType + "; charset=" + encodeCharset);

        try {

            httpPost.setEntity(new StringEntity(reqData == null ? "" : reqData, encodeCharset));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (null != entity) {

                respContent = EntityUtils.toString(entity, "utf-8");

                EntityUtils.consume(entity);

            }

            System.out.println("-------------------------------------------------------------------------------------------");

            StringBuilder respHeaderDatas = new StringBuilder();

            for (Header header : response.getAllHeaders()) {

                respHeaderDatas.append(header.toString()).append("\r\n");

            }

            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息

            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息

            String respBodyMsg = respContent; // HTTP应答报文体信息

            System.out.println("HTTP请求地址：" + reqURL);
            System.out.println("HTTP请求参数：" + reqData);
            System.out.println("HTTP请求方式：" + contentType);
            System.out.println("HTTP应答完整报文\n" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n" + respBodyMsg + "");
//
            System.out.println("-------------------------------------------------------------------------------------------");

        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下",
            // cte);

        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下",
            // ste);

        } catch (Exception e) {
            e.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown();

        }

        return respContent;

    }

    /**
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,当其为null时,则取HttpClient内部默认的ISO-8859-1编码请求参数
     * @return 远程主机响应正文
     */
    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset) {

        String responseContent = "通信失败";

        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT);

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT);

        // 创建TrustManager()

        // 用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated

        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        // 创建HostnameVerifier

        // 用于解决javax.net.ssl.SSLException: hostname in certificate didn't match:
        // <123.125.97.66> != <123.125.97.241>

        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {

            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        };

        try {

            // TLS1.0与SSL3.0基本上没有太大的差别,可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext

            SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);

            // 使用TrustManager来初始化该上下文,TrustManager只是被SSL的Socket所使用

            sslContext.init(null, new TrustManager[]{trustManager}, null);

            // 创建SSLSocketFactory

            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, hostnameVerifier);

            // 通过SchemeRegistry将SSLSocketFactory注册到HttpClient上

            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            // 创建HttpPost

            HttpPost httpPost = new HttpPost(reqURL);

            // 由于下面使用的是new
            // UrlEncodedFormEntity(....),所以这里不需要手工指定CONTENT_TYPE为application/x-www-form-urlencoded

            // 因为在查看了HttpClient的源码后发现,UrlEncodedFormEntity所采用的默认CONTENT_TYPE就是application/x-www-form-urlencoded

            // httpPost.setHeader(HTTP.CONTENT_TYPE,
            // "application/x-www-form-urlencoded; charset=" + encodeCharset);

            // 构建POST请求的表单参数

            if (null != params) {

                List<NameValuePair> formParams = new ArrayList<NameValuePair>();

                for (Map.Entry<String, String> entry : params.entrySet()) {

                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

                }

                httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset));

            }

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (null != entity) {

                responseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());

                EntityUtils.consume(entity);

            }

        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下",
            // cte);

        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下",
            // ste);

        } catch (Exception e) {
            e.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown();

        }
        return responseContent;
    }

    /**
     * @param reqURL
     * @param encodeCharset
     * @return
     */
    public static String sendGetSSLRequest(String reqURL, String encodeCharset) {

        String responseContent = "通信失败";

        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIMEOUT);

        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIMEOUT);

        // 创建TrustManager()

        // 用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated

        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        // 创建HostnameVerifier

        // 用于解决javax.net.ssl.SSLException: hostname in certificate didn't match:
        // <123.125.97.66> != <123.125.97.241>
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {

            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        };

        try {

            // TLS1.0与SSL3.0基本上没有太大的差别,可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext

            SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);

            // 使用TrustManager来初始化该上下文,TrustManager只是被SSL的Socket所使用

            sslContext.init(null, new TrustManager[]{trustManager}, null);

            // 创建SSLSocketFactory

            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, hostnameVerifier);

            // 通过SchemeRegistry将SSLSocketFactory注册到HttpClient上

            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            // 创建org.apache.http.client.methods.HttpGet
            HttpGet httpGet = new HttpGet(reqURL);

            // 由于下面使用的是new
            // UrlEncodedFormEntity(....),所以这里不需要手工指定CONTENT_TYPE为application/x-www-form-urlencoded

            // 因为在查看了HttpClient的源码后发现,UrlEncodedFormEntity所采用的默认CONTENT_TYPE就是application/x-www-form-urlencoded

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();

            if (null != entity) {

                responseContent = EntityUtils.toString(entity, encodeCharset);

                EntityUtils.consume(entity);

            }

            System.out.println(
                    "-------------------------------------------------------------------------------------------");

            StringBuilder respHeaderDatas = new StringBuilder();

            for (Header header : response.getAllHeaders()) {

                respHeaderDatas.append(header.toString()).append("\r\n");

            }

            @SuppressWarnings("unused")
            String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息

            @SuppressWarnings("unused")
            String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息

            @SuppressWarnings("unused")
            String respBodyMsg = responseContent; // HTTP应答报文体信息

//          System.out.println("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");
//
//          System.out.println(
//                  "-------------------------------------------------------------------------------------------");

        } catch (ConnectTimeoutException cte) {
            cte.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下",
            // cte);

        } catch (SocketTimeoutException ste) {
            ste.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下",ste);

        } catch (Exception e) {
            e.printStackTrace();
            // LogUtil.getLogger().error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);

        } finally {

            httpClient.getConnectionManager().shutdown();

        }

        return responseContent;

    }

    public static void main(String[] args) {

    }
}
