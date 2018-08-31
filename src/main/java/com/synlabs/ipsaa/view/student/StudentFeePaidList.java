package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;

import java.util.List;

public class StudentFeePaidList {
    List<StudentFeePaymentRequest> unpaidList;
    StudentFeePaymentRequest thisQuarter;

    public List<StudentFeePaymentRequest> getUnpaidList() {
        return unpaidList;
    }

    public void setUnpaidList(List<StudentFeePaymentRequest> unpaidList) {
        this.unpaidList = unpaidList;
    }

    public StudentFeePaymentRequest getThisQuarter() {
        return thisQuarter;
    }

    public void setThisQuarter(StudentFeePaymentRequest thisQuarter) {
        this.thisQuarter = thisQuarter;
    }
}
