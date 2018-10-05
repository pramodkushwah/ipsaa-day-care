package com.synlabs.ipsaa.service.email;

import com.synlabs.ipsaa.config.Dev;
import com.synlabs.ipsaa.config.Local;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.enums.EmailNotificationType;
import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.NotificationEmailService;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.msgs.MessageEmail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

@Service
@Local
public class MockEmailSender implements IEmailSender
{
  private static final Logger logger = LoggerFactory.getLogger(MockEmailSender.class);

  @Autowired
  private Configuration configuration;

//  @Value("${ipsaa.support.notificationemail}")
//  private String notificationEmail;

  @Autowired
  private NotificationEmailService notificationEmailService;

  @Value("${ipsaa.baseurl}")
  private String                   baseURL;

  @Value("${ipsaa.hdfc.payment.baseurl}")
  private String paymentBaseUrl;

  @Override
  public Boolean sendClockIn(StudentAttendance attendance, String... to)
  {
    logger.info("CLOCKIN to {} data {}", to, attendance);
    return true;
  }

  @Override
  public Boolean sendClockOut(StudentAttendance attendance, String... to)
  {
    logger.info("CLOCKOUT to {} data {}", to, attendance);
    return true;
  }

  @Override
  public Boolean sendPassword(String password, String to)
  {
    logger.info("password to {} data {}", to, password);
    return true;
  }

  @Override
  public Boolean sendResetPassword(String token, String email)
  {
    logger.info("email to {} subject {} msg {}", email, "Reset password", String.format("Token [%s] to reset password", token));
    return true;
  }

  @Override
  public Boolean sendSupportQueryEmail(SupportQuery query)
  {
    try
    {
      Response r = new Response()
      {
      };
      Template template = configuration.getTemplate("email_supportquery.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("query", query);
      rootMap.put("maskedId", r.mask(query.getId()));
      rootMap.put("baseURL", baseURL);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Parent Query ", null, notificationEmailService.notificationEmailList(EmailNotificationType.SupportQuery));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail!", ex);
    }
    return false;
  }

  @Override
  public Boolean sendStudentApprovalEmail(Student student)
  {
    try
    {
      Response response = new Response()
      {
      };
      Template template = configuration.getTemplate("email_student_approval.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("student", student);
      rootMap.put("maskedId", response.mask(student.getId()));
      rootMap.put("baseURL", baseURL);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Student approval", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.StudentApproval));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail!", ex);
    }
    return false;
  }

  @Override
  public Boolean sendStaffApprovalEmail(Employee employee)
  {
    try
    {
      Response response = new Response()
      {
      };
      Template template = configuration.getTemplate("email_staff_approval.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("employee", employee);
      rootMap.put("maskedId", response.mask(employee.getId()));
      rootMap.put("baseURL", baseURL);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Staff approval", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.StaffApproval));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      ;
      logger.error("Error sending mail", ex);
    }
    return false;
  }

  @Override
  public Boolean sendPendingApprovalEmail(List<ApprovalModel> approvalModels)
  {
    try
    {
      Template template = configuration.getTemplate("email_weekly_approval.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("approvalModels", approvalModels);
      rootMap.put("baseURL", baseURL);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Pending Approvals.", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.PendingApproval));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      ;
      logger.error("Error sending mail", ex);
    }
    return false;
  }

  @Override
  public Boolean sendStudentDeleteEmail(Student student)
  {
    try
    {
      Template template = configuration.getTemplate("email_staff_delete.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("student",student);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Student Deleted.", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.DeleteStudent));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail!", ex);
    }
    return false;
  }

  @Override
  public Boolean sendStaffDeleteEmail(Employee employee)
  {
    try
    {
      Template template = configuration.getTemplate("email_staff_delete.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("employee",employee);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, "Staff Deleted.", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.DeleteStaff));
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail!", ex);
    }
    return false;
  }


  public Boolean sendMessage(MessageEmail message)
  {
    try
    {
      sendEmail(message);
      return true;
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
    }
    return false;
  }

  private Boolean sendEmail(MessageEmail email)
  {
    logger.info("=======================================================");
    logger.info("TO: {} BCC {} CC {}", email.getTo(), email.getBcc(), email.getCc());
    logger.info("=======================================================");
    logger.info("TITLE: {}", email.getSubject());
    logger.info("=======================================================");
    logger.info("BODY: {}", email.getBody());
    logger.info("=======================================================");
    logger.info(String.format("attachments [%s]", email.getAttachments().size()));
    logger.info("=======================================================");
    logger.info(String.format("Inline images [%s]", email.getInlineContents().size()));
    logger.info("=======================================================");
    return true;
  }

  private void sendEmail(String body, String title, List<File> attachments, String... to)
  {
    MessageEmail email = new MessageEmail();
    //shubham adding cc for employee deactivation
      List<String>cc=new ArrayList<>();
        cc.add("ithelpdesk@ipsaa.in");
        cc.add("ipsaahr@ipsaa.in");
        cc.add("accounts@ipsaa.in");
    email.setCc(cc);

    email.setBody(body);
    email.setSubject(title);
    email.setTo(to == null ? new ArrayList<>() : Arrays.asList(to));
    email.getAttachments().addAll(attachments == null ? new ArrayDeque<>() : attachments);
    sendEmail(email);
  }
}
