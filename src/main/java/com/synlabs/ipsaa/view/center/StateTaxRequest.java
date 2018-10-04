package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.view.common.Request;
import java.math.BigDecimal;


public class StateTaxRequest implements Request {

    //TODO put the following in Tax Request? Required
    private  Long centerId;
    private Gender gender;
    private BigDecimal grossSalary;
    private String pstate;

    private Long employeeId;
    private String eid;
    private BigDecimal min;
    private BigDecimal max;
    private String state;
    private BigDecimal tax;

    public BigDecimal getTax() { return tax;}

    public void setTax(BigDecimal tax) { this.tax = tax; }

    public String getPstate() { return pstate; }

    public void setPstate(String pstate) { this.pstate = pstate; }

    public Long getId() { return unmask(employeeId); }

    public void setId(Long id) { this.employeeId = id; }

    public BigDecimal getMin() { return min; }

    public void setMin(BigDecimal min) { this.min = min; }

    public BigDecimal getMax() { return max; }

    public void setMax(BigDecimal max) { this.max = max; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public Long getCenterId() { return unmask(centerId); }

    public void setCenterId(Long centerId) { this.centerId = centerId; }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) { this.gender = gender; }

    public BigDecimal getGrossSalary() { return grossSalary; }

    public void setGrossSalary(BigDecimal grossSalary) { this.grossSalary = grossSalary; }

    public String getEid() { return eid; }

    public void setEid(String eid) { this.eid = eid; }
}
