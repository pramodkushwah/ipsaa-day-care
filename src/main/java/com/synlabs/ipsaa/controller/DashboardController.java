package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.service.DashboardService;
import com.synlabs.ipsaa.view.common.DashboardRequest;
import com.synlabs.ipsaa.view.common.StatsResponse;
import com.synlabs.ipsaa.view.common.UserResponse;
import com.synlabs.ipsaa.view.inquiry.FollowUpReportResponse;
import com.synlabs.ipsaa.view.staff.DashStaffResponse;
import com.synlabs.ipsaa.view.staff.StaffNewJoinings;
import com.synlabs.ipsaa.view.staff.StaffNewLeavings;
import com.synlabs.ipsaa.view.student.DashStudentFeeResponse;
import com.synlabs.ipsaa.view.student.DashStudentResponse;
import com.synlabs.ipsaa.view.student.ParentSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.DASHBOARD_STATS;

@RestController
@RequestMapping("/api/")
public class DashboardController
{
  @Autowired
  private DashboardService dashboardService;

  // modify by shubham
  @PostMapping("stats")
  @Secured(DASHBOARD_STATS)
  public StatsResponse getStats(@RequestBody DashboardRequest request)
  {
    //return dashboardService.getStats(request);
    return dashboardService.getStatsV2(request);
  }

  @PostMapping("stats/fee")
  @Secured(DASHBOARD_STATS)
  public StatsResponse getFeeStat(@RequestBody DashboardRequest request)
  {
    return dashboardService.getFeeStats(request);
  }

  @GetMapping("dash")
  @Secured(DASHBOARD_STATS)
  public Set<String> dashboards()
  {
    return dashboardService.getDashboardList();
  }

  @PostMapping("/dash/student")
  @Secured(DASHBOARD_STATS)
  public List<DashStudentResponse> getStudentList(@RequestBody DashboardRequest request)
  {
	  return dashboardService.listStudent(request);
  }

  // shubham
  @PostMapping("/dash/allstudent")
  @Secured(DASHBOARD_STATS)
  public List<DashStudentResponse> getAllStudentList(@RequestBody DashboardRequest request)
  {
    return dashboardService.allStudentList(request);
  }

  @PostMapping("/dash/staff")
  @Secured(DASHBOARD_STATS)
  public List<DashStaffResponse> getStaffList(@RequestBody DashboardRequest request)
  {
    return dashboardService.listStaffV2(request);
  }

  @PostMapping("/dash/studentfee")
  @Secured(DASHBOARD_STATS)
  public List<DashStudentFeeResponse> getStudentFee(@RequestBody DashboardRequest request)
  {
    return dashboardService.listStudentFee(request);
  }

  @PostMapping("/dash/user")
  @Secured(DASHBOARD_STATS)
  public List<UserResponse> getUserList(@RequestBody DashboardRequest request)
  {
    return dashboardService.listUser(request);
  }

  @PostMapping("/dash/parents")
  @Secured(DASHBOARD_STATS)
  public List<ParentSummaryResponse> getParentList(@RequestBody DashboardRequest request)
  {
    return dashboardService.listParents(request);
  }

  @PostMapping("/dash/followupreport")
  @Secured(DASHBOARD_STATS)
  public List<FollowUpReportResponse> getFollowupReport(@RequestBody DashboardRequest request)
  {
    return dashboardService.followupReport(request);
  }

  // ----------------------------------------shubham ----------------------------------------------------------
  @PostMapping("/dash/newjoinings")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewJoinings> getNewJoinings(@RequestBody DashboardRequest request)
  {
    List<StaffNewJoinings> list= dashboardService.getNewJoinigList(request);
    return list;
  }
  @PostMapping("/dash/newleavings")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewLeavings> getNewLEavings(@RequestBody DashboardRequest request)
  {
    return dashboardService.getNewLeavingsList(request);
  }
  @PostMapping("/dash/recruitmentHeadCountList")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewLeavings> getRecruitmentHeadCountList(@RequestBody DashboardRequest request)
  {
    return dashboardService.getRecruitmentHeadCountList(request);
  }


  ////////Avneet

  @PostMapping("dash/presentStaff")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewJoinings> presentStaff(@RequestBody DashboardRequest request){
    return dashboardService.presentStaff(request).stream().map(StaffNewJoinings::new).collect(Collectors.toList());
  }

  @PostMapping("dash/absentStaff")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewJoinings> absentStaff(@RequestBody DashboardRequest request){
    return dashboardService.absentStaff(request).stream().map(StaffNewJoinings::new).collect(Collectors.toList());
  }

  @PostMapping("dash/onLeaveStaff")
  @Secured(DASHBOARD_STATS)
  public List<StaffNewJoinings> onLeaveStaff(@RequestBody DashboardRequest request){
    return dashboardService.onLeaveStaff(request).stream().map(StaffNewJoinings::new).collect(Collectors.toList());
  }
}
