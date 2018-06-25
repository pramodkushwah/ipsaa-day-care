package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.view.common.Response;

public class CityResponse
    implements Response
{
  private Long id;
  private String name;
  private String zone;

  public CityResponse(City city)
  {
    this.name = city.getName();
    this.zone = (city.getZone() == null ? null : city.getZone().getName());
    this.id = mask((Long)city.getId());
  }

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setZone(String zone)
  {
    this.zone = zone;
  }

  public String getName()
  {
    return this.name;
  }

  public String getZone()
  {
    return this.zone;
  }

}
