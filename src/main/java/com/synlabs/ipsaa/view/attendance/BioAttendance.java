package com.synlabs.ipsaa.view.attendance;

import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class BioAttendance
{
  private Date clockin;

  private Date clockout;

  private LocalDate date;

  private List<Date> list = new ArrayList<>();

  private Integer biometricId;

  public BioAttendance(Integer biometricId, LocalDate date)
  {
    this.date = date;
    this.biometricId = biometricId;
  }

  public void updateClocking()
  {
    if (CollectionUtils.isEmpty(list))
    {
      return;
    }
    Collections.sort(list);
    list.sort(Comparator.naturalOrder());
    clockin = list.get(0);
    if (list.size() > 1)
    {
      clockout = list.get(list.size() - 1);
    }
  }

  public void setList(List<Date> list)
  {
    this.list = list;
  }

  public Date getClockin()
  {
    return clockin;
  }

  public Date getClockout()
  {
    return clockout;
  }

  public LocalDate getDate()
  {
    return date;
  }

  public List<Date> getList()
  {
    return list;
  }

  public Integer getBiometricId()
  {
    return biometricId;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    BioAttendance that = (BioAttendance) o;

    if (!date.equals(that.date))
    {
      return false;
    }
    return biometricId.equals(that.biometricId);
  }

  @Override
  public int hashCode()
  {
    int result = date.hashCode();
    result = 31 * result + biometricId.hashCode();
    return result;
  }
}
