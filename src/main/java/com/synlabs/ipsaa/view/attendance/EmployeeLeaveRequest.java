package com.synlabs.ipsaa.view.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.view.common.Request;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by ttn on 26/6/17.
 */
public class EmployeeLeaveRequest implements Request
{
  private String eid;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date fromDate;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST")
  private Date toDate;

  private String reason;

  private String leaveStatus;

  private String leaveType;

  private Boolean halfLeave;

  public Boolean getHalfLeave()
  {
    return halfLeave;
  }

  public void setHalfLeave(Boolean halfLeave)
  {
    this.halfLeave = halfLeave;
  }

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
  }

  public Date getFromDate()
  {
    return fromDate;
  }

  public void setFromDate(Date fromDate)
  {
    this.fromDate = fromDate;
  }

  public Date getToDate()
  {
    return toDate;
  }

  public void setToDate(Date toDate)
  {
    this.toDate = toDate;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason(String reason)
  {
    this.reason = reason;
  }

  public String getLeaveStatus()
  {
    return leaveStatus;
  }

  public void setLeaveStatus(String leaveStatus)
  {
    this.leaveStatus = leaveStatus;
  }

  public String getLeaveType()
  {
    return leaveType;
  }

  public void setLeaveType(String leaveType)
  {
    this.leaveType = leaveType;
  }

  public boolean validateRequest()
  {
    if (getEid() == null)
    {
      throw new ValidationException("EId is required");
    }
    if (getFromDate().after(getToDate()))
    {
      throw new ValidationException("Date range is not valid");
    }
    return true;
  }
}
