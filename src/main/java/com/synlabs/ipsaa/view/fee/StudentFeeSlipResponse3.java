package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StudentFeeSlipResponse3 implements Response
{
  private Long id;

  private String     fullName;
  private String     group;
  private String     program;
  private String     center;
  private int        year;
  private BigDecimal fee;
  private BigDecimal totalFee;
  private String     status;
  private BigDecimal payableAmount;

 // private List<StudentFeePaymentResponse> payments;


  public StudentFeeSlipResponse3(StudentFeePaymentRequest slip)
  {
    this.center=slip.getStudent().getCenterName();
    this.id = slip.getId();
    this.fullName = slip.getStudent().getProfile().getFullName();
    this.group = slip.getStudent().getGroup().getName();
    this.program = slip.getStudent().getProgram().getName();
    this.year = slip.getYear();
    this.totalFee = slip.getTotalFee();
    this.fee = slip.getBaseFee();
    this.status = slip.getPaymentStatus().name();
    this.payableAmount = this.totalFee;

    //this.transportFee=fee.getTransportFee().toString();
    //StudentFee fee=null;
    //fee = studentFeeRepository.findByStudent(slip.getStudent());
    //this.month = slip.getMonth();
    //this.quarter = slip.getQuarter();
    //this.extraCharge = slip.getExtraCharge();
    //this.latePaymentCharge = slip.getLatePaymentCharge();
    //this.feeDuration = slip.getFeeDuration().name();
    //this.comments = slip.getComments();
    //this.invoiceDate = slip.getInvoiceDate() == null ? null : slip.getInvoiceDate().toString();
    //this.deposit = slip.getDeposit();
    //this.annualFee = slip.getAnnualFee();
    //this.sgst = slip.getSgst();
    //this.cgst = slip.getCgst();
    //this.igst = slip.getIgst();
    //this.balance = slip.getBalance();
    //this.adjust = slip.getAdjust();
    //this.generateActive = slip.isGenerateActive();

//    this.adjust = this.adjust == null ? BigDecimal.ZERO : this.adjust;
//    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
//    this.autoComments = slip.getAutoComments();
//    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
//    {
//      payments = new ArrayList<>(slip.getPayments().size());
//      slip.getPayments().forEach(payment -> {
//        payments.add(new StudentFeePaymentResponse(payment));
//        this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
//      });
//    }
  }

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
  public BigDecimal getPayableAmount()
  {
    return payableAmount;
  }
  public int getYear()
  {
    return year;
  }

  public BigDecimal getFee()
  {
    return fee;
  }


  public BigDecimal getTotalFee()
  {
    return totalFee;
  }

  public String getStatus()
  {
    return status;
  }


//
//  public boolean isGenerateActive() {
//	return generateActive;
//  }
//
//public String getAutoComments()
//  {
//    return autoComments;
//  }
//
//  public BigDecimal getAdjust()
//  {
//    return adjust;
//  }
//
//public BigDecimal getIgst()
//{
//  return igst;
//}
//  public BigDecimal getBalance()
//  {
//    return balance;
//  }
//
//  public BigDecimal getCgst()
//  {
//    return cgst;
//  }
//
//  public BigDecimal getSgst()
//  {
//    return sgst;
//  }
//
//  public int getQuarter()
//  {
//    return quarter;
//  }
//
//  public BigDecimal getDeposit()
//  {
//    return deposit;
//  }
//
//  public BigDecimal getAnnualFee()
//  {
//    return annualFee;
//  }
//  public String getInvoiceDate()
//  {
//    return invoiceDate;
//  }
//  public BigDecimal getExtraCharge()
//  {
//    return extraCharge;
//  }
//
//  public BigDecimal getLatePaymentCharge()
//  {
//    return latePaymentCharge;
//  }
//
//  public String getComments()
//  {
//    return comments;
//  }
//
//  public String getFeeDuration()
//  {
//    return feeDuration;
//  }
//  public String getComments()
//  {
//    return comments;
//  }
//
//  public String getFeeDuration()
//  {
//    return feeDuration;
//  }



//  public int getMonth()
//  {
//    return month;
//  }




//  public List<StudentFeePaymentResponse> getPayments()
//  {
//    return payments;
//  }

  @Override
  public String toString()
  {
    return "StudentFeeSlipResponse{" +
        "id=" + id +
        ", fullName='" + fullName + '\'' +
        ", group='" + group + '\'' +
        ", program='" + program + '\'' +
  //      ", month=" + month +
      //  ", quarter=" + quarter +
        ", year=" + year +
        ", fee=" + fee +
       // ", extraCharge=" + extraCharge +
       // ", latePaymentCharge=" + latePaymentCharge +
        ", totalFee=" + totalFee +
       // ", invoiceDate='" + invoiceDate + '\'' +
        ", status='" + status + '\'' +
       // ", comment='" + comments + '\'' +
       // ", feeDuration='" + feeDuration + '\'' +
        '}';
  }
}
