package com.buswe.module.cms.entity;

import com.buswe.core.domain.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_article_comment_support")
public class ArticleCommentSupport extends IdEntity {
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="comment_id", nullable=false)
    private ArticleComment articleComment;
    private String userid;
    private Date createtime;

    public ArticleComment getArticleComment() {
        return articleComment;
    }

    public void setArticleComment(ArticleComment articleComment) {
        this.articleComment = articleComment;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
