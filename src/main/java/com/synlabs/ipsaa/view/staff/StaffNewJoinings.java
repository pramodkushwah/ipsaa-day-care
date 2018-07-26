package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;

import java.util.Date;
import java.util.List;

public class StaffNewJoinings {
    private String name;
    private String designation;
    private Date doj;
    private String mobile;
    private Center center;

    public StaffNewJoinings(Employee employee) {
       center= employee.getCostCenter();
       name=employee.getName();
       designation=employee.getDesignation();
       doj=employee.getProfile().getDoj();
       mobile=employee.getMobile();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }
}
