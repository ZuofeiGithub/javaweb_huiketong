package com.huiketong.cofpasgers.util.weixin;

import java.util.List;

public class ImageTextMsg extends BaseMessage {
    int ArticleCount;
    List<ImageTextBean> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<ImageTextBean> getArticles() {
        return Articles;
    }

    public void setArticles(List<ImageTextBean> articles) {
        Articles = articles;
    }
}
