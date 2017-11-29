package com.buswe.module.cms.entity;

import com.buswe.core.domain.AuditableEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangy on 2017/6/26.
 */
@Entity
@Table(name = "cms_website")

public class WebSite extends AuditableEntity{
    private String websiteName;
    private String websiteTitle;
    private String websiteTagline;
    private String websiteKeyword;
    private String websiteAbout;
    private String websiteAnalyticscode;
    private Date websiteDatetime;
    private Boolean websiteAllowcomments;
    private String websiteSkin;
    //
    private String websiteEditor;
    private String websiteNotice;
    private String websiteFooter;
    private String websiteCover;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "webSite")
    private Set<Lable> lables = new HashSet<Lable>(0);

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public String getWebsiteTagline() {
        return websiteTagline;
    }

    public void setWebsiteTagline(String websiteTagline) {
        this.websiteTagline = websiteTagline;
    }

    public String getWebsiteKeyword() {
        return websiteKeyword;
    }

    public void setWebsiteKeyword(String websiteKeyword) {
        this.websiteKeyword = websiteKeyword;
    }

    public String getWebsiteAbout() {
        return websiteAbout;
    }

    public void setWebsiteAbout(String websiteAbout) {
        this.websiteAbout = websiteAbout;
    }

    public String getWebsiteAnalyticscode() {
        return websiteAnalyticscode;
    }

    public void setWebsiteAnalyticscode(String websiteAnalyticscode) {
        this.websiteAnalyticscode = websiteAnalyticscode;
    }

    public Date getWebsiteDatetime() {
        return websiteDatetime;
    }

    public void setWebsiteDatetime(Date websiteDatetime) {
        this.websiteDatetime = websiteDatetime;
    }

    public Boolean getWebsiteAllowcomments() {
        return websiteAllowcomments;
    }

    public void setWebsiteAllowcomments(Boolean websiteAllowcomments) {
        this.websiteAllowcomments = websiteAllowcomments;
    }

    public String getWebsiteSkin() {
        return websiteSkin;
    }

    public void setWebsiteSkin(String websiteSkin) {
        this.websiteSkin = websiteSkin;
    }

    public String getWebsiteEditor() {
        return websiteEditor;
    }

    public void setWebsiteEditor(String websiteEditor) {
        this.websiteEditor = websiteEditor;
    }

    public String getWebsiteNotice() {
        return websiteNotice;
    }

    public void setWebsiteNotice(String websiteNotice) {
        this.websiteNotice = websiteNotice;
    }

    public String getWebsiteFooter() {
        return websiteFooter;
    }

    public void setWebsiteFooter(String websiteFooter) {
        this.websiteFooter = websiteFooter;
    }

    public String getWebsiteCover() {
        return websiteCover;
    }

    public void setWebsiteCover(String websiteCover) {
        this.websiteCover = websiteCover;
    }

    public Set<Lable> getLables() {
        return lables;
    }

    public void setLables(Set<Lable> lables) {
        this.lables = lables;
    }
}
