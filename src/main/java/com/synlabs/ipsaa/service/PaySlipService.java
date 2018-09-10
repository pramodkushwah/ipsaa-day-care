package com.synlabs.ipsaa.service;

import static com.synlabs.ipsaa.util.BigDecimalUtils.HALF;
import static com.synlabs.ipsaa.util.BigDecimalUtils.ONE;
import static com.synlabs.ipsaa.util.BigDecimalUtils.ZERO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.jpa.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.Holiday;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.common.SerialNumberSequence;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.SalaryUtilsV2;
import com.synlabs.ipsaa.view.staff.EmployeePaySlipRequest;
import com.synlabs.ipsaa.view.staff.PaySlipRegenerateRequest;

@Service
public class PaySlipService extends BaseService {

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

	@Autowired
	private EmployeeProfileRepository employeeProfileRepository;

	@Autowired
	private EmployeeLeaveRepository employeeLeaveRepository;

	@Autowired
	private HolidayRepository holidayRepository;

	private static final Logger logger = LoggerFactory.getLogger(PaySlipService.class);

	public List<EmployeePaySlip> listPayslips(Integer month, Integer year, String employerId) throws ParseException {
		if (year == null) {
			throw new ValidationException("Year is required.");
		}
		if (month == null) {
			throw new ValidationException("Month is required.");
		}
		// Avneet
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		Date generationDate = calendar.getTime();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String date = format.format(generationDate);
		List<Employee> employees;
		LegalEntity legalEntity=null;
		if(employerId.equals("ALL")){
			employees = employeeRepository.findByActiveIsTrue();
		}else{
			legalEntity = legalEntityRepository.findOne(unmask(Long.parseLong(employerId)));
			if (legalEntity == null) {
				throw new ValidationException(String.format("Cannot locate Employer[id = %s]", mask(Long.parseLong(employerId))));
			}
			employees = employeeRepository.findByActiveIsTrueAndEmployerId(legalEntity.getId());
		}
		for (Employee emp : employees) {
			String doj = format.format(emp.getProfile().getDoj());
			String dol = null;
			if (emp.getProfile().getDol() != null) {
				dol = format.format(emp.getProfile().getDol());
			}

			if ((date.compareTo(doj) >= 0 && (dol==null || dol.compareTo(date) >= 0 ))) { // only this check is added
				EmployeePaySlip employeePaySlip = employeePaySlipRepository.findOneByEmployeeAndMonthAndYear(emp, month,
						year);
				if (employeePaySlip == null) {
					EmployeeSalary employeeSalary = employeeSalaryRepository.findByEmployee(emp);
					if (employeeSalary != null) {
						if (employeeSalary.getCtc() == null) {
							continue;
						}
						generateNewPayslip(emp, employeeSalary, year, month);
					}

				}

			}
		}

		if(employerId.equals("ALL"))
			return employeePaySlipRepository.findByMonthAndYear(month, year);
		else {
			if (legalEntity != null)
				return employeePaySlipRepository.findByEmployerIdAndMonthAndYear(legalEntity.getId(), month, year);
			else {
				return null;
			}
		}
	}

	private EmployeePaySlip generateNewPayslip(Employee employee, EmployeeSalary salary, int year, int month)
			throws ParseException {
		EmployeePaySlip payslip = new EmployeePaySlip();
		payslip.setSerial(generatePaySlipSerial(salary.getEmployee().getCostCenter()));
		payslip.setEmployee(employee);
		payslip.setEmployer(employee.getEmployer());
		payslip.setCenter(employee.getCostCenter());
		payslip.setMonth(month);
		payslip.setYear(year);
		// shubham
		if (payslip.getPresents() == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);// o to 11
			cal.set(Calendar.YEAR, year);
			int totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			payslip.setPresents(new BigDecimal(totalDays));
		}
		payslip = calculatePayslip(employee, salary, year, month, payslip);
		employeePaySlipRepository.saveAndFlush(payslip);
		return payslip;
	}

	@Transactional
	public boolean lockSalary(EmployeePaySlipRequest request) {
		EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());
		if (paySlip == null) {
			throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
		}
		if (paySlip.isLock()) {
			throw new ValidationException(String.format("Salary already locked", mask(request.getId())));
		}
		paySlip.setLock(true);
		employeePaySlipRepository.saveAndFlush(paySlip);
		return true;
	}

	@Transactional
	public EmployeePaySlip reGeneratePaySlip(EmployeePaySlipRequest request)
			throws IOException, DocumentException, ParseException {

		EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());

		if (paySlip == null) {
			throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
		}

		Employee employee = paySlip.getEmployee();

		EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);

		if (salary != null && paySlip.isLock()) {
			throw new ValidationException(String.format("Cannot change locked salary", mask(request.getId())));
		}

		Integer year = paySlip.getYear();
		Integer month = paySlip.getMonth();
		paySlip.setComment(request.getComment());
		// shubham
		if (request.getPresents() != null) {
			paySlip.setPresents(request.getPresents());
		} else if (paySlip.getPresents() == null && request.getPresents() == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);// o to 11
			cal.set(Calendar.YEAR, year);
			int totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			paySlip.setPresents(new BigDecimal(totalDays));
		}
		paySlip.setOtherAllowances(request.getOtherAllowances() == null ? ZERO : request.getOtherAllowances());
		paySlip.setOtherDeductions(request.getOtherDeductions() == null ? ZERO : request.getOtherDeductions());

		paySlip = calculatePayslip(employee, salary, year, month, paySlip);

		employeePaySlipRepository.saveAndFlush(paySlip);
		logger.info(String.format("Regenerated Payslip[eid=%s,month=%s,year=%s]", employee.getEid(), month, year));

		return paySlip;

	}

	private EmployeePaySlip calculatePayslip(Employee employee, EmployeeSalary salary, int year, int month,
			EmployeePaySlip payslip) throws ParseException {
		String autoComment = "";
		// 1. count leaves and absents
		Date from = pareDate(String.format("01-%s-%s", month, year), "dd-MM-yyyyy");
		Date to = LocalDate.fromDateFields(from).plusMonths(1).minusDays(1).toDate();
		List<EmployeeAttendance> attendances = attendanceRepository.findByEmployeeAndAttendanceDateBetween(employee,
				from, to);
		attendances = attendanceService.fillAbsent(from, to, employee, employee.getCostCenter(), attendances, false);
		BigDecimal leaves = ZERO;
		BigDecimal absents = ZERO;
		BigDecimal present = ZERO;
		for (EmployeeAttendance attendance : attendances) {
			switch (attendance.getStatus()) {
			case Present:
				present = present.add(BigDecimal.ONE);
				// System.out.println(present);
				break;
			case Absent:
				absents = absents.add(BigDecimal.ONE);
				break;
			case Leave:
				if (attendance.getLeaveType() == LeaveType.UNPAID
						|| attendance.getLeaveStatus() != LeaveStatus.Approved) {
					Boolean halfLeave = attendance.getHalfLeave() == null ? false : attendance.getHalfLeave();
					leaves = leaves.add(halfLeave ? HALF : ONE);

					if (halfLeave && attendance.getCheckin() == null) {
						absents = absents.add(HALF);
					}
				}
				break;
			}
		}

		// 2. deduct unpaid leaves and absents and append autoComment
		// BigDecimal days = new BigDecimal("31");
		BigDecimal totalDays = new BigDecimal(
				Days.daysBetween(LocalDate.fromDateFields(from), LocalDate.fromDateFields(to).plusDays(1)).getDays());
		// shubham get no of months
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month - 1);// o to 11
		cal.set(Calendar.YEAR, year);
		totalDays = new BigDecimal(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		// System.out.println(totalDays);

		BigDecimal totalAbsents = absents.add(leaves);
		BigDecimal presents = totalDays.subtract(totalAbsents);
		autoComment = absents.doubleValue() > 0 ? autoComment + String.format("Absents : %s\n", absents) : autoComment;
		autoComment = presents.doubleValue() > 0 ? autoComment + String.format("Present : %s\n", presents)
				: autoComment;
		autoComment = leaves.doubleValue() > 0 ? autoComment + String.format("Leaves : %s\n", leaves) : autoComment;
		payslip.setAutoComment(autoComment);
		// shubham
		if (payslip.getPresents() != null) {
			payslip.updateV2(salary, totalDays, payslip.getPresents());
		} else
			payslip.updateV2(salary, totalDays, presents);
		payslip.roundOff();
		return payslip;
	}

	// update by shubham
	@Transactional
	public EmployeePaySlip updatePaySlip(EmployeePaySlipRequest request) throws IOException, DocumentException {
		EmployeePaySlip paySlip = employeePaySlipRepository.findOne(request.getId());
		if (paySlip == null) {
			throw new ValidationException(String.format("Cannot Locate PaySlip[id = %s]", mask(request.getId())));
		}

		if(request.getComment()!=null)
			paySlip.setComment(request.getComment());
		if (request.getOtherAllowances() == null)
			request.setOtherAllowances(ZERO);
		if (request.getOtherDeductions() == null)
			request.setOtherDeductions(ZERO);
		if (request.getTds() == null)
			request.setTds(ZERO);

		paySlip = SalaryUtilsV2.updateAndCalculateCTC(paySlip, request);
		employeePaySlipRepository.saveAndFlush(paySlip);
		return paySlip;
	}

	public InputStream generatePayslipPdf(Long id) throws IOException, DocumentException {
		if (id == null) {
			throw new ValidationException("paySlip id is required.");
		}
		EmployeePaySlip paySlip = employeePaySlipRepository.findOne(id);
		if (paySlip == null) {
			throw new ValidationException(String.format("Cannot locate paySlip[id = %s]", mask(id)));
		}

		if (paySlip.getFile() == null) {
			String fileName = documentService.generatePayslipPdf(paySlip);
			return fileStore.getStream("PAYSLIP", fileName);
		} else {
			return fileStore.getStream("PAYSLIP", paySlip.getFile());
		}
	}

	private String generatePaySlipSerial(Center center) {
		SerialNumberSequence serial = serialRepository.findOneByCenterCodeAndType(center.getCode(), "PAYSLIP");
		if (serial == null) {
			serial = new SerialNumberSequence();
			serial.setCenterCode(center.getCode());
			serial.setType("PAYSLIP");
		}
		long next = serial.getNext();
		serialRepository.saveAndFlush(serial);
		return String.format("%s/%03d", center.getCode(), next);
	}

	public void reGeneratePaySlipAll(PaySlipRegenerateRequest request)
			throws DocumentException, ParseException, IOException {
		request.validate();
		if (request.getLegalEntityId() == null) {
			throw new ValidationException("Legal entity is required.");
		}
		LegalEntity one = legalEntityRepository.findOne(request.getLegalEntityId());
		if (one == null) {
			throw new ValidationException(
					String.format("Cannot locate LegalEntity[id=%s]", mask(request.getLegalEntityId())));
		}
		List<EmployeePaySlip> payslips = employeePaySlipRepository.findByEmployerIdAndMonthAndYear(one.getId(),
				request.getMonth(), request.getYear());

		logger.info(String.format("Regenerating all payslip for [legal=%s,month=%s,year=%s,count=%s]", one.getName(),
				request.getMonth(), request.getYear(), payslips.size()));
		for (EmployeePaySlip payslip : payslips) {
			EmployeePaySlipRequest paySlipRequest = new EmployeePaySlipRequest();
			paySlipRequest.setId(mask(payslip.getId()));
			paySlipRequest.setOtherDeductions(payslip.getOtherDeductions());
			paySlipRequest.setOtherAllowances(payslip.getOtherAllowances());
			reGeneratePaySlip(paySlipRequest);
		}
	}
//
//	public void reGenerate(PaySlipRegenerateRequest request) throws DocumentException, ParseException, IOException {
//
//		request.validate();
//		if (request.getLegalEntityId() == null) {
//			throw new ValidationException("Legal entity is required.");
//		}
//		LegalEntity one = legalEntityRepository.findOne(request.getLegalEntityId());
//		if (one == null) {
//			throw new ValidationException(
//					String.format("Cannot locate LegalEntity[id=%s]", mask(request.getLegalEntityId())));
//		}
//		if (request.getIds() == null || request.getIds().isEmpty()) {
//			throw new ValidationException(String.format("select payslip to regenerate"));
//		}
//		List<EmployeePaySlip> payslips = employeePaySlipRepository.findByIdInAndEmployerIdAndMonthAndYear(
//				request.getIds(), one.getId(), request.getMonth(), request.getYear());
//
//		logger.info(String.format("Regenerating payslip for [employee eid=%s, legal=%s,month=%s,year=%s,count=%s]",
//				request.getIds().toString(), one.getName(), request.getMonth(), request.getYear(), payslips.size()));
//
//		for (EmployeePaySlip payslip : payslips) {
//			EmployeePaySlipRequest paySlipRequest = new EmployeePaySlipRequest();
//			paySlipRequest.setId(mask(payslip.getId()));
//			paySlipRequest.setOtherDeductions(payslip.getOtherDeductions());
//			paySlipRequest.setOtherAllowances(payslip.getOtherAllowances());
//			reGeneratePaySlip(paySlipRequest);
//		}
//	}



	public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\shubham\\Desktop\\ipsaa\\new attendance report2.xlsx";
	@Transactional
	public void uploadData()throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
		Sheet sheet = workbook.getSheetAt(0);
		for (int i=0;i<=sheet.getPhysicalNumberOfRows();i++) {
			if(i==0) continue;

			Row row=sheet.getRow(i);
			if(row!=null){
				if(row.getCell(1)!=null){
					String eid=row.getCell(1).getStringCellValue();
					EmployeePaySlip slip=employeePaySlipRepository.findByEmployeeEidAndMonthAndYear(eid,8,2018);
					if(slip!=null && row.getCell(6)!=null){

						EmployeePaySlipRequest req=new EmployeePaySlipRequest();

						try{
							req.setPresents(new BigDecimal(row.getCell(5).getNumericCellValue()));
							req.setOtherAllowances(new BigDecimal(row.getCell(6).getNumericCellValue()));
							req.setOtherDeductions(new BigDecimal(row.getCell(7).getNumericCellValue()));
							req.setComment(row.getCell(8).getStringCellValue());

							req.setId(mask(slip.getId()));

						}catch (Exception e){
							e.printStackTrace();
						}

						try {
							System.out.println(updatePaySlip(req).getEmployee().getEid());
						} catch (DocumentException e) {
							e.printStackTrace();
						}
					}else{

					}

				}
			}
		}
		workbook.close();
	}

   ////////////////////////Avneet -Calculate days for which the employee is to be paid.- not replaced the inoming presents
    public BigDecimal presentDays(int month,int year,Long id) throws ParseException {

    Set<String> dateSet=new HashSet<String>();
    int sundays=0;
    float add=0;

    Calendar from=Calendar.getInstance(); //current time instance
    Calendar to=Calendar.getInstance();
    Calendar calendar=Calendar.getInstance();

    from.set(year,month-1,1);
    int day=from.getActualMaximum(Calendar.DAY_OF_MONTH);
    to.set(year,month-1,day);//get max date from existing date


      Employee emp= employeeRepository.findOne(id); /////////replace all with incoming id.
    //Employee emp= employeeRepository.findByEid("E327");

      List<EmployeeAttendance> attendance=attendanceRepository.findByEmployeeAndAttendanceDateBetween(emp,from.getTime(),to.getTime());
    attendance.forEach(employeeAttendance -> dateSet.add(employeeAttendance.getAttendanceDate().toString()));

    for(int i=1; i<day;  i++){
      calendar.set(year,month-1,i);

      if( calendar.get(Calendar.DAY_OF_WEEK)== Calendar.SUNDAY){
        Date sunday=calendar.getTime();
        dateSet.add(new java.sql.Date(sunday.getTime()).toString());   //to ignore the time
        sundays++;
      }
    }

    List<EmployeeLeave> employeeLeaves=employeeLeaveRepository.findByEmployeeAndLeaveTypeAndDateBetween(emp,LeaveType.PAID,from.getTime(),to.getTime());
    employeeLeaves.forEach(employeeLeave -> dateSet.add(employeeLeave.getDate().toString()));

    List<Holiday> holidays=holidayRepository.findByCentersIdAndHolidayDateBetween(emp.getCostCenter().getId(),
            from.getTime(),to.getTime());
    holidays.forEach(holiday ->  dateSet.add(holiday.getHolidayDate().toString()) );


    List<EmployeeLeave> halfLeave=employeeLeaveRepository.findByEmployeeAndDateBetweenAndHalfLeaveIsTrue(emp,from.getTime(),to.getTime());
    for(EmployeeLeave l:halfLeave){
      if(dateSet.contains(l.getDate().toString())== false){
        add+=0.5;
        dateSet.add(l.getDate().toString());
      }
    }

    BigDecimal total=BigDecimal.valueOf(dateSet.size());
    BigDecimal totalDays=total.add(BigDecimal.valueOf(add));

    System.out.format("Salary is accounted for %s days%n",totalDays);

    return totalDays;
  }

}
