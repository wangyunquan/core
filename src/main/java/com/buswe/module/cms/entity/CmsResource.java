package com.buswe.module.cms.entity;

import com.buswe.core.domain.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cms_resouce")
public class CmsResource extends IdEntity {

    @Column(name="res_name", length=200)
    private String resName;
    @Column(name="res_type", length=200)
    private String resType;
    @Column(name="res_key", length=50)
    private String resKey;
    @Column(name="res_hash", length=50)
    private String resHash;
    @Column(name="res_websiteid", length=32)
    private String resWebsiteid;
    @Column(name="res_date", length=19)
    private Date resDate;
    @Column(name="res_creator", length=32)
    private String resCreator;
    @Column(name="res_size")
    private Long resSize;

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public String getResHash() {
        return resHash;
    }

    public void setResHash(String resHash) {
        this.resHash = resHash;
    }

    public String getResWebsiteid() {
        return resWebsiteid;
    }

    public void setResWebsiteid(String resWebsiteid) {
        this.resWebsiteid = resWebsiteid;
    }

    public Date getResDate() {
        return resDate;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public String getResCreator() {
        return resCreator;
    }

    public void setResCreator(String resCreator) {
        this.resCreator = resCreator;
    }

    public Long getResSize() {
        return resSize;
    }

    public void setResSize(Long resSize) {
        this.resSize = resSize;
    }
}
