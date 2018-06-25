package com.synlabs.ipsaa.entity.programs;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Groups are according to age slabs, 3-5 groups for each program
 */
@Entity(name = "grp")
public class ProgramGroup extends BaseEntity
{

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 200)
  private String description;

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

}
