package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;
import java.util.List;

public class StaffNewJoinings  implements Response {
    private String name;
    private String designation;
    private Date doj;
    private String mobile;
    private String center;
    private boolean isActive;

    public StaffNewJoinings(Employee employee) {
       center= employee.getCostCenter().getName();
       name=employee.getName();
       designation=employee.getDesignation();
       doj=employee.getProfile().getDoj();
       mobile=employee.getMobile();
       this.isActive=employee.isActive();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }
}
