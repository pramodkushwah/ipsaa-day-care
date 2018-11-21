package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.CenterCharge;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.fee.Charge;
import com.synlabs.ipsaa.entity.fee.HdfcResponse;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.ExcelGenerater;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.view.center.CenterChargeRequest;
import com.synlabs.ipsaa.view.center.CenterFeeRequest;
import com.synlabs.ipsaa.view.center.CenterProgramFeeRequest;
import com.synlabs.ipsaa.view.fee.*;
import com.synlabs.ipsaa.view.report.excel.FeeCollectionExcelReport;
import com.synlabs.ipsaa.view.report.excel.FeeCollectionExcelReport2;
import com.synlabs.ipsaa.view.report.excel.FeeReportExcel2;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeeService extends BaseService
{

  @Autowired
  private CenterFeeRepository feeRepository;

  @Autowired
  private ChargeRepository chargeRepository;

  @Autowired
  private CenterChargeRepository centerChargeRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private CenterProgramFeeRepository centerProgramFeeRepository;

  // shubham
  @Autowired
  private StudentAttendanceRepository attendanceRepository;

  @Autowired
  private StudentFeePaymentRepository slipRepository;

  @Autowired
  private StudentFeePaymentRequestIpsaaClubRepository ipsaaClubSlipRepository;

  @Autowired
  private StudentRepository studentRepository;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private StudentFeeRepository studentFeeRepository;

  @Autowired
  private StudentService studentService;

  @Autowired
  private HdfcResponseRepository hdfcResponseRepository;

  @Value("${ipsaa.export.directory}")
  private String exportDir;

  @PostConstruct
  public void init() throws IOException
  {
    File file = new File(exportDir);
    file.mkdirs();
    if (!file.exists())
    {
      throw new IOException("Unable to create export directory.");
    }
  }

  public List<StudentFeePaymentRequest> getStudentFeeList(){
    return slipRepository.findByQuarterAndYear(3,2018);
  }
  public List<CenterProgramFee> listCenterFee(CenterFeeRequest request)
  {
    return feeRepository.findByCenterId(request.getCenterId());
  }

  public List<CenterProgramFee> listCenterFee(Long id)
  {
    return feeRepository.findByCenterId(id);
  }

  public List<CenterCharge> listCenterCharge(CenterFeeRequest request)
  {
    return centerChargeRepository.findByCenterId(request.getCenterId());
  }

  public List<Charge> listCharges()
  {
    return chargeRepository.findAll();
  }

  public Charge save(ChargeRequest request)
  {
    Charge charge = new Charge();
    charge.setName(request.getName());
    chargeRepository.saveAndFlush(charge);
    return charge;
  }

  public Charge update(ChargeRequest request)
  {

    Charge charge = chargeRepository.findOne(request.getId());

    if (charge == null)
    {
      throw new NotFoundException(String.format("Cannot locate charge with id %d", request.getId()));
    }

    charge.setName(request.getName());
    return chargeRepository.saveAndFlush(charge);
  }

  public CenterCharge save(CenterChargeRequest request)
  {

    if (request.getCenterId() == null)
    {
      throw new ValidationException("Missing center");
    }

    if (request.getChargeId() == null)
    {
      throw new ValidationException("Missing charge");
    }

    if (request.getAmount() == null)
    {
      throw new ValidationException("Missing amount");
    }

    Center center = centerRepository.findOne(request.getCenterId());
    Charge charge = chargeRepository.findOne(request.getChargeId());
    if (center == null)
    {
      throw new NotFoundException(String.format("can not locate center with id [%d]", request.getMaskedCenterId()));
    }
    if (charge == null)
    {
      throw new NotFoundException(String.format("can not locate charge with id [%d]", request.getMaskedChargeId()));
    }

    CenterCharge centerCharge = centerChargeRepository.findByCenterAndCharge(center, charge);
    if (centerCharge == null)
    {
      centerCharge = new CenterCharge();
      centerCharge.setCenter(center);
      centerCharge.setCharge(charge);
    }
    centerCharge.setAmount(request.getAmount().intValue());
    return centerChargeRepository.saveAndFlush(centerCharge);
  }

  public CenterCharge update(CenterChargeRequest request)
  {
    if (request.getCenterId() == null)
    {
      throw new ValidationException("Missing center");
    }

    if (request.getChargeId() == null)
    {
      throw new ValidationException("Missing charge");
    }

    CenterCharge centerCharge = centerChargeRepository.findOne(request.getId());
    if (centerCharge == null)
    {
      throw new NotFoundException(String.format("Cannot locate center charge with id %d", request.getId()));
    }

    Center center = centerRepository.findOne(request.getCenterId());
    Charge charge = chargeRepository.findOne(request.getChargeId());
    centerCharge.setCharge(charge);
    centerCharge.setCenter(center);
    centerCharge.setAmount(request.getAmount().intValue());
    centerCharge = centerChargeRepository.saveAndFlush(centerCharge);
    return centerChargeRepository.saveAndFlush(centerCharge);
  }

  public void deleteCenterCharge(CenterChargeRequest request)
  {
    centerChargeRepository.delete(request.getId());
  }

  public CenterProgramFee saveCenterProgramFee(CenterProgramFeeRequest request)
  {

    if (request.getCenterId() == null)
    {
      throw new ValidationException("Missing center");
    }

    if (request.getProgramId() == null)
    {
      throw new ValidationException("Missing program");
    }

    if (request.getDeposit() == null)
    {
      request.setDeposit(0);
    }

    if (request.getAnnualFee() == null)
    {
      request.setAnnualFee(0);
    }

    Center center = centerRepository.findOne(request.getCenterId());
    Program program = programRepository.findOne(request.getProgramId());

    if (center == null)
    {
      throw new NotFoundException(String.format("can not locate center with id [%d]", request.getMaskedCenterId()));
    }
    if (program == null)
    {
      throw new NotFoundException(String.format("can not locate program with id [%d]", request.getMaskedProgramId()));
    }

    CenterProgramFee fee = centerProgramFeeRepository.findByProgramIdAndCenterId(program.getId(), center.getId());
    if (fee != null)
    {
      throw new ValidationException("Already present for this center and program, use Edit to change!");
    }

    fee = request.toEntity(null);
    fee.setCenter(center);
    fee.setProgram(program);
    return centerProgramFeeRepository.saveAndFlush(fee);
  }

  @Transactional
  public CenterProgramFee updateCenterProgramFee(CenterProgramFeeRequest request)
  {

    if (request.getCenterId() == null)
    {
      throw new ValidationException("Missing center");
    }

    if (request.getProgramId() == null)
    {
      throw new ValidationException("Missing program");
    }
    if(request.getAdmissionFee()==null){
     request.setAdmissionFee(0);
    }
    if (request.getDeposit() == null)
    {
      request.setDeposit(0);
    }

    if (request.getAnnualFee() == null)
    {
      request.setAnnualFee(0);
    }

    CenterProgramFee fee = centerProgramFeeRepository.findOne(request.getId());

    if (fee == null)
    {
      throw new NotFoundException(String.format("Cannot locate center fee with id %d", request.getId()));
    }

    fee = request.toEntity(fee);
    centerProgramFeeRepository.saveAndFlush(fee);
    //updateStudentFee(fee);
    return fee;
  }

  private void updateStudentFee(CenterProgramFee centerProgramFee)
  {
    Center center = centerProgramFee.getCenter();
    Program program = centerProgramFee.getProgram();
    int fee = centerProgramFee.getFee();
    List<Student> students = studentRepository.findByCenterAndProgram(center, program);
    List<StudentFee> studentFees = studentFeeRepository.findByStudentIn(students);
    for (StudentFee studentFee : studentFees)
    {
      studentFee.setBaseFee(new BigDecimal(fee));
      studentFee.setCgst(centerProgramFee.getCgst());
      studentFee.setSgst(centerProgramFee.getSgst());
      studentFee.setIgst(centerProgramFee.getIgst());
      studentFee.setFinalFee(FeeUtils.calculateFinalFee(studentFee));
      studentFeeRepository.saveAndFlush(studentFee);
    }
  }

  public void deleteCenterProgramFee(Long id)
  {
    centerProgramFeeRepository.delete(id);
  }

  public List<CenterProgramFee> listCenterProgramFee(){
    return centerProgramFeeRepository.findAll();
  }
  public CenterProgramFee getProgramFee(CenterProgramFeeRequest request)
  {
    Center center = centerRepository.findOne(request.getCenterId());
    if (center == null)
    {
      throw new NotFoundException(String.format("Center[%s] not Found", request.getMaskedCenterId()));
    }
    Program program = programRepository.findOne(request.getProgramId());
    if (program == null)
    {
      throw new NotFoundException(String.format("Program[%s] not Found", request.getMaskedProgramId()));
    }


    CenterProgramFee centerProgramFee = centerProgramFeeRepository.findByProgramIdAndCenterId(request.getProgramId(), request.getCenterId());
    if (centerProgramFee == null)
    {
      throw new NotFoundException(String.format("Fee not found for Center[%s] and Program[%s]", request.getMaskedCenterId(), request.getMaskedProgramId()));
    }
    return centerProgramFee;
  }

  public CenterProgramFee centerProgramFeeRepository(Long centerId){
    return centerProgramFeeRepository.findByCenterId(centerId);
  }
  public CenterProgramFee getProgramFee(SaveFeeSlipRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Slip id null.");
    }
    StudentFeePaymentRequest studentFeePaymentRequest = slipRepository.findOne(request.getId());
    Student student = studentFeePaymentRequest.getStudent();
    CenterProgramFee centerProgramFee = centerProgramFeeRepository.findByProgramIdAndCenterId(student.getProgram().getId(), student.getCenter().getId());
    if (centerProgramFee == null)
    {
      throw new NotFoundException("Center Program fee not found");
    }
    return centerProgramFee;
  }

  public StudentFeePaymentRequest getSlip(Long id)
  {
    StudentFeePaymentRequest slip = slipRepository.findOne(id);
    if (id == null)
    {
      throw new ValidationException("Slip id is required.");
    }
    if (slip == null)
    {
      throw new ValidationException(String.format("Cannot locate slip [id = %s ]", mask(id)));
    }

    if (!hasCenter(slip.getStudent().getCenter().getCode()))
    {
      throw new ValidationException(String.format("Unauthorized access to center[code=%s]", slip.getStudent().getCenter().getCode()));
    }
    return slip;
  }
  public StudentFeePaymentRequestIpsaaClub getIpsaaClubSlip(Long id)
  {
    StudentFeePaymentRequestIpsaaClub slip = ipsaaClubSlipRepository.findOne(id);
    if (id == null)
    {
      throw new ValidationException("Slip id is required.");
    }
    if (slip == null)
    {
      throw new ValidationException(String.format("Cannot locate slip [id = %s ]", mask(id)));
    }

    if (!hasCenter(slip.getStudent().getCenter().getCode()))
    {
      throw new ValidationException(String.format("Unauthorized access to center[code=%s]", slip.getStudent().getCenter().getCode()));
    }
    return slip;
  }
  public File feeReport(FeeReportRequest request) throws IOException
  {
//    1.finding students fee
    Center center = hasCenter(request.getCenterId());
    // int quater=
    if (center == null)
    {
      throw new ValidationException("Unauthorized access to center.");
    }
    request.setCenterCode(center.getCode());
    List<StudentFee> studentFees = studentFeeRepository.findByStudentCenterIdOrderByStudentProgramCode(request.getCenterId());
//    finding center fee

//    2. adding fee to sheet
    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    int rowNumber = 0;
    Sheet studentFeeSheet = workbook.createSheet("StudentFee");
    Row row = studentFeeSheet.createRow(rowNumber++);
    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Student Name");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Program Name");
    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Group Name");
    row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Expected In");
    row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Expected Out");
    row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Program Fee");
    row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Transport Fee");
    row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Discount");
    row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Final Fee");
    row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Duration");

//    row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Security Diposit");

    for (StudentFee studentFee : studentFees)
    {
//      continue if student is not active
      if (!studentFee.getStudent().isActive())
      {
        continue;
      }
      row = studentFeeSheet.createRow(rowNumber++);
      row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getName());
      row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getCenterName());
      row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getProgramName());
      row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getGroupName());
      row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getExpectedIn() == null ? "" : studentFee.getStudent().getExpectedIn().toString());
      row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getStudent().getExpectedOut() == null ? "" : studentFee.getStudent().getExpectedOut().toString());
      row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(studentFee.getBaseFee().intValue());
      row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(studentFee.getTransportFee().intValue());
      row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getDiscount().toString());
      row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(studentFee.getFinalFee().intValue());
      row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(studentFee.getFeeDuration().toString());
//      row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(studentFee..intValue());

    }

    workbook.write(fileOutputStream);
    workbook.dispose();

    return file;
  }

  public File collectionFeeReport(StudentFeeSlipRequest slipRequest) throws IOException
  {
    if (StringUtils.isEmpty(slipRequest.getPeriod()))
    {
      throw new ValidationException("Period is required.");
    }

    Set<String> set = new HashSet<>();
    set.add("Paid");
    set.add("PartiallyPaid");
    set.add("Raised");
    if (!(!StringUtils.isEmpty(slipRequest.getReportType()) && set.contains(slipRequest.getReportType())))
    {
      throw new ValidationException("Report type is required.");
    }

    PaymentStatus reportType = PaymentStatus.valueOf(slipRequest.getReportType());

    Center center = centerRepository.findByCode(slipRequest.getCenterCode());
    if (center == null)
    {
      throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
    }

    if (!hasCenter(slipRequest.getCenterCode()))
    {
      throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
    }

    List<StudentFeePaymentRequest> slips = studentService.listFeeSlips(slipRequest);

    slips = slips.stream().filter(s -> s.getPaymentStatus() == reportType).collect(Collectors.toList());

    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    Sheet feeCollectionReportSheet = workbook.createSheet("response");
    int endHeader = FeeCollectionExcelReport.createHeader(feeCollectionReportSheet, 0, reportType);
    int i;

    BigDecimal total = BigDecimal.ZERO;
    BigDecimal paid = BigDecimal.ZERO;
    BigDecimal due = BigDecimal.ZERO;

    for (i = 0; i < slips.size(); i++)
    {
      StudentFeePaymentRequest slip = slips.get(i);
      FeeCollectionExcelReport report = new FeeCollectionExcelReport(slip);
      report.export(feeCollectionReportSheet, i + endHeader, reportType);
      total = total.add(report.getRaisedAmount());
      paid = paid.add(report.getPaidAmount());
      due = due.add(report.getDueAmount());
    }

    Row row = feeCollectionReportSheet.createRow(i + endHeader);

    switch (reportType)
    {
      case Paid:
        row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(paid.doubleValue());
        break;
      case PartiallyPaid:
        row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        row.createCell(12, Cell.CELL_TYPE_NUMERIC).setCellValue(due.doubleValue());
        break;
      case Raised:
        row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        break;
    }

    workbook.write(fileOutputStream);
    workbook.dispose();
    return file;
  }


  //----------------------------shubham ---------------------------------------------------------------------------//



  // shubham for feeReport with extraout hours
  public File FeeReport2(FeeReportRequest slipRequest) throws IOException
  {
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }
      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    List<StudentFeePaymentRequest> slips = studentService.listFeeReport(slipRequest);

    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    SXSSFWorkbook workbook = new SXSSFWorkbook();
    createStyle(workbook);
    Sheet feeReportSheet = workbook.createSheet("FeeReport");
    int topHeader = FeeReportExcel2.createHeader(feeReportSheet, 0);
    int i,rowNum=1;

    BigDecimal raised = BigDecimal.ZERO;
    BigDecimal paid = BigDecimal.ZERO;
    BigDecimal due = BigDecimal.ZERO;

    rowNum=0+topHeader;
    for (StudentFeePaymentRequest slip : slips)
    {
      FeeReportExcel2 report = new FeeReportExcel2(slip);
      report.export(feeReportSheet, rowNum);

      raised = raised.add(report.getRaisedAmount());
      paid = paid.add(report.getPaidAmount());
      due = due.add(report.getDueAmount());
      rowNum=report.getRowNum();
      rowNum++;
    }
    Row row = feeReportSheet.createRow(rowNum + topHeader);
    row.createCell(5, Cell.CELL_TYPE_NUMERIC).setCellValue(raised.doubleValue());
    row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(due.doubleValue());

    workbook.write(fileOutputStream);
    workbook.dispose();
    return file;
  }
  // shubham for feeReport with extraout hours
  public List<StudentFeeSlipResponse3> FeeReportTable(FeeReportRequest slipRequest) throws IOException
  {
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }
      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    return studentService.listFeeReport2(slipRequest);
  }
  // shubham
  public List<StudentFeeSlipResponse2> collectionFeeReportTable2(StudentFeeSlipRequest slipRequest) throws IOException
  {
    if (StringUtils.isEmpty(slipRequest.getPeriod()))
    {
      throw new ValidationException("Period is required.");
    }
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }

      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    return studentService.listFeeSlipsTable2(slipRequest);
  }

  public List<StudentFeeSlipResponse3> feeReportTable2(FeeReportRequest slipRequest) throws IOException
  {
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }
      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    return studentService.listFeeReport2(slipRequest);
  }
  // shubham
  public int calculateExtraHours(Student student,int quarter,int year){
    int extra=0;
    List<StudentAttendance> list=null;
    Date startDate=null,endDate=null;
    switch (quarter){
      case 2:
        startDate =new LocalDate(year, 4, 1).toDate();
        endDate =new LocalDate(year, 6, 30).toDate();
        break;
      case 3:
        startDate =new LocalDate(year, 7, 1).toDate();
        endDate =new LocalDate(year, 9, 30).toDate();
        break;
      case 4:
        startDate =new LocalDate(year, 10, 1).toDate();
        endDate =new LocalDate(year, 12, 30).toDate();
        break;
      case 1:
        startDate =new LocalDate(year+1, 1, 1).toDate();
        endDate =new LocalDate(year+1, 3, 30).toDate();
        break;
    }

    list=attendanceRepository.findByStudentAndCreatedDateBetweenAndExtraHoursNot(student,startDate,endDate,0);

    for(StudentAttendance attendace:list){
      if(attendace.getCheckout()!=null && attendace.getCheckin() !=null)
        if(student.getExpectedOut()!=null && student.getExpectedIn()!=null) {
          if(attendace.getCheckin().before(student.getExpectedIn())){
            Period p = new Period(new DateTime(attendace.getCheckin()),new DateTime(student.getExpectedIn()));

            int hours = p.getHours();
            extra+=hours;

//            if(student.getId()==590){
//              System.out.println("check in "+attendace.getCheckin() );
//              System.out.println("check in "+attendace.getCheckout() );
//              System.out.println("expected in "+student.getExpectedIn() );
//              System.out.println("expected in "+student.getExpectedOut() );
//              System.out.println("diff"+ hours);
//              System.out.println("extraHours"+ extra);
//            }
          }else if(attendace.getCheckout().after(student.getExpectedOut())){
            Period p = new Period(new DateTime(student.getExpectedOut()),new DateTime(attendace.getCheckout()));
            int hours = p.getHours();
            extra+=hours;
          }
        }
    }
    return extra;
  }
  // shubham for collection fee list of txid ref

  public File collectionFeeReport2(StudentFeeSlipRequest slipRequest) throws IOException
  {
    if (StringUtils.isEmpty(slipRequest.getPeriod()))
    {
      throw new ValidationException("Period is required.");
    }
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }

      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    List<StudentFeePaymentRequest> slips = studentService.listFeeSlips2(slipRequest);
    BigDecimal Raised = BigDecimal.ZERO;
    BigDecimal paid = BigDecimal.ZERO;
    BigDecimal due = BigDecimal.ZERO;

    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists()) {
      try {
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        createStyle(workbook);
        Sheet feeCollectionReportSheet = workbook.createSheet("Fee Collection");// creating a blank sheet
        int topHeader = FeeCollectionExcelReport2.createHeader(feeCollectionReportSheet, 0);
        int i, rowNum = 1;

        rowNum = 0 + topHeader;
        for (StudentFeePaymentRequest slip:slips) {
          FeeCollectionExcelReport2 report = new FeeCollectionExcelReport2(slip);
          report.export(feeCollectionReportSheet, rowNum);
          Raised = Raised.add(report.getRaisedAmount());
          paid = paid.add(report.getPaidAmount());
          due = due.add(report.getDueAmount());
          rowNum = report.getRowNum();
          //rowNum++;
        }
        Row row = feeCollectionReportSheet.createRow(rowNum + topHeader);
        String formula="SUM(H3:H"+(rowNum+1)+")";
        row.createCell(7, Cell.CELL_TYPE_FORMULA).setCellFormula(formula);

        //row.createCell(11, Cell.CELL_TYPE_NUMERIC).setCellValue(paid.doubleValue());
        //row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(due.doubleValue());


        workbook.write(fileOutputStream);
        workbook.dispose();
        return file;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }
  public File collectionHdfcReport2(StudentFeeSlipRequest slipRequest) throws IOException
  {

    if (StringUtils.isEmpty(slipRequest.getPeriod()))
    {
      throw new ValidationException("Period is required.");
    }
    List<HdfcResponse> hdfc=null;
    if(slipRequest.getMonth()>0){
      hdfc = hdfcResponseRepository.findByIpsaaClubSlipMonthAndIpsaaClubSlipYear(slipRequest.getMonth(),slipRequest.getYear());
    }
    else
    hdfc = hdfcResponseRepository.findBySlipQuarterAndSlipYear(slipRequest.getQuarter(),slipRequest.getYear());

    if(hdfc==null){
      throw new ValidationException("no data found");
    }

    List<LinkedHashMap<String,Object>> list=new ArrayList<>();
    int count=1;
    for(HdfcResponse res:hdfc){
      LinkedHashMap<String,Object> row=new LinkedHashMap<>();
      row.put("s.id",count++);
      if(res.getSlip()==null){
        row.put("student_name",res.getSlip().getStudent().getProfile().getFullName());
        row.put("center",res.getSlip().getStudent().getCenterName());
        row.put("program_name",res.getSlip().getStudent().getProgramName());
        row.put("slip_id",res.getSlip().getId());
      }else{
        row.put("student_name",res.getIpsaaClubSlip().getStudent().getProfile().getFullName());
        row.put("center",res.getIpsaaClubSlip().getStudent().getCenterName());
        row.put("program_name",res.getIpsaaClubSlip().getStudent().getProgramName());
        row.put("slip_id",res.getIpsaaClubSlip().getId());
      }
      row.put("amount",res.getAmount());
      row.put("trans_date",res.getTransDate());
      row.put("type",res.getType());
      row.put("bank_ref_no",res.getBankRefNo());
      row.put("status_message",res.getStatusMessage());
      row.put("tracking_id",res.getTrackingId());
      row.put("billing_email",res.getBillingEmail());
      row.put("billing_name",res.getBillingName());

      list.add(row);
    }
    ExcelGenerater eg=new ExcelGenerater(list);
    return eg.create(exportDir,"hdfc report");
  }
  public File studentFeeSheetReport(StudentFeeSlipRequest slipRequest) throws IOException
  {

    // collection list
    // center wise collection list
    slipRequest.setCenterCode("All");
    List<StudentFeePaymentRequest> slips=studentService.listFeeSlips2(slipRequest);

    ExcelGenerater eg=new ExcelGenerater();
    List<LinkedHashMap<String,Object>> slipList=new ArrayList<>();
    int count=1;
    for(StudentFeePaymentRequest res:slips){
      LinkedHashMap<String,Object> row=new LinkedHashMap<>();
      row.put("s.id",count++);
      row.put("center",res.getStudent().getCenterName());
      row.put("student Name",res.getStudent().getName());
      row.put("StudentActive",res.getStudent().isActive());
      row.put("groupName",res.getStudent().getGroupName());
      row.put("program Name",res.getStudent().getProgramName());
      row.put("total fee",res.getTotalFee());
      row.put("paid amount",res.getPaidAmount());
      row.put("unpaid amount",res.getTotalFee().subtract(res.getPaidAmount()));
      row.put("payment status",res.getPaymentStatus().name());
      slipList.add(row);
    }

    eg.createWithSheets("hdfc report",slipList);
    for(Center centers:getUserCenters()){
      slipList.clear();
      List<StudentFeePaymentRequest> centerSlips= slips.stream()
              .filter(s->s.getStudent().getCenter().getCode().equals(centers.getCode())).collect(Collectors.toList());

      for(StudentFeePaymentRequest res:centerSlips){
        LinkedHashMap<String,Object> row=new LinkedHashMap<>();
        row.put("s.id",count++);
        row.put("center",res.getStudent().getCenterName());
        row.put("Admission Date",res.getStudent().getProfile().getAdmissionDate());
        row.put("Last Modified date",res.getLastModifiedDate());
        row.put("Student Name",res.getStudent().getName());
        row.put("Student Active",res.getStudent().isActive());
        row.put("Group name",res.getStudent().getGroupName());
        row.put("Program name",res.getStudent().getProgramName());
        row.put("base Fee",res.getFinalBaseFee());
        row.put("Base Fee Discount",res.getFinalBaseFee());

        row.put("Annual Fee",res.getFinalAnnualCharges());
        row.put("Annual Fee Discount",res.getAnnualFeeDiscount());

        row.put("Admission Fee",res.getFinalAdmissionFee());
        row.put("Admission Fee Discount",res.getAddmissionFeeDiscount());

        row.put("Security Deposite",res.getPaymentStatus().name());
        row.put("Security Deposite Discount",res.getDepositFeeDiscount());

        row.put("Monthly Transport Fee",res.getTransportFee());
        row.put("Extra Charges",res.getExtraCharge());
        row.put("Late Payment",res.getLatePaymentCharge());
        row.put("Uniform Charges",res.getUniformCharges());
        row.put("Stationary Charges",res.getStationary());
        row.put("Total Fee",res.getTotalFee());
        slipList.add(row);
      }
      eg.createWithSheets(centers.getCode(),slipList);
    }
    return eg.createWorkBook(exportDir);
  }
  //--------------------------------------shubham ---------------------------------------------------------------
  // shubham for feeReport with extraout hours
  public File FeeReportIpsaClub2(FeeReportRequest slipRequest) throws IOException
  {
    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }
      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    List<StudentFeePaymentRequestIpsaaClub> slips = studentService.listFeeReportIpsaClub(slipRequest);

    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }

    List<LinkedHashMap<String,Object>> list=new ArrayList<>();
    int count=0;
    for (StudentFeePaymentRequestIpsaaClub slip : slips)
    {
      LinkedHashMap<String,Object> row=new LinkedHashMap<>();
            row.put("s.id",count++);
            row.put("student_name",slip.getStudent().getProfile().getFullName());
            row.put("center",slip.getStudent().getCenterName());
            row.put("program_name",slip.getStudent().getProgramName());
            row.put("Invoice Date",slip.getInvoiceDate());
            row.put("Gst",slip.getGstAmount());
            row.put("Annual Fee",slip.getFinalAnnualFee());
            row.put("Security",slip.getFinalDepositFee());
            row.put("Raised Amount",slip.getTotalFee());
            row.put("Extra Amount",slip.getExtraCharge());
            row.put("Due Amount",slip.getTotalFee().subtract(slip.getPaidAmount()));
            row.put("Payment Status",slip.getPaymentStatus());
            row.put("Comment",slip.getComments());
      list.add(row);
    }
    ExcelGenerater eg=new ExcelGenerater(list);
    file=eg.create(exportDir,"ipsaa club fee report");
    return file;
  }
  public File ipsaaCollectionFeeReport2(FeeReportRequest slipRequest) throws IOException
  {

    if(!slipRequest.getCenterCode().equals("All")){
      Center center = centerRepository.findByCode(slipRequest.getCenterCode());
      if (center == null)
      {
        throw new ValidationException(String.format("Cannot locate Center[code = %s]", slipRequest.getCenterCode()));
      }
      if (!hasCenter(slipRequest.getCenterCode()))
      {
        throw new ValidationException(String.format("Unauthorized access to center[code=%s] user[email=%s].", slipRequest.getCenterCode(), getUser().getEmail()));
      }
    }
    List<StudentFeePaymentRequestIpsaaClub> slips = studentService.listFeeReportIpsaClub(slipRequest);

    File file = new File(exportDir + UUID.randomUUID() + ".xlsx");
    if (!file.exists())
    {
      file.createNewFile();
    }

    List<LinkedHashMap<String,Object>> list=new ArrayList<>();
    int count=0;
    for (StudentFeePaymentRequestIpsaaClub slip : slips)
    {
      for(StudentFeePaymentRecordIpsaaClub payment:slip.getPayments()){
        LinkedHashMap<String,Object> row=new LinkedHashMap<>();
        row.put("s.id",count++);
        row.put("student_name",payment.getStudent().getProfile().getFullName());
        row.put("center",payment.getStudent().getCenterName());
        row.put("program_name",payment.getStudent().getProgramName());
        row.put("Invoice Date",payment.getRequest().getInvoiceDate());
        row.put("Expire Date",payment.getRequest().getExpireDate());
        row.put("Paid Amount",payment.getPaidAmount());
        row.put("Txnid",payment.getTxnid());
        row.put("comment",payment.getComment()==null?"":payment.getComment());
        row.put("confirmed",payment.getConfirmed()?"True":"False");
        list.add(row);
      }
    }
    ExcelGenerater eg=new ExcelGenerater(list);
    file=eg.create(exportDir,"ipsaa club fee report");
    return file;
  }
  ///////////Avneet
  public List<Program> programByCenter(Long id){
    List<Program> list=new ArrayList<>();

    List<CenterProgramFee> centers=centerProgramFeeRepository.findByCenterIdOrderByProgramId(unmask(id));
    List<Program> programs=programRepository.findAllByOrderByIdAsc();     ////by default returns in order ?

    int size= centers.size();
    int j=0;

    for(Program p:programs){
      if(j<size && p.getId().equals(centers.get(j).getProgram().getId())){
        list.add(p);
        j++;
      }
    }
    return list;
  }
}
