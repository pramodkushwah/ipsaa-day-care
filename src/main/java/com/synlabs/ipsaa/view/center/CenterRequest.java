package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.CenterType;
import com.synlabs.ipsaa.view.common.Request;

public class CenterRequest implements Request
{
  private Long id;
  private String code;
  private String name;
  private String type;
  private int capacity;
  private String zone;
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phone;

  public Center toEntity() {
    return toEntity(null);
  }

  public Center toEntity(Center center)
  {
    if (center == null) {
      center = new Center();
      Address address = new Address();
      address.setAddress(this.address);
      address.setAddressType(AddressType.Center);
      //address.setCity();
      //address.setState(this.state);
      address.setZipcode(this.zipcode);
      address.setPhone(this.phone);
      center.setAddress(address);
    }

    center.setCode(this.code);
    center.setName(this.name);
    center.setCenterType(CenterType.valueOf(this.type));
    center.setCapacity(this.capacity);
    return center;
  }

  public CenterRequest()
  {
  }

  public CenterRequest(Long id)
  {
    this.id = id;
  }

  public Long getMaskId(){
    return id;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public int getCapacity()
  {
    return capacity;
  }

  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
  }

  public String getZone()
  {
    return zone;
  }

  public void setZone(String zone)
  {
    this.zone = zone;
  }

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
    return state.toUpperCase();
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getZipcode()
  {
    return zipcode;
  }

  public void setZipcode(String zipcode)
  {
    this.zipcode = zipcode;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }
}
