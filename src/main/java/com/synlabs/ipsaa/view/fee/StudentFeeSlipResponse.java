package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.synlabs.ipsaa.util.FeeUtils.ZERO;

public class StudentFeeSlipResponse implements Response
{
  private Long id;

  private String     fullName;
  private String     group;
  private String     program;
  private int        month;
  private int        quarter;
  private int        year;
  private BigDecimal fee;
  private BigDecimal extraCharge;
  private BigDecimal latePaymentCharge;
  private BigDecimal totalFee;
  private String     invoiceDate;
  private String     status;
  private String     comments;
  private String     feeDuration;
  private BigDecimal deposit;
  private BigDecimal annualFee;
  private BigDecimal payableAmount;
  private BigDecimal cgst;
  private BigDecimal sgst;
  private BigDecimal igst;
  private BigDecimal balance;
  private BigDecimal adjust;
  private String     autoComments;
  private boolean generateActive;


  private BigDecimal uniformCharges;
  private BigDecimal stationary;
  private BigDecimal transportFee;

  private BigDecimal finalTransportFee;
  private BigDecimal finalAnnualCharges;

  private BigDecimal admissionFee;
  private BigDecimal finalAdmissionFee;
  private BigDecimal finalBaseFee;
  private BigDecimal baseFee;
  private BigDecimal finalDepositFee;

  private BigDecimal baseFeeDiscount;
  private BigDecimal annualFeeDiscount;
  private BigDecimal addmissionFeeDiscount;
  private BigDecimal depositFeeDiscount;

  private BigDecimal gstAmount;
  private BigDecimal feeRatio;

  private BigDecimal totalOtherPaidAmount=ZERO;
  private BigDecimal totalOtherRemainningAmount=ZERO;

  private BigDecimal totalPaidAmount=ZERO;
  private BigDecimal uniformPaidAmountTotal=ZERO;
  private BigDecimal stationaryPaidAmountTotal=ZERO;
  private BigDecimal annualPaidAmountTotal=ZERO;
  private BigDecimal addmissionPaidAmountTotal=ZERO;
  private BigDecimal depositPaidAmountTotal=ZERO;
  private BigDecimal programPaidAmountTotal=ZERO;
  private BigDecimal transportPaidAmountTotal=ZERO;

  private BigDecimal extraHours;

  private List<StudentFeePaymentResponse> payments;

  public BigDecimal getExtraHours() {
    return extraHours;
  }

  public void setExtraHours(BigDecimal extraHours) {
    this.extraHours = extraHours;
  }

  public BigDecimal getTotalOtherPaidAmount() {
    return totalOtherPaidAmount;
  }

  public BigDecimal getTotalOtherRemainningAmount() {
    return totalOtherRemainningAmount;
  }

  public BigDecimal getTotalPaidAmount() {
    return totalPaidAmount;
  }

  public void setTotalPaidAmount(BigDecimal totalPaidAmount) {
    this.totalPaidAmount = totalPaidAmount;
  }

  public BigDecimal getFinalTransportFee() {
    return finalTransportFee;
  }

  public void setFinalTransportFee(BigDecimal finalTransportFee) {
    this.finalTransportFee = finalTransportFee;
  }

  public BigDecimal getUniformPaidAmountTotal() {
    return uniformPaidAmountTotal;
  }

  public void setUniformPaidAmountTotal(BigDecimal uniformPaidAmountTotal) {
    this.uniformPaidAmountTotal = uniformPaidAmountTotal;
  }

  public BigDecimal getStationaryPaidAmountTotal() {
    return stationaryPaidAmountTotal;
  }

  public void setStationaryPaidAmountTotal(BigDecimal stationaryPaidAmountTotal) {
    this.stationaryPaidAmountTotal = stationaryPaidAmountTotal;
  }

  public BigDecimal getAnnualPaidAmountTotal() {
    return annualPaidAmountTotal;
  }

  public void setAnnualPaidAmountTotal(BigDecimal annualPaidAmountTotal) {
    this.annualPaidAmountTotal = annualPaidAmountTotal;
  }

  public BigDecimal getAddmissionPaidAmountTotal() {
    return addmissionPaidAmountTotal;
  }

  public void setAddmissionPaidAmountTotal(BigDecimal addmissionPaidAmountTotal) {
    this.addmissionPaidAmountTotal = addmissionPaidAmountTotal;
  }

  public BigDecimal getDepositPaidAmountTotal() {
    return depositPaidAmountTotal;
  }

  public void setDepositPaidAmountTotal(BigDecimal depositPaidAmountTotal) {
    this.depositPaidAmountTotal = depositPaidAmountTotal;
  }

  public BigDecimal getProgramPaidAmountTotal() {
    return programPaidAmountTotal;
  }

  public void setProgramPaidAmountTotal(BigDecimal programPaidAmountTotal) {
    this.programPaidAmountTotal = programPaidAmountTotal;
  }

  public BigDecimal getTransportPaidAmountTotal() {
    return transportPaidAmountTotal;
  }

  public void setTransportPaidAmountTotal(BigDecimal transportPaidAmountTotal) {
    this.transportPaidAmountTotal = transportPaidAmountTotal;
  }

  public BigDecimal getIgst()
  {
    return igst;
  }

  public StudentFeeSlipResponse(StudentFeePaymentRequest slip)
  {

    this.id = slip.getId();
    this.extraHours=slip.getExtraHours();
    this.fullName = slip.getStudent().getProfile().getFullName();
    this.group = slip.getStudent().getGroup().getName();
    this.program = slip.getStudent().getProgram().getName();
    this.month = slip.getMonth();
    this.quarter = slip.getQuarter();
    this.year = slip.getYear();
    this.extraCharge = slip.getExtraCharge();
    this.latePaymentCharge = slip.getLatePaymentCharge();
    this.totalFee = slip.getTotalFee();
    this.fee = slip.getBaseFee();
    this.status = slip.getPaymentStatus().name();
    this.feeDuration = slip.getFeeDuration().name();
    this.comments = slip.getComments();
    this.invoiceDate = slip.getInvoiceDate() == null ? null : slip.getInvoiceDate().toString();
    this.deposit = slip.getDeposit();
    this.annualFee = slip.getAnnualFee();
    this.payableAmount = this.totalFee;
    this.sgst = slip.getSgst();
    this.cgst = slip.getCgst();
    this.igst = slip.getIgst();
    this.balance = slip.getBalance();
    this.adjust = slip.getAdjust();
    this.generateActive = slip.isGenerateActive();
    this.uniformCharges=slip.getUniformCharges()==null?ZERO:slip.getUniformCharges();
    this.stationary=slip.getStationary()==null?ZERO:slip.getStationary();
    this.transportFee=slip.getTransportFee()==null?ZERO:slip.getTransportFee();
    this.finalAnnualCharges=slip.getFinalAnnualCharges()==null?ZERO:slip.getFinalAnnualCharges();
    this.admissionFee=slip.getAdmissionFee()==null?ZERO:slip.getAdmissionFee();
    this.finalAdmissionFee=slip.getFinalAdmissionFee()==null?ZERO:slip.getFinalAdmissionFee();
    this.finalBaseFee=slip.getFinalBaseFee()==null?ZERO:slip.getFinalBaseFee();
    this.baseFee=slip.getBaseFee()==null?ZERO:slip.getBaseFee();
    this.finalDepositFee=slip.getFinalDepositFee()==null?ZERO:slip.getFinalDepositFee();
    this.baseFeeDiscount=slip.getBaseFeeDiscount()==null?ZERO:slip.getBaseFeeDiscount();
    this.annualFeeDiscount=slip.getAnnualFeeDiscount()==null?ZERO:slip.getAnnualFeeDiscount();
    this.addmissionFeeDiscount=slip.getAddmissionFeeDiscount()==null?ZERO:slip.getAddmissionFeeDiscount();
    this.depositFeeDiscount=slip.getDepositFeeDiscount()==null?ZERO:slip.getDepositFeeDiscount();
    this.gstAmount=slip.getGstAmount()==null?ZERO:slip.getGstAmount();
    this.feeRatio=slip.getFeeRatio()==null?ZERO:slip.getFeeRatio();
    this.finalTransportFee=slip.getFinalTransportFee()==null?ZERO:slip.getFinalTransportFee();
    this.adjust = this.adjust == null ? BigDecimal.ZERO : this.adjust;
    this.balance = this.balance == null ? BigDecimal.ZERO : this.balance;
    this.autoComments = slip.getAutoComments();
    if (slip.getPayments() != null && !slip.getPayments().isEmpty())
    {
      payments = new ArrayList<>(slip.getPayments().size());
      slip.getPayments().forEach(payment -> {
        if(payment.getActive()){
          uniformPaidAmountTotal=  uniformPaidAmountTotal.add(payment.getUniformPaidAmount()==null?ZERO:payment.getUniformPaidAmount());
          stationaryPaidAmountTotal=stationaryPaidAmountTotal.add(payment.getStationaryPaidAmount()==null?ZERO:payment.getStationaryPaidAmount());
          annualPaidAmountTotal=annualPaidAmountTotal.add(payment.getAnnualPaidAmount()==null?ZERO:payment.getAnnualPaidAmount());
          addmissionPaidAmountTotal=addmissionPaidAmountTotal.add(payment.getAddmissionPaidAmount()==null?ZERO:payment.getAddmissionPaidAmount());
          depositPaidAmountTotal=depositPaidAmountTotal.add(payment.getDepositPaidAmount()==null?ZERO:payment.getDepositPaidAmount());
          programPaidAmountTotal=programPaidAmountTotal.add(payment.getProgramPaidAmount()==null?ZERO:payment.getProgramPaidAmount());
          transportPaidAmountTotal=transportPaidAmountTotal.add(payment.getTransportPaidAmount()==null?ZERO:payment.getTransportPaidAmount());
          this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
          this.totalPaidAmount=this.totalPaidAmount.add(payment.getPaidAmount());
        }
        payments.add(new StudentFeePaymentResponse(payment));
      });
    }
    BigDecimal totalPaid=uniformCharges
                    .add(stationary)
                    .add(finalAnnualCharges)
                    .add(finalAdmissionFee)
                    .add(finalDepositFee)
                    .add(finalBaseFee)
                    .add(finalTransportFee);

    if(totalPaidAmount.intValue()>totalPaid.intValue()){
      totalOtherPaidAmount=totalPaidAmount.subtract(totalPaid);
    }else{
      totalOtherPaidAmount=ZERO;
    }

  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setQuarter(int quarter) {
    this.quarter = quarter;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public void setExtraCharge(BigDecimal extraCharge) {
    this.extraCharge = extraCharge;
  }

  public void setLatePaymentCharge(BigDecimal latePaymentCharge) {
    this.latePaymentCharge = latePaymentCharge;
  }

  public void setTotalFee(BigDecimal totalFee) {
    this.totalFee = totalFee;
  }

  public void setInvoiceDate(String invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void setFeeDuration(String feeDuration) {
    this.feeDuration = feeDuration;
  }

  public void setDeposit(BigDecimal deposit) {
    this.deposit = deposit;
  }

  public void setAnnualFee(BigDecimal annualFee) {
    this.annualFee = annualFee;
  }

  public void setPayableAmount(BigDecimal payableAmount) {
    this.payableAmount = payableAmount;
  }

  public void setCgst(BigDecimal cgst) {
    this.cgst = cgst;
  }

  public void setSgst(BigDecimal sgst) {
    this.sgst = sgst;
  }

  public void setIgst(BigDecimal igst) {
    this.igst = igst;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public void setAdjust(BigDecimal adjust) {
    this.adjust = adjust;
  }

  public void setAutoComments(String autoComments) {
    this.autoComments = autoComments;
  }

  public void setGenerateActive(boolean generateActive) {
    this.generateActive = generateActive;
  }

  public BigDecimal getUniformCharges() {
    return uniformCharges;
  }

  public void setUniformCharges(BigDecimal uniformCharges) {
    this.uniformCharges = uniformCharges;
  }

  public BigDecimal getStationary() {
    return stationary;
  }

  public void setStationary(BigDecimal stationary) {
    this.stationary = stationary;
  }

  public BigDecimal getTransportFee() {
    return transportFee;
  }

  public void setTransportFee(BigDecimal transportFee) {
    this.transportFee = transportFee;
  }

  public BigDecimal getFinalAnnualCharges() {
    return finalAnnualCharges;
  }

  public void setFinalAnnualCharges(BigDecimal finalAnnualCharges) {
    this.finalAnnualCharges = finalAnnualCharges;
  }

  public BigDecimal getAdmissionFee() {
    return admissionFee;
  }

  public void setAdmissionFee(BigDecimal admissionFee) {
    this.admissionFee = admissionFee;
  }

  public BigDecimal getFinalAdmissionFee() {
    return finalAdmissionFee;
  }

  public void setFinalAdmissionFee(BigDecimal finalAdmissionFee) {
    this.finalAdmissionFee = finalAdmissionFee;
  }

  public BigDecimal getFinalBaseFee() {
    return finalBaseFee;
  }

  public void setFinalBaseFee(BigDecimal finalBaseFee) {
    this.finalBaseFee = finalBaseFee;
  }

  public BigDecimal getBaseFee() {
    return baseFee;
  }

  public void setBaseFee(BigDecimal baseFee) {
    this.baseFee = baseFee;
  }

  public BigDecimal getFinalDepositFee() {
    return finalDepositFee;
  }

  public void setFinalDepositFee(BigDecimal finalDepositFee) {
    this.finalDepositFee = finalDepositFee;
  }

  public BigDecimal getBaseFeeDiscount() {
    return baseFeeDiscount;
  }

  public void setBaseFeeDiscount(BigDecimal baseFeeDiscount) {
    this.baseFeeDiscount = baseFeeDiscount;
  }

  public BigDecimal getAnnualFeeDiscount() {
    return annualFeeDiscount;
  }

  public void setAnnualFeeDiscount(BigDecimal annualFeeDiscount) {
    this.annualFeeDiscount = annualFeeDiscount;
  }

  public BigDecimal getAddmissionFeeDiscount() {
    return addmissionFeeDiscount;
  }

  public void setAddmissionFeeDiscount(BigDecimal addmissionFeeDiscount) {
    this.addmissionFeeDiscount = addmissionFeeDiscount;
  }

  public BigDecimal getDepositFeeDiscount() {
    return depositFeeDiscount;
  }

  public void setDepositFeeDiscount(BigDecimal depositFeeDiscount) {
    this.depositFeeDiscount = depositFeeDiscount;
  }

  public BigDecimal getGstAmount() {
    return gstAmount;
  }

  public void setGstAmount(BigDecimal gstAmount) {
    this.gstAmount = gstAmount;
  }

  public BigDecimal getFeeRatio() {
    return feeRatio;
  }

  public void setFeeRatio(BigDecimal feeRatio) {
    this.feeRatio = feeRatio;
  }

  public void setPayments(List<StudentFeePaymentResponse> payments) {
    this.payments = payments;
  }

  public boolean isGenerateActive() {
	return generateActive;
  }

public String getAutoComments()
  {
    return autoComments;
  }

  public BigDecimal getAdjust()
  {
    return adjust;
  }

  public BigDecimal getBalance()
  {
    return balance;
  }

  public BigDecimal getCgst()
  {
    return cgst;
  }

  public BigDecimal getSgst()
  {
    return sgst;
  }

  public BigDecimal getPayableAmount()
  {
    return payableAmount;
  }

  public BigDecimal getDeposit()
  {
    return deposit;
  }

  public BigDecimal getAnnualFee()
  {
    return annualFee;
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

  public int getMonth()
  {
    return month;
  }

  public int getQuarter()
  {
    return quarter;
  }

  public int getYear()
  {
    return year;
  }

  public BigDecimal getFee()
  {
    return fee;
  }

  public BigDecimal getExtraCharge()
  {
    return extraCharge;
  }

  public BigDecimal getLatePaymentCharge()
  {
    return latePaymentCharge;
  }

  public BigDecimal getTotalFee()
  {
    return totalFee;
  }

  public String getInvoiceDate()
  {
    return invoiceDate;
  }

  public String getStatus()
  {
    return status;
  }

  public String getComments()
  {
    return comments;
  }

  public String getFeeDuration()
  {
    return feeDuration;
  }

  public List<StudentFeePaymentResponse> getPayments()
  {
    return payments;
  }

  @Override
  public String toString()
  {
    return "StudentFeeSlipResponse{" +
        "id=" + id +
        ", fullName='" + fullName + '\'' +
        ", group='" + group + '\'' +
        ", program='" + program + '\'' +
        ", month=" + month +
        ", quarter=" + quarter +
        ", year=" + year +
        ", fee=" + fee +
        ", extraCharge=" + extraCharge +
        ", latePaymentCharge=" + latePaymentCharge +
        ", totalFee=" + totalFee +
        ", invoiceDate='" + invoiceDate + '\'' +
        ", status='" + status + '\'' +
        ", comment='" + comments + '\'' +
        ", feeDuration='" + feeDuration + '\'' +
        '}';
  }
}
