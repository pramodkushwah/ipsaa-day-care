package com.synlabs.ipsaa.view.center;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.Holiday;
import com.synlabs.ipsaa.util.CollectionUtils;
import com.synlabs.ipsaa.view.common.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ttn on 10/6/17.
 */
public class HolidayResponse implements Response
{
  private Long id;

  @JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
  private Date holidayDate;

  private boolean floating;

  private boolean optional;

  private String name;

  List<CenterSummaryResponse> centers= new ArrayList<>();

  public HolidayResponse(Holiday holiday)
  {
    this.id = mask(holiday.getId());
    this.holidayDate = holiday.getHolidayDate();
    this.name = holiday.getName();
    this.floating = holiday.isFloating();
    this.optional = holiday.isOptional();
    this.centers = holiday.getCenters().stream().map(CenterSummaryResponse::new).collect(Collectors.toList());
  }

  public List<CenterSummaryResponse> getCenters()
  {
    return centers;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = mask(id);
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
