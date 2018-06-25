package com.synlabs.ipsaa.view.inquiry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class InquiryEventLogResponse implements Response
{
  private Long            id;
  private CallDisposition callDisposition;
  private Long            inquiryId;
  private String          inquiryNumber;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm")
  private Date            callBack;
  private String          callBackNumber;
  private String          comment;
  private Date            date;

  public InquiryEventLogResponse(InquiryEventLog log){
    this.id=mask(log.getId());
    this.inquiryId=mask(log.getInquiry().getId());
    this.inquiryNumber=log.getInquiry().getInquiryNumber();
    this.callDisposition=log.getCallDisposition();
    this.callBack=log.getCallBack();
    this.callBackNumber=log.getCallBackNumber();
    this.comment=log.getComment();
    this.date= log.getCreatedDate().toDate();
  }

  public Date getDate()
  {
    return date;
  }

  public String getCallBackNumber()
  {
    return callBackNumber;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public CallDisposition getCallDisposition()
  {
    return callDisposition;
  }

  public Long getId()
  {
    return id;
  }

  public Long getInquiryId()
  {
    return inquiryId;
  }

  public Date getCallBack()
  {
    return callBack;
  }

  public String getComment()
  {
    return comment;
  }
}
