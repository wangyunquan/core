package com.buswe.module.cms.entity;

import com.buswe.core.domain.AuditableEntity;

import javax.persistence.*;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_lable")

public class Lable extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "websiteid", nullable = false)
    private WebSite webSite;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleid", nullable = false)
    private Article article;
    private String lableName;
    private String lableCreator;

    public WebSite getWebSite() {
        return webSite;
    }

    public void setWebSite(WebSite webSite) {
        this.webSite = webSite;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public String getLableCreator() {
        return lableCreator;
    }

    public void setLableCreator(String lableCreator) {
        this.lableCreator = lableCreator;
    }
}
