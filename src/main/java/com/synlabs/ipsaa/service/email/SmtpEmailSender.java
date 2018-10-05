package com.synlabs.ipsaa.service.email;

import com.google.common.util.concurrent.RateLimiter;
import com.synlabs.ipsaa.config.Dev;
import com.synlabs.ipsaa.config.Prod;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.enums.EmailNotificationType;
import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.service.NotificationEmailService;
import com.synlabs.ipsaa.view.common.Response;
import com.synlabs.ipsaa.view.msgs.InlineContent;
import com.synlabs.ipsaa.view.msgs.MessageEmail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

@Service
@Prod
@Dev
public class SmtpEmailSender implements IEmailSender
{

  @Value("${ipsaa.testenv}")
  private boolean testenv;

  @Value("${ipsaa.testemail}")
  private String testemail;

//  @Value("${ipsaa.support.notificationemail}")
//  private String notificationEmail;

  @Value("${ipsaa.baseurl}")
  private String baseURL;

  @Value("${ipsaa.email.token.link}")
  private String link;

  @Value("${spring.mail.from}")
  private String from;

  @Value("${ipsaa.hdfc.payment.baseurl}")
  private String paymentBaseUrl;

  @Value("${ipsaa.hdfc.payment.email.contact.name}")
  private String paymentEmailContactName;

  @Value("${ipsaa.hdfc.payment.email.contact.mobile}")
  private String paymentEmailContactMobile;

  @Value("${ipsaa.paymentLink.lastDate}")
  private String lastDate;

  @Autowired
  private Configuration configuration;

  @Autowired
  private MailSender mailSender;

  @Autowired
  private SimpleMailMessage templateMessage;

  @Autowired
  private NotificationEmailService notificationEmailService;

  private static final Logger logger = LoggerFactory.getLogger(SmtpEmailSender.class);

  @Value("${ipsaa.mail.rate}")
  private int perseclimit;

  private RateLimiter limit;

  @PostConstruct
  public void init()
  {
    limit = RateLimiter.create(perseclimit);
  }

  @Async
  @Override
  public Boolean sendClockIn(StudentAttendance attendance, String... to)
  {
    try
    {
      Template template = configuration.getTemplate("email_clockin.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("attendance", attendance);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String message = out.toString();
      String subject = String.format("IPSAA | %s | Clockin", attendance.getStudent().getProfile().getFullName());
      return sendEmail(message, true, subject, null, to, null, null);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
    }
    return false;
  }

  @Async
  @Override
  public Boolean sendClockOut(StudentAttendance attendance, String... to)
  {
    try
    {
      Template template = configuration.getTemplate("email_clockout.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("attendance", attendance);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String message = out.toString();
      String subject = String.format("IPSAA | %s | Clockout", attendance.getStudent().getProfile().getFullName());
      return sendEmail(message, true, subject, null, to, null, null);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
    }

    return false;
  }

  @Async
  @Override
  public Boolean sendPassword(String password, String to)
  {
    try
    {
      Template template = configuration.getTemplate("email_password.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("password", password);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String message = out.toString();
      String subject = String.format("IPSAA Password for %s ", to);
      return sendEmail(message, true, subject, null, new String[] { to }, null, null);
    }
    catch (Exception ex)
    {
      logger.error("Error sending message!", ex);
    }

    return false;
  }

  @Async
  @Override
  public Boolean sendResetPassword(String token, String email)
  {
    try
    {
      Template template = configuration.getTemplate("email_change_password.ftl");
      Map<String, Object> rootMap = new HashMap<>();

      rootMap.put("token", token);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      logger.info("Sending Password Reset link to " + email);
      sendEmail(body, true, "Password change request ", null, new String[] { email }, null, null);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail!", ex);
    }
    return false;
  }

  @Async
  @Override
  public Boolean sendSupportQueryEmail(SupportQuery query)
  {
    try
    {
      Template template = configuration.getTemplate("email_supportquery.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("query", query);
      rootMap.put("util", new Response()
      {
      });
      rootMap.put("baseURL", baseURL);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, true, "Parent Query ", null, notificationEmailService.notificationEmailList(EmailNotificationType.SupportQuery), null, null);
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
  @Async
  public Boolean sendStudentDeleteEmail(Student student)
  {
    try
    {
      Template template = configuration.getTemplate("email_student_delete.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("student", student);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();
      sendEmail(body, true, "Student Deleted.", null, notificationEmailService.notificationEmailList(EmailNotificationType.DeleteStudent), null, null);
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
  @Async
  public Boolean sendStaffDeleteEmail(Employee employee)
  {
    try
    {
      Template template = configuration.getTemplate("email_staff_delete.ftl");
      Map<String, Object> rootMap = new HashMap<>();
      rootMap.put("employee", employee);
      Writer out = new StringWriter();
      template.process(rootMap, out);
      String body = out.toString();

      //shubham- adding cc
      String cc[]=new String[2];
      cc[0]=("ithelpdesk@ipsaa.in");
      cc[1]=("ipsaahr@ipsaa.in");
      cc[2]=("accounts@ipsaa.in");

      sendEmail(body, true, "Staff Deleted.", null, notificationEmailService.notificationEmailList(EmailNotificationType.DeleteStaff), cc, null);
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
  @Async
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
      sendEmail(body, true, "Student approval", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.StudentApproval), null, null);
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
  @Async
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
      sendEmail(body, true, "Staff approval", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.StaffApproval), null, null);
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
  @Async
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
      sendEmail(body, true, "Pending Approvals.", null,
                notificationEmailService.notificationEmailList(EmailNotificationType.PendingApproval), null, null);
      return true;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error("Error sending mail", ex);
    }
    return false;
  }

  @Async
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

  private Boolean sendEmail(String message, boolean htmlContent, String subject, List<File> attachments, String[] to, String[] cc, String[] bcc) throws MessagingException
  {
    MessageEmail email = new MessageEmail();
    email.setBody(message);
    email.setSubject(subject);
    email.setHtml(htmlContent);
    email.setAttachments(attachments);
    email.setTo(to == null ? new ArrayList<>() : Arrays.asList(to));
    email.setCc(cc == null ? new ArrayList<>() : Arrays.asList(cc));
    email.setBcc(bcc == null ? new ArrayList<>() : Arrays.asList(bcc));
    return sendEmail(email);
  }

  private Boolean sendEmail(MessageEmail email) throws MessagingException
  {
    limit.acquire();
    MimeMessage msg = ((JavaMailSender) mailSender).createMimeMessage();
    MimeMessageHelper helper = (CollectionUtils.isEmpty(email.getAttachments())
        && CollectionUtils.isEmpty(email.getInlineContents()))
                               ? new MimeMessageHelper(msg)
                               : new MimeMessageHelper(msg, true);
    helper.setFrom(from);
    helper.setSubject(email.getSubject() == null ? "" : email.getSubject());
    helper.setText(email.getBody(), email.isHtml());
    //Adding to,cc and bcc
    if (testenv)
    {
      logger.warn("Sending emails Subject[{}] from test env, replacing actual emails to...{} cc...{} bcc...{}", email.getSubject(), testemail, email.getBcc());
      helper.setTo(testemail);
    }
    else
    {
      logger.warn("Sending email Subject[{}] to...{}, cc...{}, bcc... {}", email.getSubject(), email.getTo(), email.getCc(), email.getBcc());
      if (!CollectionUtils.isEmpty(email.getTo()))
      {
        helper.setTo(email.getTo().toArray(new String[] {}));
      }
      if (!CollectionUtils.isEmpty(email.getCc()))
      {
        helper.setCc(email.getCc().toArray(new String[] {}));
      }
      if (!CollectionUtils.isEmpty(email.getBcc()))
      {
        helper.setBcc(email.getBcc().toArray(new String[] {}));
      }

    }
    //Adding attachments if exist
    if (!CollectionUtils.isEmpty(email.getAttachments()))
    {
      String s = "Attachment files: ";
      for (File file : email.getAttachments())
      {
        s = s.concat(file.getName() + ", ");
        helper.addAttachment(file.getName(), file);
      }
      logger.warn(s);
    }
    //Adding inLine Content
    if (!CollectionUtils.isEmpty(email.getInlineContents()))
    {
      String s = "Inline Images: ";
      for (InlineContent inlineContent : email.getInlineContents())
      {
        s = s.concat(inlineContent.getCid() + ": " + inlineContent.getFile().getName() + ", ");
        helper.addInline(inlineContent.getCid(), inlineContent.getFile());
      }
      logger.warn(s);
    }
    ((JavaMailSender) mailSender).send(msg);
    return true;
  }

}
