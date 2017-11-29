package com.buswe.module.cms.entity;

import com.buswe.core.domain.IdEntity;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_article_text")

@Indexed //hibernate search
@Analyzer(impl=HanLPIndexAnalyzer.class)
public class ArticleText extends IdEntity {

    private String articleSummary;
    @Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
    private String articleContent;

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }


}
