package com.synlabs.ipsaa.Schedular;

import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.service.BiometricAttendanceService;
import com.synlabs.ipsaa.service.StudentService;
import com.synlabs.ipsaa.view.attendance.AttendancePullRequest;
import com.synlabs.ipsaa.view.attendance.BioAttendance;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

@Component
public class AttendancePullScheduler
{
  @Autowired
  private BiometricAttendanceService service;

  private static final Logger logger = LoggerFactory.getLogger(AttendancePullScheduler.class);

  @Scheduled(cron = "0 * * * * *")
  public void pullAttendance() throws ParseException, SQLException, IOException
  {
    logger.info("Pull attendance from Ipsaa attencance Database.");
    Set<BioAttendance> bioAttendances = service.pullFromDatabase(LocalDate.now().minusDays(7).toDate());
    service.saveAttendance(bioAttendances, false);
  }
}
