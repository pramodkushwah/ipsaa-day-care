package com.synlabs.ipsaa.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class SerialNumberSequence extends NumberSequence
{
  @Column(nullable = false, length = 20)
  private String centerCode;

  @Column(nullable = false, length = 20)
  private String type;

  public SerialNumberSequence()
  {
  }

  public SerialNumberSequence(String centerCode, String type)
  {
    this.centerCode = centerCode;
    this.type = type;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
