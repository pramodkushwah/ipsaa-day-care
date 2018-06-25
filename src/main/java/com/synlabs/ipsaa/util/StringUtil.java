package com.synlabs.ipsaa.util;

/**
 * Created by sushil on 12-05-2017.
 */
public class StringUtil
{

  public static boolean in(String verb, String...list) {
    for (String match : list) {
      if (verb.equalsIgnoreCase(match)) return true;
    }
    return false;
  }
}
