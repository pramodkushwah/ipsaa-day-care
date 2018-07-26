package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class StaffNewLeavings  implements Response {
    private String name;
    private String designation;
    private Date dol;
    private String mobile;
    private String center;

    public StaffNewLeavings(Employee employee) {
       center= employee.getCostCenter().getName();
       name=employee.getName();
       designation=employee.getDesignation();
       dol=employee.getProfile().getDol();
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

    public Date getDol() {
        return dol;
    }
    public void setDol(Date dol) {
        this.dol = dol;
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
