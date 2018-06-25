package com.synlabs.ipsaa.entity.common;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class AdmissionNumberSequence extends NumberSequence
{

  @Column(nullable = false, length = 20)
  private String centerCode;

  public AdmissionNumberSequence()
  {
  }

  public AdmissionNumberSequence(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }
}
