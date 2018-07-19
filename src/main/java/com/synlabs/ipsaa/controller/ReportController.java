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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.jpa.EmployeePaySlipRepository;
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
import com.synlabs.ipsaa.view.report.excel.StaffExcelReport;
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
	private EmployeePaySlipRepository employeePaySlipRepository;

	@Value("${ipsaa.export.directory}")
	private String exportDir;

	@Autowired
	private FeeService feeService;
	@Autowired
	private StudentFeeRepository studentFeeRepository;

	@Autowired
	private InquiryService inquiryService;

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
		File file = staffAttendanceService.attendanceReport(request);
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

	// @PostMapping("studentfee")
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
	@PostMapping("studentfee")
	@Secured(FEE_REPORT)
	public List<StudentFeeSlipResponse3> FeeReport(@RequestBody FeeReportRequest request, HttpServletResponse response)
			throws IOException {
		return feeService.FeeReportTable2(request);
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

	// shubham
	@PostMapping("collectionfee")
	@Secured(COLLECTION_FEE_REPORT)
	public List<StudentFeeSlipResponse2> collectionFeeReport(HttpServletResponse response,
			@RequestBody StudentFeeSlipRequest slipRequest) throws IOException {
		return feeService.collectionFeeReportTable2(slipRequest);
	}

	// // shubham temp
	// @GetMapping("collectionfee/export")
	// @Secured(COLLECTION_FEE_REPORT)
	// public String collectionFeeReportExport() throws IOException
	// {
	// StudentFeeExcelReport report=new
	// StudentFeeExcelReport(feeService.getStudentFeeList(),studentFeeRepository);
	// report.createExcel();
	// return "done";
	// }
	// shubham
	@PostMapping("collectionfee/excel")
	@Secured(COLLECTION_FEE_REPORT)
	public void collectionFeeReportExcel(HttpServletResponse response, @RequestBody StudentFeeSlipRequest slipRequest)
			throws IOException {
		// modifiy by shubham
		File file = feeService.collectionFeeReport2(slipRequest);
		String fileName = "";
		switch (slipRequest.getPeriod()) {
		case "Monthly":
			fileName = String.format("%s_%s_Monthly_%s_%s.xlsx", slipRequest.getCenterCode(),
					slipRequest.getReportType(), slipRequest.getMonth(), slipRequest.getYear());
			break;
		case "Quarterly":
			fileName = String.format("%s_Quarterly_%s_%s.xlsx", slipRequest.getCenterCode(), slipRequest.getQuarter(),
					slipRequest.getYear());
			break;
		case "Yearly":
			fileName = String.format("%s_%s_Yearly_%s.xlsx", slipRequest.getCenterCode(), slipRequest.getReportType(),
					slipRequest.getYear());
			break;
		}
		response.setHeader("Content-disposition", String.format("attachment; filename=%s", fileName));
		response.setHeader("fileName", fileName);

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
		List<EmployeeSalary> list = staffService.getEmployeeSalary();
		if (staffRequest.getCenterCode() != null)
			list.stream().filter(e -> e.getEmployee().getCostCenter().getName().equals(staffRequest.getCenterCode()));

		StaffExcelReport excel = new StaffExcelReport(list, staffRequest, exportDir, employeePaySlipRepository);
		File file = excel.createExcel();

		// File file = feeService.collectionFeeReport2(slipRequest);
		// String fileName = "";
		// switch (slipRequest.getPeriod()) {
		// case "Monthly":
		// fileName = String.format("%s_%s_Monthly_%s_%s.xlsx",
		// slipRequest.getCenterCode(), slipRequest.getReportType(),
		// slipRequest.getMonth(), slipRequest.getYear());
		// break;
		// case "Quarterly":
		// fileName = String.format("%s_Quarterly_%s_%s.xlsx",
		// slipRequest.getCenterCode(), slipRequest.getQuarter(),
		// slipRequest.getYear());
		// break;
		// case "Yearly":
		// fileName = String.format("%s_%s_Yearly_%s.xlsx", slipRequest.getCenterCode(),
		// slipRequest.getReportType(), slipRequest.getYear());
		// break;
		// }

		response.setHeader("Content-disposition", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				staffRequest.getEmployerCode(), staffRequest.getMonth(), staffRequest.getYear()));
		response.setHeader("fileName", String.format("attachment; filename=%s_Month_%s_Year_%s.xlsx",
				staffRequest.getEmployerCode(), staffRequest.getMonth(), staffRequest.getYear()));
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
	// shubham
	// @PostMapping("collectionfee2")
	// @Secured(COLLECTION_FEE_REPORT)
	// public void FeeReport2(HttpServletResponse response, @RequestBody
	// StudentFeeSlipRequest slipRequest) throws IOException
	// {
	// File file = feeService.collectionFeeReport(slipRequest);
	// String fileName = "";
	// switch (slipRequest.getPeriod())
	// {
	// case "Monthly":
	// fileName = String.format("%s_%s_Monthly_%s_%s.xlsx",
	// slipRequest.getCenterCode(), slipRequest.getReportType(),
	// slipRequest.getMonth(), slipRequest.getYear());
	// break;
	// case "Quarterly":
	// fileName = String.format("%s_%s_Quarterly_%s_%s.xlsx",
	// slipRequest.getCenterCode(), slipRequest.getReportType(),
	// slipRequest.getQuarter(), slipRequest.getYear());
	// break;
	// case "Yearly":
	// fileName = String.format("%s_%s_Yearly_%s.xlsx", slipRequest.getCenterCode(),
	// slipRequest.getReportType(), slipRequest.getYear());
	// break;
	// }
	// response.setHeader("Content-disposition", String.format("attachment;
	// filename=%s", fileName));
	// response.setHeader("fileName", fileName);
	//
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

}
