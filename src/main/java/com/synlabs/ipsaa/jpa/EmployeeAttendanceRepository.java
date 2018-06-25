package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long>
{
  int countByEmployeeAndAttendanceDate(Employee employee, Date date);

  EmployeeAttendance findByEmployeeAndAttendanceDate(Employee employee, Date date);

  List<EmployeeAttendance> findByEmployeeAndAttendanceDateBetween(Employee employee, Date from,Date to);

  List<EmployeeAttendance> findByCenterAndAttendanceDateBetween(Center center, Date from, Date to);

  EmployeeAttendance findOneByAttendanceDateAndEmployee(Date date,Employee employee);
}
