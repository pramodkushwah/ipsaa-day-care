package com.synlabs.ipsaa.service;

import com.google.common.eventbus.EventBus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.*;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.util.CollectionUtils;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceFilterRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeLeaveRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeLeaveSummaryResponse;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by sushil on 03-04-2018.
 */
@Service
public class StaffLeaveService extends BaseService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeSalaryRepository employeeSalaryRepository;

  @Autowired
  private EmployeeLeaveRepository employeeLeaveRepository;

  @Autowired
  private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository;

  @Autowired
  private EmployeeAttendanceRepository attendanceRepository;

  @Autowired
  private EventBus eventBus;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private CenterRepository centerRepository;

  public void singleDayLeave(String eid, Boolean halfLeave, LeaveType leaveType, Date date) {
    leaveType = leaveType == null ? LeaveType.CASUAL : leaveType;
    halfLeave = halfLeave == null ? false : halfLeave;
    LocalDate localDate = date == null ? LocalDate.now() : LocalDate.fromDateFields(date);

    Employee currentEmployee = getEmployee();
    if (currentEmployee == null) {
      throw new ValidationException("Current user is not authorised to mark leave for any center!");
    }
    Employee employee = StringUtils.isEmpty(eid) ? currentEmployee : employeeRepository.findByEid(eid);
    if (employee == null) {
      throw new ValidationException(String.format("Cannot locate Employee[eid = %s]", eid));
    }

    if (!halfLeave && attendanceRepository.countByEmployeeAndAttendanceDate(employee, localDate.toDate()) > 0) {
      throw new ValidationException("This employee is marked present");
    }

    EmployeeLeave employeeLeave = employeeLeaveRepository.findByEmployeeAndDate(employee, localDate.toDate());
    if (employeeLeave == null) {
      employeeLeave = new EmployeeLeave();
      employeeLeave.setDate(localDate.toDate());
      employeeLeave.setEmployee(employee);
      employeeLeave.setLeaveStatus(LeaveStatus.Approved);
      employeeLeave.setLeaveType(leaveType);
      employeeLeave.setReason("CASUAL LEAVE");
      employeeLeave.setHalfLeave(halfLeave);
      employeeLeaveRepository.saveAndFlush(employeeLeave);
      eventBus.post(employeeLeave);
    } else {
      if (employeeLeave.getLeaveStatus() == LeaveStatus.Approved) {
        //update leavesummary before delete leave
        localDate = LocalDate.fromDateFields(employeeLeave.getDate());
        int month = localDate.monthOfYear().get();
        int year = localDate.getYear();
        halfLeave = isHalfDayLeave(employeeLeave);
        leaveType = employeeLeave.getLeaveType();
        EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository
                .findByLeaveTypeAndMonthAndYearAndEmployeeId(leaveType, month, year, employee.getId());
        if (leaveSummary != null) {
          BigDecimal count = leaveSummary.getCount();
          count = count.subtract(halfLeave ? BigDecimalUtils.HALF : BigDecimalUtils.ONE);
          leaveSummary.setCount(count);
          employeeLeaveSummaryRepository.saveAndFlush(leaveSummary);
        }
      }

      employeeLeaveRepository.delete(employeeLeave);
    }
  }

  public boolean approveLeave(Long id) {
    EmployeeLeave leave = employeeLeaveRepository.findOne(id);
    if (leave == null) {
      throw new ValidationException(String.format("Cannot locate Leave[id = %s]", id));
    }

    Employee currentEmployee = getEmployee();
    if (currentEmployee == null) {
      throw new ValidationException("Current user is not authorised to approve leave for any center!");
    }

    Employee employee = leave.getEmployee();
    if (!isReportingManager(currentEmployee, employee)) {
      throw new ValidationException("Current user is not authorised to reject leave.");
    }

    leave.setLeaveStatus(LeaveStatus.Approved);
    employeeLeaveRepository.saveAndFlush(leave);

    //update Leave summary
    LocalDate localDate = LocalDate.fromDateFields(leave.getDate());
    int month = localDate.monthOfYear().get();
    int year = localDate.getYear();
    Boolean halfLeave = isHalfDayLeave(leave);
    LeaveType leaveType = leave.getLeaveType();
    EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository
            .findByLeaveTypeAndMonthAndYearAndEmployeeId(leaveType, month, year, employee.getId());
    if (leaveSummary == null) {
      leaveSummary = new EmployeeLeaveSummary();
      leaveSummary.setEmployee(employee);
      leaveSummary.setLeaveType(leaveType);
      leaveSummary.setMonth(month);
      leaveSummary.setYear(year);
      leaveSummary.setCount(BigDecimal.ZERO);
    }
    BigDecimal count = leaveSummary.getCount();
    count = count.add(halfLeave ? BigDecimalUtils.HALF : BigDecimalUtils.ONE);
    leaveSummary.setCount(count);
    employeeLeaveSummaryRepository.saveAndFlush(leaveSummary);
    return true;
  }

  public boolean rejectLeave(Long id) {

    EmployeeLeave leave = employeeLeaveRepository.findOne(id);
    if (leave == null) {
      throw new ValidationException(String.format("Cannot locate Leave[id = %s]", id));
    }

    Employee currentEmployee = getEmployee();
    if (currentEmployee == null) {
      throw new ValidationException("Current user is not authorised to reject leave for any center!");
    }

    Employee employee = leave.getEmployee();
    if (!isReportingManager(currentEmployee, employee)) {
      throw new ValidationException("Current user is not authorised to reject leave.");
    }

    leave.setLeaveStatus(LeaveStatus.Rejected);
    employeeLeaveRepository.saveAndFlush(leave);

    //update Leave summary
    LocalDate localDate = LocalDate.fromDateFields(leave.getDate());
    int month = localDate.monthOfYear().get();
    int year = localDate.getYear();
    Boolean halfLeave = isHalfDayLeave(leave);
    LeaveType leaveType = leave.getLeaveType();
    EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository
            .findByLeaveTypeAndMonthAndYearAndEmployeeId(leaveType, month, year, employee.getId());
    if (leaveSummary == null) {
      leaveSummary = new EmployeeLeaveSummary();
      leaveSummary.setEmployee(employee);
      leaveSummary.setLeaveType(leaveType);
      leaveSummary.setMonth(month);
      leaveSummary.setYear(year);
      leaveSummary.setCount(BigDecimal.ZERO);
    } else {
      BigDecimal count = leaveSummary.getCount();
      count = count.subtract(halfLeave ? BigDecimalUtils.HALF : BigDecimalUtils.ONE);
      leaveSummary.setCount(count);
    }
    employeeLeaveSummaryRepository.saveAndFlush(leaveSummary);

    return true;
  }

  private static final Logger logger = LoggerFactory.getLogger(StaffLeaveService.class);

  public void multiDayLeave(EmployeeLeaveRequest request) {
    LocalDate from = LocalDate.fromDateFields(request.getFromDate());
    LocalDate to = LocalDate.fromDateFields(request.getToDate());
    if (from.getYear() != from.getYear()) {
      throw new ValidationException("Year overlaping at from and to dates");
    }
    request.validateRequest();

//    if (!isHRAdmin(getUser()) && request.getFromDate().before(LocalDate.now().plusDays(1).toDate()))
//    {
//      throw new ValidationException("Option available on dates after today");
//    }

    Employee currentEmployee = getEmployee();
    if (currentEmployee == null) {
      throw new ValidationException("Current user is not authorised to mark leave for any center!");
    }
    Employee employee = employeeRepository.findByEid(request.getEid());
    if (employee == null) {
      throw new ValidationException(String.format("Cannot locate Employee[EID=%s]", request.getEid()));
    }

    if (!isReportingManager(currentEmployee, employee)) {
      throw new ValidationException("Current user is not authorised to apply multiday leaves.");
    }

    Boolean halfLeave = request.getHalfLeave();
    halfLeave = halfLeave == null ? false : halfLeave;

    List<EmployeeLeave> employeeLeaves = employeeLeaveRepository.findByEmployeeAndDateBetween(employee, request.getFromDate(), request.getToDate());
    employeeLeaveRepository.delete(employeeLeaves);
    EmployeeLeave employeeLeave;
    employeeLeaves = new ArrayList<>();
    DateTime fromDate = new DateTime(request.getFromDate());
    DateTime toDate = new DateTime(request.getToDate());
    int noOfDays = Days.daysBetween(fromDate, toDate).getDays();
    for (int i = 0; i <= noOfDays; i++) {
      employeeLeave = new EmployeeLeave();
      employeeLeave.setHalfLeave(halfLeave);
      employeeLeave.setReason(request.getReason());
      employeeLeave.setLeaveType(LeaveType.valueOf(request.getLeaveType()));
      employeeLeave.setLeaveStatus(LeaveStatus.Approved);
      employeeLeave.setDate(fromDate.plusDays(i).toDate());
      employeeLeave.setEmployee(employee);
      employeeLeaves.add(employeeLeave);
      employeeLeave.setHalfLeave(request.getHalfLeave() == null ? false : request.getHalfLeave());
      employeeLeaveRepository.saveAndFlush(employeeLeave);
    }

    //update leave summary
    int year = from.getYear();
    int fromMonth = from.getMonthOfYear();
    int toMonth = to.getMonthOfYear();

    LeaveType leaveType = LeaveType.valueOf(request.getLeaveType());

    for (int month = fromMonth; month <= toMonth; month++) {
      syncLeaveSummary(employee, month, year);
    }

    eventBus.post(employeeLeaves);
  }

  public List<EmployeeLeaveSummary> listSummary(EmployeeAttendanceFilterRequest request) {
    Employee currentEmployee = getEmployee();
    if (currentEmployee == null) {
      throw new ValidationException("Current user is not authorised to get Employee leave summary!");
    }

    Employee employee = employeeRepository.findOne(request.getEmpId());
    if (employee == null) {
      throw new ValidationException(String.format("Cannot locate Employee[id=%s]", mask(request.getEmpId())));
    }

    if (!isReportingManager(currentEmployee, employee) && !currentEmployee.equals(employee)) {
      throw new ValidationException("Current user is not authorised to get Employee leave summary!");
    }

    if (request.getMonth() == null) {
      return employeeLeaveSummaryRepository.findByEmployeeIdAndYear(employee.getId(), request.getYear());
    } else {
      return employeeLeaveSummaryRepository.findByEmployeeIdAndMonthAndYear(employee.getId(), request.getMonth(), request.getYear());
    }
  }

  @Transactional
  public void importLeaveSummary(MultipartFile file) throws IOException {
    XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
    XSSFSheet workSheet = workbook.getSheet("leaves");
    if (workSheet == null) {
      throw new ValidationException("Cannot find workSheet[name = leaves]");
    }

    int row = 1;
    XSSFRow xssfRow = workSheet.getRow(row);
    DataFormatter df = new DataFormatter();
    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
    while (xssfRow != null) {
      int col = 0;
      try {
        String eid = getString(xssfRow.getCell(col++), df);
        Integer year = getInteger(xssfRow.getCell(col++), df);
        if (year == null) {
          throw new ValidationException(String.format("Invalid Year in row-%s", row));
        }
        Integer month = getInteger(xssfRow.getCell(col++), df);
        if (month == null) {
          throw new ValidationException(String.format("Invalid Month in row-%s", row));
        }
        BigDecimal CL = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal SL = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal BL = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal PL = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal ADOPTION = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal MATERNITY = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal HOLIDAY = getBigDecimal(xssfRow.getCell(col++), df);
        BigDecimal UPL = new BigDecimal(evaluateFormula(xssfRow.getCell(col++), evaluator));

        Employee employee = employeeRepository.findByEid(eid);
        if (employee == null) {
          throw new ValidationException(String.format("Cannot locate Employee[eid = %s] in %s", eid, row));
        }

        saveEmployeeLeaveSummary(employee, LeaveType.CASUAL, year, month, CL);
        saveEmployeeLeaveSummary(employee, LeaveType.SICK, year, month, SL);
        saveEmployeeLeaveSummary(employee, LeaveType.BEREAVEMENT, year, month, BL);
        saveEmployeeLeaveSummary(employee, LeaveType.PAID, year, month, PL);
        saveEmployeeLeaveSummary(employee, LeaveType.ADOPTION, year, month, ADOPTION);
        saveEmployeeLeaveSummary(employee, LeaveType.MATERNITY, year, month, MATERNITY);
        saveEmployeeLeaveSummary(employee, LeaveType.UNPAID, year, month, UPL);

      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        throw new ValidationException(String.format("Cannot convert to number col-%s and row-%s", col - 1, row));
      } catch (ValidationException ex) {
        throw ex;
      } catch (Exception ex) {
        ex.printStackTrace();
        throw new ValidationException(String.format("Unknown error while saving col-%s and row-%s", col - 1, row));
      } finally {
        row++;
        xssfRow = workSheet.getRow(row);
      }
    }
  }

  private EmployeeLeaveSummary saveEmployeeLeaveSummary(Employee employee, LeaveType leaveType, int year, int month, BigDecimal count) {
    count = count == null ? BigDecimal.ZERO : count;
    EmployeeLeaveSummary employeeLeaveSummary = employeeLeaveSummaryRepository
            .findByLeaveTypeAndMonthAndYearAndEmployeeId(leaveType, month, year, employee.getId());
    if (employeeLeaveSummary == null) {
      employeeLeaveSummary = new EmployeeLeaveSummary();
      employeeLeaveSummary.setEmployee(employee);
      employeeLeaveSummary.setYear(year);
      employeeLeaveSummary.setMonth(month);
      employeeLeaveSummary.setLeaveType(leaveType);
    }
    employeeLeaveSummary.setCount(count);
    return employeeLeaveSummaryRepository.saveAndFlush(employeeLeaveSummary);
  }

  public List<EmployeeLeave> getEmployeeLeaves(Employee employee, Date from, Date to) {
    return employeeLeaveRepository.findByEmployeeAndDateBetween(employee, from, to);
  }

  @Transactional
  public void syncLeaveSummary(Integer month, Integer year, String centerCode, Boolean employeeActive) {
    Center center = StringUtils.isEmpty(centerCode) ? null : centerRepository.findByCode(centerCode);
    List<Employee> employees;
    if (employeeActive == null) {
      employees = center == null ? employeeRepository.findAll()
              : employeeRepository.findByCostCenter(center);
    } else {
      employees = center == null ? employeeRepository.findByActive(employeeActive)
              : employeeRepository.findByActiveAndCostCenter(employeeActive, center);
    }

    int count = 0;
    if (!CollectionUtils.isEmpty(employees)) {
      for (Employee employee : employees) {
        logger.info(String.format("Syncing Leave summary for employee %s/%s...", ++count, employees.size()));
        if (employeeActive != null &&
                employeeActive != employee.isActive()) {
          continue;
        }
        if (month == null) {
          for (int i = 1; i <= 12; i++) {
            syncLeaveSummary(employee, i, year);
          }
        } else {
          syncLeaveSummary(employee, month, year);
        }
      }
    }
  }

  private long countLeave(Employee employee, boolean half, Date from, Date to, LeaveType type, LeaveStatus status) {
    JPAQuery<Integer> query = new JPAQuery<>(entityManager);
    QEmployeeLeave leave = QEmployeeLeave.employeeLeave;

    query.select(leave).from(leave)
            .where(leave.employee.eq(employee));
    if (type != null) {
      query.where(leave.leaveType.eq(type));
    }
    if (status != null) {
      query.where(leave.leaveStatus.eq(status));
    }
    if (half) {
      query.where(leave.halfLeave.isTrue());
    } else {
      query.where(leave.halfLeave.isNull().or(leave.halfLeave.isFalse()));
    }

    query.where(leave.date.between(from, to));

    return query.fetchCount();
  }

  private void syncLeaveSummary(Employee employee, Integer month, Integer year) {
    DateTime from = new DateTime(year, month, 1, 0, 0, 0);
    DateTime to = new DateTime(year, month, from.dayOfMonth().getMaximumValue(), 23, 59);
    for (LeaveType leaveType : LeaveType.values()) {
      BigDecimal count = BigDecimal.ZERO;

      long halfCount = countLeave(employee, true, from.toDate(), to.toDate(), leaveType, LeaveStatus.Approved);
      long fullCount = countLeave(employee, false, from.toDate(), to.toDate(), leaveType, LeaveStatus.Approved);

      if (fullCount > 0) {
        count = count.add(new BigDecimal(fullCount));
      }
      if (halfCount > 0) {
        count = count.add(new BigDecimal(halfCount / 2));
      }

      EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository
              .findByEmployeeIdAndLeaveTypeAndMonthAndYear(employee.getId(), leaveType, month, year);
      if (count.doubleValue() > 0) {
        if (leaveSummary == null) {
          leaveSummary = new EmployeeLeaveSummary();
          leaveSummary.setEmployee(employee);
          leaveSummary.setYear(year);
          leaveSummary.setMonth(month);
          leaveSummary.setLeaveType(leaveType);
          leaveSummary.setCount(BigDecimal.ZERO);
        }
        leaveSummary.setCount(count);
        employeeLeaveSummaryRepository.saveAndFlush(leaveSummary);
      } else {
        if (leaveSummary != null) {
          employeeLeaveSummaryRepository.delete(leaveSummary);
        }
      }
    }

  }


  public List<EmployeeLeaveSummaryResponse> getLeavesByMonth(int month) {

    List<EmployeeLeaveSummaryResponse> list = new ArrayList<>();
    JPAQuery<EmployeeLeave> query = new JPAQuery<>(entityManager);
    EmployeeLeaveSummaryResponse summaryResponse;
    QEmployeeLeave leave = QEmployeeLeave.employeeLeave;
    List<EmployeeLeave> leaves= new ArrayList<>();
    Map<Employee, BigDecimal> leaveCount = new HashMap<>();

//    if (month == 0)
//      throw new ValidationException("Month is required");

//      QEmployeeLeaveSummary leaveSummary=QEmployeeLeaveSummary.employeeLeaveSummary;
//
//      List<Tuple> fetch=query.select(leaveSummary.employee,leaveSummary.count.sum()).from(leaveSummary)
//              .where(leaveSummary.month.eq(month))
//              .where(leaveSummary.year.eq(LocalDate.now().getYear()))
//              .groupBy(leaveSummary.employee)
//              .fetch();


//
//      for(Tuple row: fetch){
//        summaryResponse= new EmployeeLeaveSummaryResponse();
//        summaryResponse.setName(row.get(leaveSummary.employee).getName());
//        summaryResponse.setCenter(row.get(leaveSummary.employee).getCostCenter().getName());
//        summaryResponse.setEid(row.get(leaveSummary.employee).getEid());
//        summaryResponse.setCount(row.get(leaveSummary.count.sum()));
//        summaryResponse.setMonth(month);
//        System.out.println(row.get(leaveSummary.employee).getName()+ "   "+row.get(leaveSummary.count.sum()));
//        list.add(summaryResponse);
//
//
//      }

     if(month == 0) {
       leaves= query.select(leave).from(leave)
               .where(leave.date.month().between(1,12).and(leave.date.year().eq(LocalDate.now().getYear())))
               .where(leave.employee.active.isTrue())
               .where(leave.leaveStatus.eq(LeaveStatus.Approved))
               .orderBy(leave.employee.id.asc())
               .fetch();

     }
     else{
             leaves = query.select(leave).from(leave)
                  .where(leave.date.month().eq(month).and(leave.date.year().eq(LocalDate.now().getYear())))
                  .where(leave.employee.active.isTrue())
                  .where(leave.leaveStatus.eq(LeaveStatus.Approved))
                  .orderBy(leave.employee.id.asc())
                  .fetch();
     }

    for (EmployeeLeave l : leaves){
      //System.out.println(l.getEmployee().getName()+ " ");

      if (leaveCount.containsKey(l.getEmployee()))

           //leaveCount.put(l.getEmployee(),count(l,leaveCount.get(l)));
            leaveCount.put(l.getEmployee(),count(l,leaveCount.get(l.getEmployee())));
      else
        leaveCount.putIfAbsent(l.getEmployee(),count(l,BigDecimal.ZERO));
    };

      for (Map.Entry<Employee, BigDecimal> entry : leaveCount.entrySet()) {
        summaryResponse = new EmployeeLeaveSummaryResponse();
        summaryResponse.setName(entry.getKey().getName());
        summaryResponse.setCenter(entry.getKey().getCostCenter().getName());
        summaryResponse.setEid(entry.getKey().getEid());
        summaryResponse.setCount(entry.getValue());
        summaryResponse.setMonth(month);
        //System.out.println(summaryResponse.getName() + " " + summaryResponse.getCount());
        list.add(summaryResponse);
      }
    return list;
    }



    public List<EmployeeLeave> employeeLeavesMonthly (String eid,int month){

      if (eid == null)
        throw new ValidationException("Employee id is required!");

      JPAQuery<EmployeeLeave> query = new JPAQuery<>(entityManager);
      QEmployeeLeave employeeLeave = QEmployeeLeave.employeeLeave;

      List<EmployeeLeave> leaves = new ArrayList<>();

      if(month == 0){
        leaves= query.select(employeeLeave).from(employeeLeave)
                .where(employeeLeave.employee.eid.eq(eid).and(employeeLeave.date.month().between(1,12)))
                .where(employeeLeave.date.year().eq(LocalDate.now().getYear()))
                .where(employeeLeave.leaveStatus.eq(LeaveStatus.Approved))
                .orderBy(employeeLeave.date.asc())
                .fetch();
      }else {
        leaves = query.select(employeeLeave).from(employeeLeave)
                .where(employeeLeave.date.month().eq(month).and(employeeLeave.date.year().eq(LocalDate.now().getYear())))
                .where(employeeLeave.employee.eid.eq(eid))
                .where(employeeLeave.leaveStatus.eq(LeaveStatus.Approved))
                .orderBy(employeeLeave.date.asc())
                .fetch();
      }
      return leaves;
    }


 public BigDecimal count (EmployeeLeave leave,BigDecimal count){

   //System.out.println(leave);
    if (leave.getHalfLeave())
      return count.add(new BigDecimal(0.5));
    else
      return count.add(BigDecimal.ONE);

    }
}

