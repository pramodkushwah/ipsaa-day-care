package com.synlabs.ipsaa.view.batchimport;

import java.math.BigDecimal;

public class ImportMonthlySalary {
    String eid;
    BigDecimal presentDay;
    BigDecimal otherAllowance;
    BigDecimal otherDeduction;
    BigDecimal tds;
    String comments;

    public BigDecimal getPresentDay() {
        return presentDay;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setPresentDay(BigDecimal presentDay) {
        this.presentDay = presentDay;
    }
    public BigDecimal getOtherAllowance() {
        return otherAllowance;
    }

    public void setOtherAllowance(BigDecimal otherAllowance) {
        this.otherAllowance = otherAllowance;
    }

    public BigDecimal getOtherDeduction() {
        return otherDeduction;
    }

    public void setOtherDeduction(BigDecimal otherDeduction) {
        this.otherDeduction = otherDeduction;
    }

    public BigDecimal getTds() {
        return tds;
    }

    public void setTds(BigDecimal tds) {
        this.tds = tds;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
