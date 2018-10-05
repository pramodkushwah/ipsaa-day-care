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
import java.util.Calendar;
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

	// -----------------------------------------shubham
	// ---------------------------------------------------------------

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
		int month=staffRequest.getMonth();
		//System.out.println(month);
		File file = staffRequest.getMonth()!= 0 ? staffService.getAllEmployees(staffRequest):
												staffService.getEmployee(staffRequest);
		//File file=staffService.getAllEmployees(staffRequest);

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



	// TO BE REMOVED////Avneet - Read from Excel file and write into database
	/*
	 * public static final String SAMPLE_XLSX_FILE_PATH =
	 * "C:\\Users\\avnib\\Downloads\\Master Web Leads.xlsx";
	 * 
	 * @GetMapping("do")
	 * 
	 * @Transactional public void updateInquiry() throws IOException,
	 * InvalidFormatException, ParseException { File file = new
	 * File(SAMPLE_XLSX_FILE_PATH); FileInputStream inputStream = new
	 * FileInputStream(file); Workbook workbook =
	 * WorkbookFactory.create(inputStream);int c=0; String centercode; //Workbook
	 * workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
	 * 
	 * try {
	 * 
	 * 
	 * Sheet sheet = workbook.getSheetAt(0); DataFormatter formatter = new
	 * DataFormatter(); InquiryRequest inquiryRequest = new InquiryRequest();
	 * 
	 * //String city; String zip; String state;
	 * 
	 * Row row; //int i=0; int n = sheet.getPhysicalNumberOfRows(); String pattern =
	 * "dd-MM-yyyy HH:mm:ss"; SimpleDateFormat format1 = new
	 * SimpleDateFormat("MM/dd/yyyy"); SimpleDateFormat format2 = new
	 * SimpleDateFormat("dd/MM/yyyy"); SimpleDateFormat format3 = new
	 * SimpleDateFormat("HH:mm");
	 * 
	 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	 * 
	 * List<String> list = new ArrayList<>(); for (LeadSource value :
	 * LeadSource.values()) { list.add(value.toString()); }
	 * 
	 * List<CallDisposition> callDispositionList=new ArrayList<>();
	 * callDispositionList.add(CallDisposition.Followup);
	 * callDispositionList.add(CallDisposition.Enrolled);
	 * 
	 * for (int i = 1 ; i <= 194; i++) { row = sheet.getRow(i);
	 * 
	 * if (row != null) {
	 * 
	 * //formatter.formatCellValue(row.getCell(0));
	 * //System.out.println(inquiryNumber);
	 * 
	 * String leadsource = formatter.formatCellValue(row.getCell(1)).toUpperCase();
	 * String inquiryType = formatter.formatCellValue(row.getCell(2)); String center
	 * = formatter.formatCellValue(row.getCell(3));
	 * 
	 * String program = formatter.formatCellValue(row.getCell(4)); String group =
	 * formatter.formatCellValue(row.getCell(5)); String childFirstName =
	 * formatter.formatCellValue(row.getCell(7)); String childLastName =
	 * formatter.formatCellValue(row.getCell(8)); String motherFirstName =
	 * formatter.formatCellValue(row.getCell(9)); String motherlastName =
	 * formatter.formatCellValue(row.getCell(10)); String motherEmail =
	 * formatter.formatCellValue(row.getCell(11)); String motherMobile =
	 * formatter.formatCellValue(row.getCell(12)); String deposition =
	 * formatter.formatCellValue(row.getCell(13)); String inquiryDateExcel =
	 * formatter.formatCellValue(row.getCell(6)); String callbackDateExcel =
	 * formatter.formatCellValue(row.getCell(14)); // SimpleDateFormat format=new
	 * SimpleDateFormat("dd/MM/yyyy"); // String
	 * date=format.format(callbackDateExcel); String callBackTime =
	 * formatter.formatCellValue(row.getCell(15)); //Date
	 * callBackTime=row.getCell(15).getDateCellValue(); String callBackNumber =
	 * formatter.formatCellValue(row.getCell(16)); String comments =
	 * formatter.formatCellValue(row.getCell(17)); String
	 * location=formatter.formatCellValue(row.getCell(18));
	 * 
	 * 
	 * Date inquiryDate = calculateDate(inquiryDateExcel, i); Date callBackDate =
	 * calculateDate(callbackDateExcel, i); String str[]=callBackTime.split(":");
	 * 
	 * Calendar cal= Calendar.getInstance();
	 * if(!org.apache.commons.lang3.StringUtils.isEmpty(callbackDateExcel) ){
	 * cal.setTime(callBackDate); } if(
	 * !org.apache.commons.lang3.StringUtils.isEmpty(callBackTime)){
	 * cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str[0]));
	 * cal.set(Calendar.MINUTE,Integer.parseInt(str[1])); //
	 * System.out.println(cal.getTime()); }
	 * 
	 * 
	 * 
	 * switch(location){ case "Delhi": state="Delhi"; break; case "Mumbai":
	 * state="Maharashtra"; break; case "Pune": state="Maharashtra"; break; case
	 * "Chennai": state="Delhi"; break; case "Gurgaon": state="Harayana"; break;
	 * case "Bengaluru": state="Karnataka"; break; case "Dombvili":
	 * state="Maharashtra";break; case "Thane": state="Maharashtra";break; case
	 * "Noida": state="Uttar Pradesh"; break; default: state=location; break;
	 * 
	 * }
	 * 
	 * switch(state){ case "Delhi": zip="1100xx"; break; case "Maharashtra":
	 * zip="400xxx"; break; case "Karnataka": zip="5xxxxx"; break; case
	 * "Uttar Pradesh": zip="201301"; break; case "Harayana": zip="122xxx"; break;
	 * default: zip="xxxxxx"; break; }
	 * 
	 * 
	 * 
	 * if (!deposition.isEmpty() &&
	 * callDispositionList.contains(CallDisposition.valueOf(deposition))) { Inquiry
	 * inquiry = new Inquiry(); InquiryEventLog inquiryEventLog = new
	 * InquiryEventLog(); List<InquiryEventLog> logs = new ArrayList<>(); Address
	 * address = new Address();
	 * 
	 * 
	 * Center iscenter = centerRepository.getOneByName(center); if (iscenter !=
	 * null) { inquiry.setCenter(iscenter); centercode = iscenter.getCode();//r }
	 * 
	 * Program isProgram = programRepository.findByCode(program); if (isProgram !=
	 * null) { inquiry.setProgram(isProgram); }
	 * 
	 * if (group != null && isProgram != null) { List<ProgramGroup> groups =
	 * isProgram.getGroups(); ProgramGroup isGroup =
	 * programGroupRepository.findByName(group); if (groups.contains(isGroup)) {
	 * inquiry.setGroup(isGroup); } }
	 * 
	 * inquiry.setInquiryNumber(inquiryType + " " + c);
	 * inquiry.setFirstName(childFirstName); inquiry.setLastName(childLastName);
	 * inquiry.setFatherFirstName(motherFirstName);
	 * inquiry.setFatherLastName(motherlastName);
	 * inquiry.setFatherMobile(motherMobile);
	 * inquiry.setMotherFirstName(motherFirstName);
	 * inquiry.setMotherLastName(motherlastName);
	 * inquiry.setMotherMobile(motherMobile); inquiry.setMotherEmail(motherEmail);
	 * inquiry.setInquiryType(InquiryType.valueOf(inquiryType));
	 * //inquiry.setLeadSource(LeadSource.valueOf(leadsource));
	 * inquiry.setInquiryDate(inquiryDate);
	 * inquiry.setStatus(CallDisposition.valueOf(deposition));
	 * 
	 * address.setAddress(location); address.setCity(location);
	 * address.setState(state); address.setZipcode(zip);
	 * address.setPhone(callBackNumber); address.setAddressType(AddressType.Home);
	 * 
	 * inquiry.setLeadSource(LeadSource.valueOf(leadsource.toUpperCase()));
	 * //System.out.println(cal.getTime());
	 * inquiryEventLog.setCallDisposition(CallDisposition.valueOf(deposition));
	 * inquiryEventLog.setCallBack(cal.getTime());
	 * inquiryEventLog.setCallBackNumber(callBackNumber);
	 * inquiryEventLog.setComment(comments); c++;
	 * //System.out.println(inquiry.getInquiryNumber()+" "+inquiry.
	 * getResidentialAddress()+"  "+inquiry.getChildName()+"  "+inquiry.
	 * getFatherFirstName()+"  "+ inquiry.getInquiryDate()+"  "+inquiry.getStatus()+
	 * "     "+ inquiryEventLog.getCallBack()+"     "+ i+ " "+c);
	 * logs.add(inquiryEventLog); inquiry.setResidentialAddress(address);
	 * inquiry.setLogs(logs); inquiryEventLog.setInquiry(inquiry);
	 * System.out.println(inquiry.getLeadSource()+" "+inquiry.getInquiryNumber() +
	 * " " + inquiry.getResidentialAddress() + "  " + inquiry.getChildName() + "  "
	 * + inquiry.getFatherFirstName() + "  " + inquiry.getInquiryDate() + "  " +
	 * inquiry.getStatus() + "     " + inquiryEventLog.getCallBack() + "     " + i +
	 * " " + c);
	 * 
	 * inquiryRepository.saveAndFlush(inquiry);
	 * inquiryEventLogRepository.saveAndFlush(inquiryEventLog);
	 * 
	 * } } workbook.close();
	 * 
	 * }
	 * 
	 * 
	 * System.out.println(c);
	 * 
	 * 
	 * }catch(Exception e){ e.printStackTrace(); }finally{
	 * 
	 * workbook.close();
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * public Date calculateDate (String date,int i){
	 * 
	 * Date correctedDate = null; SimpleDateFormat format1 = new
	 * SimpleDateFormat("MM/dd/yyyy"); SimpleDateFormat format2 = new
	 * SimpleDateFormat("dd/MM/yyyy"); try { if (date != null && !date.isEmpty() &&
	 * i < 15) {
	 * 
	 * correctedDate = format1.parse(date); String stringdate =
	 * format2.format(correctedDate); correctedDate = format2.parse(stringdate); }
	 * else { if (date != null && !date.isEmpty()) { correctedDate =
	 * format2.parse(date); } }
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); } return correctedDate; }
	 */
	/////// TO BE REMOVED
	/*
	 * public static final String SAMPLE_XLSX_FILE_PATH1 =
	 * "C:\\Users\\avnib\\Downloads\\MWL4.xlsx";
	 * 
	 * @GetMapping("do/")
	 * 
	 * @Transactional public void updateInquiry1() throws IOException,
	 * InvalidFormatException, ParseException { File file = new
	 * File(SAMPLE_XLSX_FILE_PATH1); FileInputStream inputStream = new
	 * FileInputStream(file); Workbook workbook =
	 * WorkbookFactory.create(inputStream); int c=0; // String centercode;
	 * //Workbook workbook = WorkbookFactory.create(new
	 * File(SAMPLE_XLSX_FILE_PATH));
	 * 
	 * try {
	 * 
	 * 
	 * Sheet sheet = workbook.getSheetAt(1); DataFormatter formatter = new
	 * DataFormatter(); InquiryRequest inquiryRequest = new InquiryRequest(); Row
	 * row; String zip; String state;
	 * 
	 * String pattern = "dd-MM-yyyy HH:mm:ss"; SimpleDateFormat format1 = new
	 * SimpleDateFormat("MM/dd/yyyy");
	 * 
	 * List<CallDisposition> callDispositionList=new ArrayList<>();
	 * callDispositionList.add(CallDisposition.Followup);
	 * callDispositionList.add(CallDisposition.Enrolled);
	 * 
	 * for (int i = 1; i <= 208; i++) { row = sheet.getRow(i);
	 * 
	 * if (row != null) {
	 * 
	 * 
	 * 
	 * String leadsource = formatter.formatCellValue(row.getCell(1)).toUpperCase();
	 * String inquiryType = formatter.formatCellValue(row.getCell(2)); String center
	 * = formatter.formatCellValue(row.getCell(3));
	 * 
	 * String program = formatter.formatCellValue(row.getCell(4)); String group =
	 * formatter.formatCellValue(row.getCell(5)); String childFirstName =
	 * formatter.formatCellValue(row.getCell(7)); String childLastName =
	 * formatter.formatCellValue(row.getCell(8)); String motherFirstName =
	 * formatter.formatCellValue(row.getCell(9)); String motherlastName =
	 * formatter.formatCellValue(row.getCell(10)); String motherEmail =
	 * formatter.formatCellValue(row.getCell(11)); String motherMobile =
	 * formatter.formatCellValue(row.getCell(12));String deposition; String
	 * diposition = (formatter.formatCellValue(row.getCell(13)));
	 * if(diposition.equals("Walk In")){ deposition="Followup"; }else{
	 * deposition=(formatter.formatCellValue(row.getCell(13))); }
	 * 
	 * 
	 * 
	 * 
	 * Date callBackDate=
	 * org.apache.commons.lang3.StringUtils.isEmpty(formatter.formatCellValue(row.
	 * getCell(14)))? null: row.getCell(14).getDateCellValue(); Date inquiryDate=
	 * org.apache.commons.lang3.StringUtils.isEmpty(formatter.formatCellValue(row.
	 * getCell(6)))? null: row.getCell(6).getDateCellValue();;
	 * 
	 * 
	 * String callBackTime = formatter.formatCellValue(row.getCell(15)); String
	 * callBackNumber = formatter.formatCellValue(row.getCell(16)); String comments
	 * = formatter.formatCellValue(row.getCell(17)); String
	 * location=formatter.formatCellValue(row.getCell(18));
	 * 
	 * String str[]=callBackTime.split(":");
	 * 
	 * Calendar cal= Calendar.getInstance(); if(callBackDate!=null){
	 * 
	 * cal.setTime(callBackDate); }
	 * 
	 * if(!org.apache.commons.lang3.StringUtils.isEmpty(callBackTime)){
	 * cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str[0]));
	 * cal.set(Calendar.MINUTE,Integer.parseInt(str[1])); //
	 * System.out.println(cal.getTime()); }
	 * 
	 * 
	 * 
	 * switch(location){ case "Delhi": state="Delhi"; break; case "Mumbai":
	 * state="Maharashtra"; break; case "Pune": state="Maharashtra"; break; case
	 * "Chennai": state="Delhi"; break; case "Gurgaon": state="Harayana"; break;
	 * case "Bengaluru": state="Karnataka"; break; case "Dombvili":
	 * state="Maharashtra";break; case "Thane": state="Maharashtra";break; case
	 * "Noida": state="Uttar Pradesh"; break; default: state=location; break;
	 * 
	 * }
	 * 
	 * switch(state){ case "Delhi": zip="1100xx"; break; case "Maharashtra":
	 * zip="400xxx"; break; case "Karnataka": zip="5xxxxx"; break; case
	 * "Uttar Pradesh": zip="201301"; break; case "Harayana": zip="122xxx"; break;
	 * default: zip="xxxxxx"; break; }
	 * 
	 * 
	 * 
	 * if (!deposition.isEmpty() &&
	 * callDispositionList.contains(CallDisposition.valueOf(deposition))) { Inquiry
	 * inquiry = new Inquiry(); InquiryEventLog inquiryEventLog = new
	 * InquiryEventLog(); List<InquiryEventLog> logs = new ArrayList<>(); Address
	 * address = new Address();
	 * 
	 * 
	 * Center iscenter = centerRepository.getOneByName(center); if (iscenter !=
	 * null) { inquiry.setCenter(iscenter);
	 * 
	 * }
	 * 
	 * Program isProgram = programRepository.findByCode(program); if (isProgram !=
	 * null) { inquiry.setProgram(isProgram); }
	 * 
	 * if (group != null && isProgram != null) { List<ProgramGroup> groups =
	 * isProgram.getGroups(); ProgramGroup isGroup =
	 * programGroupRepository.findByName(group); if (groups.contains(isGroup)) {
	 * inquiry.setGroup(isGroup); } }
	 * 
	 * UUID uuid=UUID.randomUUID();
	 * inquiry.setInquiryNumber(inquiryType+" "+uuid.toString().substring(0,4));
	 * inquiry.setFirstName(childFirstName); inquiry.setLastName(childLastName);
	 * inquiry.setFatherFirstName(motherFirstName);
	 * inquiry.setFatherLastName(motherlastName);
	 * inquiry.setFatherMobile(motherMobile);
	 * inquiry.setMotherFirstName(motherFirstName);
	 * inquiry.setMotherLastName(motherlastName);
	 * inquiry.setMotherMobile(motherMobile); inquiry.setMotherEmail(motherEmail);
	 * inquiry.setInquiryType(InquiryType.valueOf(inquiryType));
	 * 
	 * if(inquiryDate!=null){ inquiry.setInquiryDate(inquiryDate); }
	 * inquiry.setStatus(CallDisposition.valueOf(deposition));
	 * 
	 * address.setAddress(location); address.setCity(location);
	 * address.setState(state); address.setZipcode(zip);
	 * address.setPhone(callBackNumber); address.setAddressType(AddressType.Home);
	 * 
	 * 
	 * inquiry.setLeadSource(LeadSource.valueOf(leadsource.toUpperCase()));
	 * inquiryEventLog.setCallDisposition(CallDisposition.valueOf(deposition));
	 * inquiryEventLog.setCallBack(cal.getTime());
	 * inquiryEventLog.setCallBackNumber(callBackNumber);
	 * inquiryEventLog.setComment(comments); c++;
	 * 
	 * logs.add(inquiryEventLog); inquiry.setResidentialAddress(address);
	 * inquiry.setLogs(logs); inquiryEventLog.setInquiry(inquiry);
	 * System.out.println(inquiry.getLeadSource()+" "+inquiry.getInquiryNumber() +
	 * " " + inquiry.getResidentialAddress() + "  " + inquiry.getChildName() + "  "
	 * + inquiry.getFatherFirstName() + "  " + inquiry.getInquiryDate() + "  " +
	 * inquiry.getStatus() + "     " + inquiryEventLog.getCallBack() + "     " + i +
	 * " " + c);
	 * 
	 * inquiryRepository.saveAndFlush(inquiry);
	 * inquiryEventLogRepository.saveAndFlush(inquiryEventLog);
	 * System.out.println("Value inserted "+c);
	 * 
	 * } } workbook.close();
	 * 
	 * }System.out.println(c);
	 * 
	 * 
	 * }catch(Exception e){ e.printStackTrace(); }finally{
	 * 
	 * workbook.close();
	 * 
	 * } }
	 */

	///////////// For futhur use//?excel file will bedirectly uploaded on portal?
	/*
	 * public static final String SAMPLE_XLSX_FILE_PATH2 =
	 * "C:\\Users\\avnib\\Downloads\\MWL4.xlsx";
	 * 
	 * @GetMapping("upload") public void uploadInquiry() throws IOException,
	 * InvalidFormatException, ParseException { inquiryService.uploadFromExcel(); }
	 */
}
