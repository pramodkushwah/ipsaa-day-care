package com.synlabs.ipsaa.service;

import com.google.common.io.ByteStreams;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.common.EIDNumberSequence;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.common.User;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeProfile;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.entity.staff.QEmployee;
import com.synlabs.ipsaa.enums.*;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.UploadException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.StringUtil;
import com.synlabs.ipsaa.view.batchimport.ImportEmployee;
import com.synlabs.ipsaa.view.batchimport.ImportSalary;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.report.excel.StaffExcelReport;
import com.synlabs.ipsaa.view.report.excel.StaffReport;
import com.synlabs.ipsaa.view.staff.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.util.StringUtil.in;

@Service
public class StaffService extends BaseService
{
  private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private static final Logger logger = LoggerFactory.getLogger(StaffService.class);

  @Value("${ipsaa.export.directory}")
  private String exportDirectory;

  @Autowired
  private EmployeePaySlipRepository employeePaySlipRepository;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private EmployeeSalaryRepository employeeSalaryRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExcelImportService excelImportService;

  @Autowired
  private EmployeeProfileRepository employeeProfileRepository;

  @Autowired
  private EIDNumberSequenceRepository eidNumberSequenceRepository;

  @Autowired
  private CommunicationService communicationService;

  @Autowired
  private LegalEntityRepository legalEntityRepository;

  public List<Employee> list()
  {
    List<Center> usercenters = getUserCenters();
    return employeeRepository.findByCostCenterIn(usercenters);
  }

  public StaffSummaryPageResponse list(StaffFilterRequest request)
  {

    List<Employee> employees;
    List<Center> centers = getUserCenters();
    JPAQuery<Employee> query = new JPAQuery<>(entityManager);
    QEmployee employee = QEmployee.employee;
    query.select(employee).from(employee);
    if (request.getActive() != null)
    {
      query.where(employee.active.eq(request.getActive()));
    }

    if (!StringUtils.isEmpty(request.getEmployerCode()))
    {
      query.where(employee.employer.code.eq(request.getEmployerCode()));
    }

    if (!StringUtils.isEmpty(request.getCenterCode()))
    {
      if (request.getCenterCode().equals("All"))
      {
        query.where(employee.costCenter.in(centers).or(employee.costCenter.isNull()));
      }
      else
      {
        query.where(employee.costCenter.code.eq(request.getCenterCode()));
      }
    }
    else
    {
      query.where(employee.active.eq(request.getActive() == null ? true : request.getActive()));
      query.where(employee.costCenter.in(centers).or(employee.costCenter.isNull()));
    }

    if (!StringUtils.isEmpty(request.getEmployeeType()) && !request.getEmployeeType().equals("ALL"))
    {
      query.where(employee.employeeType.eq(EmployeeType.valueOf(request.getEmployeeType())));
    }

    int pageSize;
    int pageNumber;
    int pageCount;
    if (request.getPageSize() != null && request.getPageSize() != 0)
    {
      long count = query.fetchCount();
      pageSize = request.getPageSize();
      pageNumber = request.getPageNumber();
      pageCount = (int) Math.ceil(count * 1.0 / pageSize);
      employees = query.limit(request.getPageSize())
                       .offset(request.getPageSize() * (request.getPageNumber() - 1))
                       .fetch();
    }
    else
    {
      employees = query.fetchAll().fetch();
      pageSize = employees.size();
      pageNumber = 1;
      pageCount = 1;
    }
    List<StaffSummaryResponse> list = employees.stream().map(StaffSummaryResponse::new).collect(Collectors.toList());
    return new StaffSummaryPageResponse(pageSize, pageNumber, pageCount, list);
  }

  public Employee getEmployee(StaffRequest request)
  {
    return employeeRepository.findOne(request.getId());
  }
// shubham
  public File getEmployeeSalary(StaffFilterRequest staffRequest){

    List<EmployeeSalary> list=new ArrayList<>();

    if (!StringUtils.isEmpty(staffRequest.getEmployerCode()) || staffRequest.getEmployerCode().equals("ALL")) {
      list = employeeSalaryRepository.findByEmployeeActiveTrueAndEmployeeCostCenterIn(getUserCenters());
    }
      StaffExcelReport excel = new StaffExcelReport(list, staffRequest, exportDirectory, employeePaySlipRepository, staffRequest.getEmployerCode());
      return excel.createExcel(); // returning file


  }
  @Transactional
  public Employee save(StaffRequest request) throws ParseException
  {
    validate(request);

    if (request.getBiometricId() != null)
    {
      Employee e1 = employeeRepository.findOneByBiometricId(request.getBiometricId());
      if (e1 != null)
      {
        throw new ValidationException(String.format("Duplicate BiometricId [center=%s,eid=%s]", e1.getCostCenter().getName(), e1.getEid()));
      }
    }

    Employee reportingManager = employeeRepository.findOne(request.getReportingManagerId());
    if (reportingManager == null)
    {
      throw new ValidationException(String.format("Cannot locate Reporting manager [id = %s]", mask(request.getReportingManagerId())));
    }

    LegalEntity legalEntity = legalEntityRepository.findOne(request.getEmployerId());
    if (legalEntity == null)
    {
      throw new ValidationException(String.format("Cannot locate Reporting manager[id = %s]", mask(request.getReportingManagerId())));
    }

    Center center = centerRepository.findOne(request.getCostCenterId());
    if (center == null)
    {
      throw new ValidationException(String.format("Cannot locate Center[id=%s]", mask(request.getCostCenterId())));
    }

    Employee employee = request.toEntity();
    employee.setEid(generateEmpId());
    employee.setActive(true);
    employee.setApprovalStatus(ApprovalStatus.NewApproval);
    employee.setCostCenter(center);
    employee.setEmployer(legalEntity);
    employee.setReportingManager(reportingManager);
    Employee employee1 = employeeRepository.saveAndFlush(employee);
    communicationService.sendStaffApprovalEmail(employee1);
    return employee1;
  }

  @Transactional
  public Employee update(StaffRequest request) throws ParseException
  {
    validate(request);

    Employee employee = employeeRepository.findOne(request.getId());
    if (employee == null)
    {
      throw new NotFoundException("Cannot locate employee!");
    }

    if (request.getBiometricId() != null)
    {
      Employee e1 = employeeRepository.findOneByBiometricId(request.getBiometricId());
      if (e1 != null && !e1.equals(employee))
      {
        throw new ValidationException(String.format("Duplicate BiometricId [center=%s,eid=%s]", e1.getCostCenter().getName(), e1.getEid()));
      }
    }

    Employee reportingManager = employeeRepository.findOne(request.getReportingManagerId());
    if (reportingManager == null)
    {
      throw new ValidationException(String.format("Cannot locate Reporting manager [id = %s]", mask(request.getReportingManagerId())));
    }

    LegalEntity legalEntity = legalEntityRepository.findOne(request.getEmployerId());
    if (legalEntity == null)
    {
      throw new ValidationException(String.format("Cannot locate Reporting manager[id = %s]", mask(request.getReportingManagerId())));
    }

    Center costCenter = centerRepository.findOne(request.getCostCenterId());
    employee = request.toEntity(employee);
    employee.setCostCenter(costCenter);
    EmployeeProfile profile = request.getProfile().toEntity(employee.getProfile());
    employee.setProfile(profile);
    employee.setEmployer(legalEntity);
    employee.setReportingManager(reportingManager);
    employeeRepository.saveAndFlush(employee);
    return employee;
  }

  private void validate(StaffRequest request)
  {
    if (StringUtils.isEmpty(request.getFirstName()))
    {
      throw new ValidationException("Missing First Name");
    }

    if (request.getCostCenterId() == null || request.getCostCenterId() == 0L)
    {
      throw new ValidationException("Missing Cost Center");
    }

    if (request.getEmployerId() == null)
    {
      throw new ValidationException("Employer is required.");
    }

    if (request.getReportingManagerId() == null)
    {
      throw new ValidationException("Reporting menager is required.");
    }

    LegalEntity legalEntity = legalEntityRepository.findOne(request.getEmployerId());
    if (legalEntity == null)
    {
      throw new ValidationException(String.format("Cannot locate LegalEntity[id = %s]", request.getEmployerId()));
    }

    if (StringUtils.isEmpty(request.getType()))
    {
      throw new ValidationException("Missing Employee Type");
    }

    if (StringUtils.isEmpty(request.getMobile()))
    {
      throw new ValidationException("Missing Mobile");
    }

    if (request.getProfile() == null)
    {
      throw new ValidationException("Please fill address on address tab");
    }

    StaffProfileRequest profile = request.getProfile();

    if (profile.getDob() == null)
    {
      throw new ValidationException("Missing DOB");
    }

    if (profile.getDob() == null)
    {
      throw new ValidationException("Missing DOJ");
    }

    if (StringUtils.isEmpty(profile.getAddress()))
    {
      throw new ValidationException("Please fill address on address tab");
    }

    if (StringUtils.isEmpty(profile.getAddress().getAddress()))
    {
      throw new ValidationException("Please fill address on address tab");
    }

    if (StringUtils.isEmpty(profile.getAddress().getCity()))
    {
      throw new ValidationException("Please fill city on address tab");
    }

    if (StringUtils.isEmpty(profile.getAddress().getState()))
    {
      throw new ValidationException("Please fill state on address tab");
    }

    if (StringUtils.isEmpty(profile.getAddress().getZipcode()))
    {
      throw new ValidationException("Please fill zipcode on address tab");
    }

    //permanent address
    if (StringUtils.isEmpty(profile.getPermanentAddress()))
    {
      throw new ValidationException("Please fill address on permanent address tab");
    }

    if (StringUtils.isEmpty(profile.getPermanentAddress().getAddress()))
    {
      throw new ValidationException("Please fill address on permanent address tab");
    }

    if (StringUtils.isEmpty(profile.getPermanentAddress().getCity()))
    {
      throw new ValidationException("Please fill city on permanent address tab");
    }

    if (StringUtils.isEmpty(profile.getPermanentAddress().getState()))
    {
      throw new ValidationException("Please fill state on permanent address tab");
    }

    if (StringUtils.isEmpty(profile.getPermanentAddress().getZipcode()))
    {
      throw new ValidationException("Please fill zipcode on permanent address tab");
    }
  }

  private String generateEmpId()
  {
    EIDNumberSequence sequence = eidNumberSequenceRepository.findOne(1L);
    if (sequence == null)
    {
      sequence = new EIDNumberSequence();
      sequence.setId(1L);
    }
    String id = String.format("E%03d", sequence.getNext());
    eidNumberSequenceRepository.saveAndFlush(sequence);
    return id;
  }

  @Transactional
  public Map<String, String> importRecords(MultipartFile file, String dryRun) throws Exception
  {
    boolean errorInFile = false;
    Map<String, String> statusMap = new HashMap<>();
    statusMap.put("error", "false");
    List<ImportEmployee> employees = excelImportService.importEmployeeRecords(file);
    List<Employee> employeeList = new ArrayList<>(employees.size());
    List<EmployeeProfile> employeeProfiles = new ArrayList<>(employees.size());
    List<User> userList = new ArrayList<>(employees.size());
    Set<String> mobile = new HashSet<>();
    if (employees.size() < 1)
    {
      throw new Exception("Error Processing file");
    }
    for (ImportEmployee employee : employees)
    {
      String validationMessage = employee.validate();
      if (validationMessage.equalsIgnoreCase("OK"))
      {
        if (!mobile.add(employee.getMobile()))
        {
          validationMessage = "Employee already exists please check Eid or Phone.";
          errorInFile = true;
        }
      }
      else
      {
        errorInFile = true;
      }
      statusMap.put(employee.getFirstName() + " " + employee.getMobile(), validationMessage);
    }
    if (!errorInFile && dryRun.equalsIgnoreCase("false"))
    {
      for (ImportEmployee employee : employees)
      {
        Employee emp = convertImportEmployeeToEmployee(employee);
        EmployeeProfile employeeProfile = convertImportEmployeeToEmployeeProfile(employee);
        Employee existingRecord = additionalValidationOnUser(emp.getMobile(), emp.getCostCenter(), employee.getEid());
        if (existingRecord != null)
        {
          emp.setId(existingRecord.getId());
          emp.setEid(existingRecord.getEid());
          employeeProfile.setId(existingRecord.getProfile().getId());
        }
        else
        {
          emp.setEid(generateEmpId());
          User user = convertImportEmployeeToUser(employee);
          if (user != null)
          {
            User existingUser = userRepository.findByEmailAndActiveTrue(employee.getEmail());
            if (existingUser != null)
            {
              user.setId(existingUser.getId());
              user.setParent(existingUser.getParent());
            }
            user.setEmployee(emp);
            userList.add(user);
          }
        }
        emp.setProfile(employeeProfile);
        employeeProfiles.add(employeeProfile);
        employeeList.add(emp);
      }
      employeeProfileRepository.save(employeeProfiles);
      employeeRepository.save(employeeList);
      userRepository.save(userList);
    }
    if (errorInFile)
    {
      statusMap.put("error", "true");
    }
    return statusMap;
  }

  @Transactional
  public Map<String, String> importSalaries(MultipartFile file, String dryRun) throws Exception
  {
    boolean errorInFile = false;
    Map<String, String> map = new HashMap<>();
    map.put("error", "false");
    List<ImportSalary> salaries = excelImportService.importSalaryRecords(file);
    List<EmployeeSalary> employeeSalaryList = new ArrayList<>(salaries.size());
    Set<String> eid = new HashSet<>();
    if (salaries.size() < 1)
    {
      throw new Exception("Error Processing file");
    }
    for (ImportSalary importSalary : salaries)
    {
      String validationMessage = importSalary.validate();
      if (validationMessage.equalsIgnoreCase("OK"))
      {
        if (additionalValidationOnUser2(importSalary.getEid()) == null)
        {
          validationMessage = "Cannot find the employee.";
          errorInFile = true;
        }
        else if (!eid.add(importSalary.getEid()))
        {
          validationMessage = "A employee can have single entry in a sheet.";
          errorInFile = true;
        }
      }
      else
      {
        errorInFile = true;
      }
      map.put(importSalary.getEid(), validationMessage);
    }
    if (!errorInFile && dryRun.equalsIgnoreCase("false"))
    {
      for (ImportSalary importSalary : salaries)
      {
        Employee employee = employeeRepository.findByEid(importSalary.getEid());
        EmployeeSalary salary = employeeSalaryRepository.findByEmployee(employee);
        salary = importSalary.toEntity(salary);
        salary.setEmployee(employee);

        employeeSalaryList.add(salary);
      }
      employeeSalaryRepository.save(employeeSalaryList);
    }
    if (errorInFile)
    {
      map.put("error", "true");
    }
    return map;
  }

  private Employee convertImportEmployeeToEmployee(ImportEmployee importEmployee)
  {
    Employee employee = new Employee();
    employee.setFirstName(importEmployee.getFirstName());
    employee.setLastName(importEmployee.getLastName());
    employee.setMobile(importEmployee.getMobile());
    employee.setSecondaryNumbers(importEmployee.getSecondaryNumbers());
    employee.setEmail(importEmployee.getEmail());
    employee.setDesignation(importEmployee.getDesignation());
    employee.setEmployeeType(EmployeeType.valueOf(importEmployee.getEmployeeType()));
    employee.setPayrollEnabled(importEmployee.isPayrollEnabled());
    employee.setAttendanceEnabled(importEmployee.isAttendanceEnabled());
    employee.setExpectedIn(importEmployee.getExpectedIn());
    employee.setExpectedOut(importEmployee.getExpectedOut());
    employee.setMaritalStatus(MaritalStatus.valueOf(importEmployee.getMaritalStatus()));
    if (!StringUtils.isEmpty(importEmployee.getCostCenterName()))
    {
      employee.setCostCenter(centerRepository.findByCode(importEmployee.getCostCenterName()));
    }
    employee.setApprovalStatus(ApprovalStatus.Approved);
    return employee;
  }

  private EmployeeProfile convertImportEmployeeToEmployeeProfile(ImportEmployee importEmployee)
  {
    EmployeeProfile employeeProfile = new EmployeeProfile();
    employeeProfile.setDob(importEmployee.getDob());
    employeeProfile.setDoj(importEmployee.getDoj());
    employeeProfile.setGender(Gender.valueOf(importEmployee.getGender()));
    if (!StringUtils.isEmpty(importEmployee.getAddress()) &&
        !StringUtils.isEmpty(importEmployee.getCity()) &&
        !StringUtils.isEmpty(importEmployee.getZipCode()) &&
        !StringUtils.isEmpty(importEmployee.getState()))
    {
      Address address = new Address();
      address.setAddress(importEmployee.getAddress());
      address.setCity(importEmployee.getCity());
      address.setState(importEmployee.getState());
      address.setPhone(importEmployee.getPhone());
      address.setZipcode(importEmployee.getZipCode());
      address.setAddressType(AddressType.Home);
      employeeProfile.setAddress(address);
    }
    if (!StringUtils.isEmpty(importEmployee.getpAddress()) &&
        !StringUtils.isEmpty(importEmployee.getpCity()) &&
        !StringUtils.isEmpty(importEmployee.getpZipCode()) &&
        !StringUtils.isEmpty(importEmployee.getpState()))
    {
      Address address = new Address();
      address.setAddress(importEmployee.getpAddress());
      address.setCity(importEmployee.getpCity());
      address.setState(importEmployee.getpState());
      address.setPhone(importEmployee.getpPhone());
      address.setZipcode(importEmployee.getpZipCode());
      address.setAddressType(AddressType.Permanent);
      employeeProfile.setPermanentAddress(address);
    }
    return employeeProfile;
  }

  private User convertImportEmployeeToUser(ImportEmployee importEmployee)
  {
    User user = null;
    if (!StringUtils.isEmpty(importEmployee.getEmail()))
    {
      user = new User();
      user.setEmail(importEmployee.getEmail());
      user.setPhone(importEmployee.getMobile());
      user.setFirstname(importEmployee.getFirstName());
      user.setLastname(importEmployee.getLastName());
      user.setUserType(UserType.valueOf(importEmployee.getUserType()));
      user.setActive(true);

      //TODO emp password during import??
      user.setPasswordHash(passwordEncoder.encode("ipsaa123"));
    }
    return user;
  }

  public String exportRecords(String employeeType) throws Exception
  {
    List<Employee> employees;
    if (!employeeType.equalsIgnoreCase("ALL"))
    {
      EmployeeType et = EmployeeType.valueOf(employeeType);
      employees = employeeRepository.findByEmployeeTypeAndActiveIsTrue(et);
    }
    else
    {
      employees = employeeRepository.findByActiveIsTrue();
    }
    Path dirpath = Paths.get(exportDirectory);
    String filename = dirpath.resolve(UUID.randomUUID().toString() + ".xls").toString();
    try (InputStream is = StudentService.class.getClassLoader().getResourceAsStream("staff.xls"))
    {
      try (OutputStream os = new FileOutputStream(filename))
      {
        Context context = new Context();
        context.putVar("employees", employees);
        JxlsHelper.getInstance().processTemplate(is, os, context);
      }
    }
    return filename;
  }

  public Employee additionalValidationOnUser(String mobile, Center center, String eid)
  {
    Employee employee = employeeRepository.findByMobileAndCostCenter(mobile, center);
    if (employee == null)
    {
      employee = employeeRepository.findByEidAndCostCenter(eid, center);
    }
    return employee;
  }

  public Employee additionalValidationOnUser2(String eid)
  {
    return employeeRepository.findByEid(eid);
  }

  //Code should be uncommented only if payroll feature required
//  public boolean additionalValidationOnSalary(ImportSalary importSalary)
//  {
//    long count = 0L;
//    if (!importSalary.isMaster())
//    {
//      JPAQuery<EmployeeSalary> query = new JPAQuery<>(entityManager);
//      QEmployeeSalary employeeSalary = QEmployeeSalary.employeeSalary;
//      count = query
//          .from(employeeSalary)
//          .where(employeeSalary.employee.eid.eq(importSalary.getEid())
//                                            .or(employeeSalary.employee.mobile.eq(importSalary.getMobile())))
//          .where(employeeSalary.month.eq(importSalary.getMonth()))
//          .where(employeeSalary.year.eq(importSalary.getYear())).fetchCount();
//
//    }
//    return count <= 0;
//  }

  public void deleteV2(StaffRequest request)
  {
    Employee employee = employeeRepository.findOne(request.getId());
    if (employee == null)
    {
      throw new NotFoundException(String.format("Emploee[%s] not found", request.getId()));
    }
    employee.setActive(false);
    employeeRepository.saveAndFlush(employee);
    communicationService.sendStaffDeleteEmail(employee);
  }
  //update by shubham
  public void delete(StaffRequest request)
  {
    Employee employee = employeeRepository.findOne(request.getId());
    if (employee == null)
    {
      throw new NotFoundException(String.format("Emploee[%s] not found", request.getId()));
    }
    // checking dol is present or not
    if (employee.getProfile().getDol() == null)
    {
      throw new ValidationException(String.format("Emploee[%s] date of leaving not found", request.getId()));
    }
      employee.setActive(false);
      employeeRepository.saveAndFlush(employee);
      communicationService.sendStaffDeleteEmail(employee);


  }

  public void uploadStaffPic(StaffRequest request, MultipartFile file)
  {
    try
    {
      Employee employee = employeeRepository.findOne(request.getId());
      if (employee == null)
      {
        throw new NotFoundException(String.format("Cannot locate employee[%s]", request.getId()));
      }
      BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

      String extn = FilenameUtils.getExtension(file.getOriginalFilename());

      if (!in(extn, "JPG", "PNG", "JPEG", "GIF"))
      {
        throw new ValidationException("Only image files (gif, png, jpg) are allowed!");
      }

      String filename = UUID.randomUUID().toString() + ".png";
      File outfile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
      ImageIO.write(originalImage, "png", outfile);
      fileStore.store("EMPLOYEEPIC", outfile);

      EmployeeProfile profile = employee.getProfile();
      profile.setImagePath(filename);
      employeeProfileRepository.saveAndFlush(profile);
    }
    catch (IOException e)
    {
      logger.error("Error in profile image upload!", e);
      throw new UploadException("Something went wrong, please try later");
    }
  }

  public String getStaffImageData(Employee employee)
  {
    if (!StringUtils.isEmpty(employee.getProfile().getImagePath()))
    {
      try
      {
        InputStream is = fileStore.getStream("EMPLOYEEPIC", employee.getProfile().getImagePath());
        byte[] bytes = ByteStreams.toByteArray(is);
        return Base64.getEncoder().encodeToString(bytes);
      }
      catch (IOException ioe)
      {
        logger.error("Error loading image from store! ", ioe);
      }
    }
    return null;
  }

  public List<Employee> newStaffApprovals(CenterRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Center id is null.");
    }
    Center center = centerRepository.findOne(request.getId());
    if (center == null)
    {
      throw new NotFoundException(String.format("Center[%s] not found", request.getMaskId()));
    }
    User user = getFreshUser();
    List<Center> centers = getUserCenters();
    if (!centers.contains(center))
    {
      throw new ValidationException(String.format("Unauthorized access to center[%s] user[%s]", request.getMaskId(), user.getEmail()));
    }
    return employeeRepository.findByActiveTrueAndApprovalStatusAndCostCenter(ApprovalStatus.NewApproval, center);
  }

  public void approveStaff(StaffRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Staff Id is null");
    }
    Employee employee = employeeRepository.findOne(request.getId());
    if (employee == null)
    {
      throw new NotFoundException(String.format("Employee[%s] not found", request.getMaskedId()));
    }
    Center center = employee.getCostCenter();
    List<Center> centers = getUserCenters();
    if (!centers.contains(center))
    {
      throw new ValidationException(String.format("Unauthorized access: staff is not in user's centers"));
    }
    employee.setApprovalStatus(ApprovalStatus.Approved);
    employeeRepository.saveAndFlush(employee);
  }

  public void rejectStaff(StaffRequest request)
  {
    if (request.getId() == null)
    {
      throw new ValidationException("Staff Id is null");
    }
    Employee employee = employeeRepository.findOne(request.getId());
    if (employee == null)
    {
      throw new NotFoundException(String.format("Employee[%s] not found", request.getMaskedId()));
    }
    Center center = employee.getCostCenter();
    List<Center> centers = getUserCenters();
    if (!centers.contains(center))
    {
      throw new ValidationException(String.format("Unauthorized access: staff is not in user's centers"));
    }
    employee.setApprovalStatus(ApprovalStatus.Rejected);
    employeeRepository.saveAndFlush(employee);
  }

  public List<Employee> listAll()
  {
//    try {
//     uploadData();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (InvalidFormatException e) {
//      e.printStackTrace();
//    }
    return employeeRepository.findByActiveIsTrue();
  }
  //-----------------------shubham-----------------------------------------
// not in use
  public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\shubham\\Desktop\\ipsaa\\payrol excel\\Bank Details.xlsx";
  @Transactional
  public void uploadData()throws IOException, InvalidFormatException {
    Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
    Sheet sheet = workbook.getSheetAt(0);
    for (int i=0;i<=sheet.getPhysicalNumberOfRows();i++) {
      if(i==0) continue;
      Row row=sheet.getRow(i);

      if(row!=null){
        String eid=row.getCell(3).getStringCellValue();
        if(eid.equals("Emp.Code"))
          continue;
        String accountNo;
        if(row.getCell(5).getCellType()==Cell.CELL_TYPE_STRING){
          accountNo=row.getCell(5).getStringCellValue();
        }else{
          accountNo=String.valueOf(row.getCell(5).getNumericCellValue());
        }
        String holdername=row.getCell(6).getStringCellValue();
        String ifsc=row.getCell(7).getStringCellValue();
        String bankName=row.getCell(8).getStringCellValue();
        String branch=row.getCell(9).getStringCellValue();
        Employee e=employeeRepository.findByEid(eid);
        if(e!=null){

            e.getProfile().setHolderName(holdername);
            e.getProfile().setBankName(bankName);
            e.getProfile().setBranchName(branch);
            e.getProfile().setIfscCode(ifsc);
          System.out.println(String.format("EmployeeId details added [%s] ",eid));
            employeeRepository.saveAndFlush(e);
        }
        else{
          System.out.println(String.format("EmployeeId not found [%s] ",eid));
        }
      }


    }
    workbook.close();
  }
}
