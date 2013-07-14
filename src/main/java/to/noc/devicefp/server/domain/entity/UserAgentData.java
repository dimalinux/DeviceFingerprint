/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.domain.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name="user_agent")
public class UserAgentData extends DeviceFkAsPk implements Serializable {

    public static final int  MAX_DEVICE_LEN = 120;

    @Size(max = 120)
    @Column(length = 120)
    private String type;

    @Size(max = 120)
    @Column(length = 120)
    private String uaFamily;

    @Size(max = 120)
    @Column(length = 120)
    private String uaName;

    @Size(max = 120)
    @Column(length = 120)
    private String uaVersion;

    @Size(max = 120)
    @Column(length = 120)
    private String uaUrl;

    @Size(max = 120)
    @Column(length = 120)
    private String uaCompany;

    @Size(max = 120)
    @Column(length = 120)
    private String uaCompanyUrl;

    @Size(max = 120)
    @Column(length = 120)
    private String uaIcon;

    @Size(max = 120)
    @Column(length = 120)
    private String uaInfoUrl;

    @Size(max = MAX_DEVICE_LEN)
    @Column(length = MAX_DEVICE_LEN)
    private String uaDevice;

    @Size(max = 120)
    @Column(length = 120)
    private String osFamily;

    @Size(max = 120)
    @Column(length = 120)
    private String osName;

    @Size(max = 120)
    @Column(length = 120)
    private String osVersion;

    @Size(max = 120)
    @Column(length = 120)
    private String osUrl;

    @Size(max = 120)
    @Column(length = 120)
    private String osCompany;

    @Size(max = 120)
    @Column(length = 120)
    private String osCompanyUrl;

    @Size(max = 120)
    @Column(length = 120)
    private String osIcon;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUaFamily() {
        return uaFamily;
    }

    public void setUaFamily(String uaFamily) {
        this.uaFamily = uaFamily;
    }

    public String getUaName() {
        return uaName;
    }

    public void setUaName(String uaName) {
        this.uaName = uaName;
    }

    public String getUaVersion() {
        return uaVersion;
    }

    public void setUaVersion(String uaVersion) {
        this.uaVersion = uaVersion;
    }

    public String getUaUrl() {
        return uaUrl;
    }

    public void setUaUrl(String uaUrl) {
        this.uaUrl = uaUrl;
    }

    public String getUaCompany() {
        return uaCompany;
    }

    public void setUaCompany(String uaCompany) {
        this.uaCompany = uaCompany;
    }

    public String getUaCompanyUrl() {
        return uaCompanyUrl;
    }

    public void setUaCompanyUrl(String uaCompanyUrl) {
        this.uaCompanyUrl = uaCompanyUrl;
    }

    public String getUaIcon() {
        return uaIcon;
    }

    public void setUaIcon(String uaIcon) {
        this.uaIcon = uaIcon;
    }

    public String getUaInfoUrl() {
        return uaInfoUrl;
    }

    public void setUaInfoUrl(String uaInfoUrl) {
        this.uaInfoUrl = uaInfoUrl;
    }

    public String getUaDevice() {
        return uaDevice;
    }

    public void setUaDevice(String uaDevice) {
        this.uaDevice = uaDevice;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsUrl() {
        return osUrl;
    }

    public void setOsUrl(String osUrl) {
        this.osUrl = osUrl;
    }

    public String getOsCompany() {
        return osCompany;
    }

    public void setOsCompany(String osCompany) {
        this.osCompany = osCompany;
    }

    public String getOsCompanyUrl() {
        return osCompanyUrl;
    }

    public void setOsCompanyUrl(String osCompanyUrl) {
        this.osCompanyUrl = osCompanyUrl;
    }

    public String getOsIcon() {
        return osIcon;
    }

    public void setOsIcon(String osIcon) {
        this.osIcon = osIcon;
    }

}
