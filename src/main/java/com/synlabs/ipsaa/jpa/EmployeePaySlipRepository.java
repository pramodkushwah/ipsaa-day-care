package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Rakesh on 06-04-2018.
 */
public interface EmployeePaySlipRepository extends JpaRepository<EmployeePaySlip, Long>
{

  EmployeePaySlip findOneByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

  List<EmployeePaySlip> findByEmployerIdAndMonthAndYear(Long employerId, Integer month, Integer year);
  //------------------------------shubham-----------------------------------------------------------

  EmployeePaySlip findOneByEmployeeAndMonthAndYearAndEmployerCode(Employee employee, Integer month, Integer year,String code);
}
