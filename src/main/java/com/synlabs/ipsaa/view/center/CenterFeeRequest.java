package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.view.common.Request;

public class CenterFeeRequest implements Request
{
  private Long centerId;
  public CenterFeeRequest()
  {
  }
  public CenterFeeRequest(Long centerId)
  {
    this.centerId = centerId;
  }
  public Long getCenterId()
  {
    return unmask(centerId);
  }
}
