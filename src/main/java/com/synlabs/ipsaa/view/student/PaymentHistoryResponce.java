package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.fee.StudentFeePaymentResponse;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentHistoryResponce implements Response {

    List<StudentFeeSlipResponse> payments;

    public PaymentHistoryResponce(List<StudentFeePaymentRequest> payments) {
        this.payments = payments.stream().map(StudentFeeSlipResponse::new).collect(Collectors.toList());
    }

    public List<StudentFeeSlipResponse> getPayments() {
        return payments;
    }

    public void setPayments(List<StudentFeeSlipResponse> payments) {
        this.payments = payments;
    }
}
