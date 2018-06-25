package com.synlabs.ipsaa.enums;

/**
 * Created by sushil on 30-06-2017.
 */
public enum SharingSheetFeedQty
{
  None,
  Some,
  Most,
  All;

  public static boolean matches(String feedQty)
  {
    for (SharingSheetFeedQty fq :SharingSheetFeedQty.values()) {
      if (fq.name().equals(feedQty)) return true;
    }
    return false;
  }
}
