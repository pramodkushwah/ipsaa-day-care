package com.synlabs.ipsaa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.synlabs.ipsaa.entity.staff.*;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.*;
import com.synlabs.ipsaa.jpa.EmployeeRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.attendance.QEmployeeAttendance;
import com.synlabs.ipsaa.entity.attendance.QStudentAttendance;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.QCenter;
import com.synlabs.ipsaa.entity.common.QUser;
import com.synlabs.ipsaa.entity.common.Role;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.entity.inquiry.QInquiryEventLog;
import com.synlabs.ipsaa.jpa.InquiryEventLogRepository;
import com.synlabs.ipsaa.jpa.InquiryRepository;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.common.DashboardRequest;
import com.synlabs.ipsaa.view.common.FeeStatsResponse;
import com.synlabs.ipsaa.view.common.StatsResponse;
import com.synlabs.ipsaa.view.common.UserResponse;
import com.synlabs.ipsaa.view.inquiry.FollowUpReportResponse;
import com.synlabs.ipsaa.view.staff.DashStaffResponse;
import com.synlabs.ipsaa.view.staff.StaffNewJoinings;
import com.synlabs.ipsaa.view.staff.StaffNewLeavings;
import com.synlabs.ipsaa.view.student.DashStudentFeeResponse;
import com.synlabs.ipsaa.view.student.DashStudentResponse;
import com.synlabs.ipsaa.view.student.ParentSummaryResponse;

@Service
public class DashboardService extends BaseService
{

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private EmployeeRepository employeeRepository;


	@Autowired
	private UserService userService;

	@Autowired
	private InquiryRepository inquiryRepository;

	@Autowired
	private InquiryEventLogRepository eventLogRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	public StatsResponse getFeeStats(DashboardRequest request) {
		StatsResponse response = new StatsResponse();
		List<Center> centers = getCenters(request);
		FeeStatsResponse expectedFee = getExpectedFee(centers, request);
		response.setExpectedFee(expectedFee);
		FeeStatsResponse collectedfee = getCollectedFee(centers, request);
		response.setCollectedFee(collectedfee);
		return response;
	}

	public StatsResponse getStats(DashboardRequest request) {
		Set<String> dashboards = new HashSet<>();
		for (Role role : getUser().getRoles()) {
			Collections.addAll(dashboards,
					StringUtils.isEmpty(role.getDashboard()) ? new String[0] : role.getDashboard().split(","));
		}

		dashboards = dashboards.stream().map(String::trim).collect(Collectors.toSet());

		StatsResponse response = new StatsResponse();
		List<Center> centers = getCenters(request);
		int studentCount = countStudents(centers);
		int corporateStudentCount = countStudents(centers, true);
		for (String dashboard : dashboards) {
			switch (dashboard) {
			case "user":
				response.setUsers(getUserCount(centers));
				break;
			case "student":
				// 1. total students
				response.setStudents(studentCount);
				response.setCorporateStudents(corporateStudentCount);

				// 2. present today
				int presentStudent = countPresentStudents(centers);
				response.setStudentPresent(presentStudent);
				break;
			case "parent":
				response.setParentUsers(getStudentParentCount(centers));
				break;
			case "center":
				// 0. total centers from query
				response.setCenters(centers.size());

				// 3. capacity and utilisation
				long capacity = countCenterCapacity(centers);
				response.setCapacity((int) capacity);
				response.setUtilisation((100.0 * studentCount) / (capacity * 1.0));

				break;
			case "staff":
				// 4. staff count, total staff cost
				int staffcount = countStaff(centers);
				response.setStaffCount(staffcount);

				// 6.
				int staffCost = staffCost(centers);
				response.setStaffCost(staffCost);

				int presentStaff = countPresentStaff(centers);
				response.setStaffPresent(presentStaff);
				break;
			case "followup":
				calculateFollowUps(request, response);
				break;
			}
		}
		return response;
	}

	private void calculateFollowUps(DashboardRequest request, StatsResponse response) {
		List<Center> centers = getCenters(request);
		LocalDate now = LocalDate.now();
		JPAQuery<InquiryEventLog> allQuery = new JPAQuery<>(entityManager);
		QInquiryEventLog event = QInquiryEventLog.inquiryEventLog;
		allQuery.select(event).from(event).where(event.done.isFalse())

				.where(event.callDisposition.in(CallDisposition.Followup, CallDisposition.Callback,
						CallDisposition.NewInquiry, CallDisposition.Revisit))
				.where(event.inquiry.center.in(centers));
		// return (int) query.fetchCount();
		// TODO : optimize it.
		int open = (int) allQuery.fetchCount();

		JPAQuery<InquiryEventLog> openQuery = new JPAQuery<>(entityManager);

		openQuery.select(event).from(event).where(event.done.isFalse()).where(event.callBack.before(now.toDate()))
				.where(event.callDisposition.in(CallDisposition.Followup, CallDisposition.Callback,
						CallDisposition.NewInquiry, CallDisposition.Revisit))
				.where(event.inquiry.center.in(centers));

		int previous = (int) openQuery.fetchCount();

		JPAQuery<InquiryEventLog> todayQuery = new JPAQuery<>(entityManager);

		todayQuery.select(event).from(event).where(event.done.isFalse())
				.where(event.callBack.between(now.minusDays(1).toDate(), now.toDate()))
				.where(event.callDisposition.in(CallDisposition.Followup, CallDisposition.Callback,
						CallDisposition.NewInquiry, CallDisposition.Revisit))
				.where(event.inquiry.center.in(centers));

		int today = (int) todayQuery.fetchCount();
		response.setTodayFollowups(today);
		response.setOpenFollowups(open);
		response.setPreviousFollowups(previous);
		Date monthStart = now.dayOfMonth().withMinimumValue().toDate();
		Date monthEnd = now.dayOfMonth().withMaximumValue().toDate();
		int monthInquiries = inquiryRepository.countByCenterInAndInquiryDateBetween(centers, monthStart, monthEnd);
		response.setMonthInquiries(monthInquiries);
	}

	private List<Center> getCenters(DashboardRequest request) {

		List<Center> usercenters = userService.getUserCenters();
		if (!StringUtils.isEmpty(request.getCenter())) {
			return usercenters.stream().filter(c -> c.getCode().equals(request.getCenter()))
					.collect(Collectors.toList());
		}

		if (!StringUtils.isEmpty(request.getCity())) {
			return usercenters.stream().filter(c -> c.getAddress().getCity().equals(request.getCity()))
					.collect(Collectors.toList());
		}

		if (!StringUtils.isEmpty(request.getZone())) {
			return usercenters.stream().filter(c -> c.getZone().getName().equals(request.getZone()))
					.collect(Collectors.toList());
		}

		return usercenters;
	}

	private int getUserCount(List<Center> centers) {
		return (int) userService.countUsers(centers);
	}

	private int getStudentParentCount(List<Center> centers) {

		JPAQuery<StudentParent> query = new JPAQuery<>(entityManager);
		QStudentParent parent = QStudentParent.studentParent;
		QStudent student = QStudent.student;
		// query.select(parent).from(parent)
		// .where(parent.student.center.in(centers));
		query.select(parent).from(parent).innerJoin(parent.students, student).distinct()
				.where(student.center.in(centers)).where(student.active.isTrue());
		return (int) query.fetchCount();
	}

	private int countStudents(List<Center> centers) {
		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		QStudent student = QStudent.student;
		query.select(student).from(student).where(student.active.isTrue()).where(student.center.in(centers));
		return (int) query.fetchCount();
	}

	private int countStudents(List<Center> centers, boolean corporate) {
		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		QStudent student = QStudent.student;
		query.select(student).from(student).where(student.active.isTrue()).where(student.corporate.eq(corporate))
				.where(student.center.in(centers));
		return (int) query.fetchCount();
	}

	private int countPresentStudents(List<Center> centers) {
		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		QStudentAttendance attendance = QStudentAttendance.studentAttendance;
		QStudent student = QStudent.student;
		query.select(attendance).from(attendance).where(student.active.isTrue())
				.where(attendance.status.eq(AttendanceStatus.Present))
				.where(attendance.attendanceDate.eq(LocalDate.now().toDate())).where(attendance.checkout.isNull())
				.where(attendance.center.in(centers));

		return (int) query.fetchCount();
	}

	private int countPresentStaff(List<Center> centers) {
		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployeeAttendance attendance = QEmployeeAttendance.employeeAttendance;
		QEmployee employee = QEmployee.employee;
		query.select(attendance).from(attendance)
				.where(employee.active.isTrue())
				.where(attendance.status.eq(AttendanceStatus.Present))
				.where(attendance.attendanceDate.eq(LocalDate.now().toDate()))
				.where(attendance.checkout.isNull())
				.where(attendance.center.in(centers));

		return (int) query.fetchCount();
	}

	private int countCenterCapacity(List<Center> centers) {
		JPAQuery<Integer> query = new JPAQuery<>(entityManager);
		QCenter center = QCenter.center;
		query.select(center.capacity.sum()).from(center).where(center.in(centers));

		Integer capacity = query.fetchFirst();
		return capacity == null ? 0 : capacity;
	}

	private int countStaff(List<Center> centers) {
		JPAQuery<Integer> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.active.isTrue()).where(employee.costCenter.in(centers));

		return (int) query.fetchCount();
	}

	private int countOnLeaveStaff(List<Center> centers) {

		JPAQuery<EmployeeLeave> leaveQuery = new JPAQuery<>(entityManager);
		QEmployeeLeave employeeLeave = QEmployeeLeave.employeeLeave;
		leaveQuery.select(employeeLeave.employee).from(employeeLeave)
				.where(employeeLeave.date.eq(LocalDate.now().toDate()))
				.where(employeeLeave.leaveStatus.in(LeaveStatus.Approved,LeaveStatus.Applied))
				.where(employeeLeave.employee.costCenter.in(centers));

		return (int) leaveQuery.fetchCount();
	}

	private int countAbsentStaff(List<Center> centers) {
		return Math.abs(countStaff(centers) - (countPresentStaff(centers) + countOnLeaveStaff(centers)));
	}

	private int staffCost(List<Center> centers) {
		LocalDate today = LocalDate.now();
		JPAQuery<BigDecimal> query = new JPAQuery<>(entityManager);
		// QEmployeePaySlip paySlip = QEmployeePaySlip.employeePaySlip;
		// BigDecimal costsum = query.select(paySlip.netSalary.sum())
		// .from(paySlip)
		// .where(paySlip.year.eq(today.getYear()))
		// .where(paySlip.month.eq(today.getMonthOfYear()))
		// .where(paySlip.center.in(centers))
		// .where(paySlip.employee.active.isTrue())
		// .fetchFirst();

		QEmployeeSalary salary = QEmployeeSalary.employeeSalary;
		BigDecimal costsum = query.select(salary.ctc.sum()).from(salary).where(salary.employee.active.isTrue())
				.where(salary.employee.costCenter.in(centers)).fetchFirst();
		return costsum == null ? 0 : costsum.intValue();
	}

	private FeeStatsResponse getExpectedFee(List<Center> centers, DashboardRequest request) {
		FeeDuration feeDuration = request.getFeeDuration();
		FeeStatsResponse feeStatsResponse = new FeeStatsResponse();

		// 1 find current month and quarter and year
		LocalDate today = LocalDate.now();
		Integer month = feeDuration == null ? today.getMonthOfYear()
				: request.getMonth() == null ? today.getMonthOfYear() : request.getMonth();
		Integer year = feeDuration == null ? today.getYear()
				: request.getYear() == null ? today.getYear() : request.getYear();
		Integer quarter = feeDuration == null ? (month / 3) + 1
				: request.getQuarter() == null ? (month / 3) + 1 : request.getQuarter();
		feeStatsResponse.setYear(today.getYear());
		feeStatsResponse.setMonth(month);

		QStudentFeePaymentRequest slip = QStudentFeePaymentRequest.studentFeePaymentRequest;
		QStudentFeePaymentRequestIpsaaClub ipsaaSlip=QStudentFeePaymentRequestIpsaaClub.studentFeePaymentRequestIpsaaClub;
		int total = 0;

		// 3. add monthly fee for this month removed
		if (feeDuration == null || feeDuration == FeeDuration.Monthly)
		{
			JPAQuery<BigDecimal> ipssaQuery = new JPAQuery<>(entityManager);
			ipssaQuery.select(ipsaaSlip.totalFee.sum())
					.from(ipsaaSlip)
					.where(ipsaaSlip.student.active.isTrue())
					.where(ipsaaSlip.student.corporate.isFalse())
					.where(ipsaaSlip.year.eq(year))
					.where(ipsaaSlip.month.eq(month))
					.where(ipsaaSlip.student.center.in(centers));
			BigDecimal totalSum = ipssaQuery.fetchFirst();
			total += totalSum == null ? 0 : totalSum.intValue();
			feeStatsResponse.setIpssaFee(totalSum == null ? 0 : totalSum.intValue());
		}

		//if start of quarter
		// 2. add quarterly fee for quarter in quarter start
		if (feeDuration == FeeDuration.Quarterly || (feeDuration == null && (month == 1 || month == 4 || month == 7 || month == 10)))
		{
			JPAQuery<BigDecimal> quarterlyquery = new JPAQuery<>(entityManager);
			quarterlyquery.select(slip.totalFee.sum()).from(slip)
					//.where(slip.student.active.isTrue())
					.where(slip.student.corporate.isFalse())
					// .where(slip.student.approvalStatus.eq(ApprovalStatus.Approved))
					.where(slip.feeDuration.eq(FeeDuration.Quarterly))
					.where(slip.year.eq(year))
					.where(slip.quarter.eq(quarter))
					.where(slip.student.center.in(centers));

			BigDecimal quarterly = quarterlyquery.fetchFirst();
			total += quarterly == null ? 0 : quarterly.intValue();
			feeStatsResponse.setQuarter(feeDuration == null ? ((month / 3) + 1) : request.getQuarter());
			feeStatsResponse.setQuarterly(quarterly == null ? 0 : quarterly.intValue());
		}

		//if start of year
		// 1. add yearly fee for year in jan
		if (feeDuration == FeeDuration.Yearly || (feeDuration == null && (month == 1)))
		{
			JPAQuery<BigDecimal> yearlyquery = new JPAQuery<>(entityManager);
			yearlyquery.select(slip.totalFee.sum()).from(slip)
					.where(slip.student.active.isTrue())
					.where(slip.student.corporate.isFalse())
					.where(slip.student.approvalStatus.eq(ApprovalStatus.Approved))
					.where(slip.feeDuration.eq(FeeDuration.Yearly))
					.where(slip.year.eq(year))
					.where(slip.student.center.in(centers));

			BigDecimal yearly = yearlyquery.fetchFirst();
			total += yearly == null ? 0 : yearly.intValue();
			feeStatsResponse.setYearly(yearly == null ? 0 : yearly.intValue());
		}

		feeStatsResponse.setTotal(total);
		return feeStatsResponse;
	}

	private FeeStatsResponse getCollectedFee(List<Center> centers, DashboardRequest request)
	{
		FeeDuration feeDuration = request.getFeeDuration();
		FeeStatsResponse feeStatsResponse = new FeeStatsResponse();
		LocalDate today = LocalDate.now();
		Integer month = feeDuration == null ? today.getMonthOfYear() : request.getMonth() == null ? today.getMonthOfYear() : request.getMonth();
		Integer year = feeDuration == null ? today.getYear() : request.getYear() == null ? today.getYear() : request.getYear();
		Integer quarter = feeDuration == null ? (month / 3) + 1 : request.getQuarter() == null ? (month / 3) + 1 : request.getQuarter();
		int total = 0;

		JPAQuery<BigDecimal> ipsaaClubq = new JPAQuery<>(entityManager);
		QStudentFeePaymentRecord payment = QStudentFeePaymentRecord.studentFeePaymentRecord;
		QStudentFeePaymentRecordIpsaaClub records=QStudentFeePaymentRecordIpsaaClub.studentFeePaymentRecordIpsaaClub;


		if (feeDuration == null || feeDuration == FeeDuration.Monthly)
		{
			ipsaaClubq.select(records.paidAmount.sum()).from(records)
					.where(records.student.active.isTrue())
					.where(records.active.isTrue())
					.where(records.student.corporate.isFalse())
					.where(records.request.year.eq(year))
					.where(records.request.month.eq(month))
					.where(records.student.center.in(centers));

			BigDecimal monthly = ipsaaClubq.fetchFirst();
			total += monthly == null ? 0 : monthly.intValue();
			feeStatsResponse.setIpssaFee(monthly==null?0:monthly.intValue());
		}
		//if start of quarter
		// 2. add quarterly fee for quarter in quarter start
		if (feeDuration == FeeDuration.Quarterly ||
				(feeDuration == null && (month == 1 || month == 4 || month == 7 || month == 10)))
		{
			JPAQuery<BigDecimal> quarterlyq = new JPAQuery<>(entityManager);
			quarterlyq.select(payment.paidAmount.sum()).from(payment)
					.where(payment.student.active.isTrue())
					.where(payment.student.corporate.isFalse())
					.where(payment.student.approvalStatus.eq(ApprovalStatus.Approved))
					.where(payment.request.feeDuration.eq(FeeDuration.Quarterly))
					.where(payment.request.year.eq(year))
					.where(payment.request.quarter.eq(quarter))
					.where(payment.paymentStatus.eq(PaymentStatus.Paid))
					.where(payment.student.center.in(centers));

			BigDecimal quarterly = quarterlyq.fetchFirst();
			total += quarterly == null ? 0 : quarterly.intValue();
			feeStatsResponse.setQuarter((month / 3) + 1);
			feeStatsResponse.setQuarterly(quarterly == null ? 0 : quarterly.intValue());
		}

		if (feeDuration == FeeDuration.Yearly || (feeDuration == null && (month == 1)))
		{
			JPAQuery<BigDecimal> yearlyq = new JPAQuery<>(entityManager);
			yearlyq.select(payment.paidAmount.sum()).from(payment)
					.where(payment.student.active.isTrue())
					.where(payment.student.corporate.isFalse())
					.where(payment.request.feeDuration.eq(FeeDuration.Yearly))
					.where(payment.student.approvalStatus.eq(ApprovalStatus.Approved))
					.where(payment.request.year.eq(year))
					.where(payment.paymentStatus.eq(PaymentStatus.Paid))
					.where(payment.student.center.in(centers));

			BigDecimal yearly = yearlyq.fetchFirst();
			total += yearly == null ? 0 : yearly.intValue();
			feeStatsResponse.setYear(year);
			feeStatsResponse.setYearly(yearly == null ? 0 : yearly.intValue());
		}

		feeStatsResponse.setTotal(total);
		return feeStatsResponse;
	}

	public Set<String> getDashboardList() {
		Set<String> dashboards = new HashSet<>();

		getUser().getRoles().forEach(role -> {
			if (!StringUtils.isEmpty(role.getDashboard())) {
				List<String> list = Arrays.stream(role.getDashboard().split(",")).map(String::trim)
						.filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
				dashboards.addAll(list);
			}
		});
		return dashboards;
	}

	public List<DashStudentResponse> listStudent(DashboardRequest request) {
		List<Center> centers = getCenters(request);

		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		JPAQuery<StudentAttendance> attquery = new JPAQuery<>(entityManager);
		QStudent student = QStudent.student;
		QStudentAttendance attendance = QStudentAttendance.studentAttendance;

		query.select(student).from(student).where(student.active.isTrue()).where(student.center.in(centers));

		List<Student> students = query.fetch();

		attquery.select(attendance).from(attendance).where(attendance.student.in(students))
				.where(attendance.student.center.in(centers)).where(attendance.student.active.isTrue())
				.where(attendance.attendanceDate.eq(LocalDate.now().toDate()));

		List<StudentAttendance> attendances = attquery.fetch();

		Map<Long, StudentAttendance> attendanceMap = new HashMap<>();

		for (StudentAttendance attendanceRecord : attendances) {
			// if(attendanceRecord.getCheckout() == null)
			attendanceMap.put(attendanceRecord.getStudent().getId(), attendanceRecord);
		}

		List<DashStudentResponse> response = new ArrayList<>(students.size());

		for (Student s : students) {
			if (attendanceMap.containsKey(s.getId())) {
				response.add(new DashStudentResponse(attendanceMap.get(s.getId())));
			} else {
				response.add(new DashStudentResponse(s));
			}
		}
		return response;
	}

	public List<DashStaffResponse> listStaff(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		JPAQuery<EmployeeAttendance> attquery = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		QEmployeeAttendance attendance = QEmployeeAttendance.employeeAttendance;
		query.select(employee).from(employee).where(employee.active.isTrue()).where(employee.costCenter.in(centers));

		List<Employee> stafflist = query.fetch();

		attquery.select(attendance).from(attendance).where(attendance.employee.in(stafflist))
				.where(attendance.employee.costCenter.in(centers)).where(attendance.employee.active.isTrue())
				.where(attendance.attendanceDate.eq(LocalDate.now().toDate()));

		List<EmployeeAttendance> attendances = attquery.fetch();

		List<DashStaffResponse> response = new ArrayList<>(stafflist.size());

		if (getFreshUser().hasPrivilege("SALARY_READ")) {
			JPAQuery<EmployeeSalary> salaryquery = new JPAQuery<>(entityManager);

			QEmployeeSalary salary = QEmployeeSalary.employeeSalary;

			salaryquery.select(salary).from(salary).where(salary.employee.costCenter.in(centers))
					.where(salary.employee.in(stafflist)).where(salary.employee.active.isTrue());

			List<EmployeeSalary> salaries = salaryquery.fetch();
			Map<Long, EmployeeAttendance> attendanceMap = new HashMap<>();

			Map<Long, EmployeeSalary> salaryMap = new HashMap<>();
			for (EmployeeSalary sal : salaries) {
				salaryMap.put(sal.getEmployee().getId(), sal);
			}

			for (EmployeeAttendance attendanceRecord : attendances) {
				if (attendanceRecord.getCheckout() == null)
					attendanceMap.put(attendanceRecord.getEmployee().getId(), attendanceRecord);
			}

			for (Employee emp : stafflist) {
				if (salaryMap.containsKey(emp.getId()) && attendanceMap.containsKey(emp.getId())) {
					response.add(
							new DashStaffResponse(salaryMap.get(emp.getId()), attendanceMap.get(emp.getId()), emp));
				} else if (salaryMap.containsKey(emp.getId())) {
					response.add(new DashStaffResponse(salaryMap.get(emp.getId())));
				} else {
					response.add(new DashStaffResponse(emp));
				}
			}
		} else {
			for (Employee emp : stafflist) {
				response.add(new DashStaffResponse(emp));
			}
		}

		return response;
	}

	/**
	 * @param request filter criteria
	 * @return
	 */
	public List<DashStudentFeeResponse> listStudentFee(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		QStudentFee studentfee = QStudentFee.studentFee;
		query.select(studentfee).from(studentfee).where(studentfee.student.active.isTrue())
				.where(studentfee.student.center.in(centers));

		if (request.getFeeDuration() != null) {
			query.where(studentfee.feeDuration.eq(request.getFeeDuration()));
		}

		List<StudentFee> fees = query.fetch();

		// JPAQuery<StudentFeePayment> paymentquery = new JPAQuery<>(entityManager);
		// QStudentFeePayment payment = QStudentFeePayment.studentFeePayment;
		// paymentquery.select(payment).from(payment)
		// .where(payment.student.active.isTrue())
		// .where(payment.year.eq(LocalDate.now().getYear()))
		// .where(payment.month.eq(LocalDate.now().getMonthOfYear()))
		// .where(payment.paymentStatus.eq(PaymentStatus.Paid))
		// .where(payment.student.center.in(centers));
		// List<StudentFeePayment> payments = paymentquery.fetch();

		List<DashStudentFeeResponse> response = new ArrayList<>(fees.size());

		for (StudentFee fee : fees) {
			response.add(new DashStudentFeeResponse(fee));
		}

		return response;
	}

	public List<UserResponse> listUser(DashboardRequest request) {
		JPAQuery<User> query = new JPAQuery<User>(entityManager);
		QUser user = QUser.user;
		query.select(user).from(user).where(user.active.isTrue());
		// .where(user.centers.)
		return query.fetch().stream().map(UserResponse::new).collect(Collectors.toList());
	}

	public List<ParentSummaryResponse> listParents(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		JPAQuery<StudentParent> query = new JPAQuery<>(entityManager);
		QStudentParent parent = QStudentParent.studentParent;
		QStudent student = QStudent.student;

		query.select(parent).from(parent).innerJoin(parent.students, student).distinct()
				.where(student.center.in(centers)).where(student.active.isTrue());

		return query.fetch().stream().map(ParentSummaryResponse::new).collect(Collectors.toList());
	}

	public List<FollowUpReportResponse> followupReport(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		List<FollowUpReportResponse> list = new ArrayList<>();
		for (Center center : centers) {
			List<InquiryEventLog> logs = eventLogRepository.findByDoneFalseAndInquiryCenterAndCallDispositionIn(center,
					Arrays.asList(CallDisposition.Followup, CallDisposition.Callback, CallDisposition.NewInquiry,
							CallDisposition.Revisit));
			if (CollectionUtils.isEmpty(logs)) {
				continue;
			}
			FollowUpReportResponse response = new FollowUpReportResponse(center);

			int open = 0, today = 0, previous = 0;

			LocalDate now = LocalDate.now();
			for (InquiryEventLog log : logs) {
				Date date = log.getCallBack();
				if (date != null) {
					LocalDate callBackDate = LocalDate.fromDateFields(date);
					int diff = now.compareTo(callBackDate);
					if (diff == 0) {
						today++;
					} else if (diff > 0) {
						previous++;
					} else {
						open++;
					}
				}
			}
			response.setTodayFollowUps(today);
			response.setOpenFollowUps(open);
			response.setPreviousFollowUps(previous);

			list.add(response);
		}
		return list;
	}

	// -----------------------------------shubham---------------------------------------------------------------
	public List<DashStaffResponse> listStaffV2(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		List<DashStaffResponse> response = new ArrayList<>();
		JPAQuery<EmployeeSalary> salaryquery = new JPAQuery<>(entityManager);

		QEmployeeSalary salary = QEmployeeSalary.employeeSalary;

		salaryquery.select(salary).from(salary).where(salary.employee.costCenter.in(centers))
				.where(salary.employee.active.isTrue());

		List<EmployeeSalary> salaries = salaryquery.fetch();

		if (getFreshUser().hasPrivilege("SALARY_READ")) {
			response = salaries.stream().map(DashStaffResponse::new).collect(Collectors.toList());
		} else {
			List<Employee> employees = salaries.stream().map(salary1 -> salary1.getEmployee())
					.collect(Collectors.toList()); // getEmployee->salary1.getEmployee
			response = employees.stream().map(DashStaffResponse::new).collect(Collectors.toList());
		}
		return response;
	}

	public List<StaffNewJoinings> getNewJoinigList(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date backDate = cal.getTime();

		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.profile.doj.between(backDate, today))
				.where(employee.costCenter.in(centers));
		List<Employee> employees = query.fetch();
		return employees.stream().map(StaffNewJoinings::new).collect(Collectors.toList());
	}

	public long getRecruitmentHeadCount(List<Center> centers) {
		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.active.isTrue()).where(employee.profile.dol.isNotNull())
				.where(employee.costCenter.in(centers));
		return query.fetchCount();
	}

	public List<StaffNewLeavings> getRecruitmentHeadCountList(DashboardRequest request) {
		List<Center> centers = getCenters(request);

		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.active.isTrue()).where(employee.profile.dol.isNotNull())
				.where(employee.costCenter.in(centers));
		List<Employee> employees = query.fetch();
		return employees.stream().map(StaffNewLeavings::new).collect(Collectors.toList());
	}

	public List<StaffNewLeavings> getNewLeavingsList(DashboardRequest request) {
		List<Center> centers = getCenters(request);
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date backDate = cal.getTime();
		JPAQuery<Employee> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.profile.dol.between(backDate, today))
				.where(employee.costCenter.in(centers));
		List<Employee> employees = query.fetch();
		return employees.stream().map(StaffNewLeavings::new).collect(Collectors.toList());
	}

	private int countNewJoinigs(List<Center> centers) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date backDate = cal.getTime();

		JPAQuery<Integer> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.profile.doj.between(backDate, today))
				.where(employee.costCenter.in(centers));
		return (int) query.fetchCount();
	}

	private int countNewLeavings(List<Center> centers) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date backDate = cal.getTime();

		JPAQuery<Integer> query = new JPAQuery<>(entityManager);
		QEmployee employee = QEmployee.employee;
		query.select(employee).from(employee).where(employee.profile.dol.between(backDate, today))
				.where(employee.costCenter.in(centers));
		return (int) query.fetchCount();
	}

	public StatsResponse getStatsV2(DashboardRequest request) {
		Set<String> dashboards = new HashSet<>();
		for (Role role : getUser().getRoles()) {
			Collections.addAll(dashboards,
					StringUtils.isEmpty(role.getDashboard()) ? new String[0] : role.getDashboard().split(","));
		}

		dashboards = dashboards.stream().map(String::trim).collect(Collectors.toSet());

		StatsResponse response = new StatsResponse();
		List<Center> centers = getCenters(request);
		int studentCount = countStudents(centers);
		int corporateStudentCount = countStudents(centers, true);

		for (String dashboard : dashboards) {
			switch (dashboard) {
			case "user":
				response.setUsers(getUserCount(centers));
				break;
			case "student":
				// 1. total students
				response.setStudents(studentCount);
				response.setCorporateStudents(corporateStudentCount);

				// 2. present today
				int presentStudent = countPresentStudents(centers);
				response.setStudentPresent(presentStudent);
				break;
			case "parent":
				response.setParentUsers(getStudentParentCount(centers));
				break;
			case "center":
				// 0. total centers from query
				response.setCenters(centers.size());

				// 3. capacity and utilisation
				long capacity = countCenterCapacity(centers);
				response.setCapacity((int) capacity);
				response.setUtilisation((100.0 * studentCount) / (capacity * 1.0));

				break;
			case "staff":
				// 4. staff count, total staff cost
				int staffcount = countStaff(centers);
				response.setStaffCount(staffcount);

				// 6.
				int staffCost = staffCost(centers);
				response.setStaffCost(staffCost);

				int presentStaff = countPresentStaff(centers);
				response.setStaffPresent(presentStaff);

				int onLeaveStaff = countOnLeaveStaff(centers);
				response.setStaffOnLeave(onLeaveStaff);

				int absentStaff = countAbsentStaff(centers);
				response.setStaffAbsent(absentStaff);

				int newJoinnigs = countNewJoinigs(centers);
				response.setNewJoinings(newJoinnigs);

				int newLeavings = countNewLeavings(centers);
				response.setNewLeavings(newLeavings);

				long recruitmentCount = countStaff(centers) - getRecruitmentHeadCount(centers);
				response.setRecruitmentCount(recruitmentCount);
				break;
			case "followup":
				calculateFollowUps(request, response);
				break;
			}
		}
		return response;
	}

	public List<DashStudentResponse> allStudentList(DashboardRequest request) {
		List<Center> centers = getCenters(request);

		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		QStudent student = QStudent.student;

		query.select(student).from(student).where(student.active.isTrue()).where(student.center.in(centers));

		return query.fetch().stream().map(DashStudentResponse::new).collect(Collectors.toList());
	}

	public List<Employee> presentStaff(DashboardRequest request) {

		List<Center> centers = getCenters(request);

		JPAQuery<EmployeeAttendance> query = new JPAQuery<>(entityManager);
		QEmployeeAttendance attendance = QEmployeeAttendance.employeeAttendance;
		// QEmployee employee=QEmployee.employee;

		query.select(attendance).from(attendance).where(attendance.attendanceDate.eq(LocalDate.now().toDate()))
				.where(attendance.status.eq(AttendanceStatus.Present))
				.where(attendance.center.in(centers));

		List<EmployeeAttendance> attendances = query.fetch();

		List<Employee> employees = attendances.stream().map(a -> a.getEmployee()).collect(Collectors.toList());
		return employees;

	}

	public List<Employee> absentStaff(DashboardRequest request) {

		List<Center> centers = getCenters(request);

		List<Employee> employeeList = employeeRepository.findByActiveTrueAndCostCenterInOrderByIdAsc(centers);

		List<Employee> presentEmployees= presentStaff(request);
		List<Employee> onLeaveEmployees= onLeaveStaff(request);

		List<Employee> absentEmployees = employeeList.stream()
				.filter(e -> (!presentEmployees.contains(e) && !onLeaveEmployees.contains(e)))
				.collect(Collectors.toList());

		return absentEmployees;

	}

	public List<Employee> onLeaveStaff(DashboardRequest request) {
		List<Center> centers = getCenters(request);

		JPAQuery<EmployeeLeave> query = new JPAQuery<>(entityManager);
		QEmployeeLeave leaves = QEmployeeLeave.employeeLeave;

		List<EmployeeLeave> onLeaveEmployees = query.select(leaves).from(leaves)
				.where(leaves.date.eq(LocalDate.now().toDate()))
				.where(leaves.leaveStatus.in(LeaveStatus.Approved,LeaveStatus.Applied))
				.where(leaves.employee.costCenter.in(centers))
				.fetch();

		List<Employee> onLeave=onLeaveEmployees.stream().map(l->l.getEmployee()).collect(Collectors.toList());
		return onLeave;

	}

}
