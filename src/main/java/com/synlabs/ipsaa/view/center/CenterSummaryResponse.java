package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.view.common.Response;

public class CenterSummaryResponse implements Response
{
  private Long id;
  private String code;
  private String name;
  private String type;
  private int capacity;
  private boolean active;

  public CenterSummaryResponse(Center center)
  {
    this.id = mask(center.getId());
    this.code = center.getCode();
    this.name = center.getName();
    this.type = center.getCenterType().name();
    this.capacity = center.getCapacity();
    this.active = center.isActive();
  }

  public boolean isActive()
  {
    return active;
  }

  public Long getId()
  {
    return id;
  }

  public String getCode()
  {
    return code;
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }

  public int getCapacity()
  {
    return capacity;
  }
}
