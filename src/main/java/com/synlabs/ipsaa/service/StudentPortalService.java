package com.synlabs.ipsaa.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.attendance.QStudentAttendance;
import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.GalleryPhoto;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.fee.PayuSetting;
import com.synlabs.ipsaa.entity.food.FoodMenu;
import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheetEntry;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.entity.support.SupportQueryEntry;
import com.synlabs.ipsaa.enums.*;
import com.synlabs.ipsaa.ex.AuthException;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.service.email.IEmailSender;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.PayUHelper;
import com.synlabs.ipsaa.view.FileResponse;
import com.synlabs.ipsaa.view.center.SupportRequest;
import com.synlabs.ipsaa.view.fee.PayuResponse;
import com.synlabs.ipsaa.view.fee.StudentFeeLedgerRequest;
import com.synlabs.ipsaa.view.student.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class StudentPortalService extends BaseService
{

  private static final Logger logger = LoggerFactory.getLogger(StudentPortalService.class);

  @Value("${ipsaa.payu.successurl}")
  private String successurl;

  @Value("${ipsaa.payu.failureurl}")
  private String failureurl;

  @Value("${ipsaa.activity.pp.days}")
  private int activityDays = 10;

  @Value("${ipsaa.activity.pp.days}")
  private int foodMenuDays = 10;

  @Value("${ipsaa.pp.gallery.page_size}")
  private int pageSize;

  @Autowired
  private GalleryPhotoRepository galleryPhotoRepository;

  @Autowired
  private IEmailSender emailSender;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StudentFeeRepository feeRepository;

  @Autowired
  private StudentFeePaymentRepository paymentRepository;

  @Autowired
  StudentFeePaymentRecordRepository paymentRecordRepository;

  @Autowired
  private PayuResponseRepository payuResponseRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private PayuSettingRepository payuSettingRepository;

  @Autowired
  private StudentProfileRepository profileRepository;

  @Autowired
  private StudentParentRepository parentRepository;

  @Autowired
  private SupportRepository supportRepository;

  @Autowired
  private SharingSheetRepository sharingSheetRepository;

  @Autowired
  private ParentSharingSheetRepository parentSharingSheetRepository;

  @Autowired
  private ParentSharingSheetEntryRepository parentSheetEntryRepository;

  @Autowired
  private PayUHelper payUHelper;

  @Autowired
  private StudentFeePaymentRepository studentFeePaymentRepository;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private DocumentService documentService;

  @Autowired
  private FoodMenuRepository foodMenuRepository;

  @Autowired
  private GroupActivityRepository activityRepository;

  @Autowired
  private StudentRepository studentRepository;

  public List<Student> profile()
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    User user = userRepository.findOne(getUser().getId());
    return user.getParent().getStudents();
  }

  public List<StudentAttendance> getStudentAttendance(PortalRequest request)
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    LocalDate som = LocalDate.now().withDayOfMonth(1);
    LocalDate eom = som.plusMonths(1).minusDays(1);

    Student student = getStudent(request);

    JPAQuery<StudentAttendance> query = new JPAQuery<>(em);
    QStudentAttendance attendance = QStudentAttendance.studentAttendance;
    query.select(attendance).from(attendance)
         .where(attendance.student.eq(student))
         .where(attendance.attendanceDate.between(som.toDate(), eom.toDate()))
    ;
    return query.fetch();
  }

  public StudentFee getStudentFee(PortalRequest request)
  {

    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    Student student = getStudent(request);
    StudentFee fee = feeRepository.findByStudent(student);
    if (fee == null)
    {
      throw new ValidationException("Student fee not created.");
    }
    return fee;
  }

//  public Center preparePaymentRequest(FeePaymentRequest request)
//  {
//
//    if (getUser().getUserType() != UserType.Parent)
//    {
//      throw new AuthException("Logged in user in not a parent!");
//    }
//
//    User freshuser = userRepository.getOne(getUser().getId());
//    StudentParent parent = freshuser.getParent();
//    Student student = getStudent(request);
//
//    StudentFeePaymentRequest fee = paymentRepository.findFirstByStudentOrderByInvoiceDateDesc(student);
//
//    if (fee == null)
//    {
//      throw new ValidationException("Missing fee details");
//    }
//
//    StudentFeePaymentRecord record = new StudentFeePaymentRecord();
//    record.setRequest(fee);
//    record.setPaymentStatus(PaymentStatus.Raised);
//    record.setStudent(fee.getStudent());
//    record.setPaymentMode(PaymentMode.PayU);
//    record.setTxnid(UUID.randomUUID().toString());
//
//    paymentRecordRepository.save(record);
//
//    if (fee.getPaymentStatus().equals(PaymentStatus.Paid))
//    {
//      return null;
//    }
//
//    //String otherPostParamSeq = "phone|surl|furl|lastname|curl|address1|address2|city|state|country|zipcode|pg";
//    //String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
//    PayuSetting setting = payuSettingRepository.findOneByCenter(fee.getStudent().getCenter());
//
//    if (setting == null)
//    {
//      throw new ValidationException("Missing payu setting for " + fee.getStudent().getCenter().getName() + " center");
//    }
//
//    if (!setting.isEnabled())
//    {
//      throw new ValidationException("This center is not enabled for payu processing");
//    }
//
//    //mandatory params
//    request.set("key", setting.getKey());
//    request.setTransactionId(record.getTxnid());
//    request.set("amount", fee.getTotalFee().toString());
//    request.set("productinfo", student.getProgram().getName());
//    request.set("firstname", parent.getFirstName());
//    request.set("email", parent.getEmail());
//
//    //optional params
//    request.set("phone", parent.getMobile());
//    request.set("surl", successurl);
//    request.set("furl", failureurl);
//    request.set("lastname", parent.getLastName());
//    request.set("address1", parent.getResidentialAddress().getAddress());
//    request.set("city", parent.getResidentialAddress().getCity());
//    request.set("state", parent.getResidentialAddress().getState());
//    request.set("zipcode", parent.getResidentialAddress().getZipcode());
//
//    return fee.getStudent().getCenter();
//  }

  public StudentFeePaymentRequest getStudentFeeLedger(PortalRequest request)
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    Student student = getStudent(request);
    StudentFeePaymentRequest studentFeePaymentRequest = paymentRepository.findFirstByStudentOrderByCreatedDateDesc(student);
    if (studentFeePaymentRequest == null)
    {
      throw new ValidationException("Missing fee details");
    }
    return studentFeePaymentRequest;
  }

  public void recordSuccess(PayuResponse response)
  {

    String hashClob;
    String salt;
    //load salt from setting
    PayuSetting setting = payuSettingRepository.findFirstByKey(response.getKey());

    if (setting != null)
    {
      salt = setting.getSalt();
    }
    else
    {
      recordFailedSuccess(response, "Failed to find salt based on key!");
      return;
    }

    //check reverse hash
    if (!StringUtils.isEmpty(response.getAdditionalCharges()))
    {
      //$additionalCharges.'|'.$salt.'|'.$status.'|||||||||||'.$email.'|'.$firstname.'|'.$productinfo.'|'.$amount.'|'.$txnid.'|'.$key;
      hashClob = String.format("%s|%s|%s|||||||||||%s|%s|%s|%s|%s|%s",
                               response.getAdditionalCharges(),
                               salt,
                               response.getStatus(),
                               response.getEmail(),
                               response.getFirstname(),
                               response.getProductinfo(),
                               response.getAmount(),
                               response.getTxnid(),
                               response.getKey()
                              );
    }
    else
    {
      //$salt.'|'.$status.'|||||||||||'.$email.'|'.$firstname.'|'.$productinfo.'|'.$amount.'|'.$txnid.'|'.$key;
      hashClob = String.format("%s|%s|||||||||||%s|%s|%s|%s|%s|%s",
                               salt,
                               response.getStatus(),
                               response.getEmail(),
                               response.getFirstname(),
                               response.getProductinfo(),
                               response.getAmount(),
                               response.getTxnid(),
                               response.getKey()
                              );
    }

    String hash = payUHelper.hashCal("SHA-512", hashClob);
    if (hash.equals(response.getHash()))
    {
      payuResponseRepository.saveAndFlush(response);
      logger.info("Recording success with response {}", response);
      StudentFeePaymentRecord record = paymentRecordRepository.findByTxnid(response.getTxnid());

      if (record == null)
      {
        return;
      }
      record.setPaymentDate(LocalDate.now().toDate());
      record.setPaymentMode(PaymentMode.PayU);
      record.setPaymentStatus(PaymentStatus.Paid);
      paymentRecordRepository.saveAndFlush(record);
    }
    else
    {
      recordFailedSuccess(response, "Hash check failed");
    }

    //TODO send email to admin
  }

  /**
   * This is when we are not able to process success
   * Either due to hash mismatch or not being able to find the salt used for txn
   */
  private void recordFailedSuccess(PayuResponse response, String msg)
  {
    response.setComment(msg);
    payuResponseRepository.saveAndFlush(response);
    StudentFeePaymentRecord record = paymentRecordRepository.findByTxnid(response.getTxnid());

    if (record == null)
    {
      return;
    }
    record.setPaymentStatus(PaymentStatus.Failed);

    paymentRecordRepository.saveAndFlush(record);

  }

  public void recordFailure(PayuResponse payuResponse)
  {
    payuResponseRepository.saveAndFlush(payuResponse);
    logger.info("Recording failure with response {}", payuResponse);
    StudentFeePaymentRecord record = paymentRecordRepository.findByTxnid(payuResponse.getTxnid());
    if (record == null)
    {
      return;
    }

    record.setPaymentStatus(PaymentStatus.Failed);
    paymentRecordRepository.saveAndFlush(record);

    //TODO send email to admin
  }

  public List<SupportQuery> supportQueryList()
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    User freshuser = userRepository.getOne(getUser().getId());
    StudentParent parent = freshuser.getParent();

    return supportRepository.findByParent(parent);

  }

  public SupportQuery supportQuery(SupportRequest request)
  {

    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    User freshuser = userRepository.getOne(getUser().getId());
    StudentParent parent = freshuser.getParent();

    return supportRepository.findByParentAndId(parent, request.getId());
  }

  public SupportQuery createQuery(SupportRequest request)
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    if (request.getStudentId() == null)
    {
      throw new ValidationException("Student is required.");
    }

    if (StringUtils.isEmpty(request.getTitle()))
    {
      throw new ValidationException("Please provide title and description of the query");
    }

    Student student = studentRepository.findOne(request.getStudentId());
    if (student == null)
    {
      throw new ValidationException(String.format("Cannot locate student[id=%s]", request.getMaskedStudentId()));
    }

    User freshuser = userRepository.getOne(getUser().getId());
    StudentParent parent = freshuser.getParent();

    SupportQuery query = new SupportQuery();
    query.setCenter(getStudent(request).getCenter());
    query.setTitle(request.getTitle());
    query.setDescription(request.getDescription());
    query.setParent(parent);
    query.setStudent(student);
    query = supportRepository.saveAndFlush(query);

    sendEmailNotificaiton(query);
    return query;
  }

  @Async
  public void sendEmailNotificaiton(SupportQuery query)
  {
    emailSender.sendSupportQueryEmail(query);
  }

  public SupportQuery postReply(SupportRequest request)
  {

    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }

    if (StringUtils.isEmpty(request.getDescription()))
    {
      throw new ValidationException("Please provide description for reply");
    }

    User freshuser = userRepository.getOne(getUser().getId());
    StudentParent parent = freshuser.getParent();

    SupportQuery query = supportRepository.findByParentAndId(parent, request.getId());

    if (query == null)
    {
      throw new NotFoundException("Cannot locate original query!");
    }

    SupportQueryEntry reply = new SupportQueryEntry();
    reply.setDescription(request.getDescription());
    reply.setSupportQuery(query);
    query.setStatus(QueryStatus.Open);
    query.addReply(reply);
    query = supportRepository.saveAndFlush(query);

    sendEmailNotificaiton(query);
    return query;
  }

  @Transactional
  public Student updateStudentProfile(StudentRequest request) throws ParseException
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    if (request.getMaskedId() == null)
    {
      throw new ValidationException("Student id is null");
    }
    Student student = studentRepository.findOne(request.getId());
    if (student == null)
    {
      throw new ValidationException(String.format("Student[id=%s] not found", request.getMaskedId()));
    }

    //load the user again as auth user may be stale and can raise lazy exception
    User freshuser = userRepository.getOne(getUser().getId());
    if (!freshuser.getParent().hasStudent(student))
    {
      throw new ValidationException("Unauthorized student access");
    }

    updateStudent(student, request);

    return student;
  }

  private void updateStudent(Student student, StudentRequest request) throws ParseException
  {
    StudentProfile studentProfile = student.getProfile();
    studentProfile.setNickName(request.getNickName());
    studentProfile.setBloodGroup(request.getBloodGroup());
    studentProfile.setFamilyType(FamilyType.valueOf(request.getFamilyType()));
//    studentProfile.setDob(request.parseDate(request.getDob()));

    List<ParentRequest> parents = request.getParents();

    for (ParentRequest parentRequest : parents)
    {
      StudentParent parent = student.getParent(Relationship.valueOf(parentRequest.getRelationship()));
      updateParent(parent, parentRequest);
    }
    profileRepository.saveAndFlush(studentProfile);
    for (StudentParent parent : student.getParents())
    {
      parentRepository.saveAndFlush(parent);
    }
  }

  private void updateParent(StudentParent parent, ParentRequest request)
  {
    parent.setMobile(request.getMobile());
    parent.setEducationalQualification(request.getEducationalQualification());
    parent.setOccupation(request.getOccupation());
    parent.setOrganisation(request.getOrganisation());
    parent.setDesignation(request.getDesignation());
    parent.setOfficeAddress(request.getOfficeAddress().toEntity(parent.getOfficeAddress()));
    parent.setResidentialAddress(request.getResidentialAddress().toEntity(parent.getResidentialAddress()));

    if (parent.isAccount())
    {
      updateparentUser(parent, request);
    }

    parent.setEmail(request.getEmail());
  }

  private void updateparentUser(StudentParent parent, ParentRequest request)
  {
    if (!parent.getEmail().equals(request.getEmail()))
    {
      User user = userRepository.findByEmail(request.getEmail());
      if (user != null)
      {
        throw new ValidationException(String.format("User already exist with email[%s]", request.getEmail()));
      }
      else
      {
        user = userRepository.findOneByParent(parent);
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setPhone(request.getMobile());
        userRepository.saveAndFlush(user);
      }
    }
  }

  public void enableSms(ParentRequest request)
  {
    User user = getFreshUser();
    if (user.getUserType() != UserType.Parent)
    {
      throw new ValidationException("User is not parent.");
    }

    if (request.getId() == null)
    {
      throw new ValidationException("Parent id not found");
    }

    StudentParent parent = user.getParent();
    List<StudentParent> parents = parent.getStudents().get(0).getParents();
    StudentParent reqParent = null;
    for (StudentParent p : parents)
    {
      if (p.getId().equals(request.getId()))
      {
        reqParent = p;
        break;
      }
    }
    if (reqParent == null)
    {
      throw new ValidationException("Unauthorized parent access!");
    }

    reqParent.setSmsEnabled(request.isSmsEnabled());
    parentRepository.saveAndFlush(reqParent);
  }

  public void enableEmail(ParentRequest request)
  {
    User user = getFreshUser();
    if (user.getUserType() != UserType.Parent)
    {
      throw new ValidationException("User is not parent.");
    }

    if (request.getId() == null)
    {
      throw new ValidationException("Parent id not found");
    }

    StudentParent parent = user.getParent();
    List<StudentParent> parents = parent.getStudents().get(0).getParents();
    StudentParent reqParent = null;
    for (StudentParent p : parents)
    {
      if (p.getId().equals(request.getId()))
      {
        reqParent = p;
        break;
      }
    }
    if (reqParent == null)
    {
      throw new ValidationException("Unauthorized parent access!");
    }

    reqParent.setEmailEnabled(request.isEmailEnabled());
    parentRepository.saveAndFlush(reqParent);
  }

  private List<Student> getParentStudent()
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    //load the user again as auth user may be stale and can raise lazy exception
    User freshuser = userRepository.getOne(getUser().getId());
    return freshuser.getParent().getStudents();
  }

  public FileResponse downloadFeeReceipt(StudentFeeLedgerRequest request) throws IOException
  {

    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    if (request.getId() == null)
    {
      throw new ValidationException("Ledger Id not found.");
    }
    StudentFeePaymentRequest feePayment = studentFeePaymentRepository.findOne(request.getId());
    if (feePayment == null)
    {
      throw new NotFoundException("Fee slip not generated.");
    }
    if (feePayment.getPaymentStatus() != PaymentStatus.Paid && feePayment.getPaymentStatus() != PaymentStatus.PartiallyPaid)
    {
      throw new ValidationException("Fee is not paid.");
    }
    User user = userRepository.findOne(getUser().getId());
    if (!user.getParent().hasStudent(feePayment.getStudent()))
    {
      throw new ValidationException("Unauthorized receipt access!");
    }
//    only generate
//    if (feePayment.getReceiptSerial() == null || feePayment.getReceiptFileName() == null)
//    {
//      documentService.generateFeeReceiptPdf(feePayment);
//    }
    documentService.generateFeeReceiptPdf(feePayment);

    InputStream is = null;
    byte[] bytes;
    try
    {
      is = fileStore.getStream("RECEIPT", feePayment.getReceiptFileName());
      bytes = IOUtils.toByteArray(is);
    }
    catch (FileNotFoundException e)
    {
      documentService.generateFeeReceiptPdf(feePayment);
      is = fileStore.getStream("RECEIPT", feePayment.getReceiptFileName());
      bytes = IOUtils.toByteArray(is);
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
    return new FileResponse(bytes, "IPSAA-" + feePayment.getStudent().getAdmissionNumber() + "-FeeReceipt.pdf");
  }

  public FileResponse downloadFeeSlip(StudentFeeLedgerRequest request) throws IOException
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new AuthException("Logged in user in not a parent!");
    }
    if (request.getId() == null)
    {
      throw new ValidationException("Ledger Id not found.");
    }
    StudentFeePaymentRequest feePayment = studentFeePaymentRepository.findOne(request.getId());
    if (feePayment == null)
    {
      throw new NotFoundException("Fee slip not generated.");
    }
    User user = userRepository.findOne(getUser().getId());
    if (!user.getParent().hasStudent(feePayment.getStudent()))
    {
      throw new ValidationException("Unauthorized slip access!");
    }
    if (feePayment.isReGenerateSlip() || feePayment.getSlipFileName() == null || feePayment.getSlipSerial() == null)
    {
      documentService.generateFeeSlipPdf(feePayment);
    }

    InputStream is = null;
    byte[] bytes;
    try
    {
      is = fileStore.getStream("SLIP", feePayment.getSlipFileName());
      bytes = IOUtils.toByteArray(is);
    }
    catch (FileNotFoundException e)
    {
      documentService.generateFeeSlipPdf(feePayment);
      is = fileStore.getStream("SLIP", feePayment.getSlipFileName());
      bytes = IOUtils.toByteArray(is);
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
    return new FileResponse(bytes, "IPSAA-" + feePayment.getStudent().getAdmissionNumber() + "-FeeSlip.pdf");
  }

  public SharingSheet getSharingSheet(String date, PortalRequest request)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Student student = getStudent(request);
    try
    {
      return sharingSheetRepository.findByStudentIdAndSharingDate(student.getId(), sdf.parse(date));
    }
    catch (ParseException e)
    {
      logger.error("bad date:( ?", e);
      throw new ValidationException("Invalid date");
    }
  }

  public ParentSharingSheet getParentSharingSheet(String date, PortalRequest request)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Student student = getStudent(request);
    try
    {
      ParentSharingSheet pss = parentSharingSheetRepository.findByStudentIdAndSharingDate(student.getId(), sdf.parse(date));

      if (pss == null)
      {
        pss = new ParentSharingSheet();
        pss.setStudent(student);
        pss.setSharingDate(LocalDate.now().toDate());
        parentSharingSheetRepository.saveAndFlush(pss);
      }

      return pss;
    }
    catch (ParseException e)
    {
      logger.error("bad date:( ?", e);
      throw new ValidationException("Invalid date");
    }
  }

  public ParentSharingSheet saveParentSharingSheet(ParentSharingSheetRequest request)
  {
    Student student = getStudent(request);
    ParentSharingSheet pss = parentSharingSheetRepository.findByStudentIdAndId(student.getId(), request.getId());

    if (pss == null)
    {
      throw new NotFoundException("Cannot locate sharing sheet!");
    }

    pss = request.toEntity(pss);
    pss = parentSharingSheetRepository.saveAndFlush(pss);
    return pss;
  }

  public ParentSharingSheet addParentSheetEntry(SharingSheetEntryRequest request)
  {
    Student student = getStudent(request);
    ParentSharingSheet pss = parentSharingSheetRepository.findByStudentIdAndId(student.getId(), request.getId());
    ParentSharingSheetEntry entry = request.toParentEntity();
    entry.setSharingSheet(pss);
    pss.addEntry(entry);
    parentSheetEntryRepository.saveAndFlush(entry);
    return pss;
  }

  public List<FoodMenu> getFoodMenu(PortalRequest request)
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new ValidationException("Logged in user is not parent.");
    }
    Center center = getStudent(request).getCenter();
    //calculate start and end date of month
    LocalDate end = LocalDate.now();
    LocalDate start = end.minusDays(foodMenuDays);

    return foodMenuRepository.findByCenterCodeAndDateBetween(center.getCode(), start.toDate(), end.toDate());
  }

  public List<GroupActivity> getActivities(PortalRequest request)
  {
    if (getUser().getUserType() != UserType.Parent)
    {
      throw new ValidationException("Logged in user is not parent.");
    }
    Student student = getStudent(request);
    Center center = student.getCenter();
    ProgramGroup group = student.getGroup();

    //calculate start and end date
    LocalDate end = LocalDate.now();
    LocalDate start = end.minusDays(activityDays);

    return activityRepository.findByCenterAndGroupAndDateBetween(center, group, start.toDate(), end.toDate());
  }

  public Student getStudent(PortalRequest request)
  {
    if (request.getStudentId() == null)
    {
      throw new ValidationException("Please select Student.");
    }
    return getStudent(request.getStudentId());
  }

  private Student getStudent(SharingSheetEntryRequest request)
  {
    if (request.getStudentId() == null)
    {
      throw new ValidationException("Please select Student.");
    }
    return getStudent(request.getStudentId());
  }

  private Student getStudent(Long id)
  {
    Student student = studentRepository.findOne(id);
    User freshuser = userRepository.getOne(getUser().getId());
    if (!freshuser.getParent().getStudents().contains(student))
    {
      throw new ValidationException("Unauthorized student access.");
    }
    return student;
  }

  public StudentParent getParent()
  {
    User user = getFreshUser();
    if (user.getUserType() != UserType.Parent)
    {
      throw new ValidationException("Logged in user is not Parent.");
    }
    return user.getParent();
  }

  public List<GalleryPhoto> getGalleryPhotos(Long studentId, Long page)
  {
    page = page == null ? 1 : page < 0 ? 1 : page;
    Student student = getStudent(unmask(studentId));
    Center center = student.getCenter();
//    JPAQuery<GalleryPhoto> query = new JPAQuery<>(em);
//    QGalleryPhoto photo = QGalleryPhoto.galleryPhoto;
//
//    query.select(photo).from(photo)
//         .orderBy(photo.createdDate.desc())
//         .where(photo.center.eq(center))
//         .where(photo.student.isNull().or(photo.student.eq(student)));
//
//    return query.fetch();
    return galleryPhotoRepository.findPhotoForPP(center, student, new PageRequest(page.intValue() - 1, pageSize));

  }
}
