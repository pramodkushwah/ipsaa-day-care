package com.synlabs.ipsaa.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import com.synlabs.ipsaa.config.Local;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.view.report.excel.StaffAttendanceRegisterReport;
import com.synlabs.ipsaa.view.report.excel.StaffAttendanceReport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.eventbus.EventBus;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.Holiday;
import com.synlabs.ipsaa.entity.center.QHoliday;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.enums.AttendanceStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.util.Utils;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceFilterRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceRequest;
import com.synlabs.ipsaa.view.attendance.HRAdminAttendanceRequest;

@Service
public class StaffAttendanceService extends BaseService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeAttendanceRepository attendanceRepository;

	@Autowired
	private EventBus eventBus;

	@Autowired
	private UserService userService;

	@Autowired
	private HolidayRepository holidayRepository;

	@Autowired
	private EmployeeLeaveRepository employeeLeaveRepository;

	@Autowired
	private EmployeeAttendanceRepository employeeAttendanceRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CenterRepository centerRepository;

	@Value("${ipsaa.export.directory}")
	private String exportDir;

	@Autowired
	private StaffLeaveService staffLeaveService;

	@PostConstruct
	public void init() throws IOException {
		File file = new File(exportDir);
		file.mkdirs();
		if (!file.exists()) {
			throw new IOException("Unable to create export directory.");
		}
	}

	public List<EmployeeAttendance> list() {
		if (getEmployee() == null) {
			throw new ValidationException("Current user is not authorised to get attendance for any center!");
		}

		List<Employee> employees = employeeRepository.findByActiveTrueAndCostCenterIn(userService.getUserCenters());
		List<EmployeeAttendance> attendances = new ArrayList<>();
		List<Holiday> holidays = holidayRepository.findDistinctByCentersInAndHolidayDate(userService.getUserCenters(),
				LocalDate.now().toDate());
		Set<String> set = new HashSet<>();
		for (Holiday holiday : holidays) {
			for (Center center : holiday.getCenters()) {
				set.add(center.getCode());
			}
		}
		for (Employee employee : employees) {
			EmployeeAttendance attendance = attendanceRepository.findByEmployeeAndAttendanceDate(employee,
					LocalDate.now().toDate());
			EmployeeLeave leave = employeeLeaveRepository.findByEmployeeAndDate(employee, LocalDate.now().toDate());
			if (attendance == null) {
				attendance = new EmployeeAttendance();
				attendance.setEmployee(employee);
				attendance.setCenter(employee.getCostCenter());
				attendance.setStatus(AttendanceStatus.Absent);
				attendance.setAttendanceDate(LocalDate.now().toDate());
				if (set.contains(employee.getCostCenter().getCode())) {
					attendance.setCalendarHoliday(true);
				}
			}

			if (leave != null) {
				attendance.setOnLeave(true);
				attendance.setHalfLeave(leave.getHalfLeave());
				attendance.setLeaveStatus(leave.getLeaveStatus());
				attendance.setLeaveId(mask(leave.getId()));
			}
			attendances.add(attendance);
		}
		return attendances;
	}

	public List<EmployeeAttendance> empAttendanceList(EmployeeAttendanceFilterRequest request) {
		Employee currentEmployee = getEmployee();
		if (currentEmployee == null) {
			throw new ValidationException("Current user is not authorised to get attendance for any center!");
		}

		Employee employee = null;// employeeRepository.findOne(request.getEmpId());
		if (request.getEmpId() == null) {
			employee = currentEmployee;
		} else {
			employee = employeeRepository.findOne(request.getEmpId());
			if (employee == null) {
				throw new ValidationException(String.format("Cannot locate Employee[id=%s]", mask(request.getEmpId())));
			}
			if (!isReportingManager(currentEmployee, employee)) {
				throw new ValidationException("Current user is not authorised to get attendance.");
			}
		}

		LocalDate from = new LocalDate(request.getYear(), request.getMonth(), 1);
		LocalDate to = from.dayOfMonth().withMaximumValue();
		List<EmployeeAttendance> attendances = employeeAttendanceRepository
				.findByEmployeeAndAttendanceDateBetween(employee, from.toDate(), to.toDate());
		return fillAbsent(from.toDate(), to.toDate(), employee, employee.getCostCenter(), attendances, true);
	}

	public List<EmployeeAttendance> fillAbsent(Date from, Date to, Employee employee, Center center,
			List<EmployeeAttendance> attendances, boolean includeSunday) {
		// 1. get all dates between from and to
		Set<Date> allDates = Utils.datesBetween(from, to, includeSunday);
		Map<Date, EmployeeAttendance> absentMap = new HashMap<>();
		allDates.forEach(d -> {
			EmployeeAttendance attendance = new EmployeeAttendance();
			attendance.setCenter(center);
			attendance.setEmployee(employee);
			attendance.setAttendanceDate(d);
			if (Utils.isSunday(d)) {
				attendance.setStatus(AttendanceStatus.Holiday);
				attendance.setCalendarHoliday(true);
				attendance.setHolidayName("Sunday");
			} else {
				attendance.setStatus(AttendanceStatus.Absent);
			}
			absentMap.put(d, attendance);
		});

		// 2. put all present in attendance map and remove from absent
		for (EmployeeAttendance attendance : attendances) {
			absentMap.put(attendance.getAttendanceDate(), attendance);
		}

		// 2. find holidays and remove from absents
		JPAQuery<Holiday> query = new JPAQuery<>(entityManager);
		QHoliday holiday = QHoliday.holiday;
		query.select(holiday).from(holiday).where(holiday.centers.any().eq(center))
				.where(holiday.holidayDate.between(from, to)).fetch().forEach(h -> {
					EmployeeAttendance attendance = absentMap.get(h.getHolidayDate());
					if (attendance != null) {
						attendance.setStatus(AttendanceStatus.Holiday);
						attendance.setCalendarHoliday(true);
						attendance.setHolidayName(h.getName());
					}
				});

		// 5. find leaves and remove from absents
		List<EmployeeLeave> employeeLeaves = staffLeaveService.getEmployeeLeaves(employee, from, to);
		employeeLeaves.forEach(l -> {
			EmployeeAttendance attendance = absentMap.get(l.getDate());
			if (attendance != null) {
				attendance.setOnLeave(true);
				attendance.setStatus(AttendanceStatus.Leave);
				attendance.setHalfLeave(l.getHalfLeave());
				attendance.setLeaveType(l.getLeaveType());
				attendance.setLeaveStatus(l.getLeaveStatus());
				attendance.setLeaveId(l.getId());
			}
		});

		List<EmployeeAttendance> allAttendances = new ArrayList<>();
		allAttendances.addAll(absentMap.values());
		return allAttendances;
	}

	public void clockin(EmployeeAttendanceRequest request) {

		if (getEmployee() == null) {
			throw new ValidationException("Current user is not authorised to mark attendance for any center!");
		}

		Employee employee = employeeRepository.findByIdAndCostCenterIn(request.getEmployeeId(),
				userService.getUserCenters());

		if (employee == null) {
			throw new NotFoundException("Cannot locate employee");
		}

		if (attendanceRepository.countByEmployeeAndAttendanceDate(employee, LocalDate.now().toDate()) > 0) {
			throw new ValidationException("Employee is already marked present");
		}

		if (employeeLeaveRepository.countByEmployeeAndDate(employee, LocalDate.now().toDate()) > 0) {
			throw new ValidationException("Employee is on leave today");
		}

		EmployeeAttendance attendance = new EmployeeAttendance();
		attendance.setCenter(employee.getCostCenter());
		attendance.setCheckin(DateTime.now().toDate());
		attendance.setStatus(AttendanceStatus.Present);
		attendance.setEmployee(employee);
		attendance.setAttendanceDate(DateTime.now().toDate());
		attendanceRepository.saveAndFlush(attendance);
		eventBus.post(attendance);
	}

	public void clockout(EmployeeAttendanceRequest request) {
		if (getEmployee() == null) {
			throw new ValidationException("Current user is not authorised to mark attendance for any center!");
		}

		Employee employee = employeeRepository.findByIdAndCostCenterIn(request.getEmployeeId(),
				userService.getUserCenters());
		EmployeeAttendance attendance = attendanceRepository.findByEmployeeAndAttendanceDate(employee,
				LocalDate.now().toDate());

		if (attendance == null) {
			throw new NotFoundException("Cant clock out, employee has not clockedin!");
		}

		if (attendance.getCheckout() != null) {
			throw new ValidationException("employee has already clocked out!");
		}

		attendance.setCheckout(DateTime.now().toDate());
		attendanceRepository.saveAndFlush(attendance);

		eventBus.post(attendance);
	}

	public File attendanceReport(AttendanceReportRequest request) throws IOException {
		if (request.getCenterId() == null) {
			throw new ValidationException("Center is required.");
		}
		Center center = hasCenter(request.getCenterId());
		if (center == null) {
			throw new ValidationException("Unauthorized access to Center.");
		}
		request.setCenterCode(center.getCode());
		Date from = request.getFrom();
		Date to = request.getTo();
		Set<Employee> employees = new HashSet<>(getEmployees(center));
		List<EmployeeAttendance> attendances = employeeAttendanceRepository.findByCenterAndAttendanceDateBetween(center,
				from, to);
		attendances = fillAbsent(request, center, employees, attendances, false);

		// 2. putting attendances in sheet
		File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		createStyle(workbook);
		int rowNumber = 0;
		Sheet attendanceSheet = workbook.createSheet("attendance");
		Row row = attendanceSheet.createRow(rowNumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Employee Name");
		row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Attendance Date");
		row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Status");
		row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
		row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Excepted In");
		row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Excepted Out");
		row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Actual In");
		row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Actual Out");
		row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Time (HH:MM)");

		for (EmployeeAttendance attendance : attendances) {
			row = attendanceSheet.createRow(rowNumber++);

			row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(attendance.getEmployee().getName());
			row.createCell(1, Cell.CELL_TYPE_STRING)
					.setCellValue(toFormattedDate(attendance.getAttendanceDate(), "yyyy-MM-dd EEE"));
			row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(attendance.getStatus().toString());
			row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(attendance.getEmployee().getCenterName());
			row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(attendance.getEmployee().getExpectedIn() == null ? ""
					: attendance.getEmployee().getExpectedIn().toString());
			row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(attendance.getEmployee().getExpectedOut() == null ? ""
					: attendance.getEmployee().getExpectedOut().toString());

			switch (attendance.getStatus()) {
			case Present:
				row.createCell(6, Cell.CELL_TYPE_STRING)
						.setCellValue(attendance.getCheckin() == null ? "" : attendance.getCheckin().toString());
				row.createCell(7, Cell.CELL_TYPE_STRING)
						.setCellValue(attendance.getCheckout() == null ? "" : attendance.getCheckout().toString());
				if (attendance.getCheckin() != null && attendance.getCheckout() != null) {
					long milliSeconds = attendance.getCheckout().getTime() - attendance.getCheckin().getTime();
					long minutes = milliSeconds / (1000 * 60);
					long hours = minutes / 60;
					minutes = minutes % 60;
					row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(String.format("%s:%s", hours, minutes));
				} else {
					row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("");
				}
				break;
			case Absent:
				row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("A");
				row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("A");
				row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("A");
				break;
			}
		}

		workbook.write(fileOutputStream);
		workbook.dispose();

		return file;
	}

	private List<EmployeeAttendance> fillAbsent(AttendanceReportRequest request, Center center, Set<Employee> employees,
            List<EmployeeAttendance> attendances,boolean includeSunday){
		// getting all dates between request.from and request.to excluding sunday
		List<EmployeeAttendance> result = new ArrayList<>();

		Set<Date> dates = Utils.datesBetween(request.getFrom(), request.getTo(), includeSunday);

		// put all attendance in map key as date
		Map<Date, List<EmployeeAttendance>> map = new HashMap<>();
		for (EmployeeAttendance attendance : attendances) {
			map.computeIfAbsent(attendance.getAttendanceDate(), (k) -> {
				return new ArrayList<>();
			}).add(attendance);
		}

		dates.forEach((date) -> {
			List<EmployeeAttendance> employeeAttendances = map.get(date);
			employees.forEach((employee) -> {
				EmployeeAttendance attendance = null;
				if (CollectionUtils.isEmpty(employeeAttendances)) {
					// all emp are absent
				} else {
					// from list of attendance
					for (EmployeeAttendance a : employeeAttendances) {
						if (employee.equals(a.getEmployee())) {
							attendance = a;
						}
					}
					if (attendance == null) {
						// employee is absent
						EmployeeAttendance a = new EmployeeAttendance();
						a.setCenter(center);
						a.setAttendanceDate(date);
						a.setEmployee(employee);
						a.setStatus(AttendanceStatus.Absent);
						a.setCheckin(null);
						a.setCheckout(null);
						result.add(a);
					} else {
						// student is present
						result.add(attendance);
					}
				}
			});
		});

		return result;
	}

	@Transactional
	public void importAttendanceExcel(MultipartFile file) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet workSheet = workbook.getSheet("attendance");
		if (workSheet == null) {
			throw new ValidationException("Cannot find workSheet[name = attendance]");
		}

		int row = 1;
		XSSFRow xssfRow = workSheet.getRow(row);
		DataFormatter df = new DataFormatter();
		while (xssfRow != null) {
			try {

				String eid = df.formatCellValue(xssfRow.getCell(0));
				String dateString = df.formatCellValue(xssfRow.getCell(1));
				String clockInString = df.formatCellValue(xssfRow.getCell(2));
				String clockOutString = df.formatCellValue(xssfRow.getCell(3));

				Date date = pareDate(dateString, "yyyy-MM-dd");
				Date clockIn = StringUtils.isEmpty(clockInString) ? null : pareDate(clockInString, "HH:mm");
				Date clockOut = StringUtils.isEmpty(clockOutString) ? null : pareDate(clockOutString, "HH:mm");

				Employee employee = employeeRepository.findByEid(eid);
				if (employee == null) {
					throw new ValidationException(String.format("Cannot locate Employee[EID=%s]", eid));
				}

				// TODO : Add validation

				EmployeeAttendance attendance = employeeAttendanceRepository.findByEmployeeAndAttendanceDate(employee,
						date);
				if (attendance == null) {
					attendance = new EmployeeAttendance();
					attendance.setAttendanceDate(date);
				}
				attendance.setCenter(employee.getCostCenter());
				attendance.setStatus(AttendanceStatus.Present);
				attendance.setEmployee(employee);

				if (attendance.getCheckin() == null) {
					attendance.setCheckin(clockIn);
				}
				if (attendance.getCheckout() == null) {
					attendance.setCheckout(clockOut);
				}

				employeeAttendanceRepository.saveAndFlush(attendance);

			} catch (ParseException e) {
				throw new ValidationException("Invalid Date Format");
			} finally {
				row++;
				xssfRow = workSheet.getRow(row);
			}
		}
	}

	public EmployeeAttendance hrAttendanceSave(HRAdminAttendanceRequest request) {
		if (!isHRAdmin(getUser())) {
			throw new ValidationException("User is not authorized.");
		}

		Employee employee = employeeRepository.findOne(request.getEmployeeId());
		if (employee == null) {
			throw new ValidationException(
					String.format("Cannot locate Employee[id=%s]", mask(request.getEmployeeId())));
		}
		EmployeeAttendance attendance = employeeAttendanceRepository.findByEmployeeAndAttendanceDate(employee,
				request.getDate());

		attendance = request.toEntity(attendance);

		if (attendance.isNew()) {
			attendance.setEmployee(employee);
			attendance.setCenter(employee.getCostCenter());
			attendance.setStatus(AttendanceStatus.Present);
		}

		employeeAttendanceRepository.saveAndFlush(attendance);
		return attendance;
	}

	public void hrAttendanceDelete(Long id) {
		if (!isHRAdmin(getUser())) {
			throw new ValidationException("User is not authorized.");
		}

		EmployeeAttendance attendance = employeeAttendanceRepository.findOne(id);
		if (attendance != null) {
			employeeAttendanceRepository.delete(id);
		}
	}

	///////////////////////////// Avneet
	public List<EmployeeAttendance> employeeAttendanceList() {

		if (getEmployee() == null) {
			throw new ValidationException("Current user is not authorised to get attendance for any center!");
		}

		List<Employee> employees = employeeRepository.findByActiveTrueAndCostCenterInOrderByIdAsc(userService.getUserCenters());
		List<EmployeeAttendance> attendances = attendanceRepository.findByEmployeeInAndAttendanceDateOrderByEmployeeIdAsc(employees,
				LocalDate.now().toDate());
		List<Holiday> holidays = holidayRepository.findDistinctByCentersInAndHolidayDate(userService.getUserCenters(),
				LocalDate.now().toDate());
		List<EmployeeLeave> employeeLeave = employeeLeaveRepository.findByEmployeeInAndDateOrderByEmployeeIdAsc(employees,
				LocalDate.now().toDate());

        Set<String> set = new HashSet<>();
		for (Holiday holiday : holidays) {
			for (Center center : holiday.getCenters()) {
				set.add(center.getCode());
			}
		}

		EmployeeAttendance attendance;
		List<EmployeeAttendance> employeeAttendances = new ArrayList<>();
		int j = 0;
		int sizeOfEmployeeAttendances = attendances.size();

		for (Employee employee : employees) {
			if (j < sizeOfEmployeeAttendances && employee.getId().equals(attendances.get(j).getEmployee().getId())) {
				employeeAttendances.add(attendances.get(j));
				j++;
			} else {
				attendance = new EmployeeAttendance();
				attendance.setEmployee(employee);
				attendance.setCenter(employee.getCostCenter());
				attendance.setAttendanceDate(LocalDate.now().toDate());
				attendance.setStatus(AttendanceStatus.Absent);

				if (set.contains(employee.getCostCenter().getCode())) {
					attendance.setCalendarHoliday(true);
					attendance.setStatus(AttendanceStatus.Holiday);
				}
				employeeAttendances.add(attendance);
			}

		}
		int i = 0;
		int sizeOfEmployeeLeave = employeeLeave.size();
		for (EmployeeAttendance attendanceOfEmplyee : employeeAttendances) {
			if (employeeLeave != null && i < sizeOfEmployeeLeave
					&& attendanceOfEmplyee.getEmployee().getId().equals(employeeLeave.get(i).getEmployee().getId())) {
				attendanceOfEmplyee.setOnLeave(true);
				attendanceOfEmplyee.setLeaveStatus(employeeLeave.get(i).getLeaveStatus());
				attendanceOfEmplyee.setLeaveId(mask(employeeLeave.get(i).getId()));
				attendanceOfEmplyee.setHalfLeave(employeeLeave.get(i).getHalfLeave());
				attendanceOfEmplyee.setStatus(AttendanceStatus.Leave);
				i++;
				System.out.println(attendanceOfEmplyee.getEmployee().getName());
			}
		}
		return employeeAttendances;
	}

	public File attendanceReport2(AttendanceReportRequest request) throws IOException {

		if (request.getCenterCode() == null) {
			throw new ValidationException("Center is required.");
		}
		Center center;
		List<Center> centers= new ArrayList<>();

		if(request.getCenterCode().equals("ALL")) {
            centers = getUserCenters();
            request.setCenterCode("ALL");
        }
		else {
            boolean isCenter = hasCenter(request.getCenterCode());
            if (!isCenter) {
                throw new ValidationException("Unauthorized access to Center.");
            }
            else{
            	center= centerRepository.findByCode(request.getCenterCode());
				centers.add(center);
				request.setCenterCode(center.getCode());
			}

        }

		Date from = request.getFrom();
		Date to = request.getTo();

		Set<Employee> employees = new HashSet<>(getEmployees(centers));
		List<EmployeeAttendance> attendanceList= attendanceRepository.findByCenterInAndAttendanceDateBetween(centers,from,to);

		List<StaffAttendanceReport> attendanceReports= new ArrayList<>();
		StaffAttendanceReport report;
		Map<Date,String> dateMap=new LinkedHashMap<>();

		for(Employee emp:employees){
			List<EmployeeAttendance> employeeAttendances=
					attendanceList.stream().filter(a->a.getEmployee().equals(emp)).collect(Collectors.toList());

			//generates attendance of employee in a month i.e, makes EmployeeAttendance(P,L,W,H,A)
			List<EmployeeAttendance> attendances=
					fillAbsent(from,to,emp,emp.getCostCenter(),employeeAttendances,true);

			dateMap=generateDateMap(attendances,from,to); // attendance date and status

			report= new StaffAttendanceReport(emp,dateMap);
			report.calculate();
			attendanceReports.add(report);
		}

		StaffAttendanceRegisterReport register= new StaffAttendanceRegisterReport(attendanceReports,exportDir,request);
		return register.createExcel();
	}


    /// date map with date(naturalOrder) and attendance status of the employee
	public Map<Date,String> generateDateMap(List<EmployeeAttendance> attendances,Date from , Date to) {

		Set<Date> dates = Utils.datesBetween(from, to, true); //sorted dates
		System.out.println(dates);
		Map<Date, String> calendar = new TreeMap<>();
		attendances.forEach(a -> {
			calendar.computeIfAbsent(a.getAttendanceDate(), status(a));   //status returns attendance stats for that date
		});

		return calendar;
	}

	public Function<Date,String> status(EmployeeAttendance attendance){
		String status;
		switch (attendance.getStatus().toString()) {

            case "Present":
                status = "P";
                break;

            case "Holiday":
                if (LocalDate.fromDateFields(attendance.getAttendanceDate()).getDayOfWeek() == DateTimeConstants.SUNDAY)
                    status = "W";
                else
                    status = "H";

                break;

            case "Leave":
                if (attendance.getHalfLeave())
                    status = "HD";
                else
                    status = "L";

                break;

            default:
                status = "A";
        }
		return (k)->status;
	}

}
