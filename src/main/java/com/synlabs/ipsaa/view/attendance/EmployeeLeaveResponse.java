package com.synlabs.ipsaa.view.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.staff.EmployeeLeave;

import java.util.Date;

public class EmployeeLeaveResponse {

    private String eid;

    private Date date;

    private String reason;

    private String leaveStatus;

    private String leaveType;

    private String halfLeave;

    private String center;

    public EmployeeLeaveResponse(EmployeeLeave employeeLeave){

        this.eid = employeeLeave.getEmployee().getEid();
        this.center = employeeLeave.getEmployee().getCostCenter().getName();
        this.date = employeeLeave.getDate();
        this.leaveStatus = employeeLeave.getLeaveStatus().name();
        this.leaveType = employeeLeave.getLeaveType().name();
        this.reason = employeeLeave.getReason();
        this.halfLeave = employeeLeave.getHalfLeave() == true? "YES":"NO";

    }

    public String getEid() { return eid; }

    public void setEid(String eid) { this.eid = eid; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public String getLeaveStatus() { return leaveStatus; }

    public void setLeaveStatus(String leaveStatus) { this.leaveStatus = leaveStatus; }

    public String getLeaveType() { return leaveType; }

    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getHalfLeave() { return halfLeave; }

    public void setHalfLeave(String halfLeave) { this.halfLeave = halfLeave; }

    public void setCenter(String center) { this.center = center; }
}
