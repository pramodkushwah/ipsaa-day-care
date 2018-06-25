package com.synlabs.ipsaa.enums;

public enum UserType
{
  Employee,
  Parent,
  Consultant,
  Management;

  public static boolean matches(String userType)
  {
    for (UserType ut :UserType.values()) {
      if (ut.name().equals(userType)) return true;
    }
    return false;
  }
}
