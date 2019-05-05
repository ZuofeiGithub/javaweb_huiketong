package com.huiketong.cofpasgers.weixinpay.config;

import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.huiketong.cofpasgers.weixinpay.util.PaymentKit;
import com.huiketong.cofpasgers.weixinpay.util.StrKit;
import com.huiketong.cofpasgers.weixinpay.util.WxPayApi;
import com.huiketong.cofpasgers.weixinpay.util.WxPayApiConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@ConfigurationProperties(prefix = "pay.wxpay")
public class MyWXPayConfig implements WXPayConfig {

    /**
     * 公众账号ID
     */
    private String appID;

    /**
     * 商户号
     */
    private String mchID;

    /**
     * API 密钥
     */
    private String key;

    /**
     * API 沙箱环境密钥
     */
    private String sandboxKey;

    /**
     * API证书绝对路径
     */
    private String certPath;

    /**
     * 退款异步通知地址
     */
    private String notifyUrl;

    private Boolean useSandbox;

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSandboxKey() {
        return sandboxKey;
    }

    public void setSandboxKey(String sandboxKey) {
        this.sandboxKey = sandboxKey;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Boolean getUseSandbox() {
        return useSandbox;
    }

    public void setUseSandbox(Boolean useSandbox) {
        this.useSandbox = useSandbox;
    }

    public void setHttpConnectTimeoutMs(int httpConnectTimeoutMs) {
        this.httpConnectTimeoutMs = httpConnectTimeoutMs;
    }

    public void setHttpReadTimeoutMs(int httpReadTimeoutMs) {
        this.httpReadTimeoutMs = httpReadTimeoutMs;
    }

    public static Logger getLog() {
        return log;
    }

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private int httpConnectTimeoutMs = 8000;

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private int httpReadTimeoutMs = 10000;

//    private String subAppId;
//    private String subMchId;
//    private String nonceStr;
//    private String body;
//    private String attach;
//    private String transactionId;
//    private String outTradeNo;
//    //订单金额
//    private String totalFee;
//    private String spbillCreateIp;
//    private WxPayApi.TradeType tradeType;
//    private String openId;
//    private String subOpenId;
//    private String authCode;
//    private String sceneInfo;
//
//    private String planId;
//    private String contractCode;
//    private String requestSerial;
//    private String contractDisplayAccount;
//    private String version;
//    private String timestamp;
//    private String returnApp;
//    private String returnWeb;
//
//    private String contractNotifyUrl;
//    private String contractId;
//
//    private MyWXPayConfig.PayModel payModel;

    /**
     * 分别对应商户模式、服务商模式
     */
    public static enum PayModel {
        BUSINESSMODEL, SERVICEMODE
    }

    /**
     * 构建请求参数
     *
     * @return Map<String ,   String>
     */
//    public Map<String, String> build() {
//        Map<String, String> map = new HashMap<String, String>();
//
//        if (getPayModel().equals(WxPayApiConfig.PayModel.SERVICEMODE)) {
//            //服务商上模式
//            map.put("sub_mch_id", getSubMchId());
//            if (StrKit.notBlank(getSubAppId())) {
//                map.put("sub_appid", subAppId);
//            }
//        }
//
//        /**
//         * openId和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
//         */
//        if (getTradeType().equals(WxPayApi.TradeType.JSAPI)) {
//            //公众号支付
//            if (StrKit.notBlank(getSubAppId())) {
//                map.put("sub_appid", subAppId);
//                map.put("sub_openid", getSubOpenId());
//            } else {
//                map.put("openid", getOpenId());
//            }
//        }
//        /**
//         * H5支付必填scene_info
//         */
//        if (getTradeType().equals(WxPayApi.TradeType.MWEB)) {
//            if (StrKit.isBlank(getSceneInfo())) {
//                throw new IllegalArgumentException("微信H5支付 scene_info 不能同时为空");
//            }
//            map.put("scene_info", getSceneInfo());
//        }
//
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//        map.put("nonce_str", getNonceStr());
//        map.put("body", getBody());
//        map.put("out_trade_no", getOutTradeNo());
//        map.put("total_fee", getTotalFee());
//        map.put("spbill_create_ip", getSpbillCreateIp());
//
//        map.put("trade_type", getTradeType().name());
//
//        map.put("attach", getAttach());
//        if (getTradeType().equals(WxPayApi.TradeType.MICROPAY)) {
//            map.put("auth_code", getAuthCode());
//            map.remove("trade_type");
//        } else {
//            map.put("notify_url", getNotifyUrl());
//        }
//
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//
//        return map;
//    }

    /**
     * 构建查询订单参数
     *
     * @return <Map<String, String>>
     */
//    public Map<String, String> orderQueryBuild() {
//        Map<String, String> map = new HashMap<String, String>();
//        if (getPayModel().equals(WxPayApiConfig.PayModel.SERVICEMODE)) {
//            //服务商上模式
//            map.put("sub_mch_id", getSubMchId());
//            map.put("sub_appid", getSubAppId());
//        }
//
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//
//        if (StrKit.notBlank(getTransactionId())) {
//            map.put("transaction_id", getTransactionId());
//        } else {
//            if (StrKit.isBlank(getOutTradeNo())) {
//                throw new IllegalArgumentException("out_trade_no,transaction_id 不能同时为空");
//            }
//            map.put("out_trade_no", getOutTradeNo());
//        }
//        map.put("nonce_str", String.valueOf(System.currentTimeMillis()));
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//        return map;
//    }

    /**
     * 构建申请签约Map
     *
     * @return 申请签约Map
     * @throws UnsupportedEncodingException
     */
//    public Map<String, String> entrustwebBuild() throws UnsupportedEncodingException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//        map.put("plan_id", getPlanId());
//        map.put("contract_code", getContractCode());
//        map.put("request_serial", getRequestSerial());
//        map.put("contract_display_account", getContractDisplayAccount());
//        map.put("notify_url", getNotifyUrl());
//        map.put("version", getVersion());
//        map.put("timestamp", getTimestamp());
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//
//        for (Map.Entry<String, String> param : map.entrySet()) {
//            String key = param.getKey();
//            String value = param.getValue();
//            value = PaymentKit.urlEncode(value);
//            map.put(key, value);
//        }
//        return map;
//    }
//
//
    public String getSign(Map<String, String> data) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append("423d8d3db6887e97a6054b34ee29e985");
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] array = new byte[0];
        try {
            array = md.digest(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb2 = new StringBuilder();
        for (byte item : array) {
            sb2.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb2.toString().toUpperCase();
    }
//
//    /**
//     * 构建支付中签约Map
//     *
//     * @return 支付中签约Map
//     */
//    public Map<String, String> contractorderBuild() {
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//        map.put("contract_appid", getAppId());
//        map.put("contract_mchid", getMchId());
//        map.put("out_trade_no", getOutTradeNo());
//        map.put("nonce_str", getNonceStr());
//        map.put("body", getBody());
//        map.put("attach", getAttach());
//        map.put("notify_url", getNotifyUrl());
//        map.put("total_fee", getTotalFee());
//        map.put("spbill_create_ip", getSpbillCreateIp());
//        map.put("trade_type", getTradeType().name());
//        if (getTradeType().equals(WxPayApi.TradeType.JSAPI)) {
//            map.put("openid", getOpenId());
//        }
//        map.put("plan_id", getPlanId());
//        map.put("contract_code", getContractCode());
//        map.put("request_serial", getRequestSerial());
//        map.put("contract_display_account", getContractDisplayAccount());
//        map.put("contract_notify_url", getContractNotifyUrl());
//
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//
//        return map;
//    }

    /**
     * 构建查询签约关系的Map
     *
     * @return 查询签约关系的Map
     */
//    public Map<String, String> querycontractBuild() {
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//
//        if (StrKit.notBlank(getPlanId())) {
//            map.put("plan_id", getPlanId());
//            map.put("contract_code", getContractCode());
//        } else {
//            map.put("contract_id", getContractId());
//        }
//        map.put("version", getVersion());
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//
//        return map;
//    }

    /**
     * 构建申请扣款的Map
     *
     * @return 申请扣款的Map
     */
//    public Map<String, String> pappayapplyBuild() {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("appid", getAppId());
//        map.put("mch_id", getMchId());
//        map.put("nonce_str", getNonceStr());
//        map.put("body", getBody());
//        map.put("attach", getAttach());
//        map.put("out_trade_no", getOutTradeNo());
//        map.put("total_fee", getTotalFee());
//        map.put("spbill_create_ip", getSpbillCreateIp());
//        map.put("notify_url", getNotifyUrl());
//        map.put("trade_type", getTradeType().name());
//        map.put("contract_id", getContractId());
//        map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
//        return map;
//    }


//    public String getSubAppId() {
//        return subAppId;
//    }
//
//    public MyWXPayConfig setSubAppId(String subAppId) {
//        if (StrKit.isBlank(subAppId))
//            throw new IllegalArgumentException("subAppId 值不能为空");
//        this.subAppId = subAppId;
//        return this;
//    }
//
//    public String getSubMchId() {
//        if (StrKit.isBlank(subMchId))
//            throw new IllegalArgumentException("subMchId 未被赋值");
//        return subMchId;
//    }
//
//    public MyWXPayConfig setSubMchId(String subMchId) {
//        if (StrKit.isBlank(subMchId))
//            throw new IllegalArgumentException("subMchId 值不能为空");
//        this.subMchId = subMchId;
//        return this;
//    }

//    public String getNonceStr() {
//        if (StrKit.isBlank(nonceStr))
//            nonceStr = String.valueOf(System.currentTimeMillis());
//        return nonceStr;
//    }
//
//    public MyWXPayConfig setNonceStr(String nonceStr) {
//        if (StrKit.isBlank(nonceStr))
//            throw new IllegalArgumentException("nonceStr 值不能为空");
//        this.nonceStr = nonceStr;
//        return this;
//    }

//    public String getBody() {
//        if (StrKit.isBlank(body))
//            throw new IllegalArgumentException("body 未被赋值");
//        return body;
//    }
//
//    public MyWXPayConfig setBody(String body) {
//        if (StrKit.isBlank(body))
//            throw new IllegalArgumentException("body 值不能为空");
//        this.body = body;
//        return this;
//    }

//    public String getAttach() {
//        return attach;
//    }
//
//    public MyWXPayConfig setAttach(String attach) {
//        if (StrKit.isBlank(attach))
//            throw new IllegalArgumentException("attach 值不能为空");
//        this.attach = attach;
//        return this;
//    }

//    public String getOutTradeNo() {
//        if (StrKit.isBlank(outTradeNo))
//            throw new IllegalArgumentException("outTradeNo 未被赋值");
//        return outTradeNo;
//    }
//
//    public MyWXPayConfig setOutTradeNo(String outTradeNo) {
//        this.outTradeNo = outTradeNo;
//        return this;
//    }

//    public String getTotalFee() {
//        if (StrKit.isBlank(totalFee))
//            throw new IllegalArgumentException("totalFee 未被赋值");
//        return totalFee;
//    }
//
//    public MyWXPayConfig setTotalFee(String totalFee) {
//        if (StrKit.isBlank(totalFee))
//            throw new IllegalArgumentException("totalFee 值不能为空");
//        this.totalFee = totalFee;
//        return this;
//    }

//    public String getSpbillCreateIp() {
//        if (StrKit.isBlank(spbillCreateIp))
//            throw new IllegalArgumentException("spbillCreateIp 未被赋值");
//        return spbillCreateIp;
//    }
//
//    public MyWXPayConfig setSpbillCreateIp(String spbillCreateIp) {
//        if (StrKit.isBlank(spbillCreateIp))
//            throw new IllegalArgumentException("spbillCreateIp 值不能为空");
//        this.spbillCreateIp = spbillCreateIp;
//        return this;
//    }

//    public String getNotifyUrl() {
//        if (StrKit.isBlank(notifyUrl))
//            throw new IllegalArgumentException("notifyUrl 未被赋值");
//        return notifyUrl;
//    }
//
//    public MyWXPayConfig setNotifyUrl(String notifyUrl) {
//        if (StrKit.isBlank(notifyUrl))
//            throw new IllegalArgumentException("notifyUrl 值不能为空");
//        this.notifyUrl = notifyUrl;
//        return this;
//    }

//    public WxPayApi.TradeType getTradeType() {
//        if (tradeType == null)
//            throw new IllegalArgumentException("tradeType 未被赋值");
//        return tradeType;
//    }
//
//    public MyWXPayConfig setTradeType(WxPayApi.TradeType tradeType) {
//        if (tradeType == null)
//            throw new IllegalArgumentException("mchId 值不能为空");
//        this.tradeType = tradeType;
//        return this;
//    }

//    public String getOpenId() {
//        if (StrKit.isBlank(openId))
//            throw new IllegalArgumentException("openId 未被赋值");
//        return openId;
//    }
//
//    public MyWXPayConfig setOpenId(String openId) {
//        if (StrKit.isBlank(openId))
//            throw new IllegalArgumentException("openId 值不能为空");
//        this.openId = openId;
//        return this;
//    }

//    public String getSubOpenId() {
//        if (StrKit.isBlank(subOpenId))
//            throw new IllegalArgumentException("subOpenId 未被赋值");
//        return subOpenId;
//    }
//
//    public MyWXPayConfig setSubOpenId(String subOpenId) {
//        if (StrKit.isBlank(subOpenId))
//            throw new IllegalArgumentException("subOpenId 值不能为空");
//        this.subOpenId = subOpenId;
//        return this;
//    }
    public String getPaternerKey() {
        if (StrKit.isBlank(key))
            throw new IllegalArgumentException("paternerKey 未被赋值");
        return key;
    }
//
//    public MyWXPayConfig setPaternerKey(String paternerKey) {
//        if (StrKit.isBlank(paternerKey))
//            throw new IllegalArgumentException("paternerKey 值不能为空");
//        this.key = paternerKey;
//        return this;
//    }

//    public MyWXPayConfig.PayModel getPayModel() {
//        if (payModel == null)
//            payModel = MyWXPayConfig.PayModel.BUSINESSMODEL;
//        return payModel;
//    }
//
//    public MyWXPayConfig setPayModel(MyWXPayConfig.PayModel payModel) {
//        if (payModel == null)
//            payModel = MyWXPayConfig.PayModel.BUSINESSMODEL;
//        this.payModel = payModel;
//        return this;
//    }

//    public String getAuthCode() {
//        if (StrKit.isBlank(authCode))
//            throw new IllegalArgumentException("authCode 未被赋值");
//        return authCode;
//    }
//
//    public MyWXPayConfig setAuthCode(String authCode) {
//        if (StrKit.isBlank(key))
//            throw new IllegalArgumentException("authCode 值不能为空");
//        this.authCode = authCode;
//        return this;
//    }

//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public MyWXPayConfig setTransactionId(String transactionId) {
//        if (StrKit.isBlank(transactionId))
//            throw new IllegalArgumentException("transactionId 值不能为空");
//        this.transactionId = transactionId;
//        return this;
//    }
//
//    public String getSceneInfo() {
//        return sceneInfo;
//    }
//
//    public MyWXPayConfig setSceneInfo(String sceneInfo) {
//        this.sceneInfo = sceneInfo;
//        return this;
//    }

//    public String getPlanId() {
//        if (StrKit.isBlank(planId))
//            throw new IllegalArgumentException("planId 未被赋值");
//        return planId;
//    }
//
//    public MyWXPayConfig setPlanId(String planId) {
//        if (StrKit.isBlank(planId))
//            throw new IllegalArgumentException("planId 值不能为空");
//        this.planId = planId;
//        return this;
//    }
//
//    public String getContractCode() {
//        if (StrKit.isBlank(contractCode))
//            throw new IllegalArgumentException("contractCode 未被赋值");
//        return contractCode;
//    }
//
//    public MyWXPayConfig setContractCode(String contractCode) {
//        if (StrKit.isBlank(contractCode))
//            throw new IllegalArgumentException("contractCode 值不能为空");
//        this.contractCode = contractCode;
//        return this;
//    }
//
//    public String getRequestSerial() {
//        if (StrKit.isBlank(requestSerial))
//            throw new IllegalArgumentException("requestSerial 未被赋值");
//        return requestSerial;
//    }
//
//    public MyWXPayConfig setRequestSerial(String requestSerial) {
//        if (StrKit.isBlank(requestSerial))
//            throw new IllegalArgumentException("requestSerial 值不能为空");
//        this.requestSerial = requestSerial;
//        return this;
//    }
//
//    public String getContractDisplayAccount() {
//        if (StrKit.isBlank(contractDisplayAccount))
//            throw new IllegalArgumentException("contractDisplayAccount 未被赋值");
//        return contractDisplayAccount;
//    }
//
//    public MyWXPayConfig setContractDisplayAccount(String contractDisplayAccount) {
//        if (StrKit.isBlank(contractDisplayAccount))
//            throw new IllegalArgumentException("contractDisplayAccount 值不能为空");
//        this.contractDisplayAccount = contractDisplayAccount;
//        return this;
//    }

//    public String getVersion() {
//        if (StrKit.isBlank(version))
//            version = "1.0";
//        return version;
//    }
//
//    public MyWXPayConfig setVersion(String version) {
//        if (StrKit.isBlank(version))
//            throw new IllegalArgumentException("version 值不能为空");
//        this.version = version;
//        return this;
//    }
//
//    public String getTimestamp() {
//        if (StrKit.isBlank(timestamp)) {
//            timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        }
//        return timestamp;
//    }

//    public MyWXPayConfig setTimestamp(String timestamp) {
//        if (StrKit.isBlank(timestamp))
//            throw new IllegalArgumentException("timestamp 值不能为空");
//        if (timestamp.length() != 10)
//            throw new IllegalArgumentException("timestamp 值必须为10位");
//        this.timestamp = timestamp;
//        return this;
//    }

//    public String getReturnApp() {
//        return returnApp;
//    }
//
//    public MyWXPayConfig setReturnApp(String returnApp) {
//        this.returnApp = returnApp;
//        return this;
//    }
//
//    public String getReturnWeb() {
//        return returnWeb;
//    }
//
//    public MyWXPayConfig setReturnWeb(String returnWeb) {
//        this.returnWeb = returnWeb;
//        return this;
//    }

//    public String getContractNotifyUrl() {
//        if (StrKit.isBlank(contractNotifyUrl))
//            throw new IllegalArgumentException("contractNotifyUrl 未被赋值");
//        return contractNotifyUrl;
//    }
//
//    public MyWXPayConfig setContractNotifyUrl(String contractNotifyUrl) {
//        this.contractNotifyUrl = contractNotifyUrl;
//        return this;
//    }

    //    public String getContractId() {
//        if (StrKit.isBlank(contractId))
//            throw new IllegalArgumentException("contractId 未被赋值");
//        return contractId;
//    }
//
//    public MyWXPayConfig setContractId(String contractId) {
//        this.contractId = contractId;
//        return this;
//    }
    @Override
    public String getAppID() {
        return this.appID;

    }

    @Override
    public String getMchID() {
        return this.mchID;
    }

    @Override
    public String getKey() {
        if (useSandbox) {
            return sandboxKey;
        }
        return this.key;
    }

    /**
     * 获取商户证书内容
     *
     * @return
     */
    @Override
    public InputStream getCertStream() {
        File cerFile = new File(certPath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(cerFile);
        } catch (FileNotFoundException e) {
            log.error("cert file not found,path={},exception is :{}", certPath, e);
        }
        return inputStream;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return this.httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return this.httpReadTimeoutMs;
    }

}
