package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.service.DocumentService;
import com.synlabs.ipsaa.service.StaffService;
import com.synlabs.ipsaa.util.ExcelExporter;
import com.synlabs.ipsaa.view.center.ApprovalCountResponse;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.staff.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

@RestController
@RequestMapping("/api/staff/")
public class StaffController
{
  @Autowired
  private StaffService staffService;

  @Autowired
  private CenterService centerService;

  @Autowired
  private DocumentService documentService;

  private ExcelExporter excelExporter=new ExcelExporter();

  //only user centers employees
  @Secured(STAFF_READ)
  @GetMapping
  public List<StaffSummaryResponse> list()
  {
    return staffService.list().stream().map(StaffSummaryResponse::new).collect(Collectors.toList());
  }

  //all employees
  @Secured(STAFF_READ)
  @GetMapping("/all/")
  public List<StaffSummaryResponse> listAll()
  {
    return staffService.listAll().stream().map(StaffSummaryResponse::new).collect(Collectors.toList());
  }
  @Secured(STAFF_READ)
  @GetMapping("/all/export")
  public String listAllExport()
  {
    excelExporter.createExcel(staffService.listAll().stream().map(StaffResponse::new).collect(Collectors.toList()));
    return "done";
  }

  @Secured(STAFF_READ)
  @GetMapping("/reporting/")
  public List<StaffSummaryResponse> listReporting()
  {
    return staffService.getReportingEmployees().stream().map(e -> new StaffSummaryResponse(e, true)).collect(Collectors.toList());
  }

  @Secured(STAFF_READ)
  @PostMapping("filter")
  public StaffSummaryPageResponse findByType(@RequestBody StaffFilterRequest request)
  {
    return staffService.list(request);
  }

  @DeleteMapping("{id}")
  @Secured(STAFF_WRITE)
  public void deleteStaff(@PathVariable Long id)
  {
    StaffRequest request = new StaffRequest();
    request.setId(id);
    staffService.delete(request);
  }

  @Secured(STAFF_READ)
  @GetMapping("{id}")
  public StaffResponse findEmployee(@PathVariable(name = "id") Long id)
  {
    StaffRequest request = new StaffRequest();
    request.setId(id);
    Employee employee = staffService.getEmployee(request);
    StaffResponse response = new StaffResponse(employee);
    if (!StringUtils.isEmpty(employee.getProfile().getImagePath()))
    {
      response.setStaffImageData(staffService.getStaffImageData(employee));
    }
    return response;
  }

  @Secured(STAFF_WRITE)
  @PutMapping
  public void updateStaff(@RequestBody StaffRequest request) throws ParseException
  {
    staffService.update(request);
  }

  @GetMapping("appt_letter/{employeeId}")
  @Secured(STAFF_READ)
  public void getAppointmentLetter(@PathVariable("employeeId") Long employeeId, HttpServletResponse response) throws IOException
  {
    InputStream in = documentService.generateAppointmentLetter(BaseService.unmask(employeeId));

    response.setContentType("application/octet-stream");
    response.setHeader("Content-disposition", String.format("attachment; filename=Appointment_Letter_%s.pdf",
                                                            employeeId));
    response.setHeader("fileName", String.format("Appointment_Letter_%s.pdf",
                                                 employeeId));
    OutputStream out = response.getOutputStream();

    IOUtils.copy(in, out);
    out.flush();
    in.close();
  }

  @Secured(STAFF_WRITE)
  @PostMapping
  public StaffResponse saveStaff(@RequestBody StaffRequest request) throws ParseException
  {
    return new StaffResponse(staffService.save(request));
  }

  @Secured(IMPORT_WRITE)
  @PostMapping(path = "import")
  public ResponseEntity<Map<String, String>> importRecords(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "true") String dryRun)
  {
    try
    {
      Map<String, String> map = staffService.importRecords(file, dryRun);
      String isSuccess = map.get("error");
      map.remove("error");
      if (isSuccess.equalsIgnoreCase("true"))
      {
        return new ResponseEntity<>(map, HttpStatus.EXPECTATION_FAILED);
      }
      return new ResponseEntity<>(map, HttpStatus.OK);
    }
    catch (Exception exp)
    {
      exp.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Secured(IMPORT_WRITE)
  @PostMapping(path = "import/salary")
  public ResponseEntity<Map<String, String>> importSalaries(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "true") String dryRun)
  {
    try
    {
      Map<String, String> map = staffService.importSalaries(file, dryRun);
      String isSuccess = map.get("error");
      map.remove("error");
      if (isSuccess.equalsIgnoreCase("true"))
      {
        return new ResponseEntity<>(map, HttpStatus.EXPECTATION_FAILED);
      }
      return new ResponseEntity<>(map, HttpStatus.OK);
    }
    catch (Exception exp)
    {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Secured("NO PERMISSION")
  //@Secured(IMPORT_READ)
  @GetMapping(path = "download")
  public void download(@RequestParam String token, @RequestParam(defaultValue = "ALL") String employeeType, HttpServletResponse response)
  {
    try
    {
      String filename = staffService.exportRecords(employeeType);
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment; filename=staff.xls");
      InputStream is = new FileInputStream(filename);
      IOUtils.copy(is, response.getOutputStream());
      response.flushBuffer();
    }
    catch (Exception exp)
    {
      response.setStatus(500);
    }
  }

  @PostMapping("/{empId}/profile-pic")
  @Secured(STAFF_WRITE)
  public void uploadProfilePic(@RequestParam("file") MultipartFile file, @PathVariable Long empId)
  {
    StaffRequest request = new StaffRequest();
    request.setId(empId);
    staffService.uploadStaffPic(request, file);
  }

  @GetMapping("approvals/count")
  @Secured(STAFF_APPROVAL_READ)
  public List<ApprovalCountResponse> countList()
  {
    return centerService.getStaffApprovalCount();
  }

  @GetMapping("approvals/{centerId}")
  @Secured(STAFF_APPROVAL_READ)
  public List<StaffSummaryResponse> list(@PathVariable("centerId") Long centerId)
  {
    CenterRequest request = new CenterRequest();
    request.setId(centerId);
    return staffService.newStaffApprovals(request).stream().map(StaffSummaryResponse::new).collect(Collectors.toList());
  }

  @GetMapping("approve/{staffId}")
  @Secured(STAFF_APPROVAL_WRITE)
  public void approveStaff(@PathVariable("staffId") Long staffId)
  {
    StaffRequest request = new StaffRequest();
    request.setId(staffId);
    staffService.approveStaff(request);
  }

  @GetMapping("reject/{staffId}")
  @Secured(STAFF_APPROVAL_WRITE)
  public void rejectStaff(@PathVariable("staffId") Long staffId)
  {
    StaffRequest request = new StaffRequest();
    request.setId(staffId);
    staffService.rejectStaff(request);
  }
}
