package com.synlabs.ipsaa.util;

import java.math.BigDecimal;

public class BigDecimalUtils
{
  public static final BigDecimal HALF      = new BigDecimal("0.5");
  public static final BigDecimal ONE_THIRD = new BigDecimal("0.3333333333");

  public static final BigDecimal ZERO   = BigDecimal.ZERO;
  public static final BigDecimal ONE    = BigDecimal.ONE;
  public static final BigDecimal TWO    = new BigDecimal(2);
  public static final BigDecimal THREE  = new BigDecimal(3);
  public static final BigDecimal FOUR   = new BigDecimal(4);
  public static final BigDecimal FIVE   = new BigDecimal(5);
  public static final BigDecimal SIX    = new BigDecimal(6);
  public static final BigDecimal SEVEN  = new BigDecimal(7);
  public static final BigDecimal EIGHTH = new BigDecimal(8);
  public static final BigDecimal NINE   = new BigDecimal(9);
  public static final BigDecimal TEN    = BigDecimal.TEN;
  public static final BigDecimal TWELVE    = new BigDecimal(12);


  public static final BigDecimal FORTY = new BigDecimal(40);

  public static final BigDecimal HUNDRED = new BigDecimal(100);

  public static boolean greaterThen(BigDecimal a, BigDecimal b)
  {
    if (a == null)
    {
      return false;
    }
    if (b == null)
    {
      return true;
    }
    return a.compareTo(b) < 0;
  }

  public static boolean lessThen(BigDecimal a, BigDecimal b)
  {
    if (a == null)
    {
      return true;
    }
    if (b == null)
    {
      return false;
    }
    return a.compareTo(b) < 0;
  }

  public static boolean equals(BigDecimal a, BigDecimal b)
  {
    if (a == null && b == null)
    {
      return true;
    }
    if (a == null || b == null)
    {
      return false;
    }
    return a.equals(b);
  }

  public static boolean isZero(BigDecimal number)
  {
    if (number == null)
    {
      return false;
    }
    return BigDecimal.ZERO.setScale(number.scale(), BigDecimal.ROUND_HALF_UP).equals(number);
  }
}
