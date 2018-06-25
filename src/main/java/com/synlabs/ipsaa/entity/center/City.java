package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class City extends BaseEntity
{
  private String name;

  @ManyToOne
  private Zone zone;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Zone getZone()
  {
    return zone;
  }

  public void setZone(Zone zone)
  {
    this.zone = zone;
  }
}
