package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by sushil on 20-03-2017.
 */
public class DashboardRequest extends PageRequest
{

  private String zone;
  private String center;
  private String city;
  private FeeDuration feeDuration;
  private Integer year;
  private Integer month;
  private Integer quarter;

  public DashboardRequest()
  {
  }

  public DashboardRequest(String zone, String center, String city)
  {
    this.zone = zone;
    this.center = center;
    this.city = city;
  }

  public FeeDuration getFeeDuration()
  {
    return feeDuration;
  }

  public void setFeeDuration(FeeDuration feeDuration)
  {
    this.feeDuration = feeDuration;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(Integer year)
  {
    this.year = year;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(Integer month)
  {
    this.month = month;
  }

  public Integer getQuarter()
  {
    return quarter;
  }

  public void setQuarter(Integer quarter)
  {
    this.quarter = quarter;
  }

  public String getZone()
  {
    return zone;
  }

  public void setZone(String zone)
  {
    this.zone = zone;
  }

  public String getCenter()
  {
    return center;
  }

  public void setCenter(String center)
  {
    this.center = center;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  @Override
  public String toString()
  {
    return "DashboardRequest{" +
        "zone='" + zone + '\'' +
        ", center='" + center + '\'' +
        ", city='" + city + '\'' +
        '}';
  }
}
