package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.fee.Charge;
import com.synlabs.ipsaa.view.common.Response;

public class ChargeResponse implements Response
{

  private Long id;
  private String name;

  public ChargeResponse(Charge charge)
  {
    if(charge==null){
      return;
    }
    this.id = mask(charge.getId());
    this.name = charge.getName();
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

}
