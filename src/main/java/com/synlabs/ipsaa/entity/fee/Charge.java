package com.synlabs.ipsaa.entity.fee;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Charge extends BaseEntity
{

  @Column(nullable = false, length = 50)
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
