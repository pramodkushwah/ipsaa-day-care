package com.synlabs.ipsaa.view.attendance;

import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;

public class AttendanceReportRequest implements Request
{
  private Long centerId;
  private String centerCode;
  private Date to;
  private Date from;

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public Long getMaskedCenterId()
  {
    return unmask(centerId);
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Date getTo()
  {
    return to;
  }

  public void setTo(Date to)
  {
    this.to = to;
  }

  public Date getFrom()
  {
    return from;
  }

  public void setFrom(Date from)
  {
    this.from = from;
  }
}
