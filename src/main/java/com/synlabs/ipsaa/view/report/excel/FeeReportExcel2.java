package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.ApprovalStatus;
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
  private String     paymentStatus = "";
  private BigDecimal raisedAmount  = BigDecimal.ZERO;
  private BigDecimal paidAmount    = BigDecimal.ZERO;
  private BigDecimal dueAmount     = BigDecimal.ZERO;
  private BigDecimal extraHours     = BigDecimal.ZERO;
  private boolean isActive;
  private int rowNum=0;

  public FeeReportExcel2(StudentFeePaymentRequest slip)
  {

  //  this.slip=slip;
   // this.extraHours=extraHours;

    //  approvalstatus = student.getApprovalStatus();

    //groupName = student.getGroupName();
    //feeDuration = slip.getFeeDuration().toString();
    // extra entry
    //motherName=student.getMother().getFullName();
    //FatherName=student.getFather().getFullName();


    //month = FeeUtils.getMonthName(slip.getMonth());
    //year = slip.getYear();
    //quarter = FeeUtils.getFYQuarter(slip.getQuarter());


      StudentFee fee=null;
      isActive=slip.getStudent().isActive();
      Student student = slip.getStudent();
      admissionNumber = student.getAdmissionNumber();
      name = student.getName();
      id=student.getId();
      centerName = student.getCenterName();
      programName = student.getProgramName();
        raisedAmount = slip.getTotalFee();
        extraHours=slip.getExtraHours();
        dueAmount = raisedAmount;

    paymentStatus = slip.getPaymentStatus().toString();
    // slip status is "Paid" or "PartiallyPaid"
    if (slip.getPaymentStatus() == PaymentStatus.Paid
        || slip.getPaymentStatus() == PaymentStatus.PartiallyPaid)
    {
      paidAmount = slip.getPaidAmount();
      dueAmount = dueAmount.subtract(paidAmount);
      if (BigDecimalUtils.lessThen(dueAmount, new BigDecimal(5)))
      {
        dueAmount = BigDecimal.ZERO;
      }
    }
  }

  public Sheet export(Sheet sheet, int rowNumber)
  {
    this.rowNum=rowNumber;
      Row row = sheet.createRow(rowNum);
      int index=0;
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(id);
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(name);
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(isActive);

      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(centerName);
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(programName);
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(extraHours.intValue());
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(raisedAmount.doubleValue());
      row.createCell(index++, Cell.CELL_TYPE_NUMERIC).setCellValue(dueAmount.doubleValue());
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue(paymentStatus);
    return sheet;
  }

  public static int createHeader(Sheet sheet, int rowNumber)
  {
    Row row = sheet.createRow(rowNumber);
    int index=0;
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Student Id");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Student Name");
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Active");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Program Name");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Extra Hours");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Raised Amount");
    row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Due Amount");
      row.createCell(index++, Cell.CELL_TYPE_STRING).setCellValue("Payment Status");
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