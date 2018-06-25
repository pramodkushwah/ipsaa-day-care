package com.synlabs.ipsaa.enums;

public enum AddressType
{
  Home,
  Office,
  Doctor,
  Center,
  Permanent;

  public static boolean matches(String addressType)
  {
    for (AddressType at : AddressType.values())
    {
      if (at.name().equals(addressType))
      {
        return true;
      }
    }
    return false;
  }
}
