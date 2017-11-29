package com.buswe.module.cms.entity;

import com.buswe.core.domain.AuditableEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_category")

public class Category extends AuditableEntity{
    private String categoryName;
    private String categoryRemark;
    private String categoryWebsiteid;
    private Integer categoryPosition;
    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="category")
    private Set<Article> articles = new HashSet<Article>(0);

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryRemark() {
        return categoryRemark;
    }

    public void setCategoryRemark(String categoryRemark) {
        this.categoryRemark = categoryRemark;
    }

    public String getCategoryWebsiteid() {
        return categoryWebsiteid;
    }

    public void setCategoryWebsiteid(String categoryWebsiteid) {
        this.categoryWebsiteid = categoryWebsiteid;
    }

    public Integer getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(Integer categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
