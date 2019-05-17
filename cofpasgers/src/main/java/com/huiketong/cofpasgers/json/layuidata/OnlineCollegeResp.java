package com.huiketong.cofpasgers.json.layuidata;

import java.util.Date;
import java.util.List;

public class OnlineCollegeResp {
    
    private int code;
    private String msg;
    private String count;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private Integer id;
        private String title;
        private Integer type;
        private Date createtime;
        private String particulars;
        private String videoIntro;
        private String videoUrl;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Date getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Date createtime) {
            this.createtime = createtime;
        }

        public String getParticulars() {
            return particulars;
        }

        public void setParticulars(String particulars) {
            this.particulars = particulars;
        }

        public String getVideoIntro() {
            return videoIntro;
        }

        public void setVideoIntro(String videoIntro) {
            this.videoIntro = videoIntro;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }
}
