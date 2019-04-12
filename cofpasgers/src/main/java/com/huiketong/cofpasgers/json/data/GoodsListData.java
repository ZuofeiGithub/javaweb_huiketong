package com.huiketong.cofpasgers.json.data;

import java.util.List;

public class GoodsListData {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 【4月爆款】19999搞定全屋低至909m²!
         * subtitle : 限时赠送2999元豪华床垫、配件
         * label : {"label1":"热门","label2":"好评","label3":"新品","label4":"推荐"}
         * image : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555066086160&di=01a6613b1901ae70ef5a6219b9c8058e&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F5243fbf2b21193134597af896e380cd791238d37.jpg
         * linkname : 推荐
         */

        private String title;
        private String subtitle;
        private LabelBean label;
        private String image;
        private String linkname;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public LabelBean getLabel() {
            return label;
        }

        public void setLabel(LabelBean label) {
            this.label = label;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLinkname() {
            return linkname;
        }

        public void setLinkname(String linkname) {
            this.linkname = linkname;
        }

        public static class LabelBean {
            /**
             * label1 : 热门
             * label2 : 好评
             * label3 : 新品
             * label4 : 推荐
             */

            private String label1;
            private String label2;
            private String label3;
            private String label4;

            public String getLabel1() {
                return label1;
            }

            public void setLabel1(String label1) {
                this.label1 = label1;
            }

            public String getLabel2() {
                return label2;
            }

            public void setLabel2(String label2) {
                this.label2 = label2;
            }

            public String getLabel3() {
                return label3;
            }

            public void setLabel3(String label3) {
                this.label3 = label3;
            }

            public String getLabel4() {
                return label4;
            }

            public void setLabel4(String label4) {
                this.label4 = label4;
            }
        }
    }
}
