package com.synlabs.ipsaa.service;

import com.google.common.eventbus.EventBus;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.enums.AttendanceStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.StudentAttendanceRepository;
import com.synlabs.ipsaa.jpa.StudentRepository;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;
import com.synlabs.ipsaa.view.attendance.StudentAttendanceRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class StudentAttendanceService extends BaseService
{

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private StudentAttendanceRepository attendanceRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private EventBus eventBus;

  @Value("${ipsaa.export.directory}")
  private String exportDir;

  @PostConstruct
  public void init() throws IOException
  {
    File file = new File(exportDir);
    file.mkdirs();
    if (!file.exists())
    {
      throw new IOException("Unable to create export directory.");
    }
  }

  public List<StudentAttendance> list()
  {
    List<Student> students = studentRepository.findByCenterInAndActive(userService.getUserCenters(), true);
    List<StudentAttendance> attendances = new ArrayList<>(students.size());

    for (Student student : students)
    {
      StudentAttendance attendance = attendanceRepository.findByStudentAndAttendanceDate(student, LocalDate.now().toDate());

      if (attendance == null)
      {
        attendance = new StudentAttendance();
        attendance.setCenter(student.getCenter());
        attendance.setStatus(AttendanceStatus.Absent);
        attendance.setStudent(student);
        attendance.setAttendanceDate(DateTime.now().toDate());
      }
      attendances.add(attendance);
    }

    return attendances;
  }

  public void clockin(StudentAttendanceRequest request)
  {

    Student student = studentRepository.findByIdAndCenterIn(request.getStudentId(), userService.getUserCenters());

    if (student == null)
    {
      throw new NotFoundException("Cannot locate student");
    }

    if (attendanceRepository.countByStudentAndAttendanceDate(student, LocalDate.now().toDate()) > 0)
    {
      throw new ValidationException("Student is already marked present");
    }

    StudentAttendance attendance = new StudentAttendance();
    attendance.setCenter(student.getCenter());
    attendance.setCheckin(DateTime.now().toDate());
    attendance.setStatus(AttendanceStatus.Present);
    attendance.setStudent(student);
    attendance.setAttendanceDate(DateTime.now().toDate());
    // shubham
    int extra=countExtra(student,attendance,true);
    attendance.setExtraHours(extra);
    attendanceRepository.saveAndFlush(attendance);
    eventBus.post(attendance);
  }

  public void clockout(StudentAttendanceRequest request)
  {

    Student student = studentRepository.findByIdAndCenterIn(request.getStudentId(), userService.getUserCenters());
    StudentAttendance attendance = attendanceRepository.findByStudentAndAttendanceDate(student, LocalDate.now().toDate());

    if (attendance == null)
    {
      throw new NotFoundException("Cant clock out, Student has not clockedin!");
    }

    if (attendance.getCheckout() != null)
    {
      throw new ValidationException("Student has already clocked out!");
    }

// shubham
    int extra=countExtra(student,attendance,false);
    attendance.setExtraHours(attendance.getExtraHours()+extra);
    attendance.setCheckout(DateTime.now().toDate());
    attendanceRepository.saveAndFlush(attendance);
    eventBus.post(attendance);
  }

  public File attendanceReport(AttendanceReportRequest request) throws IOException
  {
    //1. Finding student attendances
    if (request.getCenterId() == null)
    {
      throw new ValidationException("Center is required.");
    }
    Center center = hasCenter(request.getCenterId());
    if (center == null)
    {
      throw new ValidationException("Unauthorized access to Center.");
    }
    request.setCenterCode(center.getCode());

    Set<Student> students = new HashSet<>(studentRepository.findByActiveTrueAndCenter(center));
    List<StudentAttendance> attendances = attendanceRepository.findByCenterAndAttendanceDateBetweenOrderByStudentAdmissionNumberAsc(center, request.getFrom(), request.getTo());

    //fill absent
    attendances = fillAbsent(request, center, students, attendances);

    Collections.sort(attendances,(o1, o2) -> {
      return o1.getStudent().getAdmissionNumber().compareTo(o2.getStudent().getAdmissionNumber());
    });

    //2. putting attendances in sheet
    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    int rowNumber = 0;
    Sheet attendanceSheet = workbook.createSheet("attendance");
    Row row = attendanceSheet.createRow(rowNumber++);
    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Student Name");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Attendance Date");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Status");
    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
    row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Program Name");
    row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Group Name");
    row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Excepted In");
    row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Excepted Out");
    row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Actual In");
    row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Actual Out");
    row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Time (HH:MM)");

    for (StudentAttendance attendance : attendances)
    {
      row = attendanceSheet.createRow(rowNumber++);

      row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getName());
      row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(toFormattedDate(attendance.getAttendanceDate(),"yyyy-MM-dd EEE"));
      row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStatus().toString());
      row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getCenterName());
      row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getProgramName());
      row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getGroupName());
      row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getExpectedIn() == null ? "" : attendance.getStudent().getExpectedIn().toString());
      row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStudent().getExpectedOut() == null ? "" : attendance.getStudent().getExpectedOut().toString());

      switch (attendance.getStatus()){
        case Present:
          row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(attendance.getCheckin() == null ? "" : attendance.getCheckin().toString());
          row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue(attendance.getCheckout() == null ? "" : attendance.getCheckout().toString());
          if (attendance.getCheckin() != null && attendance.getCheckout() != null)
          {
            long milliSeconds = attendance.getCheckout().getTime() - attendance.getCheckin().getTime();
            long minutes = milliSeconds / (1000 * 60);
            long hours = minutes / 60;
            minutes = minutes % 60;
            row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(String.format("%s:%s", hours, minutes));
          }
          else
          {
            row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("");
          }
          break;
        case Absent:
          row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("A");
          row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("A");
          row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("A");
          break;
      }
    }

    workbook.write(fileOutputStream);
    workbook.dispose();

    return file;
  }

  private List<StudentAttendance> fillAbsent(AttendanceReportRequest request, Center center, Set<Student> students,
                                             List<StudentAttendance> attendances)
  {
//    getting all dates between request.from and request.to excluding sunday
    List<StudentAttendance> result = new ArrayList<>();

    List<Date> dates = new ArrayList<>();
    LocalDate from = LocalDate.fromDateFields(request.getFrom());
    LocalDate to = LocalDate.fromDateFields(request.getTo());
    int days = Days.daysBetween(from, to).getDays() + 1;
    for (int i = 0; i < days; i++)
    {
      LocalDate d = from.withFieldAdded(DurationFieldType.days(), i);
      if (d.getDayOfWeek() != DateTimeConstants.SUNDAY)
      {
        dates.add(d.toDate());
      }
    }

//    put all attendance in map key as date
    Map<Date, List<StudentAttendance>> map = new HashMap<>();
    for (StudentAttendance attendance : attendances)
    {
      map.computeIfAbsent(attendance.getAttendanceDate(), (k) -> {
        return new ArrayList<>();
      }).add(attendance);
    }

    dates.forEach((date) -> {
      List<StudentAttendance> studentAttendances = map.get(date);

      students.forEach((student) -> {
        StudentAttendance attendance = null;
        if (CollectionUtils.isEmpty(studentAttendances))
        {
          // all students are absent
        }
        else
        {
          //from list of attendance
          for (StudentAttendance a : studentAttendances)
          {
            if (student.equals(a.getStudent()))
            {
              attendance = a;
            }
          }
          if (attendance == null)
          {
            //student is absent
            StudentAttendance a = new StudentAttendance();
            a.setCenter(center);
            a.setAttendanceDate(date);
            a.setStudent(student);
            a.setStatus(AttendanceStatus.Absent);
            a.setCheckin(null);
            a.setCheckout(null);
            result.add(a);
          }
          else
          {
            //student is present
            result.add(attendance);
          }
        }
      });

    });
    return result;
  }


  // shubham
  public int countExtra(Student student, StudentAttendance attendance, boolean isCheckin) {
    int extra=0;
    if(isCheckin){
      if(attendance.getCheckin() !=null && student.getExpectedIn()!=null){
        if(attendance.getCheckin().before(student.getExpectedIn())){
          Period p = new Period(new DateTime(attendance.getCheckin()),new DateTime(student.getExpectedIn()));
          int hours = p.getHours();
          extra+=hours;
        }
      }
    }else{
      if(attendance.getCheckout()!=null && student.getExpectedOut()!=null){
        if(attendance.getCheckout().after(student.getExpectedOut())){
          Period p = new Period(new DateTime(student.getExpectedOut()),new DateTime(attendance.getCheckout()));
          int hours = p.getHours();
          extra+=hours;
        }
      }
    }
    return extra;
  }
}
