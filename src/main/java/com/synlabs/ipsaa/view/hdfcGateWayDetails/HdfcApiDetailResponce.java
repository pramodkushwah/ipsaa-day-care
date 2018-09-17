package com.synlabs.ipsaa.view.hdfcGateWayDetails;

import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.common.Response;

public class HdfcApiDetailResponce implements Response {
    private Long id;
    private String workingKey;
    private String accessCode;
    private String vsa;
    private String merchantName;
    private String hdfcTid;
    private CenterResponse center;

    public HdfcApiDetailResponce(HdfcApiDetails details) {
        this.id = details.getId();
        this.workingKey = details.getWorkingKey();
        this.accessCode = details.getAccessCode();
        this.vsa = details.getVsa();
        this.merchantName = details.getMerchantName();
        this.hdfcTid = details.getHdfcTid();
        this.center = new CenterResponse(details.getCenter());
    }
    public Long getId() {
        return mask(id);
    }
    public void setId(Long id) {
        this.id = id;
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

    public CenterResponse getCenter() {
        return center;
    }

    public void setCenter(CenterResponse center) {
        this.center = center;
    }
}
