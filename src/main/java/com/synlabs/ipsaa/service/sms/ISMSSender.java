package com.synlabs.ipsaa.service.sms;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;

import java.util.List;

public interface ISMSSender
{
  Boolean sendClockIn(StudentAttendance attendance, String... to);

  Boolean sendClockOut(StudentAttendance attendance, String... to);

  Boolean sendPassword(String password, String to);

  Boolean sendParentMessage(String message, String centerName, String... to);

  Boolean sendStaffMessage(String message, List<Employee> employees);

  Boolean sendMessage(String message, String... to);

  Boolean sendResetPassword(String token, String to);
}
