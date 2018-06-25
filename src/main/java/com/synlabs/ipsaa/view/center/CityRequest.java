package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by sushil on 27-03-2017.
 */
public class CityRequest implements Request
{
  private Long id;
  private String zone;
  private String name;

  public CityRequest() {}

  public CityRequest(String zone)
  {
    this.zone = zone;
  }

  public Long getId()
  {
    return unmask(this.id);
  }

  public Long getMaskedId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getZone()
  {
    return this.zone;
  }

  public void setZone(String zone)
  {
    this.zone = zone;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public City toEntity()
  {
    City city = new City();
    city.setName(this.name);
    return city;
  }

  public String toString()
  {
    return "CityRequest{id=" + this.id + ", zone='" + this.zone + '\'' + ", name='" + this.name + '\'' + '}';
  }
}
