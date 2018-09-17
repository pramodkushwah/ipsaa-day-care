package com.synlabs.ipsaa.controller;

import com.itextpdf.text.DocumentException;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.service.DocumentService;
import com.synlabs.ipsaa.service.StudentService;
import com.synlabs.ipsaa.view.center.ApprovalCountResponse;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.common.PageResponse;
import com.synlabs.ipsaa.view.student.ParentRequest;
import com.synlabs.ipsaa.view.student.StudentFilterRequest;
import com.synlabs.ipsaa.view.student.StudentRequest;
import com.synlabs.ipsaa.view.student.StudentResponse;
import com.synlabs.ipsaa.view.student.StudentSummaryResponse;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

@RestController
@RequestMapping("/api/student/")
public class StudentController
{

  @Autowired
  private StudentService studentService;

  @Autowired
  private DocumentService documentService;

  @Autowired
  private CenterService centerService;

  @PutMapping("/parent/resetPassword")
  @Secured(STUDENT_WRITE)
  public void changePassword(@RequestBody ParentRequest request)
  {
    studentService.resetParentPassword(request);
  }

  @PostMapping("filter")
  @Secured(STUDENT_READ)
  public PageResponse<StudentSummaryResponse> list(@RequestBody StudentFilterRequest studentFilterRequest)
  {
    return studentService.list(studentFilterRequest);
  }

  @GetMapping("{studentId}")
  @Secured(STUDENT_READ)
  public StudentResponse get(@PathVariable(name = "studentId") Long studentId)
  {
    return studentService.getStudent(new StudentRequest(studentId));
  }

  @GetMapping("/pdf/{studentId}")
  @Secured(STUDENT_READ)
  public byte[] getStudentProfile(@PathVariable(name = "studentId") Long studentId, HttpServletResponse response) throws IOException, DocumentException, TemplateException, InterruptedException, ParseException, org.dom4j.DocumentException {
    StudentResponse student = studentService.getStudent(new StudentRequest(studentId));
    response.setContentType("application/octet-stream");
// String fileName = "3dd55924-5b87-4cbb-a917-4426b42a4a35.pdf";
    response.setHeader("Content-disposition", "attachment; filename=" + student.getFirstName()+ ".pdf");
    response.setHeader("fileName", student.getFirstName()+ ".pdf");
    return studentService.generateStudentPdf(student);
  }

  @PostMapping
  @Secured(STUDENT_WRITE)
  public StudentResponse addStudent(@RequestBody StudentRequest studentRequest)
  {
    return studentService.saveStudent(studentRequest);
  }

  @PutMapping
  @Secured(STUDENT_WRITE)
  public StudentResponse editStudent(@RequestBody StudentRequest studentRequest) throws ParseException
  {
    return new StudentResponse(studentService.updateStudent(studentRequest));
  }

  @DeleteMapping(path = "{id}")
  @Secured(STUDENT_DELETE)
  public void deleteStudent(@PathVariable Long id)
  {
    StudentRequest request = new StudentRequest();
    request.setId(id);
    studentService.deleteStudent(request);
  }

  @PostMapping("/{studentId}/profile-pic")
  @Secured(STUDENT_WRITE)
  public void uploadProfilePic(@RequestParam("file") MultipartFile file, @PathVariable Long studentId)
  {
    StudentRequest request = new StudentRequest();
    request.setId(studentId);

    //////////Avneet
    long size=file.getSize();
    long max = new Double(0.5*1024*1024).longValue();
    if(size< max){
      studentService.uploadStudentPic(request, file);
    }else{
        throw new ValidationException("Size should not be more than 0.5 MB");
    }

  }

  @Secured(IMPORT_WRITE)
  @PostMapping(path = "import")
  public ResponseEntity<Map<String, String>> importRecords(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue = "true") String dryRun)
  {
    try
    {
      Map<String, String> map = studentService.importRecords(file, dryRun);
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

  //  @Secured(IMPORT_READ)
  @Secured("NO_PERMISSION")
  @GetMapping(path = "download")
  public void download(@RequestParam String token, @RequestParam(defaultValue = "ALL") String program, HttpServletResponse response)
  {
    try
    {
      String filename = studentService.exportRecords(program);
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-Disposition", "attachment; filename=student.xls");
      InputStream is = new FileInputStream(filename);
      IOUtils.copy(is, response.getOutputStream());
      response.flushBuffer();
    }
    catch (Exception exp)
    {
      response.setStatus(500);
    }
  }

  @Secured(STUDENT_WRITE)
  @GetMapping("/createParentAccount/{id}")
  public void createParentAccount(@PathVariable(name = "id") Long id)
  {
    ParentRequest request = new ParentRequest();
    request.setId(id);
    studentService.createParentAccount(request);
  }

//  TODO : remove not used
  @GetMapping("approvals")
  @Secured(STUDENT_APPROVAL_READ)
  public List<StudentSummaryResponse> list(){
    return studentService.newStudentApprovals().stream().map(StudentSummaryResponse::new).collect(Collectors.toList());
  }

  @GetMapping("approvals/count")
  @Secured(STUDENT_APPROVAL_READ)
  public List<ApprovalCountResponse> countList(){

    return centerService.getStudentApprovalCount();
  }

  @GetMapping("approvals/{centerId}")
  @Secured(STUDENT_APPROVAL_READ)
  public List<StudentSummaryResponse> list(@PathVariable("centerId") Long centerId){
    CenterRequest request=new CenterRequest();
    request.setId(centerId);
    return studentService.newStudentApprovals(request).stream().map(StudentSummaryResponse::new).collect(Collectors.toList());
  }

  @GetMapping("approve/{studentId}")
  @Secured(STUDENT_APPROVAL_WRITE)
  public void approveStudent(@PathVariable("studentId") Long studentId){
    StudentRequest request=new StudentRequest();
    request.setId(studentId);
    studentService.approve(request);
  }

  @GetMapping("reject/{studentId}")
  @Secured(STUDENT_APPROVAL_WRITE)
  public void rejectStudent(@PathVariable("studentId") Long studentId){
    StudentRequest request=new StudentRequest();
    request.setId(studentId);
    studentService.reject(request);
  }


}