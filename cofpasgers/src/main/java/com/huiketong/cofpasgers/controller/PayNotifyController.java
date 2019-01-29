package com.huiketong.cofpasgers.controller;

import com.huiketong.cofpasgers.repository.CommodityOrderRepository;
import com.huiketong.cofpasgers.repository.OrderRepository;
import com.huiketong.cofpasgers.util.RequestUtil;
import com.huiketong.cofpasgers.weixinpay.config.WXPayClient;
import com.huiketong.cofpasgers.weixinpay.util.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * @Author: 左飞
 * @Date: 2019/1/14 10:12
 * @Version 1.0
 */
@Controller
public class PayNotifyController {
    @Autowired
    CommodityOrderRepository orderRepository;
    @Value("${pay.wxpay.key}")
    private String Key;
    @Autowired
    private WXPayClient wxPayClient;
    private static final Logger logger = LoggerFactory.getLogger(PayNotifyController.class);
    @RequestMapping("notify")
    @ResponseBody
    @CrossOrigin
    public String payNotify(HttpServletRequest request, HttpServletResponse response){
        logger.error("调用回调接口");
        SortedMap<String, String> map = null;
        try {
            map = wxPayClient.getNotifyParameter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> return_data = new HashMap<String, String>();
        //创建支付应答对象
        ResponseHandler resHandler = new ResponseHandler(request, response);
        resHandler.setAllparamenters(map);
        resHandler.setKey(Key);
        //判断签名
        logger.error("开始判断签名接口");
        if (resHandler.isTenpaySign()) {

            if (!map.get("return_code").equals("SUCCESS")) {
                return_data.put("return_code", "FAIL");
                return_data.put("return_msg", "return_code不正确");
                logger.error("return_code不正确");
            } else {
                String out_trade_no = map.get("out_trade_no");
                if (!map.get("result_code").equals("SUCCESS")) {
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "result_code不正确");
                    orderRepository.updateStatusByOrderStatusNum(9,out_trade_no);
                }

                String time_end = map.get("time_end");
                BigDecimal total_fee = new BigDecimal(map.get("total_fee"));
                System.out.println("out_trade_no:"+out_trade_no);
                //付款完成后，支付宝系统发送该交易状态通知
                logger.error("交易成功");
                orderRepository.updateStatusByOrderStatusNum(1,out_trade_no);
               /* Map order = orderdao.PaymentEndGetOrderInfo(out_trade_no);
                if (order == null) {
                    System.out.println("订单不存在");
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "订单不存在");
                    return WXRequestUtil.GetMapToXML(return_data);
                }
                int order_type = (int) order.get("order_type");
                boolean payment_status = (boolean) order.get("payment_status");
                int supplier_id = (int) order.get("supplier_id");
                BigDecimal p = new BigDecimal("100");
                BigDecimal amount = (BigDecimal) order.get("amount");
                amount = amount.multiply(p);
                //如果订单已经支付返回错误
                if (payment_status) {
                    System.out.println("订单已经支付");
                    return_data.put("return_code", "SUCCESS");
                    return_data.put("return_msg", "OK");
                    return WXRequestUtil.GetMapToXML(return_data);
                }
                //如果支付金额不等于订单金额返回错误
                if (amount.compareTo(total_fee) != 0) {
                    System.out.println("资金异常");
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "金额异常");
                    return WXRequestUtil.GetMapToXML(return_data);
                }*/
                //更新订单信息
               /* if (orderdao.PaymentEndUpdateOrder(out_trade_no, time_end)) {
                    System.out.println("更新订单成功");
                    //如果该订单是幼儿产品  并且 存在代理
                    if (order_type == 2) {
                        if (supplier_id != 0) {
                           *//* Map su = userdao.getSupplierInfo(supplier_id);
                            String phone = (String) su.get("phone_number");
                            String nickname = (String) su.get("nickname");
                            String app_token = (String) su.get("app_token");
                            String content = "【三盛科创】尊敬的" + nickname + "您好。您在我们平台出售的商品有新用户下单。请您点击该链接查看发货信息。" + Config.WEB_SERVER + "/order/SupplierOrderInfo.do?order_number=" + out_trade_no + "&sid=" + app_token + ".请您务必妥善包管。";
                            MessageUtil.SendMessage(phone, content);*//*
                        }
                    } else {
                        *//*orderdao.UpdateOrderStatus(out_trade_no, 3);*//*//更新订单为已完成
                    }
                    return_data.put("return_code", "SUCCESS");
                    return_data.put("return_msg", "OK");
                    return WXRequestUtil.GetMapToXML(return_data);
                } else {
                    return_data.put("return_code", "FAIL");
                    return_data.put("return_msg", "更新订单失败");
                    return WXRequestUtil.GetMapToXML(return_data);
                }*/
            }
        } else {
            logger.error("签名不正确");
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "签名错误");
        }
        String xml = RequestUtil.GetMapToXML(return_data);
        return xml;
    }
}
