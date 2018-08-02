package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.view.common.AddressResponse;

public class CenterResponse extends CenterSummaryResponse
{

  private ZoneResponse    zone;
  private AddressResponse address;

  // shubham
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phone;
  private AddressType addressType;


  public CenterResponse(Center center)
  {
    super(center);
    this.address = new AddressResponse(center.getAddress());
    this.zone = new ZoneResponse(center.getZone());
  }

  public AddressResponse getAddress()
  {
    return address;
  }

  public ZoneResponse getZone()
  {
    return zone;
  }
}
