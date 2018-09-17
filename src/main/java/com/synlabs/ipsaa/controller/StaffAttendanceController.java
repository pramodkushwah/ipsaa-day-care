package com.synlabs.ipsaa.controller;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.HR_ADMIN;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_ATTENDANCE_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_CLOCKINOUT;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.BiometricAttendanceService;
import com.synlabs.ipsaa.service.StaffAttendanceService;
import com.synlabs.ipsaa.view.attendance.AttendancePullRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceFilterRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceRequest;
import com.synlabs.ipsaa.view.attendance.EmployeeAttendanceResponse;
import com.synlabs.ipsaa.view.attendance.HRAdminAttendanceRequest;

@RestController
@RequestMapping("/api/attendance/staff/")
public class StaffAttendanceController {

	@Autowired
	private StaffAttendanceService attendanceService;

	@Autowired
	private BiometricAttendanceService biometricAttendanceService;

	@PostMapping("hradmin")
	@Secured(HR_ADMIN)
	public EmployeeAttendanceResponse saveAttendance(@RequestBody HRAdminAttendanceRequest request) {
		return new EmployeeAttendanceResponse(attendanceService.hrAttendanceSave(request));
	}

	@DeleteMapping("hradmin/{id}")
	@Secured(HR_ADMIN)
	public void deleteAttendance(@PathVariable("id") Long id) {
		attendanceService.hrAttendanceDelete(BaseService.unmask(id));
	}

	@GetMapping
	@Secured(STAFF_ATTENDANCE_READ)
	public List<EmployeeAttendanceResponse> list() {
		return attendanceService.employeeAttendanceList().stream().map(EmployeeAttendanceResponse::new).collect(Collectors.toList());
	}

	@Secured(STAFF_CLOCKINOUT)
	@PostMapping("clockin")
	public void clockin(@RequestBody EmployeeAttendanceRequest request) {
		attendanceService.clockin(request);
	}

	@Secured(STAFF_CLOCKINOUT)
	@PostMapping("clockout")
	public void clockout(@RequestBody EmployeeAttendanceRequest request) {
		attendanceService.clockout(request);
	}

	@PostMapping
	@Secured(STAFF_ATTENDANCE_READ)
	public List<EmployeeAttendanceResponse> empAttendanceList(
			@RequestBody @Validated EmployeeAttendanceFilterRequest request) {
		return attendanceService.empAttendanceList(request).stream().map(EmployeeAttendanceResponse::new)
				.collect(Collectors.toList());
	}

	@PostMapping("pull")
	@Secured(STAFF_ATTENDANCE_READ)
	public void pullAttendance(@RequestBody AttendancePullRequest request, HttpServletResponse response)
			throws SQLException, ParseException, IOException {
		InputStream is = biometricAttendanceService.pullAttendance(request);
		String fileName = "file.xlsx";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", String.format("attachment; filename=%s", fileName));
		response.setHeader("fileName", fileName);

		OutputStream out = response.getOutputStream();
		IOUtils.copy(is, out);
		out.flush();
	}

	@PostMapping("/import/excel/")
	@Secured({ STAFF_CLOCKINOUT, STAFF_CLOCKINOUT })
	public void importStaffAttendanceExcel(@RequestParam("file") MultipartFile file) throws IOException {
		attendanceService.importAttendanceExcel(file);
	}
}
