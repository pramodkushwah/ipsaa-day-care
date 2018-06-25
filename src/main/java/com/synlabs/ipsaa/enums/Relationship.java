package com.synlabs.ipsaa.enums;

public enum Relationship
{
  Father,
  Mother,
  Guardian;

  public static boolean matches(String relationship)
  {
    for (Relationship rltnshp : Relationship.values())
    {
      if (rltnshp.name().equals(relationship))
      {
        return true;
      }
    }
    return false;
  }
}
