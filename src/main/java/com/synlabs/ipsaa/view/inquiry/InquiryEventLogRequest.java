package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.view.common.Request;
import org.springframework.util.StringUtils;

import java.text.ParseException;

public class InquiryEventLogRequest implements Request
{
  private Long            id;
  private Long            inquiryId;
  private CallDisposition callDisposition;
  private String          callBack;
  private String          comment;
  private String          callBackNumber;

  public InquiryEventLog toEntity(InquiryEventLog log)
  {
    log = log == null ? new InquiryEventLog() : log;
    log.setCallDisposition(callDisposition);
    try
    {
      switch (callDisposition)
      {
        case NewInquiry:
        case Followup:
        case Callback:
        case ParentMessage:
        case Revisit:
          log.setCallBack(parseDate(callBack, Request.dateTimeFormat));
          break;
      }
    }
    catch (ParseException e)
    {
      throw new ValidationException("Invalid Callback Date.");
    }
    log.setComment(comment);
    log.setCallBackNumber(callBackNumber);
    return log;
  }

  public CallDisposition getCallDisposition()
  {
    return callDisposition;
  }

  public void setCallDisposition(CallDisposition callDisposition)
  {
    this.callDisposition = callDisposition;
  }

  public String getCallBackNumber()
  {
    return callBackNumber;
  }

  public void setCallBackNumber(String callBackNumber)
  {
    this.callBackNumber = callBackNumber;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getInquiryId()
  {
    return inquiryId;
  }

  public void setInquiryId(Long inquiryId)
  {
    this.inquiryId = inquiryId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public String getCallBack()
  {
    return callBack;
  }

  public void setCallBack(String callBack)
  {
    this.callBack = callBack;
  }

  public boolean isEmpty()
  {
    return callDisposition == null
        && StringUtils.isEmpty(callBack)
        && StringUtils.isEmpty(callBackNumber)
        && StringUtils.isEmpty(comment)
        ;
  }
}
