package com.synlabs.ipsaa.entity.inquiry;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.InquiryStatus;

import javax.persistence.Column;
import javax.persistence.Enumerated;

public class FacebookInquiry extends BaseEntity {

    @Column(length = 25)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String center;

    @Column(length = 150)
    private String messages;

    @Enumerated
    private InquiryStatus status;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCenter() { return center; }

    public void setCenter(String center) { this.center = center; }

    public String getMessages() { return messages; }

    public void setMessages(String messages) { this.messages = messages; }

    public InquiryStatus getStatus() { return status; }

    public void setStatus(InquiryStatus status) { this.status = status; }
}
