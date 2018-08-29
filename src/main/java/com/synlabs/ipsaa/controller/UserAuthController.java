package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.common.Privilege;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.enums.UserType;
import com.synlabs.ipsaa.ex.AuthException;
import com.synlabs.ipsaa.service.UserService;
import com.synlabs.ipsaa.view.common.LoginRequest;
import com.synlabs.ipsaa.view.common.LoginResponse;
import com.synlabs.ipsaa.view.common.UserSummaryResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SELF_READ;

@RestController
@RequestMapping("/")
public class UserAuthController
{

  @Value("${ipsaa.auth.secretkey}")
  private String secretkey;

  @Value("${ipsaa.auth.ttl_hours}")
  private int ttlhours;

  @Autowired
  private UserService userService;

  @GetMapping("/api/user/me")
  @Secured(SELF_READ)
  public UserSummaryResponse getLoggedInUser()
  {

    return new UserSummaryResponse(userService.getUser());
  }

  @PostMapping("login")
  public LoginResponse login(@RequestBody LoginRequest login)
  {

    User user = userService.validate(login.getEmail(), login.getPassword());

    if (user == null)
    {
      throw new AuthException("Invalid login by user " + login.getEmail());
    }

    String authToken = Jwts.builder()
                           .setSubject(login.getEmail())
                           .setIssuedAt(new Date())
                           .setExpiration(getAuthExpiration())
                           .claim("domain", user.getUserType() == UserType.Parent ? "/pp/" : "/mis/")
                           .signWith(SignatureAlgorithm.HS512, secretkey).compact();
    return new LoginResponse(authToken,userService.getUserPrivileges(user));
  }

  private Date getAuthExpiration()
  {
    return new DateTime().plusHours(ttlhours).toDate();
  }

}
