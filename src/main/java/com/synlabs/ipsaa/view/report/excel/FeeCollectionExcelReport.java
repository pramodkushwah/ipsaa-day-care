package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.util.FeeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

public class FeeCollectionExcelReport
{
  private String admissionNumber = "";
  private String name            = "";
  private String centerName      = "";
  private String programName     = "";
  private String groupName       = "";
  private String feeDuration     = "";
  private String month           = "";
  private String quarter         = "";
  private int year;

  private String     paymentStatus = "";
  private String     paymentMode   = "";
  private BigDecimal raisedAmount  = BigDecimal.ZERO;
  private BigDecimal paidAmount    = BigDecimal.ZERO;
  private BigDecimal dueAmount     = BigDecimal.ZERO;
  private String     confirmed     = "";

  public FeeCollectionExcelReport(StudentFeePaymentRequest slip)
  {
    Student student = slip.getStudent();
    admissionNumber = student.getAdmissionNumber();
    name = student.getName();
    centerName = student.getCenterName();
    programName = student.getProgramName();
    groupName = student.getGroupName();
    feeDuration = slip.getFeeDuration().toString();
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

  public Sheet export(Sheet sheet, int rowNumber, PaymentStatus reportType)
  {
    Row row = sheet.createRow(rowNumber);

    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue(name);
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue(centerName);
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue(programName);

    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue(feeDuration);

    switch (reportType)
    {
      case Paid:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(month);
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(quarter);
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(paymentStatus);
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(paymentMode);
        row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(paidAmount.doubleValue());
        row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue(raisedAmount.doubleValue());
        row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(confirmed);
        break;
      case PartiallyPaid:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(month);
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(quarter);
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(paymentStatus);
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue(paymentMode);
        row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue(raisedAmount.doubleValue());
        row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(dueAmount.doubleValue());
        row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue(confirmed);
        break;
      case Raised:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue(month);
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue(quarter);
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue(paymentStatus);
        row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(raisedAmount.doubleValue());
        break;
    }

    return sheet;
  }

  public static int createHeader(Sheet sheet, int rowNumber, PaymentStatus reportType)
  {
    Row row = sheet.createRow(rowNumber);

    row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("Student Name");
    row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("Center Name");
    row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("Program Name");

    row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("Fee Duration");

    switch (reportType)
    {
      case Paid:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Month");
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Quarter");
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Payment Status");
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Payment Mode");
        row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Total Amount");
        row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Paid Amount");
        row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Confirmed");
        break;
      case PartiallyPaid:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Month");
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Quarter");
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Payment Status");
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Payment Mode");
        row.createCell(8, Cell.CELL_TYPE_STRING).setCellValue("Total Amount");
        row.createCell(9, Cell.CELL_TYPE_STRING).setCellValue("Due Amount");
        row.createCell(10, Cell.CELL_TYPE_STRING).setCellValue("Confirmed");
        break;
      case Raised:
        row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("Month");
        row.createCell(5, Cell.CELL_TYPE_STRING).setCellValue("Quarter");
        row.createCell(6, Cell.CELL_TYPE_STRING).setCellValue("Payment Status");
        row.createCell(7, Cell.CELL_TYPE_STRING).setCellValue("Raised Amount");
        break;
    }

    return rowNumber + 1;
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
