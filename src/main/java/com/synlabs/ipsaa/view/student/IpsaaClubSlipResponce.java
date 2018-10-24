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

    private List<StudentFeePaymentRecordResponce> payments = new ArrayList<>();
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
        this.payments=studentFeePaymentRequestIpsaaClub.getPayments()
                .stream().map(StudentFeePaymentRecordResponce::new).collect(Collectors.toList());
    }
}
