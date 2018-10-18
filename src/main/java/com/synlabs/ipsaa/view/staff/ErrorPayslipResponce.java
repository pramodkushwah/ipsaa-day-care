package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;

public class ErrorPayslipResponce {
    EmployeePaySlipResponse payslip;
    String error;

    public ErrorPayslipResponce(EmployeePaySlipResponse payslip, String error) {
        this.payslip = payslip;
        this.error = error;
    }

    public EmployeePaySlipResponse getPayslip() {
        return payslip;
    }

    public void setPayslip(EmployeePaySlipResponse payslip) {
        this.payslip = payslip;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
