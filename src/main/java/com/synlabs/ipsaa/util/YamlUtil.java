package com.synlabs.ipsaa.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sushil on 28-10-2015.
 */
@Component
public class YamlUtil
{

  private static final Logger logger = LoggerFactory.getLogger(YamlUtil.class);

  private static Map<String, Object> yamlmap = new HashMap<>();

  @PostConstruct
  public void loadall()
  {
    logger.info("Loading all yamls");
    Yaml yaml = new Yaml();
    try
    {
      Object loaded = yaml.load(new ClassPathResource("db/attendance.yaml").getInputStream());
      yamlmap.put("attendancequery", loaded);
    }
    catch (IOException e)
    {
      logger.error("Error loading yamls", e);
    }

  }

  public String get(String key, String property)
  {
    if (yamlmap.containsKey(key))
    {
      return (String) ((Map) yamlmap.get(key)).get(property);
    }
    return null;
  }
}
