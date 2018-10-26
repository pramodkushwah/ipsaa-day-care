package com.synlabs.ipsaa.service;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.synlabs.ipsaa.entity.common.SerialNumberSequence;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.util.NumberToWordConverter;
import com.synlabs.ipsaa.view.fee.FeePaymentRecordView;
import com.synlabs.ipsaa.view.fee.SaveFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.staff.EmployeePaySlipResponse;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipRequest;
import com.synlabs.ipsaa.view.student.StudentResponse;
import freemarker.template.*;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service
public class DocumentService extends BaseService
{
  @Autowired
  private Configuration configuration;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private SerialNumberSequenceRepository serialNumberSequenceRepository;

  @Autowired
  private StudentFeePaymentRepository slipRepository;
  @Autowired
  StudentFeePaymentRecordIpsaaClubRepository studentFeePaymentRecordIpsaaClubRepository;

  @Autowired
  private StudentFeeRepository studentFeeRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private EmployeeSalaryRepository salaryRepository;

  @Autowired
  private FeeService feeService;

  private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

  @Value("${ipsaa.image.appt_letter_image_url}")
  private String appt_letter_image_url;

  private String tempDirPath;

  private File tempDir;

  private final Map<String, String> wkhtmlParams;

  public DocumentService(@Value("${ipsaa.wkhtmltopdf.params}") String paramsString,
                         @Value("${ipsaa.tmp.directory}") String tempDirPath) throws IOException
  {
    this.tempDirPath = tempDirPath;
    tempDir = new File(tempDirPath);
    tempDir.mkdirs();
    if (!tempDir.exists())
    {
      throw new IOException("Unable to create export directory.");
    }

    //adding params to map
    Map<String, String> map = new HashMap<>();
    commaSplitter.split(paramsString).forEach(param -> {
      Iterator<String> iterator = colonSplitter.split(param).iterator();
      map.put(iterator.next(), iterator.next());
    });
    wkhtmlParams = Collections.unmodifiableMap(map);
  }

  private Pdf addParamsToPdf(Pdf p)
  {
    final Pdf pdf = p == null ? new Pdf() : p;
    wkhtmlParams.forEach((k, v) -> pdf.addParam(new Param(k, v)));
    return pdf;
  }

  private Pdf addParamsToPdf(Pdf p, Map<String, String> params)
  {
    final Pdf pdf = p == null ? new Pdf() : p;
    params.forEach((k, v) -> {
      pdf.addParam(new Param(k, v));
    });
    return pdf;
  }

  private Pdf addPdfMarginParam(Pdf p, int margin)
  {
    final Pdf pdf = p == null ? new Pdf() : p;
    pdf.addParam(new Param("-T", "" + margin));
    pdf.addParam(new Param("-B", "" + margin));
    pdf.addParam(new Param("-R", "" + margin));
    pdf.addParam(new Param("-L", "" + margin));
    return pdf;
  }

  public InputStream generateAppointmentLetter(Long empId)
  {
    Employee emp = employeeRepository.findOne(empId);
    if (emp == null)
    {
      throw new ValidationException(String.format("Cannot locate Employee[id=%s]", empId));
    }
    if (hasCenter(emp.getCostCenter().getId()) == null)
    {
      throw new ValidationException("Unauthorized access to Employee.");
    }
    EmployeeSalary salary = salaryRepository.findByEmployee(emp);
    if (salary == null)
    {
      throw new ValidationException("Salary not found.");
    }

    try
    {
      Template page_1_template = configuration.getTemplate("appointment_letter/page_1.ftl");
      Template page_2_template = configuration.getTemplate("appointment_letter/page_2.ftl");
      Template page_3_template = configuration.getTemplate("appointment_letter/page_3.ftl");
      Template page_4_template = configuration.getTemplate("appointment_letter/page_4.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("appt_letter_image_url", appt_letter_image_url);
      rootMap.put("emp", emp);
      rootMap.put("salary", salary.getNetSalary().multiply(new BigDecimal(12)));
      rootMap.put("salaryWords", NumberToWordConverter.convert(salary.getNetSalary().multiply(new BigDecimal(12)).longValue()));
      Writer out = new StringWriter();
      page_1_template.process(rootMap, out);
      String page_1_String = out.toString();

      rootMap = new HashMap<>();
      rootMap.put("appt_letter_image_url", appt_letter_image_url);
      out = new StringWriter();
      page_2_template.process(rootMap, out);
      String page_2_String = out.toString();

      rootMap = new HashMap<>();
      rootMap.put("appt_letter_image_url", appt_letter_image_url);
      out = new StringWriter();
      page_3_template.process(rootMap, out);
      String page_3_String = out.toString();

      rootMap = new HashMap<>();
      rootMap.put("doj_15", LocalDate.fromDateFields(emp.getProfile().getDoj()).plusDays(15).toDate());
      rootMap.put("emp", emp);
      rootMap.put("appt_letter_image_url", appt_letter_image_url);
      out = new StringWriter();
      page_4_template.process(rootMap, out);
      String page_4_String = out.toString();

      String fileName = String.format("%s_Appointment_letter.pdf", emp.getEid());
      generateMultiPagePdf("appt_letter", fileName, Arrays.asList(page_1_String,
                                                                  page_2_String,
                                                                  page_3_String,
                                                                  page_4_String));

      return fileStore.getStream("appt_letter", fileName);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error generating appointment letter pdf!", ex);
      return null;
    }
  }

  public File generateFeeSlipPdf(List<Long> slipIds, StudentFeeSlipRequest slip) throws IOException, DocumentException
  {
//    List<StudentFeePaymentRequest> feeSlips = studentService.generateFeeSlips(request);
    List<StudentFeePaymentRequest> feeSlips = new ArrayList<>();
    boolean flag = true;
    for (Long slipId : slipIds)
    {
      StudentFeePaymentRequest slip1 = feeService.getSlip(unmask(slipId));
      if (flag)
      {
        slip.setCenterCode(slip1.getStudent().getCenter().getCode());
        slip.setPeriod(slip1.getFeeDuration().toString());
        flag = false;
      }
      feeSlips.add(slip1);
    }

    Collections.sort(feeSlips, (slip1, slip2) -> {
      return slip1.getStudent().getName()
                  .compareTo(slip2.getStudent().getName());
    });

    List<String> slipPdfNames = generateSlipPdf(feeSlips);

    String fileName = UUID.randomUUID() + ".pdf";
    File mergedPdfFile = new File(tempDirPath + "/" + fileName);
    FileOutputStream out = new FileOutputStream(mergedPdfFile);

    Document document = new Document();
    PdfCopy copy = new PdfCopy(document, out);
    document.open();
    try
    {
      try
      {
        for (String slipName : slipPdfNames)
        {
          InputStream in = fileStore.getStream("SLIP", slipName);
          PdfReader reader = new PdfReader(in);
          for (int i = 1; i <= reader.getNumberOfPages(); i++)
          {
            document.newPage();
            copy.addPage(copy.getImportedPage(reader, i));
          }
          in.close();
        }
      }
      finally
      {
        out.flush();
        document.close();
        out.close();
      }
    }
    catch (ExceptionConverter ex)
    {
      if (ex.getMessage().equals("The document has no pages."))
      {
//        TODO : Handle empty pdf
      }
      else
      {
        throw ex;
      }
    }
    return mergedPdfFile;
  }

  
   @SuppressWarnings({ "unchecked", "resource" })
 public byte[] generateStudentPdf(StudentResponse student) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, InterruptedException, DocumentException{
 	  Template template = configuration.getTemplate("student-confirmation/confirmation.ftl");
 	    try
 	    {
 	      Map<String, Object> rootMap = new HashMap<>();
 	      rootMap.put("slip",student);
	      
	      
 	      rootMap.put("father",student.getParents().get(0));
 	      rootMap.put("mother",student.getParents().get(1));
	      
 	      Writer out = new StringWriter();
 	      template.process(rootMap, out);
 	      String html = out.toString();
 //	      String fileName = student.getFirstName()+".pdf";

 	      Map<String, String> params = new HashMap<>();
	      
 	      Pdf slip = addParamsToPdf(null);
 	      addParamsToPdf(slip, params);
 	      addPdfMarginParam(slip, 2);

 	      slip.addPageFromString(html);
 	      byte[] bytes = slip.getPDF();
 //	      fileStore.store("STUDENT", fileName, bytes);
 	      return bytes;
 	    }
 	    catch (TemplateException | InterruptedException e)
 	    {
 	      e.printStackTrace();
 	      logger.warn(String.format("Error generating paySlip pdf"));
 	      return null;
 	    }
   }
  
  private Map<String, Object> fillPaySlipTemplate(EmployeePaySlip paySlip, Map<String, Object> rootMap)
  {
    rootMap.put("paySlip", new EmployeePaySlipResponse(paySlip));
    rootMap.put("amountWords", NumberToWordConverter.convert(paySlip.getNetSalary().intValue()));
    return rootMap;
  }

  public String generatePayslipPdf(EmployeePaySlip paySlip) throws IOException, DocumentException
  {
    Template template = configuration.getTemplate("payslip/two_payslips.ftl");
    try
    {
      Map<String, Object> rootMap = new HashMap<>();
      rootMap = fillPaySlipTemplate(paySlip, rootMap);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String html = out.toString();
      String fileName = UUID.randomUUID() + ".pdf";

      Map<String, String> params = new HashMap<>();
      params.put("-O", "landscape");
      Pdf slip = addParamsToPdf(null);
      addParamsToPdf(slip, params);
      addPdfMarginParam(slip, 2);

      slip.addPageFromString(html);
      byte[] bytes = slip.getPDF();
      fileStore.store("PAYSLIP", fileName, bytes);
      return fileName;
    }
    catch (TemplateException | InterruptedException e)
    {
      e.printStackTrace();
      logger.warn(String.format("Error generating paySlip pdf [serial_number=%s,id=%s]", paySlip.getSerial(), paySlip.getEmployee().getEid()));
      return null;
    }
  }

  private List<String> generateSlipPdf(List<StudentFeePaymentRequest> feeSlips) throws IOException
  {
    Template template = configuration.getTemplate("slip/slip.ftl");

    List<String> slipNames = new ArrayList<>();
    for (StudentFeePaymentRequest feeSlip : feeSlips)
    {
      if (feeSlip.isReGenerateSlip() ||
          StringUtils.isEmpty(feeSlip.getSlipFileName()) ||
          !fileStore.isExist("SLIP", feeSlip.getSlipFileName()))
      {
        try
        {
          StudentFee fee = studentFeeRepository.findByStudent(feeSlip.getStudent());
          Map<String, Object> rootMap = fillSlipTemplate(feeSlip, fee, null);
          Writer out = new StringWriter();
          template.process(rootMap, out);
          String html = out.toString();

          String fileName = UUID.randomUUID() + ".pdf";
          System.out.println(fileName);
          feeSlip.setSlipFileName(fileName);
          Map<String, String> params = new HashMap<>();
          params.put("-O", "landscape");
          Pdf slip = addParamsToPdf(null);
          addParamsToPdf(slip, params);
          addPdfMarginParam(slip, 2);

          slip.addPageFromString(html);
          fileStore.store("SLIP", fileName, slip.getPDF());

          feeSlip.setReGenerateSlip(false);
          slipRepository.saveAndFlush(feeSlip);
          logger.info(String.format("Generated fee slip pdf slip[serial_number=%s,id=%s]", feeSlip.getSlipSerial(), feeSlip.getId()));
        }
        catch (TemplateException | InterruptedException e)
        {
          e.printStackTrace();
          logger.warn(String.format("Error generating slip pdf [serial_number=%s,id=%s]", feeSlip.getSlipSerial(), feeSlip.getId()));
        }
      }
      slipNames.add(feeSlip.getSlipFileName());
    }
    return slipNames;
  }

  private Map<String, Object> fillSlipTemplate(StudentFeePaymentRequest slip, StudentFee studentFee, Map<String, Object> rootMap)
  {
    String serial = slip.getSlipSerial();
    if (StringUtils.isEmpty(serial))
    {
      serial = generateSerial(slip.getStudent().getCenter().getCode(), "SLIP");
      slip.setSlipSerial(serial);
      slipRepository.saveAndFlush(slip);
    }
    if(!slip.getStudent().getCenter().getAddress().getCity().equals("Gurgaon")){
      slip.setSgst(new BigDecimal(9));
      slip.setCgst(new BigDecimal(9));
      slip.setIgst(null);
    }else{
      slip.setSgst(null);
      slip.setCgst(null);
      slip.setIgst(new BigDecimal(18));
    }
    rootMap = rootMap == null ? new HashMap<>() : rootMap;
    rootMap.put("student", slip.getStudent());
    rootMap.put("slip", slip);
    rootMap.put("date", new SimpleDateFormat("dd-MM-yyyy").format(slip.getInvoiceDate()));
    rootMap.put("month", FeeUtils.getMonth(slip));
    rootMap.put("transportFee", calculateTransportFee(studentFee));
    rootMap.put("annualCharge", slip.getAnnualFee());
    rootMap.put("security", slip.getDeposit());
    rootMap.put("isAnnualFee", slip.getAnnualFee() != null);
    rootMap.put("isDeposit", slip.getDeposit() != null);
    BigDecimal programFee = slip.getBaseFee();
    rootMap.put("programFee", programFee);

    if (slip.getBalance() != null && slip.getBalance().intValue() != 0)
    {
      rootMap.put("balance", slip.getBalance());
    }

    if (slip.getAdjust() != null && slip.getAdjust().intValue() != 0)
    {
      rootMap.put("adjust", slip.getAdjust());
    }

    BigDecimal subTotal = programFee;
    if (slip.getDeposit() != null)
    {
      subTotal = subTotal.add(slip.getDeposit());
    }
    if (slip.getAnnualFee() != null)
    {
      subTotal = subTotal.add(slip.getAnnualFee());
    }
    rootMap.put("subTotal", subTotal);
    if (slip.getSgst() != null && !slip.getSgst().equals(ZERO))
    {
      rootMap.put("sgst", slip.getSgst());
      rootMap.put("sgstAmount", FeeUtils.calculateGST(slip, GST.SGST));
    }
    if (slip.getCgst() != null && !slip.getCgst().equals(ZERO))
    {
      rootMap.put("cgst", slip.getCgst());
      rootMap.put("cgstAmount", FeeUtils.calculateGST(slip, GST.CGST));
    }
    if (slip.getIgst() != null && !slip.getIgst().equals(ZERO))         /////Added else because igst can't be null or misssing in template.
    {
      rootMap.put("igst", slip.getIgst());
      rootMap.put("igstAmount", FeeUtils.calculateGST(slip, GST.IGST));
    }else{        //set if igst is zero.
      rootMap.put("igst",0.0);
      rootMap.put("igstAmount", FeeUtils.calculateGST(slip,GST.IGST));
    }

    return rootMap;
  }
  private Map<String, Object> fillSlipTemplate(StudentFeePaymentRecordIpsaaClub slip, StudentFee studentFee, Map<String, Object> rootMap)
  {
    String serial = slip.getSlipSerial();
    if (StringUtils.isEmpty(serial))
    {
      serial = generateSerial(slip.getStudent().getCenter().getCode(), "SLIP");
      slip.setSlipSerial(serial);
      studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(slip);
    }
    if(!slip.getStudent().getCenter().getAddress().getCity().equals("Gurgaon")){
      //slip.setSgst(new BigDecimal(9));
      //slip.setCgst(new BigDecimal(9));
      //slip.setIgst(null);
    }else{
      //slip.setSgst(null);
      //slip.setCgst(null);
      //slip.setIgst(new BigDecimal(18));
    }
    rootMap = rootMap == null ? new HashMap<>() : rootMap;
    rootMap.put("student", slip.getStudent());
    rootMap.put("slip", slip);
    rootMap.put("date", new SimpleDateFormat("dd-MM-yyyy").format(slip.getStartDate()));
    rootMap.put("month", FeeUtils.getMonth(slip));
    rootMap.put("transportFee", calculateTransportFee(studentFee));
    rootMap.put("annualCharge", slip.getAnnualFee());
    rootMap.put("security", slip.getDepositFee());
    rootMap.put("isAnnualFee", slip.getAnnualFee() != null);
    rootMap.put("isDeposit", slip.getDepositFee() != null);
    BigDecimal programFee = slip.getBaseFee();
    rootMap.put("programFee", programFee);

    if (slip.getBalance() != null && slip.getBalance().intValue() != 0)
    {
      rootMap.put("balance", slip.getBalance());
    }

    BigDecimal subTotal = programFee;
    if (slip.getDepositFee() != null)
    {
      subTotal = subTotal.add(slip.getDepositFee());
    }
    if (slip.getAnnualFee() != null)
    {
      subTotal = subTotal.add(slip.getAnnualFee());
    }
    rootMap.put("subTotal", subTotal);
//    if (slip.getSgst() != null && !slip.getSgst().equals(ZERO))
//    {
//      rootMap.put("sgst", slip.getSgst());
//      rootMap.put("sgstAmount", FeeUtils.calculateGST(slip, GST.SGST));
//    }
//    if (slip.getCgst() != null && !slip.getCgst().equals(ZERO))
//    {
//      rootMap.put("cgst", slip.getCgst());
//      rootMap.put("cgstAmount", FeeUtils.calculateGST(slip, GST.CGST));
//    }
//    if (slip.getIgst() != null && !slip.getIgst().equals(ZERO))         /////Added else because igst can't be null or misssing in template.
//    {
//      rootMap.put("igst", slip.getIgst());
//      rootMap.put("igstAmount", FeeUtils.calculateGST(slip, GST.IGST));
//    }else{        //set if igst is zero.
//      rootMap.put("igst",0.0);
//      rootMap.put("igstAmount", FeeUtils.calculateGST(slip,GST.IGST));
//    }

    return rootMap;
  }

  public void generateFeeSlipPdf(StudentFeePaymentRequest feeRequest)
  {
    try
    {
      Template template = configuration.getTemplate("slip/slip.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      if (feeRequest.getExtraCharge() == null)
      {
        feeRequest.setExtraCharge(new BigDecimal(0));
      }
      if (feeRequest.getLatePaymentCharge() == null)
      {
        feeRequest.setLatePaymentCharge(new BigDecimal(0));
      }
      String serial = feeRequest.getSlipSerial();
      if (StringUtils.isEmpty(serial))
      {
        serial = generateSerial(feeRequest.getStudent().getCenter().getCode(), "SLIP");
      }
      feeRequest.setSlipSerial(serial);
      Student student = feeRequest.getStudent();
      StudentFee studentFee = studentFeeRepository.findByStudent(student);
      String filename = UUID.randomUUID().toString() + ".pdf";
      fillSlipTemplate(feeRequest, studentFee, rootMap);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String html = out.toString();
      generatePdf(html, "SLIP", filename, true, 2);
      feeRequest.setSlipSerial(serial);
      feeRequest.setSlipFileName(filename);
      feeRequest.setReGenerateSlip(false);
      slipRepository.saveAndFlush(feeRequest);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error generating slip pdf!", ex);
    }
  }
  public InputStream downloadFeeReceiptPdf(IpsaaClubSlipRequest request)  throws IOException{
    if (request.getId() == null)
    {
      throw new ValidationException("Receipt id is required.");
    }

    //StudentFeePaymentRequest slip = slipRepository.findOne(request.getId());
    StudentFeePaymentRecordIpsaaClub record = studentFeePaymentRecordIpsaaClubRepository.findOne(request.getId());
    if (record == null)
    {
      throw new ValidationException("Cannot locate record.");
    }

    String fileName = generateFeeReceiptPdf(record);
    if (fileName == null)
    {
      throw new ValidationException("Unable to generate file.");
    }
    request.setReceiptSerial(record.getReceiptSerial());
    InputStream is = fileStore.getStream("RECEIPT", fileName);
    return is;
  }

  public InputStream downloadFeeReceiptPdf(SaveFeeSlipRequest request) throws IOException
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Receipt id is required.");
    }

    StudentFeePaymentRequest slip = slipRepository.findOne(request.getId());
    if (slip == null)
    {
      throw new ValidationException("Cannot locate slip.");
    }

    String fileName = generateFeeReceiptPdf(slip);
    if (fileName == null)
    {
      throw new ValidationException("Unable to generate file.");
    }
    request.setReceiptSerial(slip.getReceiptSerial());
    InputStream is = fileStore.getStream("RECEIPT", fileName);
    return is;
  }


  public String generateFeeReceiptPdf(StudentFeePaymentRequest feeRequest)
  {
    try
    {
      Template template = configuration.getTemplate("slip/receipt.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      if (feeRequest.getExtraCharge() == null)
      {
        feeRequest.setExtraCharge(new BigDecimal(0));
      }
      if (feeRequest.getLatePaymentCharge() == null)
      {
        feeRequest.setLatePaymentCharge(new BigDecimal(0));
      }
      if(feeRequest.getFinalAdmissionFee()==null){
        feeRequest.setFinalAdmissionFee(ZERO);
      }
      if(feeRequest.getUniformCharges()==null){
        feeRequest.setUniformCharges(ZERO);
      }
      if(feeRequest.getStationary()==null){
        feeRequest.setStationary(ZERO);
      }

      String serial = feeRequest.getReceiptSerial();
      Student student = feeRequest.getStudent();
      if (StringUtils.isEmpty(serial))
      {
        serial = generateSerial(student.getCenter().getCode(), "RECEIPT");
      }
      feeRequest.setReceiptSerial(serial);
      StudentFee studentFee = studentFeeRepository.findByStudent(student);
      String filename = UUID.randomUUID().toString() + ".pdf";

      fillSlipTemplate(feeRequest, studentFee, rootMap);
      rootMap.put("from", student.getFather().getFullName());

      List<FeePaymentRecordView> payments = feeRequest.getPayments().stream().filter(payment->payment.getActive()).map(FeePaymentRecordView::new).collect(Collectors.toList());
      payments.sort(Comparator.comparing(FeePaymentRecordView::getPaymentDate));
      BigDecimal due = new BigDecimal(feeRequest.getTotalFee().toString());
      for (FeePaymentRecordView payment : payments)
      {
        due = due.subtract(payment.getPaidAmount());
        payment.setDueAmount(due);
      }
      payments.sort(Comparator.comparing(FeePaymentRecordView::getPaymentDate).reversed());
      rootMap.put("payments", payments);
      rootMap.put("totalAmount", feeRequest.getTotalFee());
      rootMap.put("dueAmount", due);
      rootMap.put("paidAmount", feeRequest.getTotalFee().subtract(due));

      Writer out = new StringWriter();
      template.process(rootMap, out);
      String html = out.toString();
      generatePdf(html, "RECEIPT", filename, true, 2);
      feeRequest.setReceiptSerial(serial);
      feeRequest.setReceiptFileName(filename);
      slipRepository.saveAndFlush(feeRequest);
      return filename;
    }
    catch (Exception ex)
    {
      logger.error("Error generating receipt pdf!", ex);
      return null;
    }
  }

  public String generateFeeReceiptPdf(StudentFeePaymentRecordIpsaaClub record)
  {
    try
    {
      Template template = configuration.getTemplate("slip/receipt.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      if (record.getExtraCharges() == null)
      {
        record.setExtraCharges(new BigDecimal(0));
      }

      String serial = record.getReceiptSerial();
      Student student = record.getStudent();
      if (StringUtils.isEmpty(serial))
      {
        serial = generateSerial(student.getCenter().getCode(), "RECEIPT");
      }
      record.setReceiptSerial(serial);
      StudentFee studentFee = studentFeeRepository.findByStudent(student);
      String filename = UUID.randomUUID().toString() + ".pdf";

      fillSlipTemplate(record, studentFee, rootMap);
      rootMap.put("from", student.getFather().getFullName());

      BigDecimal due = new BigDecimal(record.getTotalFee().toString());

        due = due.subtract(record.getPaidAmount());
      //rootMap.put("payments", payments);
      rootMap.put("totalAmount", record.getTotalFee());
      rootMap.put("dueAmount", due);
      rootMap.put("paidAmount", record.getTotalFee().subtract(due));

      Writer out = new StringWriter();
      template.process(rootMap, out);
      String html = out.toString();
      generatePdf(html, "RECEIPT", filename, true, 2);
      record.setReceiptSerial(serial);
      record.setReceiptFileName(filename);
      studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(record);
      return filename;
    }
    catch (Exception ex)
    {
      logger.error("Error generating receipt pdf!", ex);
      return null;
    }
  }
  private void generatePdf(String html, String category, String fileName, boolean landscape, int margin) throws IOException, InterruptedException
  {
    Pdf pdf = addParamsToPdf(null);
    if (landscape)
    {
      pdf.addParam(new Param("-O", "landscape"));
    }
    pdf.addParam(new Param("-T", "" + margin));
    pdf.addParam(new Param("-B", "" + margin));
    pdf.addParam(new Param("-R", "" + margin));
    pdf.addParam(new Param("-L", "" + margin));
    pdf.addPageFromString(html);
    byte[] bytes = pdf.getPDF();
    fileStore.store(category, fileName, bytes);
  }

  private void generateMultiPagePdf(String category, String fileName, List<String> htmlPages) throws IOException, InterruptedException
  {
    byte[] bytes = generateMultiPagePdf(htmlPages);
    fileStore.store(category, fileName, bytes);
  }

  private byte[] generateMultiPagePdf(List<String> htmlPages) throws IOException, InterruptedException
  {
    if (!CollectionUtils.isEmpty(htmlPages))
    {
      Pdf pdf = addParamsToPdf(null);
      htmlPages.forEach(page -> pdf.addPageFromString(page));
      return pdf.getPDF();
    }
    return null;
  }

  private String generateSerial(String centerCode, String type)
  {
    SerialNumberSequence sequence = serialNumberSequenceRepository.findOneByCenterCodeAndType(centerCode, type);
    if (sequence == null)
    {
      sequence = new SerialNumberSequence(centerCode, type);
    }
    String serial = String.format("%s-%s-%s", sequence.getType(), sequence.getCenterCode(), sequence.getNext());
    serialNumberSequenceRepository.saveAndFlush(sequence);
    return serial;
  }

  private BigDecimal calculateTransportFee(StudentFee studentFee)
  {
    BigDecimal transportFee = studentFee.getTransportFee();
    if (transportFee == null)
    {
      return new BigDecimal(0);
    }
    switch (studentFee.getFeeDuration())
    {
      case Monthly:
        transportFee = transportFee.multiply(new BigDecimal(1));
        break;
      case Quarterly:
        transportFee = transportFee.multiply(new BigDecimal(3));
        break;
      case Yearly:
        transportFee = transportFee.multiply(new BigDecimal(12));
        break;
    }
    return transportFee;
  }



//  private BigDecimal calculateProgramFee(StudentFee studentFee)
//  {
//    return FeeUtils.calculateFinalFee(studentFee.getBaseFee(), studentFee.getFeeDuration(), studentFee.getDiscount(), studentFee.getAdjust());
//  }
}
