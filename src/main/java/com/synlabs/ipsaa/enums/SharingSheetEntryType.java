package com.synlabs.ipsaa.enums;

/**
 * Created by sushil on 30-06-2017.
 */
public enum SharingSheetEntryType
{
  Food,
  Snack,
  Lunch,
  Drink,
  Sleep,
  Nap,
  Diaper,
  Note,
  Sharing;

  public static boolean matches(String entryType)
  {
    for (SharingSheetEntryType et :SharingSheetEntryType.values()) {
      if (et.name().equals(entryType)) return true;
    }
    return false;
  }
}
