package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.view.common.Request;

/**
 * Created by itrs on 4/20/2017.
 */
public class CenterChargeRequest implements Request
{
  private Long id;
  private Long centerId;
  private Long chargeId;
  private Long amount;

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public Long getMaskedCenterId(){
    return centerId;
  }
  public Long getMaskedChargeId(){
    return chargeId;
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public Long getChargeId()
  {
    return unmask(chargeId);
  }

  public void setChargeId(Long chargeId)
  {
    this.chargeId = chargeId;
  }

  public Long getAmount()
  {
    return amount;
  }

  public void setAmount(Long amount)
  {
    this.amount = amount;
  }

  @Override
  public String toString()
  {
    return "CenterChargeRequest{" +
        "id=" + id +
        ", centerId=" + centerId +
        ", chargeId=" + chargeId +
        ", amount=" + amount +
        '}';
  }
}
