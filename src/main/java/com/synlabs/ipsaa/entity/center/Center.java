package com.synlabs.ipsaa.entity.center;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.CenterType;

import javax.persistence.*;

@Entity
public class Center extends BaseEntity
{

  @Column(nullable = false, updatable = false, length = 20, unique = true)
  private String code;

  @Column(nullable = false)
  private int capacity;

  @Column(nullable = false, length = 50, unique = true)
  private String name;

  @ManyToOne(optional = false)
  private Zone zone;

  @Enumerated(EnumType.STRING)
  private CenterType centerType;

  @Embedded
  private Address address;

  private String accountNumber;

  private String accountName;

  private boolean active;

  public String getAccountName()
  {
    return accountName;
  }

  public void setAccountName(String accountName)
  {
    this.accountName = accountName;
  }

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber)
  {
    this.accountNumber = accountNumber;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode(String code)
  {
    this.code = code;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress(Address address)
  {
    this.address = address;
  }

  public Zone getZone()
  {
    return zone;
  }

  public void setZone(Zone zone)
  {
    this.zone = zone;
  }

  public CenterType getCenterType()
  {
    return centerType;
  }

  public void setCenterType(CenterType centerType)
  {
    this.centerType = centerType;
  }

  public int getCapacity()
  {
    return capacity;
  }

  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
  }

  @Override
  public String toString()
  {
    return "Center{" +
        "code='" + code + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
