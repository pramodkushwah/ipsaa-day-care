package com.synlabs.ipsaa.service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.synlabs.ipsaa.entity.attendance.EmployeeAttendance;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.EmailNotificationType;
import com.synlabs.ipsaa.ex.UploadException;
import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.jpa.EmployeeRepository;
import com.synlabs.ipsaa.jpa.StudentRepository;
import com.synlabs.ipsaa.service.email.IEmailSender;
import com.synlabs.ipsaa.service.sms.ISMSSender;
import com.synlabs.ipsaa.view.fee.SlipEmailRequest;
import com.synlabs.ipsaa.view.msgs.EmailRequest;
import com.synlabs.ipsaa.view.msgs.InlineContent;
import com.synlabs.ipsaa.view.msgs.MessageEmail;
import com.synlabs.ipsaa.view.msgs.SMSRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CommunicationService
{

  private static final Logger logger = LoggerFactory.getLogger(CommunicationService.class);

  @Autowired
  private EventBus eventBus;

  @Autowired
  private ISMSSender smsSender;

  @Autowired
  private IEmailSender emailSender;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Value("${ipsaa.upload.location}")
  private String uploaddir;

//  @Value("${ipsaa.support.notificationemail}")
//  private String notificationEmail;

  @Value("${ipsaa.upload.attachemnt}")
  private String attachmentdir;

  @Value("${ipsaa.upload.inline-image}")
  private String inlineimagedir;

  @Value("${ipsaa.mail.rate}")
  private Integer rateLimit;

  @Value("${ipsaa.hdfc.payment.baseurl}")
  private String paymentBaseUrl;

  @Autowired
  private NotificationEmailService notificationEmailService;

  private Path uploadpath;

  @PostConstruct
  public void init()
  {
    eventBus.register(this);
    makeUploadDir();
  }

  private void makeUploadDir()
  {
    uploadpath = Paths.get(uploaddir);
    if (uploadpath.toFile().exists())
    {
      if (!uploadpath.toFile().isDirectory())
      {
        logger.error("{} is not a dir, cant continue", uploaddir);
        throw new UploadException(uploaddir + "is not a dir, cant continue");
      }
    }
    else
    {
      if (!uploadpath.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", uploaddir);
        throw new UploadException(uploaddir + "cannot create upload directory!");
      }
    }
  }

  @Subscribe
  public void studentAttendanceRecorded(StudentAttendance attendance)
  {

    logger.info("Recorded, now communicate: {}", attendance);

    try
    {

      Set<String> emails = new HashSet<>();
      Set<String> phones = new HashSet<>();

      for (StudentParent parent : attendance.getStudent().getParents())
      {

        if (parent.isEmailEnabled())
        {
          emails.add(parent.getEmail());
        }
        if (parent.isSmsEnabled())
        {
          phones.add(parent.getMobile());
        }
      }

      String emailarray[] = emails.toArray(new String[emails.size()]);
      String smsarray[] = phones.toArray(new String[phones.size()]);

      if (attendance.getCheckout() != null)
      {
        if (smsarray.length > 0)
        {
          smsSender.sendClockOut(attendance, smsarray);
        }
        else
        {
          logger.warn("Nowhere to send!, not sending SMS");
        }
        if (emailarray.length > 0)
        {
          emailSender.sendClockOut(attendance, emailarray);
        }
        else
        {
          logger.warn("Nowhere to send!, not sending EMAIL");
        }
      }
      else
      {
        if (smsarray.length > 0)
        {
          smsSender.sendClockIn(attendance, smsarray);
        }
        else
        {
          logger.warn("Nowhere to send!, not sending SMS");
        }
        if (emailarray.length > 0)
        {
          emailSender.sendClockIn(attendance, emailarray);
        }
        else
        {
          logger.warn("Nowhere to send!, not sending EMAIL");
        }
      }
    }
    catch (Exception e)
    {
      logger.error("Error sending msgs", e);
    }
  }

  @Subscribe
  public void employeeAttendanceRecorded(EmployeeAttendance attendance)
  {
    logger.info("Recorded, doing nothing: {}", attendance);
  }

  public void sendStudentEmail(EmailRequest request) throws IOException
  {
    Set<String> emails = new HashSet<>();
    for (Long id : request.getIds())
    {
      Student student = studentRepository.getOne(id);
      for (StudentParent parent : student.getParents())
      {
        String email = parent.getEmail();
        if (!StringUtils.isEmpty(email) && !email.equalsIgnoreCase("na"))
        {
          emails.add(email);
        }
      }
    }
    List<File> files = copyFilesToTempDir(request.getAttachments(), attachmentdir);

    MessageEmail email = new MessageEmail();
    email.setSubject(request.getSubject());
    email.setBody(request.getEmailcontent());
    email.setAttachments(files);
    email.setHtml(true);
    List<String> cids = request.getCids();
    List<MultipartFile> inlineFiles = request.getImages();
    if (!CollectionUtils.isEmpty(cids))
    {
      for (int i = 0; i < cids.size(); i++)
      {
        if (inlineFiles.get(i) == null)
        {
          logger.warn(String.format("Inline Content[cid=%s] does not have Multipart file."));
          continue;
        }
        email.getInlineContents().add(new InlineContent(cids.get(i), copyFileToTempDir(inlineFiles.get(i), inlineimagedir)));
      }
    }
    email.getTo().addAll(Arrays.asList(notificationEmailService.notificationEmailList(EmailNotificationType.StudentEmailMessage)));

    Iterator<String> iterator = emails.iterator();
    boolean flag = false;
    while (true)
    {
      List<String> list = new ArrayList<>();
      for (int i = 0; i < rateLimit; i++)
      {
        if (iterator.hasNext())
        {
          list.add(iterator.next());
        }
        else
        {
          flag = true;
          break;
        }
      }
      if (!CollectionUtils.isEmpty(list))
      {
        MessageEmail freshEmail = new MessageEmail(email);
        freshEmail.setTo(new ArrayList<>(email.getTo()));
        freshEmail.getBcc().addAll(list);
        if(request.getCc()!=null &&  !request.getCc().isEmpty())
          freshEmail.getCc().addAll(request.getCc());
        emailSender.sendMessage(freshEmail);
      }
      if (flag)
      {
        break;
      }
    }
  }

  public void sendStudentSMS(SMSRequest request)
  {
    //1. put all student's parent phone in map with center name
    Map<String, List<String>> map = new HashMap<>();
    for (Long id : request.getIds())
    {
      Student student = studentRepository.getOne(id);
      for (StudentParent parent : student.getParents())
      {
        List<String> phones = map.get(student.getCenterName());
        if (phones == null)
        {
          phones = new ArrayList<>();
          map.put(student.getCenterName(), phones);
        }
        phones.add(parent.getMobile());
      }
    }
    //2. send message center by center to parent of that center
    for (String center : map.keySet())
    {
      List<String> phones = map.get(center);
      smsSender.sendParentMessage(request.getSmscontent(), center, phones.toArray(new String[phones.size()]));
    }
  }

  public void sendStaffEmail(EmailRequest request) throws IOException
  {
    List<Employee> employees = employeeRepository.findAll(Arrays.asList(request.getIds()));
    Set<String> emails = new HashSet<>();
    for (Employee employee : employees)
    {
      String email = employee.getEmail();
      if (!StringUtils.isEmpty(email) && !email.equalsIgnoreCase("na"))
      {
        emails.add(email);
      }
    }
    List<File> files = copyFilesToTempDir(request.getAttachments(), attachmentdir);

    MessageEmail email = new MessageEmail();
    email.setSubject(request.getSubject());
    email.setBody(request.getEmailcontent());
    email.setAttachments(files);
    email.setHtml(true);
    List<String> cids = request.getCids();
    List<MultipartFile> inlineFiles = request.getImages();
    if (!CollectionUtils.isEmpty(cids))
    {
      for (int i = 0; i < cids.size(); i++)
      {
        if (inlineFiles.get(i) == null)
        {
          logger.warn(String.format("Inline Content[cid=%s] does not have Multipart file."));
          continue;
        }
        email.getInlineContents().add(new InlineContent(cids.get(i), copyFileToTempDir(inlineFiles.get(i), inlineimagedir)));
      }
    }
    email.getTo().addAll(Arrays.asList(notificationEmailService.notificationEmailList(EmailNotificationType.StaffEmailMessage)));

    Iterator<String> iterator = emails.iterator();
    boolean flag = false;
    while (true)
    {
      List<String> list = new ArrayList<>();
      for (int i = 0; i < rateLimit; i++)
      {
        if (iterator.hasNext())
        {
          list.add(iterator.next());
        }
        else
        {
          flag = true;
          break;
        }
      }
      if (!CollectionUtils.isEmpty(list))
      {
        MessageEmail freshEmail = new MessageEmail(email);
       if(request.getCc()!=null  && !request.getCc().isEmpty())
         freshEmail.getCc().addAll(request.getCc());

        freshEmail.setTo(new ArrayList<>(email.getTo()));
        freshEmail.getBcc().addAll(list);
        emailSender.sendMessage(freshEmail);
      }
      if (flag)
      {
        break;
      }
    }
  }

  private List<File> copyFilesToTempDir(List<MultipartFile> multipartFiles, String category) throws IOException
  {
    Path finalLocation = uploadpath.resolve(category);
    if (!finalLocation.toFile().exists())
    {
      if (!finalLocation.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", finalLocation);
        throw new UploadException(finalLocation + "cannot create upload directory!");
      }
    }
    List<File> files = new ArrayList<>();
    for (MultipartFile file : multipartFiles)
    {
      String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
      Path filePath = finalLocation.resolve(fileName);
      if (!filePath.toFile().createNewFile())
      {
        logger.error("cannot create File in directory : {}", finalLocation);
        throw new UploadException(String.format("cannot create File in directory : %s", finalLocation));
      }
      FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
      fileOutputStream.write(file.getBytes());
      fileOutputStream.flush();
      fileOutputStream.close();
      files.add(filePath.toFile());
    }
    return files;
  }

  private File copyFileToTempDir(MultipartFile multipartFile, String category) throws IOException
  {

    Path finalLocation = uploadpath.resolve(category);
    if (!finalLocation.toFile().exists())
    {
      if (!finalLocation.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", finalLocation);
        throw new UploadException(finalLocation + "cannot create upload directory!");
      }
    }
    String fileName = UUID.randomUUID().toString() + multipartFile.getOriginalFilename();
    Path filePath = finalLocation.resolve(fileName);
    if (!filePath.toFile().createNewFile())
    {
      logger.error("cannot create File in directory : {}", finalLocation);
      throw new UploadException(String.format("cannot create File in directory : %s", finalLocation));
    }
    FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
    fileOutputStream.write(multipartFile.getBytes());
    fileOutputStream.flush();
    fileOutputStream.close();
    return filePath.toFile();
  }

  public void sendStaffSMS(SMSRequest request)
  {
    List<Employee> employees = employeeRepository.findAll(Arrays.asList(request.getIds()));
//    1. eagerly loading Employee name and costcenter name for async textlocalservice method
    for (Employee employee : employees)
    {
      employee.getName();
      employee.getCostCenter().getName();
    }
    smsSender.sendStaffMessage(request.getSmscontent(), employees);
  }

  public void sendPasswordEmailAndSms(StudentParent account, String password)
  {
    smsSender.sendPassword(password, account.getMobile());
    emailSender.sendPassword(password, account.getEmail());
  }

  public void sendResetPasswordSmsAndEmail(String token, String phone, String email)
  {
    emailSender.sendResetPassword(token, email);
    smsSender.sendResetPassword(token, phone);
  }

  public void sendStudentApprovalEmail(Student student)
  {
    emailSender.sendStudentApprovalEmail(student);
  }

  public void sendStaffApprovalEmail(Employee employee)
  {
    emailSender.sendStaffApprovalEmail(employee);
  }

  public void sendPendingApprovalsEmail(List<ApprovalModel> approvalModels)
  {
    emailSender.sendPendingApprovalEmail(approvalModels);
  }

  public void sendStudentDeleteEmail(Student student)
  {
    emailSender.sendStudentDeleteEmail(student);
  }

  public void sendStaffDeleteEmail(Employee employee)
  {
    emailSender.sendStaffDeleteEmail(employee);
  }

  public void sendPaymentLink(List<StudentFeePaymentRequest> slips, SlipEmailRequest request)
  {
    List<MessageEmail> emails = new ArrayList<>();
    for (StudentFeePaymentRequest slip : slips)
    {
      List<StudentParent> parents = slip.getStudent().getParents();
      for (StudentParent parent : parents)
      {
        MessageEmail email = new MessageEmail();
        email.setSubject(request.getSubject());
        email.getTo().addAll(Arrays.asList(notificationEmailService.notificationEmailList(EmailNotificationType.FeePaymentLink)));
        email.getBcc().add(parent.getEmail());
        String body = request.getBody().replace("{paymentlink}",
                                                paymentBaseUrl + "/" +
                                                    BaseService.mask(slip.getId()) + "/" +
                                                    BaseService.mask(parent.getId()));
        email.setBody(body);
        email.setHtml(true);
        emails.add(email);
      }
    }
    for (MessageEmail email : emails)
    {
      emailSender.sendMessage(email);
    }
  }

}
