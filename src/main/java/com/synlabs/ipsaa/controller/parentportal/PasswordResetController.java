package com.synlabs.ipsaa.controller.parentportal;

import com.synlabs.ipsaa.service.PasswordResetService;
import com.synlabs.ipsaa.view.common.UserRequest;
import com.synlabs.ipsaa.view.pass.PasswordResetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by itrs on 5/2/2017.
 */
@RestController
@RequestMapping({"/pp/resetpassword/", "/mis/resetpassword/"})
public class PasswordResetController
{
  @Autowired
  private PasswordResetService passwordResetService;

  @PostMapping("email")
  public boolean sendEmail(@RequestBody UserRequest request)
  {
    passwordResetService.generateToken(request);
    return true;
  }

  @PostMapping
  public void resetPassword(@RequestBody PasswordResetRequest request)
  {
    passwordResetService.resetPassword(request);
  }

}
