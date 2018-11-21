package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.view.common.AddressResponse;

public class CenterResponseV2 extends CenterSummaryResponse
{

  //private ZoneResponse    zone;
  // shubham
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phone;
  private AddressType addressType;
  private String zone;

  public CenterResponseV2(Center center)
  {
    super(center);
    AddressResponse address = new AddressResponse(center.getAddress());
    this.address=address.getAddress();
    this.city=address.getCity();
    this.addressType=address.getAddressType();
    this.zipcode=address.getZipcode();
    this.state=address.getState();
    this.zone = center.getZone().getName();
    this.phone = address.getPhone();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public void setAddressType(AddressType addressType) {
    this.addressType = addressType;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }
}
