package com.synlabs.ipsaa.view.report.excel;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.view.attendance.AttendanceReportRequest;

import java.math.BigDecimal;
import java.util.*;

public class StaffAttendanceReport {
    private Employee employee;
    private Map<Date,String> calendar= new LinkedHashMap<>();

    private BigDecimal presents;
    private BigDecimal absents;
    private BigDecimal leaves;
    private BigDecimal weekoff;
    private BigDecimal holidays;
    private BigDecimal paidDays;
    private BigDecimal halfdays;
    private BigDecimal overTime;

    public StaffAttendanceReport() {
    }

    public StaffAttendanceReport(Employee employee, Map<Date,String> dateStringMap){
        this.employee=employee;
        this.calendar=dateStringMap;
    }

    public Employee getEmployee() { return employee; }

    public Map<Date, String> getCalendar() { return calendar; }

    public BigDecimal getPresents() { return presents; }

    public BigDecimal getAbsents() { return absents; }

    public BigDecimal getLeaves() { return leaves; }

    public BigDecimal getWeekoff() { return weekoff; }

    public BigDecimal getHolidays() { return holidays; }

    public BigDecimal getPaidDays() { return paidDays; }

    public BigDecimal getOverTime() { return overTime; }

    public BigDecimal getHalfdays() { return halfdays; }

    public void calculate(){

       this.presents=BigDecimal.valueOf( calendar.entrySet().stream().filter(e->e.getValue().equals("P")).count())
                    .add(BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("HD")).count())
                            .divide(BigDecimal.valueOf(2)));

       this.absents= BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("A")).count());

       this.leaves= BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("L")).count())
                        .add(BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("HD")).count())
                                .divide(BigDecimal.valueOf(2)));

       this.weekoff= BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("W")).count());

       this.holidays= BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("H")).count());

       this.halfdays= BigDecimal.valueOf(calendar.entrySet().stream().filter(e->e.getValue().equals("HD")).count());

       this.paidDays= this.presents.add(this.leaves).add(weekoff).add(holidays);
    }

    @Override
    public String toString() {
        return "StaffAttendanceReport{" +
                "employee=" + employee +
                ", presents=" + presents +
                ", absents=" + absents +
                ", leaves=" + leaves +
                ", weekoff=" + weekoff +
                ", holidays=" + holidays +
                ", paidDays=" + paidDays +
                ", overTime=" + overTime +
                '}';
    }
}
