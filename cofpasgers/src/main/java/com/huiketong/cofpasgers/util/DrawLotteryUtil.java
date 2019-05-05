package com.huiketong.cofpasgers.util;

import com.huiketong.cofpasgers.entity.Gift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: 左飞
 * @Date: 2019/1/18 16:19
 * @Version 1.0
 */
public class DrawLotteryUtil {
    public static int drawGift(List<Gift> giftList) {

        if (null != giftList && giftList.size() > 0) {
            List<Double> orgProbList = new ArrayList<Double>(giftList.size());
            for (Gift gift : giftList) {
                //按顺序将概率添加到集合中
                orgProbList.add(gift.getProb());
            }

            return draw(orgProbList);

        }
        return -1;
    }

    public static int draw(List<Double> giftProbList) {

        List<Double> sortRateList = new ArrayList<Double>();

        // 计算概率总和
        Double sumRate = 0D;
        for (Double prob : giftProbList) {
            sumRate += prob;
        }

        if (sumRate != 0) {
            double rate = 0D;   //概率所占比例
            for (Double prob : giftProbList) {
                rate += prob;
                // 构建一个比例区段组成的集合(避免概率和不为1)
                sortRateList.add(rate / sumRate);
            }

            // 随机生成一个随机数，并排序
            double random = Math.random();
            sortRateList.add(random);
            Collections.sort(sortRateList);

            // 返回该随机数在比例集合中的索引
            return sortRateList.indexOf(random);
        }


        return -1;
    }

    public static void main(String[] args) {
        Gift iphone = new Gift();
        iphone.setId(101);
        iphone.setPoint(10);
        iphone.setProb(0.1D);

        Gift thanks = new Gift();
        thanks.setId(102);
        thanks.setPoint(30);
        thanks.setProb(0.5D);

        Gift vip = new Gift();
        vip.setId(103);
        vip.setPoint(100);
        vip.setProb(0.4D);

        List<Gift> list = new ArrayList<Gift>();
        list.add(vip);
        list.add(thanks);
        list.add(iphone);

        for (int i = 0; i < 100; i++) {
            int index = drawGift(list);
            System.out.println(list.get(index).getPoint());
        }
    }
}
