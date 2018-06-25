package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.view.common.Request;

public class HolidayFilterRequest implements Request
{
  private Long centerId;

  private Integer year;
  private Integer month;

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
