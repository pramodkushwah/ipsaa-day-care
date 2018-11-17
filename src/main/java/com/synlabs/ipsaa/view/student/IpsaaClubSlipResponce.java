package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.center.CenterResponseV2;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.fee.StudentFeePaymentResponse;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IpsaaClubSlipResponce implements Response {
    private long studentId;
    private long id;
    private CenterResponseV2 center;
    private BigDecimal annualFee;
    private BigDecimal annualFeeDiscount;
    private BigDecimal finalAnnualFee;
    private BigDecimal baseFeeDiscount;
    private BigDecimal finalBaseFee;
    private BigDecimal baseFee;
    private BigDecimal deposit;
    private BigDecimal finalDepositFee;
    private BigDecimal depositFeeDiscount;
    private BigDecimal extraCharge;
    private BigDecimal gstAmount;
    private int noOfFullDays;
    private int noOfHalfDays;
    private int totalNoOfDays;
    private int month;
    private int year;
    private BigDecimal balance;
    private BigDecimal totalFee;
    private BigDecimal finalFee;
    private String name;

    private List<IpsaaClubRecordResponce> payments = new ArrayList<>();
    private PaymentStatus paymentStatus;
    private String slipSerial;
    private String slipFileName;
    private String receiptSerial;
    private String receiptFileName;
    private Date invoiceDate;
    private Date expireDate;
    private String comments;
    private String autoComments;
    private String tnxid;
    private Date lastGenerationDate;

    private boolean reGenerateSlip = true;
    private boolean generateActive = false;
    private BigDecimal payableAmount;
    private BigDecimal totalDaysFee;

    public IpsaaClubSlipResponce(StudentFeePaymentRequestIpsaaClub slip) {
        center=new CenterResponseV2(slip.getStudent().getCenter());
        this.setStudentId(slip.getStudent().getId());
        this.name=slip.getStudent().getName();
        this.annualFee=slip.getAnnualFee();
        this.annualFeeDiscount=slip.getAnnualFeeDiscount();
        this.autoComments=slip.getAutoComments();
        this.balance=slip.getBalance();
        this.totalNoOfDays=slip.getTotalNoOfDays();
        this.noOfFullDays=slip.getNoOfFullDays();
        this.noOfHalfDays=slip.getNoOfHalfDays();
        this.baseFee=slip.getBaseFee();
        this.baseFeeDiscount=slip.getBaseFeeDiscount();
        this.comments=slip.getComments();
        this.deposit=slip.getDeposit();
        this.depositFeeDiscount=slip.getDepositFeeDiscount();
        this.expireDate=slip.getExpireDate();
        this.finalAnnualFee=slip.getFinalAnnualFee();
        this.finalBaseFee=slip.getFinalBaseFee();
        this.finalDepositFee=slip.getFinalDepositFee();
        this.finalFee=slip.getFinalFee();
        this.generateActive=slip.isGenerateActive();
        this.gstAmount=slip.getGstAmount();
        this.invoiceDate=slip.getInvoiceDate();
        this.lastGenerationDate=slip.getLastGenerationDate();
        this.month=slip.getMonth();
        this.id=mask(slip.getId());
        this.totalFee=slip.getTotalFee();
        this.payableAmount=slip.getTotalFee();
        this.paymentStatus=slip.getPaymentStatus();
        this.totalDaysFee=slip.getTotalDaysFee();
        this.extraCharge=slip.getExtraCharge();
        if (slip.getPayments() != null && !slip.getPayments().isEmpty())
        {
            payments = new ArrayList<>(slip.getPayments().size());
            slip.getPayments().forEach(payment -> {
                payments.add(new IpsaaClubRecordResponce(payment));
                this.payableAmount = this.payableAmount.subtract(payment.getPaidAmount());
                //   System.out.println("break");
            });
        }
    }

    public BigDecimal getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(BigDecimal extraCharge) {
        this.extraCharge = extraCharge;
    }

    public BigDecimal getTotalDaysFee() {
        return totalDaysFee;
    }

    public void setTotalDaysFee(BigDecimal totalDaysFee) {
        this.totalDaysFee = totalDaysFee;
    }

    public CenterResponseV2 getCenter() {
        return center;
    }

    public void setCenter(CenterResponseV2 center) {
        this.center = center;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = mask(studentId);
    }

    public BigDecimal getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(BigDecimal annualFee) {
        this.annualFee = annualFee;
    }

    public BigDecimal getAnnualFeeDiscount() {
        return annualFeeDiscount;
    }

    public void setAnnualFeeDiscount(BigDecimal annualFeeDiscount) {
        this.annualFeeDiscount = annualFeeDiscount;
    }

    public BigDecimal getFinalAnnualFee() {
        return finalAnnualFee;
    }

    public void setFinalAnnualFee(BigDecimal finalAnnualFee) {
        this.finalAnnualFee = finalAnnualFee;
    }

    public BigDecimal getBaseFeeDiscount() {
        return baseFeeDiscount;
    }

    public void setBaseFeeDiscount(BigDecimal baseFeeDiscount) {
        this.baseFeeDiscount = baseFeeDiscount;
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

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getFinalDepositFee() {
        return finalDepositFee;
    }

    public void setFinalDepositFee(BigDecimal finalDepositFee) {
        this.finalDepositFee = finalDepositFee;
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

    public int getNoOfFullDays() {
        return noOfFullDays;
    }

    public void setNoOfFullDays(int noOfFullDays) {
        this.noOfFullDays = noOfFullDays;
    }

    public int getNoOfHalfDays() {
        return noOfHalfDays;
    }

    public void setNoOfHalfDays(int noOfHalfDays) {
        this.noOfHalfDays = noOfHalfDays;
    }

    public int getTotalNoOfDays() {
        return totalNoOfDays;
    }

    public void setTotalNoOfDays(int totalNoOfDays) {
        this.totalNoOfDays = totalNoOfDays;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getFinalFee() {
        return finalFee;
    }

    public void setFinalFee(BigDecimal finalFee) {
        this.finalFee = finalFee;
    }

    public List<IpsaaClubRecordResponce> getPayments() {
        return payments;
    }

    public void setPayments(List<IpsaaClubRecordResponce> payments) {
        this.payments = payments;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSlipSerial() {
        return slipSerial;
    }

    public void setSlipSerial(String slipSerial) {
        this.slipSerial = slipSerial;
    }

    public String getSlipFileName() {
        return slipFileName;
    }

    public void setSlipFileName(String slipFileName) {
        this.slipFileName = slipFileName;
    }

    public String getReceiptSerial() {
        return receiptSerial;
    }

    public void setReceiptSerial(String receiptSerial) {
        this.receiptSerial = receiptSerial;
    }

    public String getReceiptFileName() {
        return receiptFileName;
    }

    public void setReceiptFileName(String receiptFileName) {
        this.receiptFileName = receiptFileName;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAutoComments() {
        return autoComments;
    }

    public void setAutoComments(String autoComments) {
        this.autoComments = autoComments;
    }

    public String getTnxid() {
        return tnxid;
    }

    public void setTnxid(String tnxid) {
        this.tnxid = tnxid;
    }

    public Date getLastGenerationDate() {
        return lastGenerationDate;
    }

    public void setLastGenerationDate(Date lastGenerationDate) {
        this.lastGenerationDate = lastGenerationDate;
    }

    public boolean isReGenerateSlip() {
        return reGenerateSlip;
    }

    public void setReGenerateSlip(boolean reGenerateSlip) {
        this.reGenerateSlip = reGenerateSlip;
    }

    public boolean isGenerateActive() {
        return generateActive;
    }

    public void setGenerateActive(boolean generateActive) {
        this.generateActive = generateActive;
    }
}
