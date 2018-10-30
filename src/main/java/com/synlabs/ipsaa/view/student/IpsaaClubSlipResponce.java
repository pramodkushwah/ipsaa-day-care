package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.view.common.Response;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IpsaaClubSlipResponce implements Response {
    private long studentId;
    private long id;
    private BigDecimal annualFee;
    private BigDecimal annualFeeDiscount;
    private BigDecimal finalAnnualFee;
    private BigDecimal baseFeeDiscount;
    private BigDecimal finalBaseFee;
    private BigDecimal baseFee;
    private BigDecimal deposit;
    private BigDecimal finalDepositFee;
    private BigDecimal depositFeeDiscount;
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

    public IpsaaClubSlipResponce(StudentFeePaymentRequestIpsaaClub studentFeePaymentRequestIpsaaClub) {
        this.setStudentId(studentFeePaymentRequestIpsaaClub.getStudent().getId());
        this.name=studentFeePaymentRequestIpsaaClub.getStudent().getName();
        this.annualFee=studentFeePaymentRequestIpsaaClub.getAnnualFee();
        this.annualFeeDiscount=studentFeePaymentRequestIpsaaClub.getAnnualFeeDiscount();
        this.autoComments=studentFeePaymentRequestIpsaaClub.getAutoComments();
        this.balance=studentFeePaymentRequestIpsaaClub.getBalance();
        this.baseFee=studentFeePaymentRequestIpsaaClub.getBaseFee();
        this.baseFeeDiscount=studentFeePaymentRequestIpsaaClub.getBaseFeeDiscount();
        this.comments=studentFeePaymentRequestIpsaaClub.getComments();
        this.deposit=studentFeePaymentRequestIpsaaClub.getDeposit();
        this.depositFeeDiscount=studentFeePaymentRequestIpsaaClub.getDepositFeeDiscount();
        this.expireDate=studentFeePaymentRequestIpsaaClub.getExpireDate();
        this.finalAnnualFee=studentFeePaymentRequestIpsaaClub.getFinalAnnualFee();
        this.finalBaseFee=studentFeePaymentRequestIpsaaClub.getFinalBaseFee();
        this.finalDepositFee=studentFeePaymentRequestIpsaaClub.getFinalDepositFee();
        this.finalFee=studentFeePaymentRequestIpsaaClub.getFinalFee();
        this.generateActive=studentFeePaymentRequestIpsaaClub.isGenerateActive();
        this.gstAmount=studentFeePaymentRequestIpsaaClub.getGstAmount();
        this.invoiceDate=studentFeePaymentRequestIpsaaClub.getInvoiceDate();
        this.lastGenerationDate=studentFeePaymentRequestIpsaaClub.getLastGenerationDate();
        this.month=studentFeePaymentRequestIpsaaClub.getMonth();
        this.id=mask(studentFeePaymentRequestIpsaaClub.getId());
        this.totalFee=studentFeePaymentRequestIpsaaClub.getTotalFee();
        this.payments=studentFeePaymentRequestIpsaaClub.getPayments()
                .stream().map(IpsaaClubRecordResponce::new).collect(Collectors.toList());
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
