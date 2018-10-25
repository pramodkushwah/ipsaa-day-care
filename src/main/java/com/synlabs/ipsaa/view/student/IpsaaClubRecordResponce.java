package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecordIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.view.common.Response;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;

public class IpsaaClubRecordResponce implements Response{


    private long student;
    private long recordId;
    private StudentFeePaymentRequestIpsaaClub request;
    private Date paymentDate;
    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private String txnid;
    private BigDecimal paidAmount;
    private Boolean confirmed = false;
    private Boolean active = true;

    private BigDecimal annualFee;
    private BigDecimal depositFee;
    private BigDecimal baseFee;
    private BigDecimal extraCharges;

    private int noOfFullDays;
    private int noOfHalfDays;
    private int totalNoOfDays;

    private int month;
    private int year;

    private BigDecimal balance;

    private BigDecimal gstAmount;

    private BigDecimal totalFee;

    private BigDecimal finalFee;

    private String slipSerial;
    private String slipFileName;
    private String receiptSerial;
    private String receiptFileName;

    private Date startDate;
    private Date endDate;

    private String comments;

    private String autoComments;

    private String comment;

    private boolean isExpire = false;


    public IpsaaClubRecordResponce(StudentFeePaymentRecordIpsaaClub record) {
        this.autoComments=record.getAutoComments();
        this.balance=record.getBalance();
        this.baseFee=record.getBaseFee();
        this.comments=record.getComments();
        this.totalFee=record.getTotalFee();
        this.annualFee=record.getAnnualFee();
        //this.deposit=record.getDeposit();
        this.finalFee=record.getFinalFee();
        this.gstAmount=record.getGstAmount();
        this.month=record.getMonth();
        this.setRecordId(record.getId());
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = mask(recordId);
    }

    public long getStudent() {
        return student;
    }

    public void setStudent(long student) {
        this.student = mask(student);
    }

    public StudentFeePaymentRequestIpsaaClub getRequest() {
        return request;
    }

    public void setRequest(StudentFeePaymentRequestIpsaaClub request) {
        this.request = request;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(BigDecimal annualFee) {
        this.annualFee = annualFee;
    }

    public BigDecimal getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(BigDecimal depositFee) {
        this.depositFee = depositFee;
    }

    public BigDecimal getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(BigDecimal baseFee) {
        this.baseFee = baseFee;
    }

    public BigDecimal getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(BigDecimal extraCharges) {
        this.extraCharges = extraCharges;
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

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }
}
