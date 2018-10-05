package com.synlabs.ipsaa.service;

import static com.synlabs.ipsaa.util.BigDecimalUtils.HALF;
import static com.synlabs.ipsaa.util.BigDecimalUtils.ONE;
import static com.synlabs.ipsaa.util.BigDecimalUtils.ZERO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.staff.QEmployee;
import com.synlabs.ipsaa.view.batchimport.ImportMonthlySalary;
import com.synlabs.ipsaa.view.staff.EmployeePaySlipResponse;

import com.synlabs.ipsaa.entity.staff.EmployeeLeave;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.view.staff.ErrorPayslipResponce;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

@Service
public class PaySlipService extends BaseService {

	@Autowired
	private LegalEntityRepository legalEntityRepository;

	@Autowired
	private ExcelImportService excelImportService;

	@Autowired
	private StaffAttendanceService attendanceService;
	@Autowired
	private EntityManager entityManager;

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
		Calendar calendar =Calendar.getInstance();
		calendar.set(year,month-1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date generationDate = calendar.getTime();

		calendar.set(year,month-1,1);
		Date startDate=calendar.getTime();

		List<Employee> employees;
		LegalEntity legalEntity=null;

		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployee qemp = QEmployee.employee;
		query.select(qemp).from(qemp)
				.where(qemp.active.isTrue().and(qemp.profile.doj.loe(generationDate))
											.and(qemp.profile.dol.isNull().or(qemp.profile.dol.goe(startDate)) ).or(
												qemp.active.isFalse()
														.and(qemp.profile.dol.goe(startDate))
														.and(qemp.profile.doj.loe(generationDate))///////Avneet-changed from equals to greater than equals
											)
						);
		if(employerId.equals("ALL")){
			employees=query.fetch();
		}else{
			legalEntity = legalEntityRepository.findOne(unmask(Long.parseLong(employerId)));
			if (legalEntity == null) {
				throw new ValidationException(String.format("Cannot locate Employer[id = %s]", mask(Long.parseLong(employerId))));
			}
			employees = query.where(qemp.employer.eq(legalEntity)).fetch();
			}
		for (Employee emp : employees) {
				EmployeePaySlip employeePaySlip = employeePaySlipRepository.findOneByEmployeeAndMonthAndYear(emp, month,
						year);
				if (employeePaySlip == null) {
					EmployeeSalary employeeSalary = employeeSalaryRepository.findByEmployee(emp);
					if (employeeSalary != null) {
						if (employeeSalary.getCtc() == null) {
							//System.out.println("ctc not found" +emp.getEid());
							continue;
						}
						generateNewPayslip(emp, employeeSalary, year, month);
					}
					else{
						//System.out.println("salary not found"+emp.getEid());
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

    @Transactional
	public Map<String,Object> uploadData(MultipartFile file,Integer month,Integer year,String employerId)throws IOException, InvalidFormatException {
		boolean errorInFile = false;
		Map<String, Object> statusMap = new HashMap<>();
		statusMap.put("error", "false");
		List<ErrorPayslipResponce> ungenerate=new ArrayList<>();

		List<ImportMonthlySalary> employees = excelImportService.importMonthlySalaryRecords(file);
		if(!employees.isEmpty()){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.YEAR, year);
			for (ImportMonthlySalary newslip : employees) {
				EmployeePaySlip slip = employeePaySlipRepository.findByEmployeeEidAndMonthAndYear(newslip.getEid(), month, year);
				if (slip != null) {
					EmployeePaySlipRequest req = new EmployeePaySlipRequest();
					try {

                        if(!employerId.equals("ALL") && slip.getEmployee().getEmployer().getId()==Long.parseLong(employerId)){
                            throw new ValidationException("employer code not matched");
                        }

						if (newslip.getPresentDay().intValue()>cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
							throw new ValidationException("present days are more then no of days"+ newslip.getEid());
						}
						req.setPresents(newslip.getPresentDay());
						req.setOtherAllowances(newslip.getOtherAllowance() == null ? ZERO : newslip.getOtherAllowance());
						req.setOtherDeductions(newslip.getOtherDeduction() == null ? ZERO : newslip.getOtherDeduction());
						req.setComment(newslip.getComments() == null ? "" : newslip.getComments());
						req.setTds(newslip.getTds());
						req.setId(mask(slip.getId()));

						this.updatePaySlip(req);
						logger.info(String.format("Regenrating payslip eid %s present days [%s] ",newslip.getEid(),newslip.getPresentDay()));
					} catch (Exception e) {
						logger.info(String.format("error in regenrating payslip eid %s present days [%s] ",newslip.getEid(),newslip.getPresentDay()));
						ungenerate.add(new ErrorPayslipResponce(new EmployeePaySlipResponse(slip),e.getMessage()));
					}
				}else{
					//slip not found
					logger.info(String.format("error in regenrating payslip eid %s present days [%s] ",newslip.getEid(),newslip.getPresentDay()));
					ungenerate.add(new ErrorPayslipResponce(null,"Salary not found of id "+newslip.getEid()));
				}
			}
		}else{
			statusMap.put("error", "true");
		}
		statusMap.put("ungenerate", ungenerate);
		return statusMap;
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
