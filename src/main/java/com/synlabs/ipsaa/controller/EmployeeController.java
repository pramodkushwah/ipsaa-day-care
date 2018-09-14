package com.synlabs.ipsaa.controller;

import com.itextpdf.text.DocumentException;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.EmployeeService;
import com.synlabs.ipsaa.service.PaySlipService;
import com.synlabs.ipsaa.view.staff.*;
import com.synlabs.ipsaa.view.student.EmployeeSalaryRequest;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

/**
 * Created by ttn on 18/5/17.
 */

@RestController
@RequestMapping("/api/employee/")
public class EmployeeController
{

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private PaySlipService paySlipService;

  @Secured(SALARY_READ)
  @GetMapping("/salary/{eid}")
  public EmployeeSalaryResponse getSalary(@PathVariable(name = "eid") String eid)
  {
    EmployeeSalaryRequest request = new EmployeeSalaryRequest();
    request.setEid(eid);
    return new EmployeeSalaryResponse(employeeService.getSalary(request));
  }

  @Secured(SALARY_READ)
  @GetMapping("salary")
  public List<EmployeeSalaryResponse> list()
  {
    return employeeService.list().stream().map(EmployeeSalaryResponse::new).collect(Collectors.toList());
  }

  @Secured(SALARY_READ)
  @PostMapping("salary")
  public EmployeeSalaryPageResponse list(@RequestBody EmployeeSalaryFilterRequest request)
  {
    return new EmployeeSalaryPageResponse(employeeService.list(request));
  }

  @Secured(SALARY_READ)
  @PostMapping("payslip")
  public List<EmployeePaySlipResponse> list(@RequestParam Integer month, @RequestParam Integer year, @RequestParam String employerId) throws ParseException
  {
    return paySlipService.listPayslips(month, year, employerId).stream().map(EmployeePaySlipResponse::new).collect(Collectors.toList());
  }

  @Secured(SALARY_WRITE)
  @PostMapping("salary/update")
  public EmployeeSalaryResponse save(@RequestBody EmployeeSalaryRequest request)
  {
    return new EmployeeSalaryResponse(employeeService.save(request));
  }

  @Secured(PAYSLIP_READ)
  @GetMapping("/payslip/pdf/{id}")
  public void downloadPaySlip(@PathVariable(name = "id") Long id, HttpServletResponse response) throws IOException, DocumentException
  {
    InputStream is = paySlipService.generatePayslipPdf(BaseService.unmask(id));

    response.setContentType("application/octet-stream");
    String fileName = "Payslip.pdf";
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    response.setHeader("fileName", fileName);

    OutputStream out = response.getOutputStream();
    IOUtils.copy(is, out);
    out.flush();
    is.close();
  }

  @Secured(PAYSLIP_WRITE)
  @PutMapping("/payslip/")
  public EmployeePaySlipResponse update(@RequestBody EmployeePaySlipRequest request) throws IOException, DocumentException
  {
    return new EmployeePaySlipResponse(paySlipService.updatePaySlip(request));
  }

  // shubham
  @Secured(PAYSLIP_WRITE)
  @PutMapping("/payslip/upload")
  public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file,@RequestParam Integer month, @RequestParam Integer year, @RequestParam String employerId) throws IOException, DocumentException
  {
    //to upload present days excel
    try {
      Map<String, Object> map = paySlipService.uploadData(file,month,year,employerId);
      String isSuccess = (String)map.get("error");
      map.remove("error");
      if (isSuccess.equalsIgnoreCase("true"))
      {
        throw new ValidationException("file not uploaded");
      }
      return new ResponseEntity<>(map,HttpStatus.OK);
    } catch (InvalidFormatException e) {
      throw new ValidationException("file not uploaded");
    }
  }

  @Secured(PAYSLIP_WRITE)
  @PutMapping("/payslip/regenerate/")
  public EmployeePaySlipResponse regeneratePaySlip(@RequestBody EmployeePaySlipRequest request) throws IOException, DocumentException, ParseException
  {
    return new EmployeePaySlipResponse(paySlipService.reGeneratePaySlip(request));
  }


  //------------------------------shubham-----------------------------------------------------------------
  // shubham
  @Secured(PAYSLIP_WRITE)
  @PostMapping("/payslip/regenerateall/")
  public void regeneratePaySlipAll(@RequestBody PaySlipRegenerateRequest request) throws IOException, DocumentException, ParseException
  {
    paySlipService.reGeneratePaySlipAll(request);
  }
  @Secured(PAYSLIP_LOCK)
  @PutMapping("/payslip/lock")
  public boolean lockPaySalary(@RequestBody EmployeePaySlipRequest request) throws IOException, DocumentException, ParseException
  {
    return paySlipService.lockSalary(request);
  }
}
