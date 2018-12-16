package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.view.common.PageRequest;

public class StudentFilterRequest extends PageRequest
{
  private String programCode;
  private String centerCode;
  private boolean active;

  public boolean getActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public void setCenterCode(String centerCode)
  {
    this.centerCode = centerCode;
  }

  public StudentFilterRequest(){}

  public StudentFilterRequest(String programCode)
  {
    this.programCode = programCode;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public void setProgramCode(String programCode)
  {
    this.programCode = programCode;
  }
}
