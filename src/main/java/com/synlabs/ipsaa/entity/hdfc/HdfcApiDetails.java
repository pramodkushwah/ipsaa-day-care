package com.synlabs.ipsaa.entity.hdfc;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

// create by shubham nxtlife
// 11/9/2018
@Entity
public class HdfcApiDetails  extends BaseEntity {


    private Long id;
    @Column(length = 200)
 private String workingKey;

    @Column(length = 200)
 private String accessCode;

    @Column(length = 200)
 private String vsa;

    @Column(length = 200)
 private String merchantName;

    @Column(length = 200)
 private String hdfcTid;

    @OneToOne(optional = false)
    private Center center;

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public String getWorkingKey() {
        return workingKey;
    }

    public void setWorkingKey(String workingKey) {
        this.workingKey = workingKey;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getVsa() {
        return vsa;
    }

    public void setVsa(String vsa) {
        this.vsa = vsa;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getHdfcTid() {
        return hdfcTid;
    }

    public void setHdfcTid(String hdfcTid) {
        this.hdfcTid = hdfcTid;
    }

    @Override
    public String toString() {
        return "HdfcApiDetails{" +
                "id=" + id +
                ", workingKey='" + workingKey + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", vsa='" + vsa + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", hdfcTid='" + hdfcTid + '\'' +
                ", center=" + center +
                '}';
    }
}
