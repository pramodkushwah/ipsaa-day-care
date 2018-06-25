package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.StudentAttendanceService;
import com.synlabs.ipsaa.view.attendance.StudentAttendanceRequest;
import com.synlabs.ipsaa.view.attendance.StudentAttendanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STUDENT_CLOCKINOUT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STUDENT_READ;

@RestController
@RequestMapping("/api/attendance/student/")
public class StudentAttendanceController
{

  @Autowired
  private StudentAttendanceService attendanceService;

  @GetMapping
  @Secured(STUDENT_READ)
  public List<StudentAttendanceResponse> list() {
    return attendanceService.list().stream().map(StudentAttendanceResponse::new).collect(Collectors.toList());
  }

  @Secured(STUDENT_CLOCKINOUT)
  @PostMapping("clockin")
  public void clockin(@RequestBody StudentAttendanceRequest request) {
    attendanceService.clockin(request);
  }

  @Secured(STUDENT_CLOCKINOUT)
  @PostMapping("clockout")
  public void clockout(@RequestBody StudentAttendanceRequest request) {
    attendanceService.clockout(request);
  }

}
