package com.synlabs.ipsaa.util;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Utils
{
  public static Set<Date> datesBetween(Date from, Date to, boolean includeSunday)
  {
    Set<Date> dates = new HashSet<>();
    LocalDate f = LocalDate.fromDateFields(from);
    LocalDate t = LocalDate.fromDateFields(to);
    if (f.isAfter(t))
    {
      LocalDate d = f;
      t = f;
      f = d;
    }
    int days = Days.daysBetween(f, t).getDays() + 1;
    for (int i = 0; i < days; i++)
    {
      LocalDate d = f.withFieldAdded(DurationFieldType.days(), i);
      if (includeSunday || d.getDayOfWeek() != DateTimeConstants.SUNDAY)
      {
        dates.add(d.toDate());
      }
    }
    return dates;
  }

  public static boolean isSunday(Date date)
  {
    return isSunday(LocalDate.fromDateFields(date));
  }

  public static boolean isSunday(LocalDate date)
  {
    return date.dayOfWeek().get() == DateTimeConstants.SUNDAY;
  }

  public static boolean getBooleanValue(String s)
  {
    return !StringUtils.isEmpty(s)
        && (s.compareToIgnoreCase("yes") == 0 ||
        s.compareToIgnoreCase("true") == 0);
  }

}
