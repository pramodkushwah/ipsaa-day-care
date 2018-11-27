package com.synlabs.ipsaa.controller;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.COLLECTION_FEE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.FEE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.INQUIRY_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_ATTENDANCE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STD_ATTENDANCE_REPORT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.InquiryEventLogRepository;
import com.synlabs.ipsaa.jpa.InquiryRepository;
import com.synlabs.ipsaa.jpa.ProgramGroupRepository;
import com.synlabs.ipsaa.jpa.ProgramRepository;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.FeeService;
import com.synlabs.ipsaa.service.InquiryService;
import com.synlabs.ipsaa.service.StaffAttendanceService;
import com.synlabs.ipsaa.service.StaffService;
import com.synlabs.ipsaa.service.StudentAttendanceService;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;
import com.synlabs.ipsaa.view.fee.FeeReportRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse2;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse3;
import com.synlabs.ipsaa.view.inquiry.InquiryReportRequest;
import com.synlabs.ipsaa.view.staff.StaffFilterRequest;

@RestController
@RequestMapping("/api/report/")
public class ReportController {

	@Autowired
	private StaffService staffService;
	@Autowired
	private StudentAttendanceService studentAttendanceService;

	@Autowired
	private StaffAttendanceService staffAttendanceService;
	@Autowired
	private FeeService feeService;
	@Autowired
	private StudentFeeRepository studentFeeRepository;

	@Autowired
	private InquiryService inquiryService;

	// Avneet
	@Autowired
	private CenterRepository centerRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProgramGroupRepository programGroupRepository;

	@Autowired
	private InquiryRepository inquiryRepository;

	@Autowired
	private InquiryEventLogRepository inquiryEventLogRepository;

	@PostMapping("studentattendance")
	@Secured(STD_ATTENDANCE_REPORT)
	public void studentAttendanceReport(@RequestBody AttendanceReportRequest request, HttpServletResponse response)
			throws IOException {
		File file = studentAttendanceService.attendanceReport(request);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", String.format("attachment; filename=Atten_Report_%s_%s_%s.xlsx",
				request.getCenterCode(), request.getFrom(), request.getTo()));
		response.setHeader("fileName", String.format("Atten_Report_%s_%s_%s.xlsx", request.getCenterCode(),
				BaseService.formatDate(request.getFrom()), BaseService.formatDate(request.getTo())));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);

		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("staffattendance")
	@Secured(STAFF_ATTENDANCE_REPORT)
	public void staffAttendanceReport(@RequestBody AttendanceReportRequest request, HttpServletResponse response)
			throws IOException {
		File file = staffAttendanceService.attendanceReport2(request); /// Avneet
		// File file = staffAttendanceService.attendanceReport(request);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", String.format("attachment; filename=Atten_Report_%s_%s_%s.xlsx",
				request.getCenterCode(), request.getFrom(), request.getTo()));
		response.setHeader("fileName", String.format("Atten_Report_%s_%s_%s.xlsx", request.getCenterCode(),
				BaseService.formatDate(request.getFrom()), BaseService.formatDate(request.getTo())));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);

		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	// @PostMapping("studentfee_v2")
	// @Secured(FEE_REPORT)
	// public void FeeReport(@RequestBody FeeReportRequest request,
	// HttpServletResponse response) throws IOException
	// {
	// File file = feeService.feeReport(request);
	// response.setContentType("application/octet-stream");
	// response.setHeader("Content-disposition", String.format("attachment;
	// filename=Fee_Report_%s.xlsx",
	// request.getCenterCode()));
	// response.setHeader("fileName", String.format("Fee_Report_%s.xlsx",
	// request.getCenterCode()));
	// OutputStream out = response.getOutputStream();
	// FileInputStream in = new FileInputStream(file);
	//
	// // copy from in to out
	// IOUtils.copy(in, out);
	// out.flush();
	// in.close();
	// if (!file.delete())
	// {
	// throw new IOException("Could not delete temporary file after processing: " +
	// file);
	// }
	// }
	// shubham

	@PostMapping("inquiry")
	@Secured(INQUIRY_REPORT)
	public void InquiryReport(@RequestBody InquiryReportRequest request, HttpServletResponse response)
			throws IOException {
		File file = inquiryService.inquiryReport(request);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", String.format("attachment; filename=Inquiry_Report_%s_%s_%s.xlsx",
				request.getCenterCode(), request.getFrom(), request.getTo()));
		response.setHeader("fileName", String.format("Inquiry_Report_%s_%s_%s.xlsx", request.getCenterCode(),
				BaseService.formatDate(request.getFrom()), BaseService.formatDate(request.getTo())));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);

		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("studentfee")
	@Secured(FEE_REPORT)
	public List<StudentFeeSlipResponse3> FeeReport(@RequestBody FeeReportRequest request, HttpServletResponse response)
			throws IOException {
		return feeService.feeReportTable2(request);
	}

	@PostMapping("studentfee/excel")
	@Secured(FEE_REPORT)
	public void FeeReportExcel(@RequestBody FeeReportRequest request, HttpServletResponse response) throws IOException {
		File file = feeService.FeeReport2(request);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition",
				String.format("attachment; filename=Fee_Report_%s.xlsx", request.getCenterCode()));
		response.setHeader("fileName", String.format("Fee_Report_%s.xlsx", request.getCenterCode()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("collectionfee")
	@Secured(COLLECTION_FEE_REPORT)
	public List<StudentFeeSlipResponse2> collectionFeeReport(HttpServletResponse response,
			@RequestBody StudentFeeSlipRequest slipRequest) throws IOException {
		return feeService.collectionFeeReportTable2(slipRequest);
	}

	@PostMapping("collectionfee/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void collectionFeeReportExcel(HttpServletResponse response, @RequestBody StudentFeeSlipRequest slipRequest)
			throws IOException {
		File file = feeService.studentFeeSheetReport(slipRequest);
		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				slipRequest.getCenterCode(), slipRequest.getPeriod(), slipRequest.getYear()));
		response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx", slipRequest.getCenterCode(),
				slipRequest.getPeriod(), slipRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("studentFeeSheetReport/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void studentFeeReportExcel(HttpServletResponse response, @RequestBody StudentFeeSlipRequest slipRequest)
			throws IOException {
		File file = feeService.collectionFeeReport2(slipRequest);
		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				slipRequest.getCenterCode(), slipRequest.getPeriod(), slipRequest.getYear()));
		response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx", slipRequest.getCenterCode(),
				slipRequest.getPeriod(), slipRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("collectionfee/hdfc")
	@Secured(COLLECTION_FEE_REPORT)
	public void collectionHdfcReportExcel(HttpServletResponse response, @RequestBody StudentFeeSlipRequest slipRequest)
			throws IOException {
		File file = feeService.collectionHdfcReport2(slipRequest);

		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				slipRequest.getCenterCode(), slipRequest.getPeriod(), slipRequest.getYear()));
		response.setHeader("fileName",
				String.format("%s_Quarter_%s_Year.xlsx", slipRequest.getQuarter(), slipRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	// shubham staff collection
	@PostMapping("staffCollection/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void staffCollectionExcel(HttpServletResponse response, @RequestBody StaffFilterRequest staffRequest)
			throws IOException {
		// modifiy by shubham
		File file = staffService.getEmployeeSalary(staffRequest);
		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				staffRequest.getEmployerCode(), staffRequest.getMonth(), staffRequest.getYear()));
		response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx", staffRequest.getEmployerCode(),
				staffRequest.getMonth(), staffRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("staff/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void staffExcel(HttpServletResponse response, @RequestBody StaffFilterRequest staffRequest)
			throws IOException {
		// modifiy by shubham
		int month = staffRequest.getMonth();
		// System.out.println(month);
		//// Avneet
		File file = staffRequest.getMonth() != 0 ? staffService.getAllEmployees(staffRequest)
				: staffService.getEmployee(staffRequest);

		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				staffRequest.getEmployerCode(), staffRequest.getMonth(), staffRequest.getYear()));
		response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx", staffRequest.getEmployerCode(),
				staffRequest.getMonth(), staffRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	// ---------------------------ipsaa
	// report------------------------------------------------------------

	@PostMapping("ipsaaclub/collectionfee/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void ipsaaclubCollectionFeeReportExcel(HttpServletResponse response,
			@RequestBody FeeReportRequest slipRequest) throws IOException {
		File file = feeService.ipsaaCollectionFeeReport2(slipRequest);
		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				slipRequest.getCenterCode(), slipRequest.getMonth(), slipRequest.getYear()));
		response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx", slipRequest.getCenterCode(),
				slipRequest.getMonth(), slipRequest.getYear()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

	@PostMapping("ipsaaclub/studentfee/excel")
	@Secured(FEE_REPORT)
	public void ipsaaClubFeeReportExcel(@RequestBody FeeReportRequest request, HttpServletResponse response)
			throws IOException {
		File file = feeService.FeeReportIpsaClub2(request);

		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition",
				String.format("attachment; filename=Fee_Report_%s.xlsx", request.getCenterCode()));
		response.setHeader("fileName", String.format("Fee_Report_%s.xlsx", request.getCenterCode()));
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		if (!file.delete()) {
			throw new IOException("Could not delete temporary file after processing: " + file);
		}
	}

}
