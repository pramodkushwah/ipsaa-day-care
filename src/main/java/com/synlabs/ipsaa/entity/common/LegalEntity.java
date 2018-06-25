package com.synlabs.ipsaa.entity.common;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Created by sushil on 02-04-2018.
 */
@Entity
public class LegalEntity extends BaseEntity
{
  @Column(nullable = false, updatable = false, length = 20, unique = true)
  private String code;

  @Column(nullable = false, updatable = false, length = 200, unique = true)
  private String name;

  @Column(length = 20, unique = true)
  private String cin;

  @Column(length = 20, unique = true)
  private String pan;

  @Column(length = 20, unique = true)
  private String tan;

  @Column(length = 20, unique = true)
  private String gst;

  @Embedded
  private Address address;

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

  public String getCin()
  {
    return cin;
  }

  public void setCin(String cin)
  {
    this.cin = cin;
  }

  public String getPan()
  {
    return pan;
  }

  public void setPan(String pan)
  {
    this.pan = pan;
  }

  public String getTan()
  {
    return tan;
  }

  public void setTan(String tan)
  {
    this.tan = tan;
  }

  public String getGst()
  {
    return gst;
  }

  public void setGst(String gst)
  {
    this.gst = gst;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  @Transient
  public String getAddressString()
  {
    if (address != null)
    {
      return String.format("%s %s %s %s",
                           address.getAddress().trim(),
                           address.getCity().trim(),
                           address.getState().trim(),
                           address.getZipcode().trim())
                   .trim();
    }
    return "";
  }
}
