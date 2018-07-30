package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Long>
{
  Page<EmployeeSalary> findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrueAndEmployeeCostCenterCode(String code, Pageable pageable);

  Page<EmployeeSalary> findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrue(Pageable pageable);

  EmployeeSalary findByEmployee(Employee employee);

  Page<EmployeeSalary> findByEmployeeActiveTrueAndEmployeeEmployerCode(String employerCode, Pageable pageable);

  List<EmployeeSalary> findByEmployeeActiveTrueAndEmployeeCostCenterIn(List<Center> userCenters);
  //------------------------------------shubham----------------------------------------------------------------
  List<EmployeeSalary> findByEmployeeActiveTrueAndEmployeeCostCenterCode(String code);
  List<EmployeeSalary> findByEmployeeActiveTrue();
}
