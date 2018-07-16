package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.util.FeeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

// shubham
public class FeeReportExcel2
{
  private String admissionNumber = "";
  private String name            = "";
  private Long id;
  private String centerName      = "";
  private String programName     = "";
  private String groupName       = "";
  private String feeDuration     = "";
  private String month           = "";
  private String quarter         = "";
  private int year;
  // extra
  private String txnid           ="";
  private String motherName      ="";
  private String FatherName      ="";

 // private BigDecimal extraHours   = BigDecimal.ZERO;
  private String     paymentStatus = "";
  private String     paymentMode   = "";
  private BigDecimal raisedAmount  = BigDecimal.ZERO;
  private BigDecimal paidAmount    = BigDecimal.ZERO;
  private BigDecimal dueAmount     = BigDecimal.ZERO;
  private String     confirmed     = "";
  private StudentFeePaymentRequest slip;
  private int rowNum=0;

  public FeeReportExcel2(StudentFeePaymentRequest slip/*, BigDecimal extraHours*/,StudentFeeRepository studentFeeRepository)
  {

    this.slip=slip;
   // this.extraHours=extraHours;
      StudentFee fee=null;
      fee = studentFeeRepository.findByStudent(slip.getStudent());
    Student student = slip.getStudent();
    admissionNumber = student.getAdmissionNumber();
    name = student.getName();
    id=student.getId();
    centerName = student.getCenterName();
    programName = student.getProgramName();
    groupName = student.getGroupName();
    feeDuration = slip.getFeeDuration().toString();
    // extra entry
    motherName=student.getMother().getFullName();
    FatherName=student.getFather().getFullName();


    month = FeeUtils.getMonthName(slip.getMonth());
    year = slip.getYear();
    quarter = FeeUtils.getFYQuarter(slip.getQuarter());
    raisedAmount = slip.getTotalFee();
    dueAmount = raisedAmount;

    paymentStatus = slip.getPaymentStatus().toString();
    // slip status is "Paid" or "PartiallyPaid"
    if (slip.getPaymentStatus() == PaymentStatus.Paid
        || slip.getPaymentStatus() == PaymentStatus.PartiallyPaid)
    {
      if (!CollectionUtils.isEmpty(slip.getPayments()))
      {
        paymentMode = slip.getPayments().get(0).getPaymentMode().toString();
      }
      paidAmount = slip.getPaidAmount();

      dueAmount = dueAmount.subtract(paidAmount);
      if (BigDecimalUtils.lessThen(dueAmount, new BigDecimal(5)))
      {
        dueAmount = BigDecimal.ZERO;
      }
    }

    if (slip.getPaymentStatus() == PaymentStatus.PartiallyPaid
        || slip.getPaymentStatus() == PaymentStatus.Paid)
    {
      boolean confirmed = true;
      for (StudentFeePaymentRecord payment : slip.getPayments())
      {
        if (payment.getConfirmed() == null || !payment.getConfirmed())
        {
          confirmed = false;
        }
      }
      this.confirmed = confirmed ? "Confirmed" : "Not Confirmed";

    }
  }

  public Sheet export(Sheet sheet, int rowNumber)
  {
    this.rowNum=rowNumber;
    //for(int i=0;i<slip.getPayments().size();i++){
      //StudentFeePaymentRecord record=slip.getPayments().get(i);
      //System.out.println("studentData "+id +" "+record.getPaidAmount());
      Row row = sheet.createRow(++this.rowNum);
      //if(record.getTxnid()!=null)
      //row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(record.getTxnid());
      row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(id);
      row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(name);
      row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(centerName);
      row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(programName);
      row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(feeDuration);
      row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(month);
      row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(quarter);
      row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(paymentStatus);
      row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue(paymentMode);
      row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(paidAmount.intValue());
      //this.paidAmount.add(new BigDecimal(record.getPaidAmount().doubleValue()));
      row.createCell(11, Cell.CELL_TYPE_STRING).setCellValue(raisedAmount.doubleValue());
      row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue(confirmed);
      //row.createCell(13, Cell.CELL_TYPE_NUMERIC).setCellValue(extraHours.doubleValue());
      row.createCell(14, Cell.CELL_TYPE_NUMERIC).setCellValue(dueAmount.doubleValue());
      row.createCell(15, Cell.CELL_TYPE_STRING).setCellValue(motherName);
      row.createCell(16, Cell.CELL_TYPE_STRING).setCellValue(FatherName);
      //row.createCell(17, Cell.CELL_TYPE_STRING).setCellValue(record.getPaymentDate().toString());
      //rowNumber++;
    return sheet;
  }

  public static int createHeader(Sheet sheet, int rowNumber)
  {
    Row row = sheet.createRow(rowNumber);

    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("TXN ID");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Student Id");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Student Name");
    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
    row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Program Name");
    row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Fee Duration");
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Month");
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Quarter");
        row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Payment Status");
        row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Payment Mode");
        row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Total Amount Pain");
        row.createCell(11, Cell.CELL_TYPE_STRING).setCellValue("Raised Amount");
        row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue("Confirmed");
        row.createCell(13, Cell.CELL_TYPE_STRING).setCellValue("Extra hours");
        row.createCell(14, Cell.CELL_TYPE_STRING).setCellValue("Due Amount");

        //
        row.createCell(15, Cell.CELL_TYPE_STRING).setCellValue("Mother Name");
    row.createCell(16, Cell.CELL_TYPE_STRING).setCellValue("Father Name");
    row.createCell(17, Cell.CELL_TYPE_STRING).setCellValue("Payment Date");

    return rowNumber + 1;
  }

  public int getRowNum() {
    return rowNum;
  }

  public void setRowNum(int rowNum) {
    this.rowNum = rowNum;
  }

  public BigDecimal getRaisedAmount()
  {
    return raisedAmount;
  }

  public BigDecimal getPaidAmount()
  {
    return paidAmount;
  }

  public BigDecimal getDueAmount()
  {
    return dueAmount;
  }
}