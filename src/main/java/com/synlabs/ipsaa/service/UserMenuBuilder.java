package com.synlabs.ipsaa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.view.common.Menu;
import com.synlabs.ipsaa.view.common.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserMenuBuilder
{
  private static final Logger logger = LoggerFactory.getLogger(UserMenuBuilder.class);

  @Autowired
  private ResourceLoader resourceLoader;

  private Map<String, MenuItem> submannuMap = new HashMap<>();
  private Map<String, MenuItem> mannuMap = new HashMap<>();


  private Map<String, MenuItem> submannuMap2 = new HashMap<>();
  private Map<String, MenuItem> mannuMap2 = new HashMap<>();

  private ObjectMapper jsonMapper = new ObjectMapper();

  @PostConstruct
  public void init() {
    try {
      Resource[] resources
          = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                                .getResources("classpath*:/menu/*.json");          ////////////Cahnge it for angular1

      for (Resource resource : resources) {
        logger.info("Loaded menu from {}", resource.getFilename());
        MenuItem item = jsonMapper.readValue(resource.getFile(), MenuItem.class);
        mannuMap.put(item.getLabel(), item);

        for (MenuItem submenu : item.getSubmenu()) {
          submenu.setParent(item.getLabel());
          submannuMap.put(submenu.getLabel(), submenu);
        }
      }
      resources
              = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
              .getResources("classpath*:/menus/*.json");          ////////////Cahnge it for angular1

      for (Resource resource : resources) {
        logger.info("Loaded menu from {}", resource.getFilename());
        MenuItem item = jsonMapper.readValue(resource.getFile(), MenuItem.class);
        mannuMap2.put(item.getLabel(), item);

        for (MenuItem submenu : item.getSubmenu()) {
          submenu.setParent(item.getLabel());
          submannuMap2.put(submenu.getLabel(), submenu);
        }
      }

    } catch (IOException e) {
      logger.error("Cannot load menu from disk!", e);
    }
  }

  public Menu buildForUser(User user) {
    Menu navigation = new Menu();
    for (MenuItem submenu : submannuMap.values()) {
      if (user.hasPrivilege(submenu.getP())) {
        MenuItem parent = mannuMap.get(submenu.getParent());
        navigation.add(parent, submenu);
      }
    }
    return navigation;
  }
  public Menu buildForUserV2(User user) {
    Menu navigation = new Menu();
    for (MenuItem submenu : submannuMap2.values()) {
      if (user.hasPrivilege(submenu.getP())) {
        MenuItem parent = mannuMap2.get(submenu.getParent());
        navigation.add(parent, submenu);
      }
    }
    return navigation;
  }
}
