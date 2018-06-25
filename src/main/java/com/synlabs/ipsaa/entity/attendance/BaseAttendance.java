package com.synlabs.ipsaa.entity.attendance;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.AttendanceStatus;
import com.synlabs.ipsaa.enums.LeaveStatus;
import com.synlabs.ipsaa.enums.LeaveType;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseAttendance extends BaseEntity
{
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Center center;

  @Temporal(TemporalType.DATE)
  private Date attendanceDate;

  @Temporal(TemporalType.TIME)
  @Column(nullable = false)
  private Date checkin;

  @Temporal(TemporalType.TIME)
  private Date checkout;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AttendanceStatus status;

  @Transient
  private String holidayName;

  @Transient
  private LeaveType leaveType;

  @Transient
  private Boolean halfLeave;

  @Transient
  private LeaveStatus leaveStatus;

  public LeaveStatus getLeaveStatus()
  {
    return leaveStatus;
  }

  public void setLeaveStatus(LeaveStatus leaveStatus)
  {
    this.leaveStatus = leaveStatus;
  }

  public Boolean getHalfLeave()
  {
    return halfLeave;
  }

  public void setHalfLeave(Boolean halfLeave)
  {
    this.halfLeave = halfLeave;
  }

  public String getHolidayName()
  {
    return holidayName;
  }

  public void setHolidayName(String holidayName)
  {
    this.holidayName = holidayName;
  }

  public LeaveType getLeaveType()
  {
    return leaveType;
  }

  public void setLeaveType(LeaveType leaveType)
  {
    this.leaveType = leaveType;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public Date getAttendanceDate()
  {
    return attendanceDate;
  }

  public void setAttendanceDate(Date attendanceDate)
  {
    this.attendanceDate = attendanceDate;
  }

  public Date getCheckin()
  {
    return checkin;
  }

  public void setCheckin(Date checkin)
  {
    this.checkin = checkin;
  }

  public Date getCheckout()
  {
    return checkout;
  }

  public void setCheckout(Date checkout)
  {
    this.checkout = checkout;
  }

  public AttendanceStatus getStatus()
  {
    return status;
  }

  public void setStatus(AttendanceStatus status)
  {
    this.status = status;
  }
}
