package com.synlabs.ipsaa.view.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.view.common.Request;

import java.util.Date;
import java.util.List;

/**
 * Created by ttn on 10/6/17.
 */
public class HolidayRequest implements Request
{
  private Long id;

  private List<Long> centers;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date holidayDate;

  private boolean floating;

  private boolean optional;

  private String name;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public List<Long> getCenters()
  {
    return centers;
  }

  public void setCenters(List<Long> centers)
  {
    this.centers = centers;
  }

  public Date getHolidayDate()
  {
    return holidayDate;
  }

  public void setHolidayDate(Date holidayDate)
  {
    this.holidayDate = holidayDate;
  }

  public boolean isFloating()
  {
    return floating;
  }

  public void setFloating(boolean floating)
  {
    this.floating = floating;
  }

  public boolean isOptional()
  {
    return optional;
  }

  public void setOptional(boolean optional)
  {
    this.optional = optional;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
