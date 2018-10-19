package com.synlabs.ipsaa.service;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.City;
import com.synlabs.ipsaa.entity.center.State;
import com.synlabs.ipsaa.entity.center.StateTax;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeProfile;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.view.center.StateTaxRequest;
import com.synlabs.ipsaa.view.staff.EmployeeSalaryFilterRequest;
import com.synlabs.ipsaa.view.student.EmployeeSalaryRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ttn on 18/5/17.
 */

@Service
public class EmployeeService extends BaseService {

  @Autowired
  private EmployeeSalaryRepository employeeSalaryRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private StateRepository stateRepository;

  @Autowired
  private StateTaxRepository taxRepository;

  @Autowired
  private EmployeeProfileRepository profileRepository;

  public Page<EmployeeSalary> list(EmployeeSalaryFilterRequest request) {
    if ((StringUtils.isEmpty(request.getCenterCode()) || request.getCenterCode().equalsIgnoreCase("ALL"))) {
      return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrue(request.getPageable());
    }
    return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterActiveTrueAndEmployeeCostCenterCode(request.getCenterCode(), request.getPageable());
  }

  public EmployeeSalary save(EmployeeSalaryRequest request) {
    return employeeSalaryRepository.saveAndFlush(validateRequest(request));
  }

  private EmployeeSalary validateRequest(EmployeeSalaryRequest request) {

    if (request.getBasic() == null) {
      throw new ValidationException("Salary basic component missing");
    }
    if (request.getBonus() == null) {
      throw new ValidationException("Salary bonus component missing");
    }
    if (request.getConveyance() == null) {
      throw new ValidationException("Salary conveyance component missing");
    }
   /* if (request.getEntertainment() == null)
    {
      throw new ValidationException("Salary entertainment component missing");
    }*/
    if (request.getHra() == null) {
      throw new ValidationException("Salary HRA component missing");
    }
//    if (request.getMedical() == null)
//    {
//      throw new ValidationException("Salary medical component missing");
//    }
//    if (request.getArrears() == null)
//    {
//      throw new ValidationException("Salary arrears component missing");
//    }
//    if (request.getShoes() == null)
//    {
//      throw new ValidationException("Salary shoes component missing");
//    }
    if (request.getSpecial() == null) {
      throw new ValidationException("Salary special component missing");
    }
//    if (request.getTiffin() == null)
//    {
//      throw new ValidationException("Salary tiffin component missing");
//    }
//    if (request.getUniform() == null)
//    {
//      throw new ValidationException("Salary uniform component missing");
//    }
//    if (request.getWashing() == null)
//    {
//      throw new ValidationException("Salary washing component missing");
//    }
    if (request.isEsid() && request.getEsi() == null) {
      throw new ValidationException("Salary ESI component missing");
    }
    if (request.isPfd() && request.getPfe() == null) {
      throw new ValidationException("Salary Employee Pf component missing");
    }
    if (request.isPfd() && request.getPfr() == null) {
      throw new ValidationException("Salary Employer Pf component missing");
    }
    if (request.getTds() == null) {
      throw new ValidationException("Salary TDS component missing");
    }
    if (request.getAdvance() == null) {
      throw new ValidationException("Salary advance component missing");
    }
    if (request.getRetention() == null) {
      throw new ValidationException("Salary retention component missing");
    }
    if ((StringUtils.isEmpty(request.getEid()))) {
      throw new ValidationException("Employee not found for update");
    }
    Employee employee = employeeRepository.findByEid(request.getEid());
    if (employee == null) {
      throw new ValidationException("Employee not found for update");
    }

    EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
    salary = request.toEntity(salary);
    salary.setEmployee(employee);
    return salary;
  }

  public EmployeeSalary getSalary(EmployeeSalaryRequest request) {
    if (StringUtils.isEmpty(request.getEid())) {
      throw new ValidationException("eid is null or empty");
    }
    Employee employee = employeeRepository.findByEid(request.getEid());
    if (employee == null) {
      throw new ValidationException("Employee not found with eid " + request.getEid());
    }
    Center costCenter = employee.getCostCenter();
    List<Center> centers = getUserCenters();
    if (!centers.contains(costCenter)) {
      throw new ValidationException(String.format("Employee[%s] is not in user's center list."));
    }
    EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
    if (salary == null) {
      throw new NotFoundException("Salary not found");
    }
    return salary;
  }

  public List<EmployeeSalary> list() {
    return employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterIn(getUserCenters());
  }

  public List<Employee> findEmployeeByCenter(Center Center) {
    return employeeRepository.findByCostCenter(Center);
  }


  public BigDecimal calculatePfTax(StateTaxRequest request) {

    String eid = request.getEid();
    BigDecimal gross = request.getGrossSalary();
    BigDecimal upper = new BigDecimal(10000);
    BigDecimal professionalTax = new BigDecimal(0);
    Boolean istrue;

    Employee employee = employeeRepository.findByEid(eid);
    EmployeeProfile profile = employee.getProfile();
    Gender gender = profile.getGender();

    String pstate = profile.getpState();
    State state = stateRepository.findOneByName(pstate);

    if (state == null) {
      throw new ValidationException("Pstate is missing!!!");
    }

    List<StateTax> taxes = taxRepository.findByStateId(state.getId());
    if (taxes == null || taxes.isEmpty()) {
      professionalTax = BigDecimal.ZERO;
    }
    if (gender == Gender.Female && pstate.equals("MAHARASHTRA") && BigDecimalUtils.lessThan(upper, gross)) {
      System.out.println("Female Employees with salary under 10K are exempted from professional tax");
      professionalTax = BigDecimal.ZERO;

    } else {

      for (StateTax t : taxes) {

        istrue = BigDecimalUtils.inBetween(t.getMax(), t.getMin(), gross);
        if (istrue) {
          professionalTax = t.getProfessionalTax();
          break;
        }
      }
    }

    return professionalTax;
  }

//  @Transactional
//  public void update() {
//    List<Center> centers= centerRepository.findAll();
//    for(Center c: centers){
//      String citY= c.getAddress().getCity();
//      City C=cityRepository.findOneByName(citY);
//      State sTATE=C.getState();
//      String stateName= sTATE.getName();
//      c.getAddress().setState(stateName);
//
//      centerRepository.saveAndFlush(c);
//
//    }
//    List<Employee> emp = employeeRepository.findByActiveIsTrue();
//      BigDecimal upper = new BigDecimal(10000);
//      boolean istrue;
//
//   for (Employee e : emp) {
//      EmployeeProfile profile = e.getProfile();
//      String city = e.getCostCenter().getAddress().getCity();
//      City c = cityRepository.findOneByName(city);
//      State state = c.getState();
//      String stateName = state.getName();
//      profile.setpState(stateName);
//      profileRepository.saveAndFlush(profile);
//      employeeRepository.saveAndFlush(e);
//
//
//    }
//    List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.findByEmployeeActiveTrue();
//      System.out.println(employeeSalaries);
//
//
//    for (EmployeeSalary salary : employeeSalaries) {
//      Employee e = salary.getEmployee();
//      String pstate = e.getProfile().getpState();
//      State state = stateRepository.findOneByName(pstate);
//      Gender gender= e.getProfile().getGender();
//
//      List<StateTax> taxes = taxRepository.findByStateId(state.getId());
//      if (taxes == null || taxes.isEmpty()) {
//
//        System.out.println("Professional Tax is null for:" + state.getName());
//        salary.setProfd(false);
//        salary.setProfessionalTax(BigDecimal.ZERO);
//
//      }
//      else{
//
//      if ( gender == Gender.Female && pstate.equals("MAHARASHTRA") && BigDecimalUtils.lessThan(upper,salary.getGrossSalary())) {
//          //System.out.println("Female Employees with salary under 10K are exempted from professional tax");
//          salary.setProfd(true);
//          salary.setProfessionalTax(BigDecimal.ZERO);
//
//      } else {
//        for (StateTax t : taxes) {
//
//          istrue = BigDecimalUtils.inBetween(t.getMax(), t.getMin(), salary.getGrossSalary());
//          if (istrue) {
//            BigDecimal p = t.getProfessionalTax();
//            //System.out.println("professional tax=" + t.getProfessionalTax());
//            salary.setProfd(true);
//            salary.setProfessionalTax(t.getProfessionalTax());
//            break;
//          } //salary.setProfessionalTax(t.getProfessionalTax());
//
//        }
//      }
//      }
//
//
//      System.out.format("Tax for %s and Gross %s is= %s",pstate,salary.getGrossSalary(),salary.getProfessionalTax());
//      employeeSalaryRepository.saveAndFlush(salary);
//
//    }
//  }



    /////////by eid
//    @Transactional
//    public void updateByEid(StateTaxRequest request){
//
//      String eid = request.getEid();
//      String pState = request.getPstate();
//      Employee emp = employeeRepository.findByEid(eid);
//
//      EmployeeProfile profile = emp.getProfile();
//      Gender gender = profile.getGender();
//      State state = stateRepository.findOneByName(pState);
//
//      if (state != null) {
//        profile.setpState(state.getName());
//        profileRepository.saveAndFlush(profile);
//      }
//
//      //profileRepository.saveAndFlush(profile);
//
//      EmployeeSalary salary = employeeSalaryRepository.findByEmployee(emp);
//      BigDecimal upper = new BigDecimal(10000);
//      Boolean istrue;
//      BigDecimal grossSalary = salary.getGrossSalary();
//      String pstate = emp.getProfile().getpState();
//      List<StateTax> taxes = taxRepository.findByStateId(state.getId());
//
//      if (taxes == null || taxes.isEmpty()) {
//
//        System.out.println("Professional Tax is null for:" + state.getName());
//        salary.setProfd(false);
//        salary.setProfessionalTax(BigDecimal.ZERO);
//
//      } else {
//
//        if (gender == Gender.Female && pstate.equals("MAHARASHTRA") && BigDecimalUtils.lessThan(upper, salary.getGrossSalary())) {
//          salary.setProfd(true);
//          salary.setProfessionalTax(BigDecimal.ZERO);
//        } else {
//          for (StateTax t : taxes) {
//
//            istrue = BigDecimalUtils.inBetween(t.getMax(), t.getMin(), salary.getGrossSalary());
//            if (istrue) {
//              BigDecimal p = t.getProfessionalTax();
//              salary.setProfd(true);
//              salary.setProfessionalTax(p);
//              break;
//            }
//          }
//        }
//
//      }
//      System.out.println(String.format("for %s_%s ", emp.getName(), emp.getEid())
//              + "with gross" + " " + grossSalary + "has pft:" + salary.getProfessionalTax());
//      employeeSalaryRepository.saveAndFlush(salary);
//    }
//
//  }
}