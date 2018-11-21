package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.view.common.Request;

public class IpsaaClubSlipRequest implements Request {
    private Long   id;
    private String centerCode;
    private String      receiptSerial;

    public String getReceiptSerial() {
        return receiptSerial;
    }

    public void setReceiptSerial(String receiptSerial) {
        this.receiptSerial = receiptSerial;
    }

    public Long getId() {
        return unmask(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
}
