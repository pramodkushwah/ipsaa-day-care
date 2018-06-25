package com.synlabs.ipsaa.view.common;

import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.staff.Employee;
import org.springframework.util.StringUtils;

public class UserSummaryResponse implements Response
{
  private String email;
  private String firstname;
  private String lastname;
  private String name;
  private String profileImage;
  private String profileImageData;

  public UserSummaryResponse(User user)
  {
    this.name = user.getName();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.email = user.getEmail();
    this.profileImage = user.getImagePath();
    if (StringUtils.isEmpty(this.profileImage))
    {
      Employee employee = user.getEmployee();
      if (employee != null)
      {
        this.profileImage = employee.getProfile().getImagePath();
      }
    }
  }

  public String getEmail()
  {
    return email;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public String getLastname()
  {
    return lastname;
  }

  public String getName()
  {
    return name;
  }

  public String getProfileImage()
  {
    return profileImage;
  }

  public void setProfileImageData(String profileImageData)
  {
    this.profileImageData = profileImageData;
  }

  public String getProfileImageData()
  {
    return profileImageData;
  }
}
