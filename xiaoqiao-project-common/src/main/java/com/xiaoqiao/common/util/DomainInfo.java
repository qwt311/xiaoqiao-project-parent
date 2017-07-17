package com.xiaoqiao.common.util;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by qiaowentao on 2017/7/14.
 */
public class DomainInfo implements Serializable {
    private static final long serialVersionUID = 7523967970034938905L;

    /**
     * 域名
     */
    private String domainName;

    /**
     * 注册商
     */
    private String sponsoringRegistrar;

    /**
     * 联系人
     */
    private String registrant;

    /**
     * 联系电话
     */
    private String registrantTel;

    /**
     * 联系邮箱
     */
    private String registrantContactEmail;

    /**
     * 创建时间
     */
    private Date creationDate;

    /**
     * 更新时间
     */
    private Date updatedDate;

    /**
     * 到期时间
     */
    private Date expirationDate;

    /**
     * 域名服务器
     */
    private String whoisServer;

    /**
     * DNS
     */
    private String nameServer;

    /**
     * 状态
     */
    private String status;

    public DomainInfo() {
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getSponsoringRegistrar() {
        return sponsoringRegistrar;
    }

    public void setSponsoringRegistrar(String sponsoringRegistrar) {
        this.sponsoringRegistrar = sponsoringRegistrar;
    }

    public String getRegistrant() {
        return registrant;
    }

    public void setRegistrant(String registrant) {
        this.registrant = registrant;
    }

    public String getRegistrantTel() {
        return registrantTel;
    }

    public void setRegistrantTel(String registrantTel) {
        this.registrantTel = registrantTel;
    }

    public String getRegistrantContactEmail() {
        return registrantContactEmail;
    }

    public void setRegistrantContactEmail(String registrantContactEmail) {
        this.registrantContactEmail = registrantContactEmail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getWhoisServer() {
        return whoisServer;
    }

    public void setWhoisServer(String whoisServer) {
        this.whoisServer = whoisServer;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DomainInfo{" +
                "domainName='" + domainName + '\'' +
                ", sponsoringRegistrar='" + sponsoringRegistrar + '\'' +
                ", registrant='" + registrant + '\'' +
                ", registrantTel='" + registrantTel + '\'' +
                ", registrantContactEmail='" + registrantContactEmail + '\'' +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                ", expirationDate=" + expirationDate +
                ", whoisServer=" + whoisServer +
                ", nameServer='" + nameServer + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
