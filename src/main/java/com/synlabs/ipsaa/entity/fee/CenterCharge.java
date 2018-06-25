package com.synlabs.ipsaa.entity.fee;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CenterCharge extends BaseEntity
{

  @ManyToOne
  private Center center;

  @ManyToOne
  private Charge charge;

  private int amount;

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public Charge getCharge()
  {
    return charge;
  }

  public void setCharge(Charge charge)
  {
    this.charge = charge;
  }

  public int getAmount()
  {
    return amount;
  }

  public void setAmount(int amount)
  {
    this.amount = amount;
  }
}
