package com.buswe.module.cms.entity;

import com.buswe.core.domain.AuditableEntity;
import com.buswe.module.cms.search.ArticleTextBridge;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "cms_article")

@Indexed //hibernate search
@Analyzer(impl=HanLPIndexAnalyzer.class)
public class Article extends AuditableEntity {
    /**
     * 标题
     */
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.YES)
    private String articleTitle;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="article_categoryid", nullable=false)
    private Category category;

    @Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
    private String websiteid;

    @Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
    private Boolean articlePrivate;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="article")
    //缓存子对象
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private Set<Lable> lables = new HashSet<Lable>(0);

    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="article")
    private Set<ArticleComment> articleComments = new HashSet<ArticleComment>(0);


    @FieldBridge(impl=ArticleTextBridge.class)
    //   @Field(index=Index.YES, analyze=Analyze.YES, store=Store.YES)
    @IndexedEmbedded
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id", nullable=false)
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    private ArticleText articleText;


    /**
     * 标题图
     */
    private String headpic;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 永久链接
     */
    private String permalink;
    /**
     * 点击量
     */
    private Integer hits = 0;
    /**
     * 文章来源
     */
    private String source;
    /**
     * 权重
     */
    private String weight;
    /**
     * 置顶
     */
    private Boolean top;

    /**
     * 允许评论
     */
    private Boolean allowComment;
    private Short articleStatus;
    private Integer articleViewcount;


    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getWebsiteid() {
        return websiteid;
    }

    public void setWebsiteid(String websiteid) {
        this.websiteid = websiteid;
    }

    public Boolean getArticlePrivate() {
        return articlePrivate;
    }

    public void setArticlePrivate(Boolean articlePrivate) {
        this.articlePrivate = articlePrivate;
    }

    public Set<Lable> getLables() {
        return lables;
    }

    public void setLables(Set<Lable> lables) {
        this.lables = lables;
    }

    public ArticleText getArticleText() {
        return articleText;
    }

    public void setArticleText(ArticleText articleText) {
        this.articleText = articleText;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public Short getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(Short articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Integer getArticleViewcount() {
        return articleViewcount;
    }

    public void setArticleViewcount(Integer articleViewcount) {
        this.articleViewcount = articleViewcount;
    }

    public Set<ArticleComment> getArticleComments() {
        return articleComments;
    }

    public void setArticleComments(Set<ArticleComment> articleComments) {
        this.articleComments = articleComments;
    }
}
