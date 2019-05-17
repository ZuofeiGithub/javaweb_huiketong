package com.huiketong.cofpasgers.json.layuidata;

import java.util.Date;
import java.util.List;

/**
 * 推广活动返回数据集合
 */
public class PromotActivityResp {
    private int code;
    private String msg;
    private String count;
    private List<PromotActivityResp.DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PromotActivityResp.DataBean> getData() {
        return data;
    }

    public void setData(List<PromotActivityResp.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        Integer id;
        Integer activityType;
        String activityTitle;
        Date beginTime;
        Date endTime;
        Date createTime;
        Integer activityStatus;
        String actAlias;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getActivityType() {
            return activityType;
        }

        public void setActivityType(Integer activityType) {
            this.activityType = activityType;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public Date getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Date beginTime) {
            this.beginTime = beginTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Integer getActivityStatus() {
            return activityStatus;
        }

        public void setActivityStatus(Integer activityStatus) {
            this.activityStatus = activityStatus;
        }
        public String getActAlias() {
            return actAlias;
        }

        public void setActAlias(String actAlias) {
            this.actAlias = actAlias;
        }
    }
}
