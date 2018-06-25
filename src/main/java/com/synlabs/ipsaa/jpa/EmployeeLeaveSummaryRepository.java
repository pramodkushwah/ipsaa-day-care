package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.staff.EmployeeLeaveSummary;
import com.synlabs.ipsaa.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ttn on 26/6/17.
 */
public interface EmployeeLeaveSummaryRepository extends JpaRepository<EmployeeLeaveSummary, Long>
{
  List<EmployeeLeaveSummary> findByEmployeeIdAndMonthAndYear(Long eid, int month, int year);

  EmployeeLeaveSummary findByEmployeeIdAndLeaveTypeAndMonthAndYear(Long eid, LeaveType leaveType, int month, int year);

  List<EmployeeLeaveSummary> findByEmployeeIdAndYear(Long eid, int year);

  EmployeeLeaveSummary findByLeaveTypeAndMonthAndYearAndEmployeeId(LeaveType leaveType, int month, int year, Long eid);

}
