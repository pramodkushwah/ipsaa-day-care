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
    @Column(length = 200)
    private String comment;

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


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
