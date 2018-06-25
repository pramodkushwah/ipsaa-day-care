package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.fee.CenterCharge;
import com.synlabs.ipsaa.view.fee.ChargeResponse;
import com.synlabs.ipsaa.view.common.Response;

public class CenterChargeResponse implements Response
{
  private Long                  id;
  private CenterSummaryResponse center;
  private ChargeResponse        charge;
  private int                   amount;

  public CenterChargeResponse(CenterCharge centerCharge)
  {
    this.id = mask(centerCharge.getId());
    this.center = new CenterSummaryResponse(centerCharge.getCenter());
    this.charge = new ChargeResponse(centerCharge.getCharge());
    this.amount = centerCharge.getAmount();
  }

  public Long getId()
  {
    return id;
  }

  public CenterSummaryResponse getCenter()
  {
    return center;
  }

  public int getAmount()
  {
    return amount;
  }

  public ChargeResponse getCharge()
  {
    return charge;
  }
}
