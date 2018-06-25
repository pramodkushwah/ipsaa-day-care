package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.common.Request;

public class FeeReportRequest implements Request
{
  private Long centerId;
  private String centerCode;
  private Integer month;
  private Integer year;

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

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(Integer month)
  {
    this.month = month;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(Integer year)
  {
    this.year = year;
  }
}
