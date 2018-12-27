package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.AttendanceStatus;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.EmployeeAttendanceRepository;
import com.synlabs.ipsaa.jpa.EmployeeRepository;
import com.synlabs.ipsaa.view.attendance.AttendancePullRequest;
import com.synlabs.ipsaa.view.attendance.BioAttendance;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

@Service
public class BiometricAttendanceService extends BaseService
{
  @Autowired
  @Qualifier("attendanceDataSource")
  private DataSource attendanceDataSource;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeAttendanceRepository attendanceRepository;

  @Autowired
  private TaskExecutor taskExecutor;

  private static final Logger logger=LoggerFactory.getLogger(BiometricAttendanceService.class);

  public InputStream pullAttendance(AttendancePullRequest request) throws SQLException, ParseException, IOException
  {
    if (request.getFrom() == null)
    {
      throw new ValidationException("From date is required.");
    }
    if (request.getTo() == null)
    {
      throw new ValidationException("From to is required.");
    }
    boolean dryRun = request.getDryRun() == null ? true : request.getDryRun();
    Set<BioAttendance> bioAttendances = pullFromDatabase(request.getFrom());
    List<EmployeeAttendance> employeeAttendances = saveAttendance(bioAttendances, dryRun);
    return export(employeeAttendances);
  }

  private InputStream export(List<EmployeeAttendance> employeeAttendances) throws IOException
  {
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    int rowNumber = 0;
    Sheet attendanceSheet = workbook.createSheet("attendance");
    Row row = attendanceSheet.createRow(rowNumber++);
    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Eid");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Date");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Checkin");
    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Checkout");
    row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Center");
    row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Employee Name");

    for (EmployeeAttendance attendance : employeeAttendances)
    {
      row = attendanceSheet.createRow(rowNumber++);
      Employee employee = attendance.getEmployee();
      row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(attendance.getEmployee().getEid());
      row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(toFormattedDate(attendance.getAttendanceDate(), "yyyy-MM-dd"));
      row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(attendance.getCheckin() == null ? "" : toFormattedDate(attendance.getCheckin(), "HH:mm"));
      row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(attendance.getCheckout() == null ? "" : toFormattedDate(attendance.getCheckout(), "HH:mm"));
      row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(attendance.getCenter().getName());
      row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(employee.getName());
    }

    PipedInputStream pis = new PipedInputStream();
    PipedOutputStream pos = new PipedOutputStream();
    pis.connect(pos);

    Runnable task = () -> {
      try
      {
        workbook.write(pos);
        workbook.dispose();
      }
      catch (Exception e)
      {

      }
    };
    taskExecutor.execute(task);
    return pis;
  }

  public Set<BioAttendance> pullFromDatabase(Date from) throws SQLException
  {
//    if (from.after(to))
//    {
//      Date temp = to;
//      to = from;
//      from = temp;
//    }
    LocalDate fromLocal = new LocalDate(from);
//    LocalDate toLocal = new LocalDate(to);

    Connection connection = null;
    try
    {
      String sql = String.format("select * from %s where LogDateTime > ? and LogDateTime < ?;", "payrol");
      try{connection = attendanceDataSource.getConnection();
      }catch(Exception e){ e.printStackTrace();
        System.out.println(e);}
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setDate(1, new java.sql.Date(fromLocal.toDate().getTime()));
     // stmt.setDate(1, fromLocal.toDate().getTime());
//      stmt.setDate(2, new java.sql.Date(toLocal.toDate().getTime()));
      System.out.println(stmt.toString());
      ResultSet resultSet = stmt.executeQuery();
      logger.info("Attendances picked :"+resultSet.getFetchSize());
      Map<BioAttendance, List<Date>> map = new HashMap<>();
      while (resultSet.next())
      {
        int biometricId = resultSet.getInt("EmpCode");
        Date dateTime = resultSet.getTimestamp("LogDateTime");
        LocalDate localDate = LocalDate.fromDateFields(dateTime);
        map.computeIfAbsent(
            new BioAttendance(biometricId, localDate),
            k -> new ArrayList<>()).add(dateTime);

      }
      logger.info("Attendance to to synced are: "+map.size());
      map.forEach((k, v) -> {
        k.setList(v);
        k.updateClocking();
      });
      logger.info("ID's fetched for updating:"+ map.keySet());
      return map.keySet();
    }
    finally
    {
      if (connection != null)
      {
        connection.close();
      }
    }
  }

  public List<EmployeeAttendance> saveAttendance(Collection<BioAttendance> bioAttendances, boolean dryRun)
  {
    List<EmployeeAttendance> list = new ArrayList<>();
    bioAttendances.forEach(ba -> {
      Employee employee =
          employeeRepository.findOneByBiometricId(ba.getBiometricId());

      if (employee != null)
      {
        EmployeeAttendance attendance = attendanceRepository
            .findOneByAttendanceDateAndEmployee
                (ba.getDate().toDate(), employee);
        if (attendance == null)
        {
          attendance = new EmployeeAttendance();
          attendance.setEmployee(employee);
          attendance.setCenter(employee.getCostCenter());
          attendance.setAttendanceDate(ba.getDate().toDate());
          attendance.setStatus(AttendanceStatus.Present);

          if(attendance.getCheckin() == null)
            attendance.setCheckin(ba.getClockin());

          if(attendance.getCheckout() == null)
          attendance.setCheckout(ba.getClockout());
        }
        else{
          ////update timings
          attendance.setCheckout(ba.getClockout());
        }
//        if (attendance.getCheckin() == null)
//        {
//          System.out.println(ba.getClockin());
//          attendance.setCheckin(ba.getClockin());
//        }
//        if (attendance.getCheckout() == null)
//        {
//          attendance.setCheckout(ba.getClockout());
//        }
        logger.info("Attendance added for employee:"+ attendance.getEmployee()+"( "+ba.getBiometricId()+") of date:"
                +attendance.getAttendanceDate());
        list.add(attendance);
      }
      else{
        logger.info("Employee mot found for biometric:" + ba.getBiometricId());
      }
    });
    if (!dryRun)
    {
      attendanceRepository.save(list);
    }
    return list;
  }
}
