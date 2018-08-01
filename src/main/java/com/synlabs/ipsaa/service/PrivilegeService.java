package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.auth.IPSAAAuth;
import com.synlabs.ipsaa.entity.common.Privilege;
import com.synlabs.ipsaa.entity.common.Role;
import com.synlabs.ipsaa.jpa.PrivilegeRepository;
import com.synlabs.ipsaa.jpa.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Service
public class PrivilegeService extends BaseService
{
  private static Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  private RoleRepository roleRepository;

  @PostConstruct
  public void init()
  {
    Field[] fields = IPSAAAuth.Privileges.class.getDeclaredFields(); // taking variable name
    for (Field f : fields)
    {
      if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
      {
        logger.info("Found privilege {} ", f.getName());
        if (!privilegeRepository.findByName(f.getName()).isPresent())
        {
          Privilege p = new Privilege();
          p.setName(f.getName());
          p.setDescription(f.getName());
          privilegeRepository.saveAndFlush(p);
          logger.info("Not in db, saved {}", f.getName());

          if (f.getName().equals("HR_ADMIN") || f.getName().equals("PAYSLIP_LOCK"))
          {
            continue;
          }

          //attach it to tech admin role:)
          Role role = roleRepository.getOneByName("TECHADMIN");
          if (role == null)
          {
            role = new Role();
            role.setName("TECHADMIN");
            roleRepository.saveAndFlush(role);
          }
          role.addPrivilege(p);
          roleRepository.saveAndFlush(role);
          logger.info("attached to tech admin, saved {}", f.getName());
        }
      }
    }
  }
}

