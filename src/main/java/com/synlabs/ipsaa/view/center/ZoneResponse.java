package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Zone;
import com.synlabs.ipsaa.view.center.CityResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneResponse implements Response
{

  private Long               id;
  private String             name;
  private List<CityResponse> cities;
  public ZoneResponse(Zone zone)
  {
    this.name = zone.getName();
    this.id=mask(zone.getId());
    if (zone.getCities() != null && !zone.getCities().isEmpty()) {
      cities = zone.getCities().stream().map(CityResponse::new).collect(Collectors.toList());
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setCities(List<CityResponse> cities)
  {
    this.cities = cities;
  }

  public String getName()
  {
    return name;
  }

  public List<CityResponse> getCities()
  {
    return cities;
  }
}
