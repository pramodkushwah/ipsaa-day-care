package com.synlabs.ipsaa.service.sms;

import com.synlabs.ipsaa.config.Dev;
import com.synlabs.ipsaa.config.Local;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Dev
@Service
public class MockSMSSenderService extends BaseService implements ISMSSender
{

  private static final Logger logger = LoggerFactory.getLogger(MockSMSSenderService.class);

  @Override
  public Boolean sendClockIn(StudentAttendance attendance, String... to)
  {
    return true;
  }

  @Override
  public Boolean sendClockOut(StudentAttendance attendance, String... to)
  {
    return true;
  }

  @Override
  public Boolean sendPassword(String password, String to)
  {
    return true;
  }

  @Override
  public Boolean sendParentMessage(String message, String centerName, String... to)
  {
    return true;
  }

  @Override
  public Boolean sendStaffMessage(String message, List<Employee> employees)
  {
    return true;
  }

  @Override
  public Boolean sendMessage(String message, String... to)
  {
    return true;
  }

  @Override
  public Boolean sendResetPassword(String token, String to)
  {
    return true;
  }
}
