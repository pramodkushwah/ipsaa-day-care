package com.synlabs.ipsaa.enums;

public enum FamilyType
{
  Nuclear,
  Joint;

  public static boolean matches(String familyType)
  {
    for (FamilyType ft : FamilyType.values())
    {
      if (ft.name().equals(familyType))
      {
        return true;
      }
    }
    return false;
  }
}
