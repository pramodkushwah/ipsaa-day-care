package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.entity.token.PasswordResetToken;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.PasswordResetTokenRepository;
import com.synlabs.ipsaa.jpa.StudentParentRepository;
import com.synlabs.ipsaa.jpa.UserRepository;
import com.synlabs.ipsaa.view.common.UserRequest;
import com.synlabs.ipsaa.view.pass.PasswordResetRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by itrs on 5/2/2017.
 */
@Service
public class PasswordResetService extends BaseService
{

  private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Value("${ipsaa.email.token.validTill}")
  private int validTill;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommunicationService communicationService;

  public void generateToken(UserRequest request)
  {
    if (StringUtils.isEmpty(request.getEmail()))
    {
      throw new ValidationException("Invalid Email " + request.getEmail());
    }
    User user = userRepository.findByEmailAndActiveTrue(request.getEmail());
    if (user == null)
    {
      throw new ValidationException(String.format("User [%s] not found in portal.", request.getEmail()));
    }


    String phone;
    String email;


    StudentParent parent = user.getParent();
    if (parent != null)
    {
      phone = parent.getMobile();
      email = parent.getEmail();
    }
    else {
      phone = user.getPhone();
      email = user.getEmail();
    }

//  generating token string
    String tokenString = RandomStringUtils.randomNumeric(6);

//    Create Valid till date
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR_OF_DAY, validTill);
    Date validTill = cal.getTime();

//    saving token to database
    PasswordResetToken token = new PasswordResetToken();
    token.setExpired(false);
    token.setToken(tokenString);
    token.setValidTill(validTill);
    token.setEmail(request.getEmail());
    passwordResetTokenRepository.saveAndFlush(token);

//    Sending Email and sms
    communicationService.sendResetPasswordSmsAndEmail(tokenString, phone, email);
  }

  public void resetPassword(PasswordResetRequest request)
  {
    String token = request.getToken();
    String password = request.getPassword();
    if (StringUtils.isEmpty(token))
    {
      throw new ValidationException(String.format("Empty token"));
    }
    if (StringUtils.isEmpty(password))
    {
      throw new ValidationException(String.format("Empty password"));
    }
    PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(token);
    if (passwordToken == null)
    {
      throw new ValidationException("Invalid Token");
    }
    if (passwordToken.isExpired() || passwordToken.getValidTill().getTime() < (new Date().getTime()))
    {
      throw new ValidationException("Token expired");
    }
    String email = passwordToken.getEmail();
    User user = userRepository.findByEmailAndActiveTrue(email);
    user.setPasswordHash(passwordEncoder.encode(password));
    userRepository.saveAndFlush(user);
    passwordResetTokenRepository.delete(passwordToken.getId());
  }
}
