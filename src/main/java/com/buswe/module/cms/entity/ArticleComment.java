package com.buswe.module.cms.entity;

import com.buswe.core.domain.IdEntity;
import com.buswe.module.core.entity.Userinfo;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_article_comment")
public class ArticleComment extends IdEntity {
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="article_id", nullable=false)
    private Article article;
    private String CText;
    private Date CDatetime;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="c_user_id", nullable=false)
    private Userinfo CUser;
    private String CWebsiteId;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_tosomeone")
    private Userinfo CTosomeone;
    private String CIsdel;
    private Integer CSupport;
    private String CRoot;
    private Integer CReplyTotal;
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="articleComment")
    private Set<ArticleCommentSupport> supports = new HashSet<ArticleCommentSupport>(0);

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getCText() {
        return CText;
    }

    public void setCText(String CText) {
        this.CText = CText;
    }

    public Date getCDatetime() {
        return CDatetime;
    }

    public void setCDatetime(Date CDatetime) {
        this.CDatetime = CDatetime;
    }

    public Userinfo getCUser() {
        return CUser;
    }

    public void setCUser(Userinfo CUser) {
        this.CUser = CUser;
    }

    public String getCWebsiteId() {
        return CWebsiteId;
    }

    public void setCWebsiteId(String CWebsiteId) {
        this.CWebsiteId = CWebsiteId;
    }

    public Userinfo getCTosomeone() {
        return CTosomeone;
    }

    public void setCTosomeone(Userinfo CTosomeone) {
        this.CTosomeone = CTosomeone;
    }

    public String getCIsdel() {
        return CIsdel;
    }

    public void setCIsdel(String CIsdel) {
        this.CIsdel = CIsdel;
    }

    public Integer getCSupport() {
        return CSupport;
    }

    public void setCSupport(Integer CSupport) {
        this.CSupport = CSupport;
    }

    public String getCRoot() {
        return CRoot;
    }

    public void setCRoot(String CRoot) {
        this.CRoot = CRoot;
    }

    public Integer getCReplyTotal() {
        return CReplyTotal;
    }

    public void setCReplyTotal(Integer CReplyTotal) {
        this.CReplyTotal = CReplyTotal;
    }

    public Set<ArticleCommentSupport> getSupports() {
        return supports;
    }

    public void setSupports(Set<ArticleCommentSupport> supports) {
        this.supports = supports;
    }
}
