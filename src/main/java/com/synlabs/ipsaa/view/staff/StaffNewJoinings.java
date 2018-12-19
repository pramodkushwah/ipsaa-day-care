package com.synlabs.ipsaa.view.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
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
    private long id;

    @JsonFormat(pattern = "HH:mm", timezone = "IST")
    private Date checkIn;

    @JsonFormat(pattern = "HH:mm", timezone = "IST")
    private Date checkOut;

    private String employer;
    public StaffNewJoinings(Employee employee) {
        id=mask(employee.getId());
       center= employee.getCostCenter().getName();
       name=employee.getName();
       designation=employee.getDesignation();
       doj=employee.getProfile().getDoj();
       mobile=employee.getMobile();
       this.isActive=employee.isActive();
       this.employer=employee.getEmployer().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    ///For Attendance----- Avneet
    public StaffNewJoinings(EmployeeAttendance attendance){
        id=mask(attendance.getEmployee().getId());
        center= attendance.getEmployee().getCostCenter().getName();
        name=attendance.getEmployee().getName();
        designation=attendance.getEmployee().getDesignation();
        doj=attendance.getEmployee().getProfile().getDoj();
        mobile=attendance.getEmployee().getMobile();
        this.isActive=attendance.getEmployee().isActive();
        this.employer=attendance.getEmployee().getEmployer().getName();
        checkIn=attendance.getCheckin();
        checkOut=attendance.getCheckout();
    }
    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
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

    public Date getCheckIn() { return checkIn; }

    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut;}

    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }
}
