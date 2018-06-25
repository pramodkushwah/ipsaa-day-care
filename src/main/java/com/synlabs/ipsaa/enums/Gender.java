package com.synlabs.ipsaa.enums;

public enum Gender
{
  Male("Boy"),
  Female("Girl");

  private String alt;

  Gender(String alt)
  {
    this.alt = alt;
  }

  public static boolean matches(String gender)
  {
    for (Gender gndr : Gender.values())
    {
      if (gndr.name().equals(gender))
      {
        return true;
      }
    }
    return false;
  }
}
