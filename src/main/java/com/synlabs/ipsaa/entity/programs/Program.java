package com.synlabs.ipsaa.entity.programs;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Only four programs
 */
@Entity
public class Program extends BaseEntity
{

  @Column(updatable = false, nullable = false, length = 20)
  private String code;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 200)
  private String description;

  @ManyToMany
  @JoinTable
      (
          name="program_group",
          joinColumns={ @JoinColumn(name="PROGRAM_ID", referencedColumnName="ID") },
          inverseJoinColumns={ @JoinColumn(name="GROUP_ID", referencedColumnName="ID") }
      )
  private List<ProgramGroup> groups;

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
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

  public List<ProgramGroup> getGroups()
  {
    return groups;
  }

  public void setGroups(List<ProgramGroup> groups)
  {
    this.groups = groups;
  }
}
