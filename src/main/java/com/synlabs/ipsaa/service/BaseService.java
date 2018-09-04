package com.synlabs.ipsaa.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.auth.JwtAuthenticationToken;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.CurrentUser;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.entity.staff.QEmployee;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.EmployeeRepository;
import com.synlabs.ipsaa.jpa.UserRepository;
import com.synlabs.ipsaa.util.LongObfuscator;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

abstract public class BaseService
{
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EntityManager entityManager;

  public final static Splitter commaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();
  public final static Splitter colonSplitter = Splitter.on(':').omitEmptyStrings().trimResults();
  public final static Splitter pipeSplitter  = Splitter.on('|').omitEmptyStrings().trimResults();
  public final static Splitter ampSplitter   = Splitter.on('&').omitEmptyStrings().trimResults();
  public final static Splitter equalSplitter = Splitter.on('=').omitEmptyStrings().trimResults();

  protected final Joiner pipeJoiner = Joiner.on('|');

  public User getUser()
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated())
    {
      return null;
    }

    if (authentication.getPrincipal() instanceof CurrentUser)
    {
      return ((CurrentUser) authentication.getPrincipal()).getUser();
    }
    return null;
  }

  public void markStale()
  {
    //we need to refresh the auth token
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken)
    {
      JwtAuthenticationToken mytoken = (JwtAuthenticationToken) authentication;
      mytoken.markStale();
    }
  }

  public boolean hasCenter(String code)
  {
    for (Center center : getUserCenters())
    {
      if (center.getCode().equals(code))
      {
        return true;
      }
    }
    return false;
  }

  public Center hasCenter(Long id)
  {
    for (Center center : getUserCenters())
    {
      if (center.getId().equals(id))
      {
        return center;
      }
    }
    return null;
  }

  protected void createStyle(SXSSFWorkbook workbook)
  {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setBorderTop(CellStyle.BORDER_THIN);
    cellStyle.setBorderRight(CellStyle.BORDER_THIN);
    cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
    cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
  }

  public Date pareDate(String date) throws ParseException
  {
    return pareDate(date, Request.dateFormat);
  }

  public Date pareDate(String date, String format) throws ParseException
  {
    if (date == null)
    {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.parse(date);
  }

  public static BigInteger getNumericUUID()
  {
    UUID randomUUID = UUID.randomUUID();
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(randomUUID.getMostSignificantBits());
    bb.putLong(randomUUID.getLeastSignificantBits());
    return new BigInteger(1, bb.array());
  }

  public String toFormattedDate(Date date, String format)
  {
    if (date == null)
    {
      return "";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }

  public static Long mask(final Long number)
  {
    return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
  }

  public static Long unmask(final Long number)
  {
    return number != null ? LongObfuscator.INSTANCE.unobfuscate(number) : null;
  }

  public String toFormattedDate(Date date)
  {
    return toFormattedDate(date, Request.dateFormat);
  }

  public static String formatDate(Date date)
  {
    return date == null ? "" : new SimpleDateFormat(Request.dateFormat).format(date);
  }

  public User getFreshUser()
  {
    return userRepository.findOne(getUser().getId());
  }

  public List<Center> getUserCenters()
  {
    return userRepository.findOne(getUser().getId())
                         .getCenters()
                         .stream()
                         .filter(c -> c.isActive())
                         .collect(Collectors.toList());
  }

  public boolean isReportingManager(Employee currentEmployee, Employee employee)
  {
    User user = userRepository.findByEmployee(currentEmployee);
    if (user != null && isHRAdmin(user))
    {
      return true;
    }
    if (currentEmployee == null)
    {
      throw new ValidationException("Current user is not authorised to report for any center!");
    }
    if (employee == null)
    {
      return false;
    }
    Set<Employee> reportingManagers = getReportingManagers(employee);

    return reportingManagers.contains(currentEmployee);
  }

  private Set<Employee> getReportingManagers(Employee employee)
  {
    Set<Employee> reportingManagers = new HashSet<>();
    Employee reportingManager = employee.getReportingManager();
    while (reportingManager != null
        && reportingManagers.add(reportingManager))
    {
      reportingManager = reportingManager.getReportingManager();
    }
    return reportingManagers;
  }

  public BigDecimal getBigDecimal(XSSFCell cell, DataFormatter df)
  {
    String string = getString(cell, df);
    if (StringUtils.isEmpty(string))
    {
      return null;
    }
    return new BigDecimal(string);
  }

  public String getString(XSSFCell cell, DataFormatter df)
  {
    if (df == null)
    {
      df = new DataFormatter();
    }
    return df.formatCellValue(cell);
  }

  public Integer getInteger(XSSFCell cell, DataFormatter df) throws NumberFormatException
  {
    String s = getString(cell, df);
    return StringUtils.isEmpty(s) ? null : Integer.parseInt(s);
  }

  public Double getDouble(XSSFCell cell, DataFormatter df) throws NumberFormatException
  {
    String s = getString(cell, df);
    return StringUtils.isEmpty(s) ? null : Double.parseDouble(s);
  }

  public Double evaluateFormula(XSSFCell cell, FormulaEvaluator evaluator)
  {
    if (cell.getCellType() == XSSFCell.CELL_TYPE_FORMULA)
    {
      return cell.getNumericCellValue();
    }
    else
    {
      return getDouble(cell, null);
    }
  }

  public Set<Employee> getReportingEmployees()
  {
    Employee employee = getEmployee();
    if (employee == null)
    {
      throw new ValidationException("User is not employee.");
    }
    Set<Employee> employees = new HashSet<>();

    User user = getUser();
    if (isHRAdmin(user))
    {
      List<Employee> list = employeeRepository.findByActiveTrueAndCostCenterIn(getUserCenters());
      return new HashSet<>(list);
    }

    //getting all employees of first level in hierarchy
    List<Employee> list = employeeRepository.findByActiveTrueAndReportingManager(employee);
    if (CollectionUtils.isEmpty(list))
    {
      return employees;
    }
    employees.addAll(list);

    while (true)
    {
      list = employeeRepository.findByActiveTrueAndReportingManagerIn(list);
      if (employees.containsAll(list))
      {
        break;
      }
      employees.addAll(list);
    }
    return employees;
  }

  public Employee getEmployee()
  {
    return getFreshUser().getEmployee();
  }

  public List<Employee> getEmployees()
  {
    List<Center> centers = getUserCenters();
    JPAQuery<Employee> query = new JPAQuery<>(entityManager);
    QEmployee emp = QEmployee.employee;
    query.select(emp).from(emp)
         .where(emp.active.isTrue())
         .where(emp.costCenter.in(centers));
    return query.fetch();
  }

  public List<Employee> getEmployees(Center center)
  {
    JPAQuery<Employee> query = new JPAQuery<>(entityManager);
    QEmployee emp = QEmployee.employee;
    query.select(emp).from(emp)
         .where(emp.active.isTrue())
         .where(emp.costCenter.eq(center));
    return query.fetch();
  }

  public boolean isHalfDayLeave(EmployeeLeave leave)
  {
    return leave.getHalfLeave() == null ? false : leave.getHalfLeave();
  }

  public boolean isHalfDayLeave(EmployeeAttendance attendance)
  {
    return attendance.getHalfLeave() == null ? false : attendance.getHalfLeave();
  }

  public boolean isHRAdmin(User user)
  {
    return user.hasPrivilege("HR_ADMIN");
  }

}
