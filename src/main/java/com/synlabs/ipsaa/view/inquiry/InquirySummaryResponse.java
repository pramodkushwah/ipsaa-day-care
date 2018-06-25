package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.view.common.Response;

public class InquirySummaryResponse implements Response
{
  private Long   id;
  private String inquiryNumber;
  private String centerCode;
  private String programCode;
  private String groupName;
  private String childName;
  private String feeOffer;
  private String numbers;

  public InquirySummaryResponse(Inquiry inquiry)
  {
    this.id = mask(inquiry.getId());
    this.inquiryNumber = inquiry.getInquiryNumber();
    this.childName = inquiry.getChildName();
    this.centerCode = inquiry.getCenter().getCode();
    this.programCode = inquiry.getProgram().getCode();
    this.groupName = inquiry.getGroup().getName();
    this.feeOffer = inquiry.getFeeOffer();

    StringBuffer sb = new StringBuffer();
    sb.append(inquiry.getMotherMobile());
    sb.append(inquiry.getFatherMobile());
    sb.append(", ");
    sb.append(inquiry.getSecondaryNumbers());

    for (InquiryEventLog eventLog : inquiry.getLogs())
    {
      sb.append(eventLog.getCallBackNumber());
    }
    numbers = sb.toString();
  }

  public String getNumbers()
  {
    return numbers;
  }

  public String getChildName()
  {
    return childName;
  }

  public Long getId()
  {
    return id;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public String getFeeOffer()
  {
    return feeOffer;
  }
}
