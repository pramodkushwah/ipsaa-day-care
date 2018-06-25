package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.view.common.Request;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ProgramGroupRequest implements Request
{
  private Long id;

  @NotBlank
  @Size(min = 2,max = 50)
  private String name;

  @NotBlank
  @Size(min = 2,max = 200)
  private String description;

  public ProgramGroupRequest()
  {
  }

  public ProgramGroupRequest(Long id)
  {
    this.id = id;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public ProgramGroup toEntity()
  {
    return toEntity(null);
  }

  public ProgramGroup toEntity(ProgramGroup group)
  {
    if (group == null) group = new ProgramGroup();
    group.setName(this.name);
    group.setDescription(this.description);
    return group;
  }
}
