package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.view.common.Response;

public class ProgramGroupResponse implements Response
{

  private Long   id;
  private String name;
  private String description;

  public ProgramGroupResponse(ProgramGroup group)
  {
    this.id = mask(group.getId());
    this.name = group.getName();
    this.description = group.getDescription();
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
