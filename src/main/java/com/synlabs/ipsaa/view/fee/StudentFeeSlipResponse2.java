package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.student.IpsaaClubRecordResponce;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.synlabs.ipsaa.util.BigDecimalUtils.ZERO;

public class StudentFeeSlipResponse2 implements Response
{
  private Long id;
  private String     fatherName;
  private String     motherName;
  private String     fullName;
  private String     group;
  private String     program;
  private BigDecimal fee;
  private BigDecimal extraCharge;
  private BigDecimal totalFee;
  private String     status;
  private BigDecimal payableAmount;
  private BigDecimal balance;
  private BigDecimal adjust;
  private boolean generateActive;

  private List<StudentFeePaymentResponse> payments;
  private List<IpsaaClubRecordResponce> ipsaaPayments;


  public StudentFeeSlipResponse2(StudentFeePaymentRequest slip)
  {
    this.id = slip.getId();
    this.motherName=slip.getStudent().getMother().getFullName();
    this.fatherName=slip.getStudent().getFather().getFullName();
    this.fullName = slip.getStudent().getProfile().getFullName();
    this.group = slip.getStudent().getGroup().getName();
    this.program = slip.getStudent().getProgram().getName();

    this.extraCharge = slip.getExtraCharge();
    this.totalFee = slip.getTotalFee();
    this.fee = slip.getBaseFee();
    this.status = slip.getPaymentStatus().name();
    this.payableAmount = this.totalFee;
    this.balance = slip.getBalance();
    this.adjust = slip.getAdjust();
    this.generateActive = slip.isGenerateActive();

    this.adjust = this.adjust == null ? BigDecimal.ZERO : this.adjust;
    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
    //this.autoComments = slip.getAutoComments();
    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
    {
      payments = new ArrayList<>(slip.getPayments().size());
      slip.getPayments().forEach(payment -> {
        payments.add(new StudentFeePaymentResponse(payment));
        this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
     //   System.out.println("break");
      });
    }
  }

  public StudentFeeSlipResponse2(StudentFeePaymentRequestIpsaaClub slip)
  {
    this.id = slip.getId();
    this.motherName=slip.getStudent().getMother().getFullName();
    this.fatherName=slip.getStudent().getFather().getFullName();
    this.fullName = slip.getStudent().getProfile().getFullName();
    this.group = slip.getStudent().getGroup().getName();
    this.program = slip.getStudent().getProgram().getName();

    this.extraCharge = slip.getExtraCharge();
    this.totalFee = slip.getTotalFee();
    this.fee = slip.getBaseFee();
    this.status = slip.getPaymentStatus().name();
    this.payableAmount = this.totalFee;
    this.balance = slip.getBalance();
    this.adjust = ZERO;
    this.generateActive = slip.isGenerateActive();

    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
    //this.autoComments = slip.getAutoComments();
    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
    {
      ipsaaPayments = new ArrayList<>(slip.getPayments().size());
      slip.getPayments().forEach(payment -> {
        ipsaaPayments.add(new IpsaaClubRecordResponce(payment));
        this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
      });
    }
  }
  public List<IpsaaClubRecordResponce> getIpsaaPayments() {
    return ipsaaPayments;
  }

  public void setIpsaaPayments(List<IpsaaClubRecordResponce> ipsaaPayments) {
    this.ipsaaPayments = ipsaaPayments;
  }

  public boolean isGenerateActive() {
	return generateActive;
  }

//public String getAutoComments()
//  {
//    return autoComments;
//  }

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public BigDecimal getBalance()
  {
    return balance;
  }

  public BigDecimal getPayableAmount()
  {
    return payableAmount;
  }

//  public BigDecimal getDeposit()
//  {
//    return deposit;
//  }
//
//  public BigDecimal getAnnualFee()
//  {
//    return annualFee;
//  }

  public Long getId()
  {
    return mask(id);
  }

  public String getFullName()
  {
    return fullName;
  }

  public String getGroup()
  {
    return group;
  }

  public String getProgram()
  {
    return program;
  }

//  public int getMonth()
//  {
//    return month;
//  }
//
//  public int getQuarter()
//  {
//    return quarter;
//  }
//
//  public int getYear()
//  {
//    return year;
//  }

  public BigDecimal getFee()
  {
    return fee;
  }

  public BigDecimal getExtraCharge()
  {
    return extraCharge;
  }
//
//  public BigDecimal getLatePaymentCharge()
//  {
//    return latePaymentCharge;
//  }

  public BigDecimal getTotalFee()
  {
    return totalFee;
  }

//  public String getInvoiceDate()
//  {
//    return invoiceDate;
//  }

  public String getStatus()
  {
    return status;
  }

//  public String getComments()
//  {
//    return comments;
//  }

//  public String getFeeDuration()
//  {
//    return feeDuration;
//  }

  public List<StudentFeePaymentResponse> getPayments()
  {
    return payments;
  }

  public String getFatherName() {
    return fatherName;
  }

  public String getMotherName() {
    return motherName;
  }


  @Override
  public String toString()
  {
    return "StudentFeeSlipResponse{" +
        "id=" + id +
        ", fullName='" + fullName + '\'' +
        ", group='" + group + '\'' +
        ", program='" + program + '\'' +
 //       ", month=" + month +
    //    ", quarter=" + quarter +
   //     ", year=" + year +
        ", fee=" + fee +
        ", extraCharge=" + extraCharge +
      //  ", latePaymentCharge=" + latePaymentCharge +
        ", totalFee=" + totalFee +
       // ", invoiceDate='" + invoiceDate + '\'' +
        ", status='" + status + '\'' +
       // ", comment='" + comments + '\'' +
       // ", feeDuration='" + feeDuration + '\'' +
        '}';
  }
}
