package com.synlabs.ipsaa.entity.hygiene;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by itrs on 7/7/2017.
 */
@Entity
public class Hygiene extends BaseEntity
{
  @Column(length = 50, nullable = false)
  private String name;

  @ManyToOne
  private Center center;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }
}
