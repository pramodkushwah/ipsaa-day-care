package com.synlabs.ipsaa.view.common;

/**
 * Created by sushil on 27-06-2017.
 */
public class FeeStatsResponse
{
  private int yearly;
  private int year;
//  private int monthly;
  private int month;
  private int ipssaFee;

  private int quarterly;
  private int quarter;

  private int total;

  public int getYearly()
  {
    return yearly;
  }

  public void setYearly(int yearly)
  {
    this.yearly = yearly;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public int getIpssaFee() {
    return ipssaFee;
  }

  public void setIpssaFee(int ipssaFee) {
    this.ipssaFee = ipssaFee;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    this.month = month;
  }

  public int getQuarterly()
  {
    return quarterly;
  }

  public void setQuarterly(int quarterly)
  {
    this.quarterly = quarterly;
  }

  public int getQuarter()
  {
    return quarter;
  }

  public void setQuarter(int quarter)
  {
    this.quarter = quarter;
  }

  public void setTotal(int total)
  {
    this.total = total;
  }

  public int getTotal()
  {
    return total;
  }
}
