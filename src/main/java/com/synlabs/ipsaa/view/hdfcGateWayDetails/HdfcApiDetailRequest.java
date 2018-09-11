package com.synlabs.ipsaa.view.hdfcGateWayDetails;

import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.common.Response;

import static com.synlabs.ipsaa.service.BaseService.unmask;

public class HdfcApiDetailRequest implements Response {
    private String workingKey;
    private String accessCode;
    private String vsa;
    private String merchantName;
    private String hdfcTid;
    private Long centerId;

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

    public Long getCenterId() {
        return unmask(centerId);
    }
    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    // add center left
    public HdfcApiDetails toEntity() {
        HdfcApiDetails details=new HdfcApiDetails();
        details.setAccessCode(this.accessCode);
        details.setWorkingKey(this.workingKey);
        details.setMerchantName(this.merchantName);
        details.setHdfcTid(this.hdfcTid);
        details.setVsa(this.vsa);
        return details;
    }
}
