package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by sushil on 27-03-2017.
 */
public class CenterListRequest implements Request
{
  private String zone;
  private String city;

  public CenterListRequest()
  {
  }

  public CenterListRequest(String zone)
  {
    this.zone = zone;
  }

  public CenterListRequest(String zone, String city)
  {
    this.zone = zone;
    this.city = city;
  }

  public String getZone()
  {
    return zone;
  }

  public void setZone(String zone)
  {
    this.zone = zone;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }
}
