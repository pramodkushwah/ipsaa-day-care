package com.synlabs.ipsaa.enums;

public enum EmployeeType
{
  CxO,
  Management,
  Staff,
  Teacher;

  public static boolean matches(String employeeType)
  {
    for (EmployeeType et :EmployeeType.values()) {
      if (et.name().equals(employeeType)) return true;
    }
    return false;
  }
}
