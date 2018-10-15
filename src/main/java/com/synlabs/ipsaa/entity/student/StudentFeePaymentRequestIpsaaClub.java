package com.synlabs.ipsaa.entity.student;

import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentFeePaymentRequestIpsaaClub {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Student student;

    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal annualFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal annualFeeDiscount;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal finalAnnualFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")

    private BigDecimal baseFeeDiscount;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal finalBaseFee;
    @Column(precision = 16, scale = 2, nullable = false,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal baseFee;

    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal deposit;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal finalDepositFee;
    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal depositFeeDiscount;


    @Column(precision = 16, scale = 2,columnDefinition ="Decimal(10,2) default '0.00'")
    private BigDecimal gstAmount;

    private int noOfFullDays;
    private int noOfHalfDays;
    private int totalNoOfDays;

    private int month;
    private int year;

    @Column(precision = 16, scale = 2)
    private BigDecimal balance;
    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal totalFee;
    @Column(precision = 16, scale = 2)
    private BigDecimal finalFee;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request", cascade = CascadeType.ALL)
    @OrderBy("paymentDate DESC")
    private List<StudentFeePaymentRecord> payments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;


    @Column(unique = true, length = 20)
    private String slipSerial;
    @Column(unique = true, length = 200)
    private String slipFileName;
    @Column(unique = true, length = 20)
    private String receiptSerial;
    @Column(unique = true, length = 200)
    private String receiptFileName;



    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @Temporal(TemporalType.DATE)
    private Date expireDate;

    @Column(length = 200)
    private String comments;

    @Column(length = 200)
    private String autoComments;

    @Column(unique = true)
    private String tnxid;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean isExpire = false;

    @Temporal(TemporalType.DATE)
    private Date lastGenerationDate;

    private boolean reGenerateSlip = true;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean generateActive = false;


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

    public Date getLastGenerationDate() {
        return lastGenerationDate;
    }

    public void setLastGenerationDate(Date lastGenerationDate) {
        this.lastGenerationDate = lastGenerationDate;
    }

    public BigDecimal getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(BigDecimal annualFee) {
        this.annualFee = annualFee;
    }

    public BigDecimal getFinalAnnualFee() {
        return finalAnnualFee;
    }

    public void setFinalAnnualFee(BigDecimal finalAnnualFee) {
        this.finalAnnualFee = finalAnnualFee;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public List<StudentFeePaymentRecord> getPayments() {
        return payments;
    }

    public void setPayments(List<StudentFeePaymentRecord> payments) {
        this.payments = payments;
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

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTnxid() {
        return tnxid;
    }

    public void setTnxid(String tnxid) {
        this.tnxid = tnxid;
    }

    public boolean isExpire() {
        return isExpire;
    }

    public void setExpire(boolean expire) {
        isExpire = expire;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFinalFee() {
        return finalFee;
    }

    public void setFinalFee(BigDecimal finalFee) {
        this.finalFee = finalFee;
    }

    @Transient
    public BigDecimal getPaidAmount()
    {
        BigDecimal paidAmount = BigDecimal.ZERO;
        for (StudentFeePaymentRecord payment : payments)
        {
            if(payment.getActive())
                if (payment.getPaymentStatus() == PaymentStatus.Paid
                        || payment.getPaymentStatus() == PaymentStatus.PartiallyPaid)
                {
                    paidAmount = paidAmount.add(payment.getPaidAmount());
                }
        }
        return paidAmount;
    }
}
