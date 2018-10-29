package com.synlabs.ipsaa.controller;

import com.itextpdf.text.DocumentException;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.service.DocumentService;
import com.synlabs.ipsaa.service.IpsaaClubFeeSerivce;
import com.synlabs.ipsaa.service.StudentFeeService;
import com.synlabs.ipsaa.service.StudentService;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.fee.*;
import com.synlabs.ipsaa.view.student.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;

@RestController
@RequestMapping("/api/student/")
public class StudentFeeController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentFeeService studentFeeService;

    @Autowired
    private IpsaaClubFeeSerivce ipsaaClubFeeSerivce;

    @Autowired
    private DocumentService documentService;


    @PostMapping("/paymentLink/")
    @Secured(STUDENTFEE_READ)
    public void sentPaymentLink(@RequestBody SlipEmailRequest request) {
        studentService.sendPaymentLink(request);
    }

    @PostMapping("fee/get")
    @Secured(STUDENTFEE_READ)
    public StudentFeeResponse getStudentFee(@RequestBody StudentFeeRequest request) {
        return new StudentFeeResponse(studentService.getStudentFee(request));
    }

    @GetMapping("/fee/{studentId}")
    @Secured(STUDENTFEE_READ)
    public StudentFeeResponse getStudentFee(@PathVariable Long studentId) {
        StudentRequest request = new StudentRequest();
        request.setId(studentId);
        StudentFee studentFee = studentService.getStudentFee(request);
        if (studentFee != null) {
            return new StudentFeeResponse(studentFee);
        }
        return new StudentFeeResponse();
    }

    @Secured(STUDENTFEE_READ)
    @GetMapping("/{studentId}/fee/ledger")
    public List<StudentFeeLedgerResponse> listStudentLedger(@PathVariable(name = "studentId") Long studentId) {
        return studentService.listFeeLedger(new StudentFeeLedgerRequest(studentId)).stream().map(StudentFeeLedgerResponse::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/feeslip/generate-all")
    public List<StudentFeeSlipResponse> generateStudentsSlip(@RequestBody List<Long> ids) {
        return studentService.generateFinalFeeSlips(ids).stream().map(StudentFeeSlipResponse::new).collect(Collectors.toList());

    }

    @Secured(STUDENTFEE_RECEIPT_READ)
    @PutMapping("/payfee/{id}")
    public StudentFeePaymentResponse getReceipt(@PathVariable("id") Long id) {
        SaveFeeSlipRequest request = new SaveFeeSlipRequest();
        request.setId(id);
        return new StudentFeePaymentResponse(studentService.getReceipt(request));
    }

    @Secured(STUDENTFEE_RECEIPT_WRITE)
    @GetMapping("/download/receipt/{slipId}")
    public void downloadReceipt(@PathVariable("slipId") Long slipId, HttpServletResponse response) throws IOException {
        SaveFeeSlipRequest request = new SaveFeeSlipRequest();
        request.setId(slipId);
        InputStream is = documentService.downloadFeeReceiptPdf(request);
        response.setContentType("application/octet-stream");
        String fileName = request.getReceiptSerial();

        response.setHeader("Content-disposition", String.format("attachment; filename=%s.pdf", fileName));
        response.setHeader("fileName", String.format("%s.pdf", fileName));
        OutputStream out = response.getOutputStream();

//    System.out.println(in);
        IOUtils.copy(is, out);
        out.flush();
        is.close();
    }

    @Secured(STUDENTFEE_SLIP_READ)
    @PostMapping("/feeslips/pdf")
    public void downloadFeeSlips(@RequestBody List<Long> ids, HttpServletResponse response) throws IOException, DocumentException {
        StudentFeeSlipRequest request = new StudentFeeSlipRequest();
        File file = documentService.generateFeeSlipPdf(ids, request);

        response.setContentType("application/octet-stream");
        String fileName = String.format("Slips_%s_%s.pdf", request.getCenterCode(), request.getPeriod());
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setHeader("fileName", fileName);

        OutputStream out = response.getOutputStream();
        FileInputStream is = new FileInputStream(file);

        IOUtils.copy(is, out);
        out.flush();
        is.close();
        if (!file.delete()) {
            throw new IOException("Could not delete temporary file after processing: " + file);
        }
    }
    //----------------------------------shubham-----------------------------------------

    @GetMapping("fee")
    @Secured(STUDENTFEE_READ)
    public List<StudentFeeResponse> list(@RequestParam(required = false) Long centerId) {
        return studentService.listFee(new StudentFeeRequest(centerId)).stream().map(StudentFeeResponse::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_SLIP_READ)
    @PostMapping("/feeslip/list")
    public List<StudentFeeSlipResponse> listStudentSlips(@RequestBody StudentFeeSlipRequest request) {
        return studentFeeService.listFeeSlips(request).stream().map(StudentFeeSlipResponse::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_WRITE)
    @PostMapping("/fee/")
    public StudentFeeResponse saveStudentFee(@RequestBody StudentFeeRequestV2 request) {
        return new StudentFeeResponse(studentFeeService.saveStudentFee(request));
    }

    @Secured(STUDENTFEE_WRITE)
    @PutMapping("/fee/")
    public StudentFeeResponse updateStudentFee(@RequestBody StudentFeeRequestV2 request) {
        return new StudentFeeResponse(studentFeeService.updateStudentFee(request));
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/feeslip/generateFee")
    public StudentFeeSlipResponse generateSingleStudentsSlip(@RequestParam Long slipId) {
        StudentFeeRequestV2 request = new StudentFeeRequestV2();
        request.setId(slipId);
        return new StudentFeeSlipResponse(studentFeeService.generateFirstFeeSlip(request.getId()));
    }

    @GetMapping("payments/history/{id}")
    @Secured(STUDENT_READ)
    public PaymentHistoryResponce StudentPaymentHistory(@PathVariable Long id) {
        StudentRequest request = new StudentRequest();
        request.setId(id);
        return studentFeeService.getStudentPaymentHistory(request.getId());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/feeslip/generate")
    public List<StudentFeeSlipResponse> generateStudentSlips(@RequestBody StudentFeeSlipRequest request) {
        return studentFeeService.generateFeeSlips(request).stream().map(StudentFeeSlipResponse::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/feeslip/regenerate")
    public StudentFeeSlipResponse reGenerateStudentSlip(@RequestBody StudentFeeSlipRequestV2 request) {
        return new StudentFeeSlipResponse(studentFeeService.regenerateStudentSlip(request));
    }
  @Secured(STUDENTFEE_REGENERATE_WRITE)
  @PostMapping("/feeslip/regenerateAll")
  public void reGenerateStudentSlipAll(@RequestBody List<Long> ids)
  {
    studentFeeService.regenerateStudentSlipAll(ids);
  }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/feeslip")
    // Called when slip save button is pressed
    public StudentFeeSlipResponse updateSlip(@RequestBody StudentFeeSlipRequestV2 request) {
        return new StudentFeeSlipResponse(studentFeeService.updateSlip(request));
    }

    @Secured(STUDENTFEE_RECEIPT_WRITE)
    @PostMapping("/payfee")
    //record payment button
    public StudentFeeSlipResponse payFee(@RequestBody SaveFeeSlipRequest request) {
        return new StudentFeeSlipResponse(studentFeeService.payFee(request));
    }

    @Secured(STUDENTFEE_RECEIPT_CONFIRM)
    @PutMapping("/payfee")
    //Confirm payment
    public StudentFeePaymentResponse updatePayFee(@RequestBody SaveFeeSlipRequest request) {
        return new StudentFeePaymentResponse(studentFeeService.updatePayFee(request));
    }

    //-----------------------------------------ipsaa club---------------------------------------------------------//

    @GetMapping("/ipsaaclub/fee/{centerId}")
    @Secured(STUDENTFEE_READ)
    public List<StudentFeeResponse> ipsaaList(@PathVariable("centerId")Long centerId) {
        return studentService.listFeeIpsaa(new StudentFeeRequest(centerId)).stream().map(StudentFeeResponse::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_SLIP_READ)
    @PostMapping("/ipsaaclub/feeslip/list")
    public List<IpsaaClubSlipResponce> listStudentIpsaaSlips(@RequestBody IpsaaClubSlipRequest request) {
        return ipsaaClubFeeSerivce.listFeeSlips(request).stream().map(IpsaaClubSlipResponce::new).collect(Collectors.toList());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/ipsaaclub/slip/{student_id}")
    public IpsaaClubSlipResponce getSlip(@PathVariable("student_id") Long id) {
        IpsaaClubSlipRequest request = new IpsaaClubSlipRequest();
        request.setId(id);
        return ipsaaClubFeeSerivce.getStudnetSlip(request.getId());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/ipsaaclub/generate/{id}")
    public IpsaaClubSlipResponce generate(@PathVariable("id") Long id) {
        IpsaaClubSlipRequest request = new IpsaaClubSlipRequest();
        request.setId(id);
        return ipsaaClubFeeSerivce.generateSlip(request.getId());
    }

    @Secured(STUDENTFEE_SLIP_WRITE)
    @PostMapping("/ipsaaclub/slip/update")
    // Called when slip save button is pressed
    public IpsaaClubRecordResponce updateIpsaClubSlip(@RequestBody StudentFeeSlipRequestV2 request) {
        return new IpsaaClubRecordResponce(ipsaaClubFeeSerivce.updateRecord(request));
    }

    @Secured(STUDENTFEE_RECEIPT_WRITE)
    @PostMapping("/ipsaaclub/payfee")
    //record payment button
    public IpsaaClubRecordResponce payIpsaaFee(@RequestBody SaveFeeSlipRequest request) {
        return new IpsaaClubRecordResponce(ipsaaClubFeeSerivce.payFee(request));
    }

    @Secured(STUDENTFEE_RECEIPT_CONFIRM)
    @PutMapping("/ipsaaclub/update")
    //Confirm payment
    public IpsaaClubRecordResponce updateIpsaaClubPayFee(@RequestBody SaveFeeSlipRequest request) {
        return new IpsaaClubRecordResponce(ipsaaClubFeeSerivce.updatePayFee(request));
    }

    @Secured(STUDENTFEE_RECEIPT_WRITE)
    @GetMapping("/download/ipssaclub/receipt/{recordId}")
    public void downloadReceiptIpssa(@PathVariable("recordId") Long recordId, HttpServletResponse response) throws IOException {

        IpsaaClubSlipRequest request = new IpsaaClubSlipRequest();
        request.setId(recordId);
        InputStream is = documentService.downloadFeeReceiptPdf(request);
        response.setContentType("application/octet-stream");
        String fileName = request.getReceiptSerial();

        response.setHeader("Content-disposition", String.format("attachment; filename=%s.pdf", fileName));
        response.setHeader("fileName", String.format("%s.pdf", fileName));
        OutputStream out = response.getOutputStream();

//    System.out.println(in);
        IOUtils.copy(is, out);
        out.flush();
        is.close();
    }

    @Secured(STUDENTFEE_SLIP_READ)
    @PostMapping("/ipsaaclub/feeslips/pdf")
    public void downloadIpsaaClubFeeSlips(@RequestBody List<Long> ids, HttpServletResponse response) throws IOException, DocumentException {
        StudentFeeSlipRequest request = new StudentFeeSlipRequest();
        File file = documentService.generateFeeSlipPdf(ids, request);

        response.setContentType("application/octet-stream");
        String fileName = String.format("Slips_%s_%s.pdf", request.getCenterCode(), request.getPeriod());
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setHeader("fileName", fileName);

        OutputStream out = response.getOutputStream();
        FileInputStream is = new FileInputStream(file);

        IOUtils.copy(is, out);
        out.flush();
        is.close();
        if (!file.delete()) {
            throw new IOException("Could not delete temporary file after processing: " + file);
        }
    }
}
