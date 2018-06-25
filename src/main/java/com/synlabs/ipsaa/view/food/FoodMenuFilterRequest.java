package com.synlabs.ipsaa.view.food;

import com.synlabs.ipsaa.view.common.PageRequest;

/**
 * Created by ttn on 2/7/17.
 */
public class FoodMenuFilterRequest extends PageRequest
{
  private String centerCode;

  private Long zoneId;

  private Long year;

  private Long month;

  public FoodMenuFilterRequest()
  {

  }

  public FoodMenuFilterRequest(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public Long getZoneId()
  {
    return zoneId;
  }

  public Long getUnmaskedZoneId(){
    return unmask(zoneId);
  }

  public void setZoneId(Long zoneId)
  {
    this.zoneId = zoneId;
  }

  public Long getYear()
  {
    return year;
  }

  public void setYear(Long year)
  {
    this.year = year;
  }

  public Long getMonth()
  {
    return month;
  }

  public void setMonth(Long month)
  {
    this.month = month;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }
}
