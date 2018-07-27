package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.view.staff.EmployeeSalaryFilterRequest;
import com.synlabs.ipsaa.view.student.EmployeeSalaryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by ttn on 18/5/17.
 */

@Service
public class EmployeeService extends BaseService
{

  @Autowired
  private EmployeeSalaryRepository employeeSalaryRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  public Page<EmployeeSalary> list(EmployeeSalaryFilterRequest request)
  {
    if ((StringUtils.isEmpty(request.getCenterCode()) || request.getCenterCode().equalsIgnoreCase("ALL")))
    {
      return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrue(request.getPageable());
    }
    return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrueAndEmployeeCostCenterCode(request.getCenterCode(), request.getPageable());
  }
  public EmployeeSalary save(EmployeeSalaryRequest request)
  {
    return employeeSalaryRepository.saveAndFlush(validateRequest(request));
  }

  private EmployeeSalary validateRequest(EmployeeSalaryRequest request)
  {

    if (request.getBasic() == null)
    {
      throw new ValidationException("Salary basic component missing");
    }
    if (request.getBonus() == null)
    {
      throw new ValidationException("Salary bonus component missing");
    }
    if (request.getConveyance() == null)
    {
      throw new ValidationException("Salary conveyance component missing");
    }
    if (request.getEntertainment() == null)
    {
      throw new ValidationException("Salary entertainment component missing");
    }
    if (request.getHra() == null)
    {
      throw new ValidationException("Salary HRA component missing");
    }
    if (request.getMedical() == null)
    {
      throw new ValidationException("Salary medical component missing");
    }
    if (request.getArrears() == null)
    {
      throw new ValidationException("Salary arrears component missing");
    }
    if (request.getShoes() == null)
    {
      throw new ValidationException("Salary shoes component missing");
    }
    if (request.getSpecial() == null)
    {
      throw new ValidationException("Salary special component missing");
    }
    if (request.getTiffin() == null)
    {
      throw new ValidationException("Salary tiffin component missing");
    }
    if (request.getUniform() == null)
    {
      throw new ValidationException("Salary uniform component missing");
    }
    if (request.getWashing() == null)
    {
      throw new ValidationException("Salary washing component missing");
    }
    if (request.isEsid() && request.getEsi() == null)
    {
      throw new ValidationException("Salary ESI component missing");
    }
    if (request.isPfd() && request.getPfe() == null)
    {
      throw new ValidationException("Salary Employee Pf component missing");
    }
    if (request.isPfd() && request.getPfr() == null)
    {
      throw new ValidationException("Salary Employer Pf component missing");
    }
    if (request.getTds() == null)
    {
      throw new ValidationException("Salary TDS component missing");
    }
    if (request.getAdvance() == null)
    {
      throw new ValidationException("Salary advance component missing");
    }
    if (request.getRetention() == null)
    {
      throw new ValidationException("Salary retention component missing");
    }

    if ((StringUtils.isEmpty(request.getEid())))
    {
      throw new ValidationException("Employee not found for update");
    }
    Employee employee = employeeRepository.findByEid(request.getEid());
    if (employee == null)
    {
      throw new ValidationException("Employee not found for update");
    }

    EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
    salary = request.toEntity(salary);
    salary.setEmployee(employee);
    return salary;
  }

  public EmployeeSalary getSalary(EmployeeSalaryRequest request)
  {
    if (StringUtils.isEmpty(request.getEid()))
    {
      throw new ValidationException("eid is null or empty");
    }
    Employee employee = employeeRepository.findByEid(request.getEid());
    if (employee == null)
    {
      throw new ValidationException("Employee not found with eid " + request.getEid());
    }
    Center costCenter = employee.getCostCenter();
    List<Center> centers = getUserCenters();
    if (!centers.contains(costCenter))
    {
      throw new ValidationException(String.format("Employee[%s] is not in user's center list."));
    }
    EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
    if (salary == null)
    {
      throw new NotFoundException("Salary not found");
    }
    return salary;
  }

  public List<EmployeeSalary> list()
  {
    return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterIn(getUserCenters());
  }
  public List<Employee> findEmployeeByCenter(Center Center){
    return employeeRepository.findByCostCenter(Center);
  }
}