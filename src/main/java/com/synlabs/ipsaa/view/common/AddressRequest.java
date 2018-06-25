package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.Address;

/**
 * Created by itrs on 4/4/2017.
 */
public class AddressRequest
{
  public static final String defaultValue = "NA";
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phone;

  public AddressRequest(){}

  public AddressRequest(String address){
    this.address=address;
  }

  public String getPhone()
  {
    return phone;
  }

  public AddressRequest setPhone(String phone)
  {
    this.phone = phone;
    return this;
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

  public void setAddress(String address)
  {
    this.address = address;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public void setZipcode(String zipcode)
  {
    this.zipcode = zipcode;
  }

  public Address toEntity()
  {
    return toEntity(null);
  }

  public Address toEntity(Address address)
  {
    if (address == null)
    {
      address = new Address();
    }
    address.setAddress(this.address == null ? defaultValue : this.address);
    address.setCity(city == null ? defaultValue : city);
    address.setState(state == null ? defaultValue : state);
    address.setZipcode(zipcode == null ? defaultValue : zipcode);
    address.setPhone(phone == null ? defaultValue : phone);
    return address;
  }

  @Override
  public String toString()
  {
    return "AddressRequest{" +
        "address='" + address + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", zipcode='" + zipcode + '\'' +
        '}';
  }
}
