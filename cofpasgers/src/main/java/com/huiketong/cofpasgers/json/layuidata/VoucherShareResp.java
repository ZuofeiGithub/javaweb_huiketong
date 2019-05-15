package com.huiketong.cofpasgers.json.layuidata;

import java.util.List;

public class VoucherShareResp {

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
        private String context;
        private String image;
        private String link_url;
        private Integer sharetype;
        private Integer goods_id;

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

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }

        public Integer getSharetype() {
            return sharetype;
        }

        public void setSharetype(Integer sharetype) {
            this.sharetype = sharetype;
        }

        public Integer getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(Integer goods_id) {
            this.goods_id = goods_id;
        }
    }
}
