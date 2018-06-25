package com.synlabs.ipsaa.entity.support;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class SupportQueryEntry extends BaseEntity
{

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private SupportQuery supportQuery;

  @Column(length = 2000)
  private String description;

  public SupportQuery getSupportQuery()
  {
    return supportQuery;
  }

  public void setSupportQuery(SupportQuery supportQuery)
  {
    this.supportQuery = supportQuery;
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
