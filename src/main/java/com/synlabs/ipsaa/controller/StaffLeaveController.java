package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.enums.LeaveType;
import com.synlabs.ipsaa.service.StaffLeaveService;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceFilterRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeLeaveRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeLeaveResponse;
import com.synlabs.ipsaa.view.attendance.EmployeeLeaveSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_LEAVE_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_LEAVE_WRITE;

@RestController
@RequestMapping("/api/staff/leave")
public class StaffLeaveController
{

  @Autowired
  private StaffLeaveService leaveService;

  @PostMapping("/summary/")
  @Secured(STAFF_LEAVE_READ)
  public List<EmployeeLeaveSummaryResponse> listSummary(@RequestBody EmployeeAttendanceFilterRequest request)
  {
    return leaveService.listSummary(request).stream().map(EmployeeLeaveSummaryResponse::new).collect(Collectors.toList());
  }

  @GetMapping("/approve/{id}")
  public boolean approveLeave(@PathVariable("id") Long id)
  {
    return leaveService.approveLeave(id);
  }

  @GetMapping("/reject/{id}")
  public boolean rejectLeave(@PathVariable("id") Long id)
  {
    return leaveService.rejectLeave(id);
  }

  @Secured(STAFF_LEAVE_WRITE)
  @PostMapping("single-day-leave")
  public void singleDayLeave(@RequestParam(name = "eid", required = false) String eid,
                             @RequestParam(value = "halfLeave", required = false) Boolean halfLeave,
                             @RequestParam(value = "leaveType", required = false) LeaveType leaveType,
                             @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date)
  {
    leaveService.singleDayLeave(eid,  halfLeave, leaveType, date);
  }


  @Secured(STAFF_LEAVE_WRITE)
  @PostMapping("multi-day-leave")
  public void multiDayLeave(@RequestBody EmployeeLeaveRequest employeeLeaveRequest)
  {
    leaveService.multiDayLeave(employeeLeaveRequest);
  }

  @Secured(STAFF_LEAVE_WRITE)
  @PostMapping("import")
  public void importLeaveSummary(@RequestParam("file") MultipartFile file) throws IOException
  {
    leaveService.importLeaveSummary(file);
  }

  @Secured(STAFF_LEAVE_WRITE)
  @GetMapping("syncLeaveSummary")
  public void syncLeaveSummary(@RequestParam("year") Integer year,
                               @RequestParam(value = "month", required = false) Integer month,
                               @RequestParam(value = "centerCode", required = false) String centerCode,
                               @RequestParam(value = "employeeActive", required = false) Boolean employeeActive)
  {
    leaveService.syncLeaveSummary(month, year, centerCode, employeeActive);
  }

  @PostMapping("{month}")
  public List<EmployeeLeaveSummaryResponse> leavesBymonth(@PathVariable ("month") int month){
    return leaveService.getLeavesByMonth(month);
  }

  @PostMapping("/employeeMonthly")
  public List<EmployeeLeaveResponse> employeeLeaves(@RequestParam("employeeId") Long employeeId,
                                                           @RequestParam("month") Integer month){
    return leaveService.employeeLeavesMonthly(employeeId,month).stream().map(EmployeeLeaveResponse::new)
            .collect(Collectors.toList());
  }
}
