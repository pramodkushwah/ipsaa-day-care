package com.synlabs.ipsaa.service;

import com.itextpdf.text.DocumentException;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.common.SerialNumberSequence;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.view.fee.lockSalaryRequest;
import com.synlabs.ipsaa.view.staff.EmployeePaySlipRequest;
import com.synlabs.ipsaa.view.staff.PaySlipRegenerateRequest;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.synlabs.ipsaa.util.BigDecimalUtils.*;

@Service
public class PaySlipService extends BaseService
{

  @Autowired
  private LegalEntityRepository legalEntityRepository;

  @Autowired
  private StaffAttendanceService attendanceService;

  @Autowired
  private EmployeeAttendanceRepository attendanceRepository;

  @Autowired
  private DocumentService documentService;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private SerialNumberSequenceRepository serialRepository;

  @Autowired
  private EmployeePaySlipRepository employeePaySlipRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeSalaryRepository employeeSalaryRepository;

  private static final Logger logger = LoggerFactory.getLogger(PaySlipService.class);

  public List<EmployeePaySlip> listPayslips(Integer month, Integer year, Long employerId) throws ParseException
  {
    if (year == null)
    {
      throw new ValidationException("Year is required.");
    }
    if (month == null)
    {
      throw new ValidationException("Month is required.");
    }

    LegalEntity legalEntity = legalEntityRepository.findOne(unmask(employerId));
    if (legalEntity == null)
    {
      throw new ValidationException(String.format("Cannot locate Employer[id = %s]", mask(employerId)));
    }

    List<Employee> employees = employeeRepository.findByActiveIsTrueAndEmployerId(legalEntity.getId());
    for (Employee emp : employees)
    {

      EmployeePaySlip employeePaySlip = employeePaySlipRepository.findOneByEmployeeAndMonthAndYear(emp, month, year);
      if (employeePaySlip == null)
      {
        EmployeeSalary employeeSalary = employeeSalaryRepository.findByEmployee(emp);
        if (employeeSalary != null)
        {
          if (employeeSalary.getCtc() == null)
          {
            continue;
          }
          generateNewPayslip(emp, employeeSalary, year, month);
        }
      }
    }
    return employeePaySlipRepository.findByEmployerIdAndMonthAndYear(legalEntity.getId(), month, year);
  }

  private EmployeePaySlip generateNewPayslip(Employee employee, EmployeeSalary salary, int year, int month) throws ParseException
  {
    EmployeePaySlip payslip = new EmployeePaySlip();
    payslip.setSerial(generatePaySlipSerial(salary.getEmployee().getCostCenter()));
    payslip.setEmployee(employee);
    payslip.setEmployer(employee.getEmployer());
    payslip.setCenter(employee.getCostCenter());
    payslip.setMonth(month);
    payslip.setYear(year);
    // shubham
    if(payslip.getPresents()==null ){
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.MONTH, month-1);// o to 11
      cal.set(Calendar.YEAR, year);
      int totalDays=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      payslip.setPresents(new BigDecimal(totalDays));
    }
    payslip = calculatePayslip(employee, salary, year, month, payslip);
    employeePaySlipRepository.saveAndFlush(payslip);
    return payslip;
  }
  @Transactional
  public boolean lockSalary(EmployeePaySlipRequest request){
    EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());
    if (paySlip == null)
    {
      throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
    }
    if (paySlip.isLock())
    {
      throw new ValidationException(String.format("salary is already locked", mask(request.getId())));
    }
    paySlip.setLock(true);
    employeePaySlipRepository.saveAndFlush(paySlip);
    return true;
  }
  @Transactional
  public EmployeePaySlip reGeneratePaySlip(EmployeePaySlipRequest request) throws IOException, DocumentException, ParseException
  {
    EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());
    if (paySlip == null)
    {
      throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
    }

    Employee employee = paySlip.getEmployee();
    EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
    if (salary !=null  && paySlip.isLock())
    {
      throw new ValidationException(String.format("Cannot change locked salary", mask(request.getId())));
    }

    Integer year = paySlip.getYear();
    Integer month = paySlip.getMonth();
    paySlip.setComment(request.getComment());
    // shubham
     if(request.getPresents()!=null){
      paySlip.setPresents(request.getPresents());
     }
    else if(paySlip.getPresents()==null && request.getPresents()==null){
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.MONTH, month-1);// o to 11
      cal.set(Calendar.YEAR, year);
      int totalDays=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      paySlip.setPresents(new BigDecimal(totalDays));
    }
    paySlip.setOtherAllowances(request.getOtherAllowances() == null ? ZERO : request.getOtherAllowances());
    paySlip.setOtherDeductions(request.getOtherDeductions() == null ? ZERO : request.getOtherDeductions());

    paySlip = calculatePayslip(employee, salary, year, month, paySlip);
    employeePaySlipRepository.saveAndFlush(paySlip);
    logger.info(String.format("Regenerated Payslip[eid=%s,month=%s,year=%s]",
                              employee.getEid(),
                              month,
                              year));
    return paySlip;
  }

  private EmployeePaySlip calculatePayslip(Employee employee, EmployeeSalary salary, int year, int month, EmployeePaySlip payslip) throws ParseException
  {
    String autoComment = "";
    //1. count leaves and absents
    Date from = pareDate(String.format("01-%s-%s", month, year), "dd-MM-yyyyy");
    Date to = LocalDate.fromDateFields(from).plusMonths(1).minusDays(1).toDate();
    List<EmployeeAttendance> attendances = attendanceRepository.findByEmployeeAndAttendanceDateBetween(employee, from, to);
    attendances = attendanceService.fillAbsent(from, to, employee, employee.getCostCenter(), attendances, false);
    BigDecimal leaves = ZERO;
    BigDecimal absents = ZERO;
    BigDecimal present = ZERO;
    for (EmployeeAttendance attendance : attendances)
    {
      switch (attendance.getStatus())
      {
        case Present:
          present=present.add(BigDecimal.ONE);
         // System.out.println(present);
          break;
        case Absent:
          absents = absents.add(BigDecimal.ONE);
          break;
        case Leave:
          if (attendance.getLeaveType() == LeaveType.UNPAID || attendance.getLeaveStatus() != LeaveStatus.Approved)
          {
            Boolean halfLeave = attendance.getHalfLeave() == null ? false : attendance.getHalfLeave();
            leaves = leaves.add(halfLeave ? HALF : ONE);

            if (halfLeave && attendance.getCheckin() == null)
            {
              absents = absents.add(HALF);
            }
          }
          break;
      }
    }

    //2. deduct unpaid leaves and absents and append autoComment
//    BigDecimal days = new BigDecimal("31");
    BigDecimal totalDays = new BigDecimal(Days.daysBetween(LocalDate.fromDateFields(from), LocalDate.fromDateFields(to).plusDays(1)).getDays());
    // shubham get no of months
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.MONTH, month-1);// o to 11
    cal.set(Calendar.YEAR, year);
    totalDays=new BigDecimal(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
   // System.out.println(totalDays);

    BigDecimal totalAbsents = absents.add(leaves);
    BigDecimal presents = totalDays.subtract(totalAbsents);
    autoComment = absents.doubleValue() > 0 ? autoComment + String.format("Absents : %s\n", absents) : autoComment;
    autoComment = presents.doubleValue() > 0 ? autoComment + String.format("Present : %s\n", presents) : autoComment;
    autoComment = leaves.doubleValue() > 0 ? autoComment + String.format("Leaves : %s\n", leaves) : autoComment;
    payslip.setAutoComment(autoComment);
    // shubham
    if(payslip.getPresents()!=null){
      payslip.updateV2(salary, totalDays,payslip.getPresents());
    }else
      payslip.updateV2(salary, totalDays,presents);
    payslip.roundOff();
    return payslip;
  }
  // update by shubham
  @Transactional
  public EmployeePaySlip updatePaySlip(EmployeePaySlipRequest request) throws IOException, DocumentException
  {
    EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());
    if (paySlip == null)
    {
      throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
    }
    if(request.getPresents()!=null){
      paySlip.setPresents(request.getPresents());
    }
    paySlip.setComment(request.getComment());
    if(request.getOtherAllowances() == null)
      request.setOtherAllowances(ZERO);
    if(request.getOtherDeductions() == null)
      request.setOtherDeductions(ZERO);

    paySlip.setTotalEarning(paySlip.getTotalEarning().subtract(paySlip.getOtherAllowances().add(request.getOtherAllowances())));
    paySlip.setOtherAllowances(request.getOtherAllowances());

    paySlip.setTotalDeduction(paySlip.getTotalEarning().subtract(paySlip.getTotalDeduction().add(request.getOtherDeductions())));
    paySlip.setOtherDeductions(request.getOtherDeductions());
    BigDecimal oldRatio=paySlip.getPresents().divide(paySlip.getTotalDays());
    BigDecimal newRatio=request.getPresents().divide(paySlip.getTotalDays());

    paySlip.setCtc(paySlip.getCtc().divide(oldRatio).multiply(newRatio));
    paySlip.setBasic(paySlip.getBasic().divide(oldRatio).multiply(newRatio));
    paySlip.setHra(paySlip.getHra().divide(oldRatio).multiply(newRatio));
    paySlip.setConveyance(paySlip.getConveyance().divide(oldRatio).multiply(newRatio));
    paySlip.setBonus(paySlip.getBonus().divide(oldRatio).multiply(newRatio));
    paySlip.setSpecial(paySlip.getSpecial().divide(oldRatio).multiply(newRatio));
    paySlip.setExtraMonthlyAllowance(paySlip.getExtraMonthlyAllowance().divide(oldRatio).multiply(newRatio));
    //paySlip.update();
    employeePaySlipRepository.saveAndFlush(paySlip);
    return paySlip;
  }


  public InputStream generatePayslipPdf(Long id) throws IOException, DocumentException
  {
    if (id == null)
    {
      throw new ValidationException("paySlip id is required.");
    }
    EmployeePaySlip paySlip = employeePaySlipRepository.findOne(id);
    if (paySlip == null)
    {
      throw new ValidationException(String.format("Cannot locate paySlip[id = %s]", mask(id)));
    }

    if (paySlip.getFile() == null)
    {
      String fileName = documentService.generatePayslipPdf(paySlip);
      return fileStore.getStream("PAYSLIP", fileName);
    }
    else
    {
      return fileStore.getStream("PAYSLIP", paySlip.getFile());
    }
  }

  private String generatePaySlipSerial(Center center)
  {
    SerialNumberSequence serial = serialRepository.findOneByCenterCodeAndType(center.getCode(), "PAYSLIP");
    if (serial == null)
    {
      serial = new SerialNumberSequence();
      serial.setCenterCode(center.getCode());
      serial.setType("PAYSLIP");
    }
    long next = serial.getNext();
    serialRepository.saveAndFlush(serial);
    return String.format("%s/%03d", center.getCode(), next);
  }

  public void reGeneratePaySlipAll(PaySlipRegenerateRequest request) throws DocumentException, ParseException, IOException
  {
    request.validate();
    if (request.getLegalEntityId() == null)
    {
      throw new ValidationException("Legal entity is required.");
    }
    LegalEntity one = legalEntityRepository.findOne(request.getLegalEntityId());
    if (one == null)
    {
      throw new ValidationException(String.format("Cannot locate LegalEntity[id=%s]", mask(request.getLegalEntityId())));
    }
    List<EmployeePaySlip> payslips = employeePaySlipRepository.findByEmployerIdAndMonthAndYear(one.getId(), request.getMonth(), request.getYear());

    logger.info(String.format("Regenerating all payslip for [legal=%s,month=%s,year=%s,count=%s]",
                              one.getName(), request.getMonth(), request.getYear(), payslips.size()));
    for (EmployeePaySlip payslip : payslips)
    {
      EmployeePaySlipRequest paySlipRequest = new EmployeePaySlipRequest();
      paySlipRequest.setId(mask(payslip.getId()));
      paySlipRequest.setOtherDeductions(payslip.getOtherDeductions());
      paySlipRequest.setOtherAllowances(payslip.getOtherAllowances());
      reGeneratePaySlip(paySlipRequest);
    }
  }
}
