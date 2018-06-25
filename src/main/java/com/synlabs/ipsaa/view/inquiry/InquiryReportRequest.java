package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class InquiryReportRequest implements Request
{
  private Long centerId;
  private String centerCode;
  private Date from;
  private Date to;

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public Long getMaskedCenterId()
  {
    return centerId;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
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
    return to;
  }

  public void setTo(Date to)
  {
    this.to = to;
  }
}
