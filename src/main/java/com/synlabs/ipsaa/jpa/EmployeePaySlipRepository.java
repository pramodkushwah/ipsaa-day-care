package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.enums.ApprovalStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Rakesh on 06-04-2018.
 */
public interface EmployeePaySlipRepository extends JpaRepository<EmployeePaySlip, Long>
{

  EmployeePaySlip findOneByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);

  List<EmployeePaySlip> findByEmployerIdAndEmployeeApprovalStatusAndMonthAndYear(Long employerId,ApprovalStatus approvalStatus, Integer month, Integer year);
}
