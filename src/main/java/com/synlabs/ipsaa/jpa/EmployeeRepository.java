package com.synlabs.ipsaa.jpa;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.EmployeeType;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, Repository<Employee, Long>, QueryDslPredicateExecutor<Employee>
{

  List<Employee> findByCostCenterIn(List<Center> centers);

  List<Employee> findByActive(Boolean active);

  List<Employee> findByActiveIsTrue();
  List<Employee> findByActiveIsTrueAndProfileDolBeforeOrProfileDol(Date today,Date Etoday);

  List<Employee> findByEmployeeTypeAndActiveIsTrue(EmployeeType employeeType);

  List<Employee> findByActiveIsTrueAndEmployerId(Long employerId);

  Employee findByIdAndCostCenterIn(Long employeeId, List<Center> centers);

  Employee findByEidAndCostCenter(String eid, Center center);

  Employee findByMobileAndCostCenter(String mobile, Center center);

  Employee findByEidAndMobile(String eid, String mobile);

  Employee findByEid(String eid);

  List<Employee> findByActiveTrueAndApprovalStatusAndCostCenter(ApprovalStatus new_approval, Center center);

  Integer countByCostCenterAndApprovalStatus(Center center, ApprovalStatus new_approval);

  List<Employee> findByActiveTrueAndCostCenterIn(List<Center> userCenters);
  // shubham
  List<Employee> findByActiveTrueAndCostCenterInAndEmployerCode(List<Center> userCenters,String employerCode);

  List<Employee> findByActiveTrueAndReportingManager(Employee employee);

  List<Employee> findByActiveTrueAndReportingManagerIn(List<Employee> employees);

  Employee findByActiveIsTrueAndBiometricId(Integer biometricId);

  List<Employee> findByCostCenter(Center center);

  List<Employee> findByActiveAndCostCenter(Boolean active, Center center);

  Employee findOneByBiometricId(Integer biometricId);

  //Avneet
  List<Employee> findByActiveTrueAndCostCenterInOrderByIdAsc(List<Center> centers);

  List<Employee> findByEidIn(List<String> eids);

}
