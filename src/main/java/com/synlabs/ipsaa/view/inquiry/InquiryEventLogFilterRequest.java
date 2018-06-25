package com.synlabs.ipsaa.view.inquiry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.view.common.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InquiryEventLogFilterRequest implements Request
{
  private Long   inquiryId;
  private String inquiryNumber;
  private Long   logId;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date   date;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date   from;
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date   To;
  private List<CallDisposition> dispositions = new ArrayList<>();
  private List<String> centerCodes=new ArrayList<>();

  public Long getMaskedLogId()
  {
    return logId;
  }

  public List<String> getCenterCodes()
  {
    return centerCodes;
  }

  public void setCenterCodes(List<String> centerCodes)
  {
    this.centerCodes = centerCodes;
  }

  public Long getMaskedInquiryId()
  {
    return inquiryId;
  }

  public Long getLogId()
  {
    return unmask(logId);
  }

  public Long getInquiryId()
  {
    return unmask(inquiryId);
  }

  public List<CallDisposition> getDispositions()
  {
    return dispositions;
  }

  public void setDispositions(List<CallDisposition> dispositions)
  {
    this.dispositions = dispositions;
  }

  public void setInquiryId(Long inquiryId)
  {
    this.inquiryId = inquiryId;
  }

  public void setLogId(Long logId)
  {
    this.logId = logId;
  }

  public String getInquiryNumber()
  {
    return inquiryNumber;
  }

  public void setInquiryNumber(String inquiryNumber)
  {
    this.inquiryNumber = inquiryNumber;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public Date getFrom()
  {
    return from;
  }

  public void setFrom(Date from)
  {
    this.from = from;
  }

  public Date getTo()
  {
    return To;
  }

  public void setTo(Date to)
  {
    To = to;
  }
}
