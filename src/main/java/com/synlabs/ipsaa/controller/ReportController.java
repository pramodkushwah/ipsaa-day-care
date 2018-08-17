package com.synlabs.ipsaa.controller;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.COLLECTION_FEE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.FEE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.INQUIRY_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_ATTENDANCE_REPORT;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STD_ATTENDANCE_REPORT;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import javax.servlet.http.HttpServletResponse;

import com.amazonaws.util.JodaTime;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.inquiry.Inquiry;
import com.synlabs.ipsaa.entity.inquiry.InquiryEventLog;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.enums.CallDisposition;
import com.synlabs.ipsaa.enums.InquiryType;
import com.synlabs.ipsaa.enums.LeadSource;
import com.synlabs.ipsaa.view.inquiry.InquiryRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.loader.plan.spi.LoadPlan;
import org.jxls.common.CellData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.jpa.EmployeePaySlipRepository;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.ProgramRepository;
import com.synlabs.ipsaa.jpa.ProgramGroupRepository;
import com.synlabs.ipsaa.jpa.InquiryRepository;
import com.synlabs.ipsaa.jpa.InquiryEventLogRepository;
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
    private FeeService feeService;
    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private InquiryService inquiryService;

    //Avneet
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

    //-----------------------------------------shubham ---------------------------------------------------------------


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
        response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx",
                slipRequest.getCenterCode(), slipRequest.getPeriod(), slipRequest.getYear()));
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
        response.setHeader("fileName", String.format("%s_Month_%s_Year_%s.xlsx",
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

    //////Avneet - Read from Excel file and write into database
    public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\avnib\\Downloads\\Master Web Leads.xlsx";

    @GetMapping("do")
    public void updateInquiry() throws IOException, InvalidFormatException, ParseException {
        File file = new File(SAMPLE_XLSX_FILE_PATH);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(inputStream);
        //Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

        try {


            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            InquiryRequest inquiryRequest = new InquiryRequest();
            Inquiry inquiry = new Inquiry();
            InquiryEventLog inquiryEventLog = new InquiryEventLog();
            List<InquiryEventLog> logs = new ArrayList<>();
            Row row;
            //int i=0;
            int n = sheet.getPhysicalNumberOfRows();
            String pattern = "dd-MM-yyyy HH:mm:ss";
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format3 = new SimpleDateFormat("HH:mm");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            List<LeadSource> list = new ArrayList<>();
            for (LeadSource value : LeadSource.values()) {
                list.add(value);
            }

            for (int i = 1; i <= 194; i++) {
                row = sheet.getRow(i);

                if (row != null) {

                    String leadsource = formatter.formatCellValue(row.getCell(1));
                    String inquiryType = formatter.formatCellValue(row.getCell(2));
                    String center = formatter.formatCellValue(row.getCell(3));

                    String program = formatter.formatCellValue(row.getCell(4));
                    String group = formatter.formatCellValue(row.getCell(5));
                    String childFirstName = formatter.formatCellValue(row.getCell(7));
                    String childLastName = formatter.formatCellValue(row.getCell(8));
                    String motherFirstName = formatter.formatCellValue(row.getCell(9));
                    String motherlastName = formatter.formatCellValue(row.getCell(10));
                    String motherEmail = formatter.formatCellValue(row.getCell(11));
                    String motherMobile = formatter.formatCellValue(row.getCell(12));
                    String deposition = formatter.formatCellValue(row.getCell(13));
                    String inquiryDateExcel = formatter.formatCellValue(row.getCell(6));
                    String callbackDateExcel = formatter.formatCellValue(row.getCell(14));
                    String callBackTime = formatter.formatCellValue(row.getCell(15));
                    //Date callBackTime=row.getCell(15).getDateCellValue();
                    String callBackNumber = formatter.formatCellValue(row.getCell(16));
                    String comments = formatter.formatCellValue(row.getCell(17));

                    Date inquiryDate = calculateDate(inquiryDateExcel, i);
                    Date callBackDate = calculateDate(callbackDateExcel, i);
                    String str[]=callBackTime.split(":");

                    Calendar cal= Calendar.getInstance();
                    cal.setTime(callBackDate);
                    cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str[0]));
                    cal.set(Calendar.MINUTE,Integer.parseInt(str[1]));
                    System.out.println(cal.getTime());

                    /*if (inquiryDate != null && !inquiryDate.isEmpty() && i<15) {
						Date date = format1.parse(inquiryDate);
						String d=format2.format(date);
						Date inquiryDate1 = format2.parse(d);
						//inquiryDate1=row.getCell(6).getDateCellValue();
						System.out.println(String.format("InquiryDate[%s]", inquiryDate + "    " + i+ "   "+inquiryDate1));
					}


					//String callBackDate = formatter.formatCellValue(row.getCell(14));
					if (callbackDate != null && !callbackDate.isEmpty()) {
					//	Date date = format1.parse(callbackDate);
						Date callbackDate1 =  format2.parse(callbackDate);;//for values above 15
						System.out.print(String.format("     [CallBackDate[%s]]",callbackDate1));
					}



					}
*/


                    if (center != null) {
                        Center iscenter = centerRepository.getOneByName(center);
                        if (iscenter != null) {
                            inquiry.setCenter(iscenter);

                            Program isProgram = programRepository.findByName(program);
                            if (isProgram != null) {
                                inquiry.setProgram(isProgram);
                            }

                            if (group != null && isProgram != null) {
                                List<ProgramGroup> groups = isProgram.getGroups();
                                ProgramGroup isGroup = programGroupRepository.findByName(group);
                                if (groups.contains(isGroup)) {
                                    inquiry.setGroup(isGroup);
                                }
                            }

                            inquiry.setFirstName(childFirstName);
                            inquiry.setLastName(childLastName);
                            inquiry.setMotherFirstName(motherFirstName);
                            inquiry.setMotherLastName(motherlastName);
                            inquiry.setMotherMobile(motherMobile);
                            inquiry.setMotherEmail(motherEmail);
                            inquiry.setInquiryType(InquiryType.valueOf(inquiryType));

                            if (list.contains(leadsource)) {
                                int j = list.indexOf(leadsource);
                                String enumLeadSource = list.get(j).toString();
                                inquiry.setLeadSource(list.get(j));
                            }

                            System.out.println(cal.getTime());
                            inquiryEventLog.setCallBackNumber(callBackNumber);
                            inquiryEventLog.setComment(comments);
                         //   inquiryEventLog.setCallBack(cal);
                            System.out.println(inquiry);
						/*try {
							date=simpleDateFormat.parse(inquiryDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						inquiry.setInquiryDate(date);

						try {
							date=simpleDateFormat.parse(callBackDate+" "+callBackTime);
						} catch (ParseException e) {
							e.printStackTrace();
						}	*/
                            //inquiryEventLog.setCallBack(date);



						/*System.out.println(inquiry.getChildName()+" "+inquiry.getInquiryDate()+" "+inquiryEventLog.getCallBack()+ i);
						inquiryRepository.saveAndFlush(inquiry);
						inquiryEventLog.setInquiry(inquiry);
						inquiryEventLogRepository.saveAndFlush(inquiryEventLog);*/
                        }

                    }
                    workbook.close();
                }
            }

                }catch(Exception e){
                    e.printStackTrace();
                }finally{

                    workbook.close();

                }
            }
            public Date calculateDate (String date,int i){

                Date correctedDate = null;
                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    if (date != null && !date.isEmpty() && i < 15) {

                        correctedDate = format1.parse(date);
                        String stringdate = format2.format(correctedDate);
                        correctedDate = format2.parse(stringdate);
                    } else {
                        if (date != null && !date.isEmpty()) {
                            correctedDate = format2.parse(date);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return correctedDate;
            }


        }
