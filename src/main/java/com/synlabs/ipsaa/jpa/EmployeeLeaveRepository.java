package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by ttn on 26/6/17.
 */
public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long>
{
  int countByEmployeeAndDate(Employee employee, Date date);

  EmployeeLeave findByEmployeeAndDate(Employee employee, Date date);

  List<EmployeeLeave> findByEmployeeAndDateBetween(Employee employee, Date from, Date to);

  List<EmployeeLeave> findByEmployeeAndLeaveTypeAndDateBetween(Employee employee, LeaveType leaveType, Date from, Date to);

  int countByLeaveTypeAndDateBetweenAndEmployee(LeaveType leaveType, Date from, Date to, Employee employee);

  ///Avneet

  List<EmployeeLeave> findByEmployeeInAndDateOrderByEmployeeIdAsc(List<Employee> employees,Date date);

  List<EmployeeLeave> findByEmployeeInAndDate(List<Employee> employees,Date date);

  List<EmployeeLeave> findByEmployeeAndDateBetweenAndHalfLeaveIsTrue(Employee employee,Date from,Date to);

}
