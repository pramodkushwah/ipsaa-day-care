package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.UserService;
import com.synlabs.ipsaa.view.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SELF_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SELF_WRITE;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.USER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.USER_WRITE;

@RestController
@RequestMapping("/api/user/")
public class UserController
{

  @Autowired
  private UserService userService;

  @GetMapping
  @Secured(USER_READ)
  public List<UserResponse> list()
  {
    return userService.list().stream().map(UserResponse::new).collect(Collectors.toList());
  }

  @GetMapping("menu")
  @Secured(SELF_READ)
  public Menu getMenu()
  {
    return userService.getCurrentUserMenu();
  }


  @DeleteMapping("{userId}")
  @Secured(USER_WRITE)
  public void deleteUser(@PathVariable Long userId)
  {
    userService.deleteUser(new UserRequest(userId));
  }

  @GetMapping("{userId}")
  @Secured(USER_READ)
  public UserResponse list(@PathVariable(name = "userId") Long userId)
  {
    return new UserResponse(userService.getUserDetail(new UserRequest(userId)));
  }

  @PostMapping
  @Secured(USER_WRITE)
  public UserResponse createUser(@RequestBody @Validated UserRequest request, BindingResult result)
  {

    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new UserResponse(userService.saveUser(request));
  }

  @PutMapping
  @Secured(USER_WRITE)
  public UserResponse updateUser(@RequestBody @Valid UserRequest request, BindingResult result)
  {
    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new UserResponse(userService.updateUser(request));
  }

  @GetMapping("roles")
  @Secured(USER_READ)
  public List<RoleResponse> roles()
  {
    return userService.getRoles().stream().map(RoleResponse::new).collect(Collectors.toList());
  }

  @PostMapping("role")
  @Secured(USER_WRITE)
  public RoleResponse createRole(@RequestBody @Validated RoleRequest request, BindingResult result)
  {

    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new RoleResponse(userService.saveRole(request));
  }

  @PutMapping("role")
  @Secured(USER_WRITE)
  public RoleResponse updateRole(@RequestBody @Valid RoleRequest request, BindingResult result)
  {
    if (result.hasErrors())
    {
      throw new ValidationException(result.toString());
    }
    return new RoleResponse(userService.updateRole(request));
  }

  @PostMapping("profile-pic")
  @Secured(SELF_WRITE)
  public void uploadProfilePic(@RequestParam("file") MultipartFile file)
  {
    userService.uploadProfilePic(file);
  }

  @DeleteMapping("role/{roleId}")
  @Secured(USER_WRITE)
  public void deleteRole(@PathVariable Long roleId)
  {
    userService.deleteRole(new RoleRequest(roleId));
  }

  @GetMapping("privileges")
  @Secured(USER_READ)
  public List<PrivilegeResponse> privileges()
  {
    return userService.getPrivileges().stream().map(PrivilegeResponse::new).collect(Collectors.toList());
  }

  @GetMapping("my_privileges")
  @Secured(SELF_READ)
  public Set<String> myPrivileges()
  {
    return userService.getMyPrivileges();
  }

  @PostMapping("resetpwd")
  @Secured(USER_WRITE)
  public void resetPassword(@RequestBody UserPasswordChangeRequest request)
  {
    userService.changePassword(request);
  }
}
