package com.synlabs.ipsaa.entity.common;

import com.synlabs.ipsaa.enums.AddressType;

import javax.persistence.*;

/**
 * Same address will be used across
 * i.e. for student, mother, father and guardian if the are same
 */
@Embeddable
public class Address
{
  @Column(nullable = false, length = 200)
  private String address;

  @Column(nullable = false, length = 50)
  private String city;

  @Column(nullable = false, length = 50)
  private String state;

  @Column(nullable = false, length = 20)
  private String zipcode;

  @Column(nullable = true, length = 20)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AddressType addressType;

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public AddressType getAddressType()
  {
    return addressType;
  }

  public void setAddressType(AddressType addressType)
  {
    this.addressType = addressType;
  }

  public String getZipcode()
  {
    return zipcode;
  }

  public void setZipcode(String zipcode)
  {
    this.zipcode = zipcode;
  }

  @Override
  public String toString()
  {
    return "Address{" +
        "address='" + address + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", zipcode='" + zipcode + '\'' +
        ", phone='" + phone + '\'' +
        ", addressType=" + addressType +
        '}';
  }
}
