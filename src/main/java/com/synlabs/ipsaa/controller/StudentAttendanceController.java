package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.service.StudentAttendanceService;
import com.synlabs.ipsaa.service.StudentFeeService;
import com.synlabs.ipsaa.view.attendance.StudentAttendanceRequest;
import com.synlabs.ipsaa.view.attendance.StudentAttendanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
  @Autowired
  private StudentFeeService studentFeeService;

  @GetMapping
  @Secured(STUDENT_READ)
  public List<StudentAttendanceResponse> list() {
    return attendanceService.studentAttendanceList().stream().map(StudentAttendanceResponse::new).collect(Collectors.toList());
  }

  @Secured(STUDENT_CLOCKINOUT)
  @PostMapping("clockin")
  public StudentAttendanceResponse clockin(@RequestBody StudentAttendanceRequest request) {
   return new StudentAttendanceResponse(attendanceService.clockin(request));
  }

  @Secured(STUDENT_CLOCKINOUT)
  @PostMapping("clockout")
  public StudentAttendanceResponse clockout(@RequestBody StudentAttendanceRequest request) {
    return  new StudentAttendanceResponse(attendanceService.clockout(request));
  }

  ///////////////////////////Avneet

  @Secured(STUDENT_READ)
  @GetMapping("present")
  public List<StudentAttendanceResponse> presentStudents(){
    return attendanceService.listOfPresentStudents().stream().map(StudentAttendanceResponse::new).collect(Collectors.toList());
  }

  @Secured(STUDENT_READ)
  @GetMapping("/corporate/{isCorporate}")
  public List<StudentAttendanceResponse> studentList(@PathVariable("isCorporate") boolean isCorporate){
    return attendanceService.listOfStudents(isCorporate).stream().map(StudentAttendanceResponse::new).collect(Collectors.toList());
  }

  //////////School Attendance
  @Secured(STUDENT_CLOCKINOUT)
  @PostMapping("markPresents")
  //////add programs
  public List<StudentAttendanceResponse> markPresent(@RequestParam("centerId") Long centerId,
                                                     @RequestParam("programId") Long programId){
    return attendanceService.mark(centerId,programId).stream().map(StudentAttendanceResponse::new).collect(Collectors.toList());
  }

  @Secured(STUDENT_CLOCKINOUT)
  @PutMapping("markAbsents/{studentId}")
  public StudentAttendanceResponse markAbsent(@PathVariable("studentId") Long studentId){
    return new StudentAttendanceResponse(attendanceService.markAbsents(studentId));//StudentAttendanceResponse(attendanceService.markAbsents(studentId));
  }

  //shubham update attendance
  @Secured(STUDENT_READ)
  @GetMapping("updateAtt")
  public String updateAttendance(){
    return attendanceService.updateRunV2()+"";
  }
  @Secured(STUDENT_READ)
  @GetMapping("updateFee")
  public String updateFee(){
    studentFeeService.updateExtraHours();
    return "";
  }
}
