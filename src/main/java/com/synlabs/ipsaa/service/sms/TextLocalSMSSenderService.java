package com.synlabs.ipsaa.service.sms;

import com.synlabs.ipsaa.config.Local;
import com.synlabs.ipsaa.config.Prod;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.service.BaseService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Prod
@Service
@Local
public class TextLocalSMSSenderService extends BaseService implements ISMSSender
{

  private static final Logger logger = LoggerFactory.getLogger(TextLocalSMSSenderService.class);

  private String apiurl = "http://api.textlocal.in/send/";

  @Autowired
  private Configuration configuration;

  @Value("${ipsaa.testenv}")
  private boolean testenv;

  @Value("${ipsaa.testphone}")
  private String testphone;

  @Value("${ipsaa.sms.apikey}")
  private String apikey;

  @Value("${ipsaa.sms.sender}")
  private String sender;

  @Async
  @Override
  public Boolean sendClockIn(StudentAttendance attendance, String... to)
  {
    try
    {
      Template template = configuration.getTemplate("sms_clockin.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("attendance", attendance);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  @Async
  @Override
  public Boolean sendClockOut(StudentAttendance attendance, String... to)
  {

    try
    {
      Template template = configuration.getTemplate("sms_clockout.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("attendance", attendance);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  @Async
  @Override
  public Boolean sendPassword(String password, String to)
  {

    try
    {
      Template template = configuration.getTemplate("sms_password.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("password", password);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  @Async
  @Override
  public Boolean sendMessage(String message, String... to)
  {
    try
    {
      Template template = configuration.getTemplate("sms_message.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("message", message);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  @Override
  public Boolean sendParentMessage(String message, String centerName, String... to)
  {
    try
    {
      Template template = configuration.getTemplate("sms_message_parent.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("message", message);
      rootMap.put("center",centerName);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  @Override
  @Async
  public Boolean sendStaffMessage(String message, List<Employee> employees){
    try
    {
      Template template = configuration.getTemplate("sms_message_staff.ftl");
      for (Employee employee : employees)
      {
        try
        {
          if (StringUtils.isEmpty(employee.getMobile())
              || employee.getMobile().equalsIgnoreCase("na"))
          {
            continue;
          }
          Map<String, Object> rootMap = new HashMap<>();
          rootMap.put("message", message);
          rootMap.put("name", employee.getName());
          rootMap.put("center", employee.getCostCenter().getName());
          Writer out = new StringWriter();
          template.process(rootMap, out);
          String msg = out.toString();
          sendSMS(msg, employee.getMobile());
        }
        catch (Exception ex)
        {
          logger.error("Error sending message!", ex);
        }
      }
    }
    catch (IOException ex)
    {
      logger.error("Error sending message!", ex);
    }
    return true;
  }

  @Override
  @Async
  public Boolean sendResetPassword(String token, String to)
  {
    try
    {
      Template template = configuration.getTemplate("sms_change_password.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("token", token);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String msg = out.toString();
      return sendSMS(msg, to);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
      return false;
    }
  }

  private boolean sendSMS(String msg, String... recepients) throws IOException
  {

    String numbers = StringUtils.join(recepients, ",");
    if (testenv)
    {
      logger.warn("Actual sms service enabled in test env, replacing phones by {}", testphone);
      numbers = testphone;
    }

    msg = URLEncoder.encode(msg, "UTF-8");
    String data = String.format("apiKey=%s&message=%s&sender=%s&numbers=%s", apikey, msg, sender, numbers);
    HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
    logger.info("Send data {}", data);
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
    conn.getOutputStream().write(data.getBytes("UTF-8"));
    logger.info("Response -{} ", fromInputStreamToString(conn.getInputStream()));
    return true;

  }

  private String fromInputStreamToString(InputStream is) throws IOException
  {
    final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    final StringBuilder stringBuffer = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null)
    {
      stringBuffer.append(line);
    }
    rd.close();
    return stringBuffer.toString();
  }

}
