package com.synlabs.ipsaa.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.*;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.enums.UserType;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.UploadException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.view.common.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.synlabs.ipsaa.util.StringUtil.in;

@Service
public class UserService extends BaseService implements UserDetailsService
{
  private static Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  private UserMenuBuilder menuBuilder;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private EmployeeRepository employeeRepository;

  private Cache<String, Menu> menuCache = CacheBuilder.newBuilder().build();

  private PasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
  {
    User user = userRepository.findByEmailAndActiveTrue(email);
    if (user == null)
    {
      logger.warn("user not found {}", email);
      throw new UsernameNotFoundException(String.format("User %s was not found", email));
    }
    user.setLastLogin(DateTime.now().toDate());
    userRepository.saveAndFlush(user);
    return new CurrentUser(user, new String[] { "TODO_ROLES" });
  }

  public User validate(String email, String password)
  {
    User user = userRepository.findByEmailAndActiveTrue(email);

    if (user != null && encoder.matches(password, user.getPasswordHash()))
    {
      user.setLastLogin(DateTime.now().toDate());
      userRepository.saveAndFlush(user);
      return user;
    }

    return null;
  }

  public List<User> list()
  {
    List<Center> usercenters = getUserCenters();

    JPAQuery<User> query = new JPAQuery<>(entityManager);
    QUser user = QUser.user;

    query.select(user).distinct().from(user)
         .where(user.active.isTrue())
         .where(user.userType.ne(UserType.Parent))
         .where(user.centers.isEmpty());

    List<User> nocenterusers = query.fetch();

    List<User> allusers = userRepository.findDistinctByCentersInAndActiveTrue(usercenters);
    allusers.addAll(nocenterusers);
    return allusers;
  }

  public long countUsers(List<Center> centers)
  {
    JPAQuery<User> query = new JPAQuery<>(entityManager);
    QUser user = QUser.user;

    query.select(user).distinct().from(user)
         .where(user.active.isTrue())
         .where(user.userType.ne(UserType.Parent))
         .where(user.centers.isEmpty());

    long noCenterUsersCount = query.fetchCount();
    long allUsersCount = userRepository.countDistinctByActiveTrueAndCentersIn(centers);
    return noCenterUsersCount + allUsersCount;
  }

  public User getUserDetail(UserRequest request)
  {
    return userRepository.getOne(request.getId());
  }

  public List<Role> getRoles()
  {
    return roleRepository.findByNameNot("HR_ADMIN");
  }

  public void deleteUser(UserRequest request)
  {
    User user = userRepository.getOne(request.getId());

    if (user == null)
    {
      throw new NotFoundException("Cannot locate user");
    }

    if (Objects.equals(user.getId(), getUser().getId()))
    {
      throw new ValidationException("You Cannot deactivate yourself!");
    }

    user.setActive(false);
    user.setCenters(Collections.emptyList());
    user.setRoles(Collections.emptyList());

    userRepository.saveAndFlush(user);
  }

  public List<Privilege> getPrivileges()
  {
    return privilegeRepository.findByNameNot("HR_ADMIN");
  }

  public User saveUser(UserRequest request)
  {
    validateUser(request);
    User user = userRepository.findByEmail(request.getEmail());
    if (user != null)
    {
      throw new ValidationException(String.format("Already exist [email=%s]", request.getEmail()));
    }
    user = request.toEntity();
    String randomPassword = encoder.encode(RandomStringUtils.randomAlphanumeric(8));

    putEmployee(request, user);

    logger.info("Saving user {} with password {}", user.getEmail(), randomPassword);
    user.setPasswordHash(randomPassword);

    for (String role : request.getRoles())
    {
      user.addRole(roleRepository.getOneByName(role));
    }

    for (String center : request.getCenters())
    {
      user.addCenter(centerRepository.getOneByName(center));
    }

    return userRepository.saveAndFlush(user);
  }

  public User updateUser(UserRequest request)
  {
    validateUser(request);

    User user = userRepository.getOne(request.getId());
    if (user == null)
    {
      throw new NotFoundException("Missing user");
    }

    User byEmail = userRepository.findByEmail(request.getEmail());
    if (byEmail != null
        && !user.getEmail().equals(byEmail.getEmail())
        && !user.equals(byEmail))
    {
      throw new ValidationException(String.format("Already exist [email=%s]", request.getEmail()));
    }

    request.toEntity(user);
    putEmployee(request, user);

    user.getRoles().clear();
    for (String role : request.getRoles())
    {
      user.addRole(roleRepository.getOneByName(role));
    }

    user.getCenters().clear();
    for (String center : request.getCenters())
    {
      user.addCenter(centerRepository.getOneByName(center));
    }
    invalidateMenuCache(user);
    return userRepository.saveAndFlush(user);
  }

  private void validateUser(UserRequest request)
  {
    if (StringUtils.isEmpty(request.getFirstname()))
    {
      throw new ValidationException("First Name is required.");
    }

    if (StringUtils.isEmpty(request.getPhone()))
    {
      throw new ValidationException("User phone is required.");
    }

    if (StringUtils.isEmpty(request.getEmail()))
    {
      throw new ValidationException("User email is required.");
    }
  }

  private void putEmployee(UserRequest request, User user)
  {
    UserType type = UserType.valueOf(request.getType());
    if (type == UserType.Employee)
    {
      if (request.getEmpId() == null)
      {
        throw new ValidationException("Employee id is required for UserType Employee");
      }
      Employee employee = employeeRepository.findOne(request.getEmpId());
      if (employee == null)
      {
        throw new ValidationException(String.format("Cannot locate Employee[id=%s]", mask(request.getEmpId())));
      }
      user.setEmployee(employee);
    }
  }

  private void invalidateMenuCache(User user)
  {
    Menu menu = menuBuilder.buildForUser(user);
    menuCache.put(user.getEmail(), menu);
  }

  public Menu getCurrentUserMenu()
  {
    User user = getUser();
    Menu menu = menuCache.getIfPresent(user.getEmail());
    if (menu == null)
    {
      menu = menuBuilder.buildForUser(user);
      menuCache.put(user.getEmail(), menu);
    }
    return menu;
  }

  public Menu getCurrentUserMenuV2()
  {
    User user = getUser();
    Menu menu = menuCache.getIfPresent(user.getEmail());
    //if (menu == null)
    //{
      menu = menuBuilder.buildForUserV2(user);
      menuCache.put(user.getEmail(), menu);
    //}
    return menu;
  }
  public Role saveRole(RoleRequest request)
  {
    Role role = request.toEntity();
    logger.info("Saving role {}", role.getName());

    for (PrivilegeRequest pr : request.getPrivileges())
    {
      role.addPrivilege(privilegeRepository.getOne(pr.getId()));
    }
    invalidateMenuCache();
    return roleRepository.saveAndFlush(role);
  }

  public Role updateRole(RoleRequest request)
  {

    Role role = roleRepository.getOne(request.getId());
    if (role == null)
    {
      throw new NotFoundException("Missing role");
    }

    role.getPrivileges().clear();

    for (PrivilegeRequest pr : request.getPrivileges())
    {
      role.addPrivilege(privilegeRepository.getOne(pr.getId()));
    }

    invalidateMenuCache();
    return roleRepository.saveAndFlush(role);

  }

  public void deleteRole(RoleRequest request)
  {
    roleRepository.delete(request.getId());
    invalidateMenuCache();
  }

  private void invalidateMenuCache()
  {
    logger.info("Invalidating menu cache as something got changed somewhere!");
    menuCache.invalidateAll();
  }

  public User getUser(String email)
  {
    return userRepository.findByEmailAndActiveTrue(email);
  }

  public void uploadProfilePic(MultipartFile file)
  {
    try
    {
      BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

      String extn = FilenameUtils.getExtension(file.getOriginalFilename());

      if (!in(extn, "JPG", "PNG", "JPEG", "GIF"))
      {
        throw new ValidationException("Only image files (gif, png, jpg) are allowed!");
      }

      String filename = UUID.randomUUID().toString() + ".png";
      File outfile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
      ImageIO.write(originalImage, "png", outfile);
      fileStore.store("PROFILEPIC", outfile);

      User user = userRepository.getOne(getUser().getId());
      user.setImagePath(filename);
      userRepository.saveAndFlush(user);
      markStale();
    }
    catch (IOException e)
    {
      logger.error("Error in profile image upload!", e);
      throw new UploadException("Something went wrong, please try later");
    }
  }

  public void changePassword(UserPasswordChangeRequest request)
  {
    User user = userRepository.getOne(request.getId());

    if (user == null)
    {
      throw new NotFoundException("Cannot locate user");
    }

    user.setPasswordHash(encoder.encode(request.getPassword()));
    userRepository.saveAndFlush(user);
  }

  public Set<String> getMyPrivileges()
  {
    return getUserPrivileges(userRepository.findOne(getUser().getId()));
  }

  public Set<String> getUserPrivileges(User user)
  {
    return user.getPrivileges();
  }
}
