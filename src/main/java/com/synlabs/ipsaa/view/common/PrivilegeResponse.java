package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.Privilege;

public class PrivilegeResponse implements Response
{

  private Long id;
  private String name;

  private String description;

  public PrivilegeResponse(Privilege privilege)
  {
    this.id = mask(privilege.getId());
    this.name = privilege.getName();
    this.description = privilege.getDescription();
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }
}
