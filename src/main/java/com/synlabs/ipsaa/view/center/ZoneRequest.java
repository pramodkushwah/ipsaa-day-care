package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Zone;
import com.synlabs.ipsaa.view.common.Request;

public class ZoneRequest
    implements Request
{
  private Long id;
  private String name;
  public ZoneRequest(){}
  public ZoneRequest(Long id){
    this.id=id;
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

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Zone toEntity()
  {
    Zone zone = new Zone();
    zone.setName(this.name);
    return zone;
  }

  public String toString()
  {
    return "ZoneRequest{id=" + this.id + ", name='" + this.name + '\'' + '}';
  }
}
