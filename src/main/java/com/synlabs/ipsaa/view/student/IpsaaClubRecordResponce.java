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


    private long studentId;
    private long id;
    private Date paymentDate;
    private PaymentStatus paymentStatus;
    private PaymentMode paymentMode;
    private String txnid;

    private BigDecimal paidAmount;

    private Boolean confirmed = false;

    private Boolean active = true;

    private String comment;


    public IpsaaClubRecordResponce(StudentFeePaymentRecordIpsaaClub record) {
        this.comment=record.getComment();
        this.setId(record.getId());
        this.setStudentId(record.getStudent().getId());
        paidAmount=record.getPaidAmount();
        paymentStatus=record.getPaymentStatus();
        paymentMode=record.getPaymentMode();
        txnid=record.getTxnid();
        confirmed=record.getConfirmed();
        active=record.getActive();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = mask(id);
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = mask(studentId);
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
