package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.*;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;
import com.synlabs.ipsaa.view.fee.FeeReportRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.inquiry.InquiryReportRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

@RestController
@RequestMapping("/api/report/")
public class ReportController
{
  @Autowired
  private StudentAttendanceService studentAttendanceService;

  @Autowired
  private StaffAttendanceService staffAttendanceService;

  @Autowired
  private FeeService feeService;

  @Autowired
  private InquiryService inquiryService;

  @PostMapping("studentattendance")
  @Secured(STD_ATTENDANCE_REPORT)
  public void studentAttendanceReport(@RequestBody AttendanceReportRequest request, HttpServletResponse response) throws IOException
  {
    File file = studentAttendanceService.attendanceReport(request);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", String.format("attachment; filename=Atten_Report_%s_%s_%s.xlsx",
                                                            request.getCenterCode(), request.getFrom(), request.getTo()));
    response.setHeader("fileName", String.format("Atten_Report_%s_%s_%s.xlsx",
                                                 request.getCenterCode(),
                                                 BaseService.formatDate(request.getFrom()),
                                                 BaseService.formatDate(request.getTo())));
    OutputStream out = response.getOutputStream();
    FileInputStream in = new FileInputStream(file);

    // copy from in to out
    IOUtils.copy(in, out);
    out.flush();
    in.close();
    if (!file.delete())
    {
      throw new IOException("Could not delete temporary file after processing: " + file);
    }
  }

  @PostMapping("staffattendance")
  @Secured(STAFF_ATTENDANCE_REPORT)
  public void staffAttendanceReport(@RequestBody AttendanceReportRequest request, HttpServletResponse response) throws IOException
  {
    File file = staffAttendanceService.attendanceReport(request);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", String.format("attachment; filename=Atten_Report_%s_%s_%s.xlsx",
                                                            request.getCenterCode(), request.getFrom(), request.getTo()));
    response.setHeader("fileName", String.format("Atten_Report_%s_%s_%s.xlsx",
                                                 request.getCenterCode(),
                                                 BaseService.formatDate(request.getFrom()),
                                                 BaseService.formatDate(request.getTo())));
    OutputStream out = response.getOutputStream();
    FileInputStream in = new FileInputStream(file);

    // copy from in to out
    IOUtils.copy(in, out);
    out.flush();
    in.close();
    if (!file.delete())
    {
      throw new IOException("Could not delete temporary file after processing: " + file);
    }
  }

  @PostMapping("studentfee")
  @Secured(FEE_REPORT)
  public void FeeReport(@RequestBody FeeReportRequest request, HttpServletResponse response) throws IOException
  {
    File file = feeService.feeReport(request);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", String.format("attachment; filename=Fee_Report_%s.xlsx",
                                                            request.getCenterCode()));
    response.setHeader("fileName", String.format("Fee_Report_%s.xlsx",
                                                 request.getCenterCode()));
    OutputStream out = response.getOutputStream();
    FileInputStream in = new FileInputStream(file);

    // copy from in to out
    IOUtils.copy(in, out);
    out.flush();
    in.close();
    if (!file.delete())
    {
      throw new IOException("Could not delete temporary file after processing: " + file);
    }
  }

  @PostMapping("inquiry")
  @Secured(INQUIRY_REPORT)
  public void InquiryReport(@RequestBody InquiryReportRequest request, HttpServletResponse response) throws IOException
  {
    File file = inquiryService.inquiryReport(request);
    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", String.format("attachment; filename=Inquiry_Report_%s_%s_%s.xlsx",
                                                            request.getCenterCode(), request.getFrom(), request.getTo()));
    response.setHeader("fileName", String.format("Inquiry_Report_%s_%s_%s.xlsx",
                                                 request.getCenterCode(),
                                                 BaseService.formatDate(request.getFrom()),
                                                 BaseService.formatDate(request.getTo())));
    OutputStream out = response.getOutputStream();
    FileInputStream in = new FileInputStream(file);

    // copy from in to out
    IOUtils.copy(in, out);
    out.flush();
    in.close();
    if (!file.delete())
    {
      throw new IOException("Could not delete temporary file after processing: " + file);
    }
  }

  @PostMapping("collectionfee")
  @Secured(COLLECTION_FEE_REPORT)
  public void collectionFeeReport(HttpServletResponse response, @RequestBody StudentFeeSlipRequest slipRequest) throws IOException
  {
    File file = feeService.collectionFeeReport(slipRequest);
    String fileName = "";
    switch (slipRequest.getPeriod())
    {
      case "Monthly":
        fileName = String.format("%s_%s_Monthly_%s_%s.xlsx", slipRequest.getCenterCode(), slipRequest.getReportType(), slipRequest.getMonth(), slipRequest.getYear());
        break;
      case "Quarterly":
        fileName = String.format("%s_%s_Quarterly_%s_%s.xlsx", slipRequest.getCenterCode(), slipRequest.getReportType(), slipRequest.getQuarter(), slipRequest.getYear());
        break;
      case "Yearly":
        fileName = String.format("%s_%s_Yearly_%s.xlsx", slipRequest.getCenterCode(), slipRequest.getReportType(), slipRequest.getYear());
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
    if (!file.delete())
    {
      throw new IOException("Could not delete temporary file after processing: " + file);
    }
  }

}
