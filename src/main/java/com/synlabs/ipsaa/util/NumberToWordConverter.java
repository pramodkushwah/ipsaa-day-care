package com.synlabs.ipsaa.util;

import java.text.DecimalFormat;

public class NumberToWordConverter
{
  private static final String[] tensNames = {
      "",
      " Ten",
      " Twenty",
      " Thirty",
      " Forty",
      " Fifty",
      " Sixty",
      " Seventy",
      " Eighty",
      " Ninety"
  };

  private static final String[] numNames = {
      "",
      " One",
      " Two",
      " Three",
      " Four",
      " Five",
      " Six",
      " Seven",
      " Eight",
      " Nine",
      " Ten",
      " Eleven",
      " Twelve",
      " Thirteen",
      " Fourteen",
      " Fifteen",
      " Sixteen",
      " Seventeen",
      " Eighteen",
      " Nineteen"
  };

  private static String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

  /**
   * It should be used for 0-999 only.
   *
   * @param number
   * @return String represents to number
   */
  private static String convertLessThanOneThousand(int number)
  {
    String soFar;

    if (number % 100 < 20)
    {
      soFar = numNames[number % 100];
      number /= 100;
    }
    else
    {
      soFar = numNames[number % 10];
      number /= 10;

      soFar = tensNames[number % 10] + soFar;
      number /= 10;
    }
    if (number == 0)
    {
      return soFar;
    }
    return (numNames[number] + " Hundred" + soFar).trim();
  }

  /**
   * It should be used for 0-999999999 only.
   *
   * @param number
   * @return String represents to number
   */
  public static String convert(long number)
  {
    // 0 to 999 999 999 999
    if (number == 0)
    {
      return "Zero";
    }

    String snumber = Long.toString(number);

    // pad with "0"
    String mask = "000000000000";
    DecimalFormat df = new DecimalFormat(mask);
    snumber = df.format(number);

    int lessThenThousand = Integer.parseInt(snumber.substring(9, 12));
    int thousands = Integer.parseInt(snumber.substring(7, 9));
    int lakhs = Integer.parseInt(snumber.substring(5, 7));
    int crores = Integer.parseInt(snumber.substring(3, 5));

    String result = convertLessThanOneThousand(lessThenThousand);

    if (thousands != 0)
    {
      result = convertLessThanOneThousand(thousands) + " Thousand " + result;
    }
    if (lakhs != 0)
    {
      result = convertLessThanOneThousand(lakhs) + " Lakh" + result;
    }
    if (crores != 0)
    {
      result = convertLessThanOneThousand(crores) + " Crore" + result;
    }
    return result.trim();
  }

  public static String addNumberOrdinal(int i)
  {
    switch (i % 100)
    {
      case 11:
      case 12:
      case 13:
        return i + "th";
      default:
        return i + sufixes[i % 10];

    }
  }
}
