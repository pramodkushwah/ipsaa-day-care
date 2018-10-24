package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.enums.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class StudentFeePaymentRecordIpsaaClub extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private StudentFeePaymentRequestIpsaaClub request;
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMode paymentMode;
    @Column(length = 200)
    private String txnid;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal paidAmount;
    private Boolean confirmed = false;
    @Column(columnDefinition = "bit(1) default 1")
    private Boolean active = true;

    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal annualFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal depositFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal baseFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal extraCharges;

    private int noOfFullDays;
    private int noOfHalfDays;
    private int totalNoOfDays;

    private int month;
    private int year;

    @Column(precision = 16, scale = 2)
    private BigDecimal balance;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal gstAmount;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal totalFee;

    @Column(precision = 16, scale = 2)
    private BigDecimal finalFee;

    @Column(unique = true, length = 20)
    private String slipSerial;
    @Column(unique = true, length = 200)
    private String slipFileName;
    @Column(unique = true, length = 20)
    private String receiptSerial;
    @Column(unique = true, length = 200)
    private String receiptFileName;



    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(length = 200)
    private String comments;

    @Column(length = 200)
    private String autoComments;

    @Column(length = 200)
    private String comment;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean isExpire = false;

    @Override
    public String toString()
    {
        return "{" +
                "admission_number" + (student == null ? null : student.getAdmissionNumber()) +
                "paymentDate=" + paymentDate +
                ", paymentStatus=" + paymentStatus +
                ", paymentMode=" + paymentMode +
                ", txnid='" + txnid + '\'' +
                ", paidAmount=" + paidAmount +
                ", confirmed=" + confirmed +
                '}';
    }


    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public BigDecimal getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(BigDecimal extraCharges) {
        this.extraCharges = extraCharges;
    }
}
