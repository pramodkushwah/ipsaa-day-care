package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.enums.AddressType;

public class AddressResponse implements Response
{
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phone;
  private AddressType addressType;

  public AddressResponse(Address address)
  {
    this.address = address.getAddress();
    this.city = address.getCity();
    this.state = address.getState();
    this.zipcode = address.getZipcode();
    this.phone = address.getPhone();
    this.addressType = address.getAddressType();
  }

  public String getAddress()
  {
    return address;
  }

  public String getCity()
  {
    return city;
  }

  public String getState()
  {
    return state;
  }

  public String getZipcode()
  {
    return zipcode;
  }

  public String getPhone()
  {
    return phone;
  }

  public AddressType getAddressType()
  {
    return addressType;
  }
}
