package com.synlabs.ipsaa.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Privilege extends BaseEntity
{

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false, length = 200)
  private String description;

  public Privilege()
  {
  }

  public Privilege(Privilege p)
  {
    this.name = p.name;
    this.description = p.description;
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
}
