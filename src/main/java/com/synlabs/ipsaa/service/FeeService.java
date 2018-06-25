package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.CenterCharge;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.fee.Charge;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.view.center.CenterChargeRequest;
import com.synlabs.ipsaa.view.center.CenterFeeRequest;
import com.synlabs.ipsaa.view.center.CenterProgramFeeRequest;
import com.synlabs.ipsaa.view.fee.ChargeRequest;
import com.synlabs.ipsaa.view.fee.FeeReportRequest;
import com.synlabs.ipsaa.view.fee.SaveFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.report.excel.FeeCollectionExcelReport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

  @Autowired
  private StudentFeePaymentRepository slipRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private StudentFeeRepository studentFeeRepository;

  @Autowired
  private StudentService studentService;

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

  public List<CenterProgramFee> listCenterFee(CenterFeeRequest request)
  {
    return feeRepository.findByCenterId(request.getCenterId());
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
    updateStudentFee(fee);
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

  public File feeReport(FeeReportRequest request) throws IOException
  {
//    1.finding students fee
    Center center = hasCenter(request.getCenterId());
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
        row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(paid.doubleValue());
        break;
      case PartiallyPaid:
        row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(due.doubleValue());
        break;
      case Raised:
        row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(total.doubleValue());
        break;
    }

    workbook.write(fileOutputStream);
    workbook.dispose();
    return file;
  }
}
