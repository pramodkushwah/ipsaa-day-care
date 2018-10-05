package com.synlabs.ipsaa.service;

import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static com.synlabs.ipsaa.util.StringUtil.in;
import static java.math.BigDecimal.ZERO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;

import com.synlabs.ipsaa.entity.common.*;
import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.dom4j.DocumentException;
import org.joda.time.LocalDate;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteStreams;
import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.entity.programs.ProgramGroup;
import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.entity.sharing.SharingSheetEntry;
import com.synlabs.ipsaa.entity.student.QStudent;
import com.synlabs.ipsaa.entity.student.QStudentFee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRecord;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.entity.student.StudentProfile;
import com.synlabs.ipsaa.enums.AddressType;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.FamilyType;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.Gender;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.enums.Relationship;
import com.synlabs.ipsaa.enums.SharingSheetEntryType;
import com.synlabs.ipsaa.enums.SharingSheetFeedQty;
import com.synlabs.ipsaa.enums.UserType;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.UploadException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.AdmissionNumberSequenceRepository;
import com.synlabs.ipsaa.jpa.CenterProgramFeeRepository;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.ParentSharingSheetRepository;
import com.synlabs.ipsaa.jpa.ProgramGroupRepository;
import com.synlabs.ipsaa.jpa.ProgramRepository;
import com.synlabs.ipsaa.jpa.RoleRepository;
import com.synlabs.ipsaa.jpa.SharingSheetEntryRepository;
import com.synlabs.ipsaa.jpa.SharingSheetRepository;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRecordRepository;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRepository;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.jpa.StudentParentRepository;
import com.synlabs.ipsaa.jpa.StudentProfileRepository;
import com.synlabs.ipsaa.jpa.StudentRepository;
import com.synlabs.ipsaa.jpa.UserRepository;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.util.BigDecimalUtils;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.view.batchimport.ImportStudentFull;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.common.PageResponse;
import com.synlabs.ipsaa.view.fee.FeeReportRequest;
import com.synlabs.ipsaa.view.fee.SaveFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.SlipEmailRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeLedgerRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse2;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse3;
import com.synlabs.ipsaa.view.student.ParentRequest;
import com.synlabs.ipsaa.view.student.SharingSheetEntryRequest;
import com.synlabs.ipsaa.view.student.SharingSheetRequest;
import com.synlabs.ipsaa.view.student.StudentFilterRequest;
import com.synlabs.ipsaa.view.student.StudentPageResponse;
import com.synlabs.ipsaa.view.student.StudentRequest;
import com.synlabs.ipsaa.view.student.StudentResponse;
import com.synlabs.ipsaa.view.student.StudentSummaryResponse;

import freemarker.template.TemplateException;

@Service
public class StudentService extends BaseService {

	@Value("${ipsaa.export.directory}")
	private String exportDirectory;

	@Autowired
	private FileStore fileStore;

	@Autowired
	private CommunicationService communicationService;

	@Autowired
	private StudentParentRepository studentParentRepository;

	@Autowired
	private ExcelImportService excelImportService;

	@Autowired
	private StudentProfileRepository studentProfileRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ProgramGroupRepository programGroupRepository;

	@Autowired
	private CenterRepository centerRepository;

	@Autowired
	private StudentRepository repository;

	@Autowired
	private StudentFeeRepository feeRepository;

	@Autowired
	private StudentFeePaymentRecordRepository paymentRecordRepository;

	@Autowired
	private StudentFeePaymentRepository feePaymentRepository;

	@Autowired
	private AdmissionNumberSequenceRepository admissionNumberSequenceRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SharingSheetRepository sharingSheetRepository;

	@Autowired
	private SharingSheetEntryRepository sharingSheetEntryRepository;

	@Autowired
	private ParentSharingSheetRepository parentSheetRepository;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private CenterProgramFeeRepository centerProgramFeeRepository;

	@Autowired
	private StudentFeeService studentFeeService;

	@Autowired
	private StudentFeeRepository studentFeeRepository;

	@Value("${ipsaa.hdfc.payment.baseurl}")
	private String paymentBaseUrl;

	private final double FEE_CALCULATION_TOLERANCE = 1.0;

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private PasswordEncoder encoder = new BCryptPasswordEncoder();

	private List<Center> getCenters(StudentFilterRequest request) {
		List<Center> usercenters = getUserCenters();
		if (!StringUtils.isEmpty(request.getCenterCode())) {
			return usercenters.stream().filter(c -> c.getCode().equals(request.getCenterCode()))
					.collect(Collectors.toList());
		}

		return usercenters;
	}

	public PageResponse<StudentSummaryResponse> list(StudentFilterRequest request) {

		List<Center> centers = getCenters(request);

		JPAQuery<Student> query = new JPAQuery<>(entityManager);
		QStudent student = QStudent.student;

		query.select(student).from(student).where(student.center.in(centers))
				.where(student.active.eq(request.getActive()));

		if (!StringUtils.isEmpty(request.getProgramCode()) && !request.getProgramCode().equals("ALL")) {
			query.where(student.program.code.eq(request.getProgramCode()));
		}

		query.orderBy(student.admissionNumber.asc());
		List<Student> students;
		int pageSize;
		int pageNumber;
		int pageCount;
		if (request.getPageSize() != null && request.getPageSize() != 0) {
			long count = query.fetchCount();
			pageSize = request.getPageSize();
			pageNumber = request.getPageNumber();
			pageCount = (int) Math.ceil(count * 1.0 / pageSize);
			students = query.limit(request.getPageSize()).offset(request.getPageSize() * (request.getPageNumber() - 1))
					.fetch();
		} else {
			students = query.fetchAll().fetch();
			pageSize = students.size();
			pageNumber = 1;
			pageCount = 1;
		}
		List<StudentSummaryResponse> list = students.stream().map(StudentSummaryResponse::new)
				.collect(Collectors.toList());
		PageResponse<StudentSummaryResponse> response = new StudentPageResponse(pageSize, pageNumber, pageCount, list);
		return response;
	}

	public StudentResponse getStudent(StudentRequest request) {
		Student student=studentRepository.findOne(request.getId());
		StudentResponse response=null;
		if(student!=null){
			StudentFee fee=studentFeeRepository.findByStudentId(request.getId());
			if(fee!=null){
				response = new StudentResponse(fee);
			}else{
				response = new StudentResponse(student);
			}
			if (!StringUtils.isEmpty(student.getProfile().getImagePath())) {
				response.setStudentImageData(getStudentImageData(student));
			}
		}
		return response;
	}

	public List<StudentFee> listFee(StudentFeeRequest request) {
		QStudentFee fee = QStudentFee.studentFee;
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		query.select(fee).from(fee).where(fee.student.active.isTrue()).where(fee.feeDuration.eq(FeeDuration.Quarterly))
				.where(fee.student.corporate.isFalse());

		List<StudentFee> fees;
		if (request.getStudentId() != null) {
			return query.where(fee.student.id.eq(request.getStudentId())).fetch();
		}

		if (request.getCenterId() != null) {
			fees = query.where(fee.student.center.id.eq(request.getCenterId())).fetch();
			// to ajust chnages
			//ajustChnages();
			//adjustGst();
			//addSecurity();

			return fees;
		}
		fees = query.where(fee.student.center.in(getUserCenters())).fetch();
		return fees;
	}


	@Transactional
	private void addSecurity() {
			String SAMPLE_XLSX_FILE_PATH ="C:/Users/shubham/Desktop/ipsaa/query/duplicate.xlsx";
			try{
				File file = new File(SAMPLE_XLSX_FILE_PATH);
				FileInputStream inputStream = new FileInputStream(file);
				Workbook workbook = WorkbookFactory.create(inputStream);
				workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
				Sheet sheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = sheet.iterator();
				while (iterator.hasNext()) {

					Row currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();

					Cell currentCell = cellIterator.next();
					if(currentCell.getStringCellValue().equals("Merchant Name"))
						continue;
					currentCell = cellIterator.next();
					Student student =studentRepository.findByAdmissionNumber(currentCell.getStringCellValue());
					if(student!=null){
						StudentFee fee=studentFeeRepository.findByStudent(student);
						if(fee!=null){
							fee.setDepositFeeDiscount(new BigDecimal(0));
							fee.setDepositFee(new BigDecimal(0));
							fee.setFinalDepositFee(new BigDecimal(0));
							fee.setFinalFee(fee.getFinalFee().add(fee.getDepositFee()));
						}
						System.out.println(student);
						studentRepository.save(student);
					}else {
						System.out.println("center not found"+currentCell.getStringCellValue());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
	}

	@Transactional
	private void adjustGst() {
		QStudentFee fee = QStudentFee.studentFee;
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		query.select(fee).from(fee)
				.where(fee.student.active.isTrue())
				.where(fee.feeDuration.eq(FeeDuration.Quarterly))
				.where(fee.student.corporate.isFalse());

		for (StudentFee studentFee:query.fetch() ){
			if(studentFee.getStudent().getProgram().getId()==11) {
				CenterProgramFee centerProgramFee=centerProgramFeeRepository.findByProgramIdAndCenterId(studentFee.getStudent().getProgram().getId(),studentFee.getStudent().getCenter().getId());
				if(centerProgramFee==null){
					throw new NotFoundException("Center program fee not found ");
				}
				studentFee.getStudent().setFormalSchool(true);
				studentFee.getStudent().setSchoolName("Formal School Name yet to be added");
				studentFee.setIgst(new BigDecimal(18));
				studentFee.setGstAmount(studentFee.getFinalBaseFee().multiply(new BigDecimal(0.18)));
				studentFeeRepository.saveAndFlush(studentFee);
				System.out.println(studentFee.getStudent().getName());
				}
			}
		}

	@Transactional
	private void ajustChnages() {
		//  code for adjust new fee changes
		QStudentFee fee = QStudentFee.studentFee;
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		query.select(fee).from(fee)
				.where(fee.student.active.isTrue())
				.where(fee.feeDuration.eq(FeeDuration.Quarterly))
				.where(fee.student.corporate.isFalse());

		for (StudentFee studentFee:query.fetch() ){
			BigDecimal adjust=ZERO;
			if(studentFee.getAdjust()!=null)
				adjust=studentFee.getAdjust().multiply(THREE);
			else
				studentFee.setAdjust(ZERO);

			if(studentFee.getDiscount()!=null && studentFee.getDiscount().intValue()>0)
				studentFee.setBaseFeeDiscount(studentFee.getDiscount());
			studentFee.setFinalBaseFee(FeeUtilsV2.calculateDiscountAmount(studentFee.getBaseFee(),studentFee.getBaseFeeDiscount()));

			studentFee.setBaseFeeDiscount(FeeUtilsV2.calculateDiscount(studentFee.getBaseFee(),studentFee.getFinalBaseFee().add(studentFee.getAdjust())));
			studentFee.setFinalFee(studentFee.getFinalFee().add(adjust));

			studentFeeService.saveStudentFee(studentFee);
			System.out.println(studentFee.getStudent().getName());
			//studentFeeRepository.save(studentFee);
		}
	}

	public List<StudentFeePaymentRequest> listFeeLedger(StudentFeeLedgerRequest request) {
		return feePaymentRepository.findByStudentId(request.getStudentId());
	}

	private void validateStudentRequest(StudentRequest request) {
		if (!Gender.matches(request.getGender())) {
			throw new ValidationException("Missing gender");
		}
		if (!FamilyType.matches(request.getFamilyType())) {
			throw new ValidationException("Missing family type");
		}
		if (StringUtils.isEmpty(request.getFirstName())) {
			throw new ValidationException("Missing First Name");
		}
		if (StringUtils.isEmpty(request.getDob())) {
			throw new ValidationException("Missing Date of Birth");
		}
		if (StringUtils.isEmpty(request.getAdmissionDate())) {
			throw new ValidationException("Missing Date of Admission");
		}
		if (StringUtils.isEmpty(request.getProfile())) {
			throw new ValidationException("Missing Profile");
		}
		if (request.getCenterId() == null || request.getCenterId() == 0L) {
			throw new ValidationException("Missing center");
		}
		if (request.getProgramId() == null || request.getProgramId() == 0L) {
			throw new ValidationException("Missing program");
		}
		if (request.getGroupId() == null || request.getGroupId() == 0L) {
			throw new ValidationException("Missing group");
		}
	}

	private boolean isEmptyOrNa(String string) {
		return StringUtils.isEmpty(string) || string.equalsIgnoreCase("na");
	}

	public byte[] generateStudentPdf(StudentResponse student) throws IOException, DocumentException, TemplateException,
			InterruptedException, ParseException, com.itextpdf.text.DocumentException {
		return documentService.generateStudentPdf(student);
	}

	@Transactional
	public StudentResponse saveStudent(StudentRequest request) {
		validateStudentRequest(request);

		Center center = centerRepository.findOne(request.getCenterId());
		Program program = programRepository.findOne(request.getProgramId());
		ProgramGroup programGroup = programGroupRepository.findOne(request.getGroupId());
		if (!program.getGroups().contains(programGroup)) {
			throw new ValidationException("Program group mismatch!");
		}

		Student student;
		try {
			request.setActive(true);
			student = request.toEntity();
		} catch (ParseException e) {
			throw new ValidationException("Invalid date");
		}
		StudentProfile studentProfile = student.getProfile();
		student.setAdmissionNumber(generateAdmissionNumber(center.getCode()));
		student.setCenter(center);
		student.setProgram(program);
		student.setGroup(programGroup);
		student.setProfile(studentProfile);
		student.setApprovalStatus(ApprovalStatus.NewApproval);

		Student sibling = null;
		if (request.getSiblingId() != null) {
			sibling = studentRepository.findOne(request.getSiblingId());
			if (sibling == null) {
				throw new ValidationException(String.format("Sibling[id=%s] not Found", request.getMaskedSiblingId()));
			}
			List<StudentParent> parents = sibling.getParents();
			student.setApprovalStatus(ApprovalStatus.NewApproval);
			student.setParents(new ArrayList<>(parents));
			studentProfileRepository.saveAndFlush(student.getProfile());
			studentRepository.saveAndFlush(student);
			for (StudentParent parent : parents) {
				parent.getStudents().add(student);
			}
			StudentResponse response = new StudentResponse(student);

			if (request.getFee() != null && !request.getFee().isEmpty()) {
				request.getFee().setStudentId(mask(student.getId()));
				request.getFee().setCenterId(mask(center.getId()));
				StudentFee slip = studentFeeService.saveStudentFee(request.getFee());
				studentFeeService.generateFirstFeeSlip(slip.getId());
				// feeRepository.saveAndFlush(studentFee);
			}

			communicationService.sendStudentApprovalEmail(student);
			return response;
		}

		List<Address> allAddresses = student.getAllAddresses();
		if (allAddresses.size() < 1) {
			throw new ValidationException("Invalid address");
		}
		for (Address address : allAddresses) {
			address.setState(isEmptyOrNa(address.getState()) ? center.getAddress().getState() : address.getState());
			address.setCity(isEmptyOrNa(address.getCity()) ? center.getAddress().getCity() : address.getCity());
			address.setZipcode(
					isEmptyOrNa(address.getZipcode()) ? center.getAddress().getZipcode() : address.getZipcode());
		}

		studentProfileRepository.saveAndFlush(studentProfile);
		List<StudentParent> parents = student.getParents();
		for (StudentParent parent : parents) {
			parent.getStudents().add(student);
			parent.setAccount(false);
			parent.setEmailEnabled(false);
			parent.setSmsEnabled(false);
			studentParentRepository.saveAndFlush(parent);
		}
		studentRepository.saveAndFlush(student);
		communicationService.sendStudentApprovalEmail(student);

		StudentResponse response = new StudentResponse(student);

		if (request.getFee() != null && !request.getFee().isEmpty()) {
			request.getFee().setCenterId(mask(center.getId()));
			request.getFee().setStudentId(mask(student.getId()));
			StudentFee slip = studentFeeService.saveStudentFee(request.getFee());
			studentFeeService.generateFirstFeeSlip(slip.getId());
			// feeRepository.saveAndFlush(studentFee);
		}

		return response;
	}

	@Transactional
	public Student updateStudent(StudentRequest request) throws ParseException {
		Student dbStudent = studentRepository.findOne(request.getId());
		if (dbStudent == null) {
			throw new NotFoundException(String.format("Cannot locate student with id %s", request.getId()));
		}
		Center center = centerRepository.findOne(request.getCenterId());
		Program program = programRepository.findOne(request.getProgramId());
		ProgramGroup programGroup = programGroupRepository.findOne(request.getGroupId());
		if (center == null) {
			throw new NotFoundException(String.format("Cannot locate center with id %s", request.getCenterId()));
		}
		if (program == null) {
			throw new NotFoundException(String.format("Cannot locate program with id %s", request.getProgramId()));
		}
		if (programGroup == null) {
			throw new NotFoundException(String.format("Cannot locate group with id %s", request.getGroupId()));
		}

		updateParents(dbStudent, request);

		boolean feeChange = !(dbStudent.getProgram().equals(program) && dbStudent.getCenter().equals(center));
		if(feeChange){
			request.getFee().setProgramChange(true);
		   Set<String> privileges= getUser().getPrivileges();
		   List<Role> roles=getUser().getRoles();
        }

		// check chnage in addmision date and validate it
        Calendar cal=Calendar.getInstance();
        if(!dbStudent.getProfile().getAdmissionDate().equals(request.parseDate(request.getAdmissionDate()))){
            cal.setTime(request.parseDate(request.getAdmissionDate()));
            if(FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH)+1)!=FeeUtilsV2.getQuarter() || cal.get(Calendar.YEAR)!= java.time.LocalDate.now().getYear()){
                throw new ValidationException("Admission date can not be set to before or after running quarter");
            }
        }

        dbStudent = request.toEntity(dbStudent);
		dbStudent.setGroup(programGroup);
		dbStudent.setProgram(program);
		dbStudent.setCenter(center);

//
//		boolean isFormalChange=(dbStudent.isFormalSchool() ^ request.isFormalSchool());
//		boolean isCorporateChange=(dbStudent.isCorporate() ^ request.isCorporate());

			if(!request.isCorporate()){
				request.getFee().setStudentId(mask(dbStudent.getId()));
				studentFeeService.updateStudentFee(request.getFee());
			}

//		if (feeChange) {
//			CenterProgramFee centerFee = centerProgramFeeRepository.findByProgramIdAndCenterId(program.getId(),
//					center.getId());
//			StudentFee oldStudentFee = studentFeeRepository.findByStudent(dbStudent);
//
//			if (oldStudentFee != null) {
//				if (centerFee != null) {
//					oldStudentFee.setBaseFee(new BigDecimal(centerFee.getFee()));
//					oldStudentFee.setCgst(centerFee.getCgst());
//					oldStudentFee.setIgst(centerFee.getIgst());
//					oldStudentFee.setSgst(centerFee.getSgst());
//					oldStudentFee.setFinalFee(FeeUtils.calculateFinalFee(oldStudentFee));
//					studentFeeRepository.saveAndFlush(oldStudentFee);
//				} else {
//					studentFeeRepository.delete(oldStudentFee.getId());
//				}
//			}
//		}
		studentRepository.saveAndFlush(dbStudent);
		return dbStudent;
	}

	private void updateParents(Student dbStudent, StudentRequest request) {
		List<ParentRequest> parents = request.getParents();
		for (ParentRequest parentRequest : parents) {
			if (!Relationship.matches(parentRequest.getRelationship())) {
				throw new ValidationException(
						String.format("Invalid Relationship[%s]", parentRequest.getRelationship()));
			}
			StudentParent parent = dbStudent.getParent(Relationship.valueOf(parentRequest.getRelationship()));
			if (parent != null) {
				if (parent.isAccount()) {
					User user = userRepository.findByEmailAndActiveTrue(parentRequest.getEmail());
					if (user != null && user.getParent() != parent) {
						throw new ValidationException(
								String.format("User already exist with email[%s]", parentRequest.getEmail()));
					}
					if (user == null) {
						user = userRepository.findOneByParent(parent);
					}
					user.setCenters(null);
					user.setEmail(parentRequest.getEmail());
					user.setFirstname(parentRequest.getFirstName());
					user.setLastname(parentRequest.getLastName());
					user.setPhone(parentRequest.getMobile());
					user.setUserType(UserType.Parent);
					user.setEmployee(null);
					user.setParent(parent);
					userRepository.saveAndFlush(user);
				}
				parentRequest.toEntity(parent);
				if (!parent.isAccount()) {
					parent.setEmailEnabled(false);
					parent.setSmsEnabled(false);
				}
				studentParentRepository.saveAndFlush(parent);
			}
		}
	}

	@Transactional
	public void resetParentPassword(ParentRequest request) {
		StudentParent parent = studentParentRepository.findOne(request.getId());
		if (parent == null) {
			throw new NotFoundException(String.format("Parent not found [%s]", request.getMaskedId()));
		}

		User user = userRepository.findByEmail(parent.getEmail());
		if (user == null) {
			throw new NotFoundException(String.format("User not found [%s]", parent.getEmail()));
		}
		if (!user.isActive()) {
			throw new ValidationException(String.format("User is inactive[%s]", parent.getEmail()));
		}

		String password = RandomStringUtils.randomAlphanumeric(8);
		user.setPasswordHash(encoder.encode(password));
		userRepository.saveAndFlush(user);
		communicationService.sendPasswordEmailAndSms(parent, password);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Transactional
	public void deleteStudent(StudentRequest request) {
		Student student = studentRepository.findOne(request.getId());
		if (student == null) {
			throw new NotFoundException(String.format("Cannot locate student with id %s", request.getId()));
		}
		StudentFeePaymentRequest slip=studentFeeService.getStudentBalance(student);
		if(slip!=null)
		if( (slip.getBalance()!=null && slip.getBalance().intValue()>0) || !slip.getPaymentStatus().equals(PaymentStatus.Paid) ){
			throw new ValidationException(
					String.format("Some balance fee is remaining of student [%s]", student.getName()));
		}

		student.setActive(false);
		studentRepository.saveAndFlush(student);
		logger.info("Student deactivated "+ student.getName());
		List<StudentParent> parents = student.getParents();
		for (StudentParent parent : parents) {
			if (parent.isAccount()) {
				List<Student> students = parent.getStudents();
				boolean flag = true;
				for (Student s : students) {
					if (s.isActive()) {
						flag = false;
						break;
					}
				}
				if (flag) {
					User user = userRepository.findOneByParent(parent);
					user.setActive(false);
					userRepository.saveAndFlush(user);
				}
			}
		}
		student.getCenter().getName();
		student.getProgram().getName();
		student.getGroup().getName();
		student.getProfile().getFullName();
		communicationService.sendStudentDeleteEmail(student);
	}

	public StudentFee getStudentFee(StudentRequest request) {
		return feeRepository.findByStudentId(request.getId());
	}

	private String generateAdmissionNumber(String centerCode) {
		AdmissionNumberSequence sequence = admissionNumberSequenceRepository.getOneByCenterCode(centerCode)
				.orElse(new AdmissionNumberSequence(centerCode));
		admissionNumberSequenceRepository.saveAndFlush(sequence);
		return String.format("%s/%03d", centerCode, sequence.getNext());
	}

	@Transactional
	public Map<String, String> importRecords(MultipartFile file, String dryRun) throws Exception {
		boolean errorInFile = false;
		String validationMessage;
		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("error", "false");
		List<StudentParent> allparentlist = new ArrayList<>();
		Map<String, ArrayList> studentRecords = excelImportService.importStudentRecords(file);
		ArrayList<ImportStudentFull> importStudentsList = studentRecords.get("students");
		// ArrayList<ImportParent> importParentsList = studentRecords.get("parents");
		List<Student> studentList = new ArrayList<>(importStudentsList.size());

		for (ImportStudentFull importStudent : importStudentsList) {
			validationMessage = importStudent.validate();
			if (validationMessage.equalsIgnoreCase("OK")) {
				if (programRepository.findByCode(importStudent.getProgramCode()) == null) {
					validationMessage = "No program found.";
					errorInFile = true;
				} else if (centerRepository.findByCode(importStudent.getCenterCode()) == null) {
					validationMessage = "No center found.";
					errorInFile = true;
				} else if (programGroupRepository.findByName(importStudent.getGroupName()) == null) {
					validationMessage = "No program group.";
					errorInFile = true;
				}
			} else {
				errorInFile = true;
			}
			statusMap.put(importStudent.getFullName(), validationMessage);
		}

		if (!errorInFile && dryRun.equalsIgnoreCase("false")) {
			List<StudentProfile> studentProfiles = new ArrayList<>();

			for (ImportStudentFull importStudent : importStudentsList) {

				List<StudentParent> studentParentList = new ArrayList<>();
				Center center = centerRepository.findByCode(importStudent.getCenterCode());
				Pair<StudentParent, StudentParent> parents = convertImportParentToStudentParent(center, importStudent);
				studentParentList.add(parents.getLeft());
				studentParentList.add(parents.getRight());
				allparentlist.add(parents.getLeft());
				allparentlist.add(parents.getRight());
				Student student = convertImportStudentToStudent(importStudent, studentParentList);
				student.setAdmissionNumber(generateAdmissionNumber(importStudent.getCenterCode()));

				if (student.getParents() != null && student.getParents().size() > 0) {
					for (StudentParent studentParent : student.getParents()) {
						studentParent.getStudents().add(student);
					}
				}
				studentList.add(student);
				studentProfiles.add(student.getProfile());
			}
			studentProfileRepository.save(studentProfiles);
			studentRepository.save(studentList);
			studentParentRepository.save(allparentlist);
		}
		if (errorInFile) {
			statusMap.put("error", "true");
		}
		return statusMap;
	}

	private Pair<StudentParent, StudentParent> convertImportParentToStudentParent(Center center,
			ImportStudentFull importstudent) {

		if (StringUtils.isEmpty(importstudent.getCity())) {
			importstudent.setCity(center.getAddress().getCity());
		}

		if (StringUtils.isEmpty(importstudent.getState())) {
			importstudent.setState(center.getAddress().getState());
		}

		if (StringUtils.isEmpty(importstudent.getZipCode())) {
			importstudent.setZipCode(center.getAddress().getZipcode());
		}

		// mother
		StudentParent mother = new StudentParent();
		mother.setFirstName(importstudent.getMotherFirstName());
		mother.setLastName(importstudent.getMotherLastName());
		mother.setMobile(importstudent.getMotherMobile());
		mother.setEmail(importstudent.getMotherEmail());
		mother.setRelationship(Relationship.Mother);
		mother.setEducationalQualification(importstudent.getMotherQualification());
		mother.setOccupation(importstudent.getMotherOccupation());
		mother.setOrganisation(importstudent.getMotherCompany());

		Address address = new Address();
		address.setAddress(importstudent.getAddress());
		address.setCity(importstudent.getCity());
		address.setState(importstudent.getState());
		address.setZipcode(importstudent.getZipCode());
		address.setPhone(importstudent.getMotherMobile());
		address.setAddressType(AddressType.Home);
		mother.setResidentialAddress(address);

		if (!StringUtils.isEmpty(importstudent.getMotherWorkAddress())) {
			address = new Address();
			address.setAddress(importstudent.getMotherWorkAddress());
			address.setCity(importstudent.getCity());
			address.setState(importstudent.getState());
			address.setZipcode(importstudent.getZipCode());
			address.setPhone(importstudent.getMotherMobile());
			address.setAddressType(AddressType.Office);
			mother.setOfficeAddress(address);
		}

		// father
		StudentParent father = new StudentParent();
		father.setFirstName(importstudent.getFatherFirstName());
		father.setLastName(importstudent.getFatherLastName());
		father.setMobile(importstudent.getFatherMobile());
		// father.setSecondaryNumbers(importstudent.getSecondaryNumbers());
		father.setEmail(importstudent.getFatherEmail());
		father.setRelationship(Relationship.Father);
		father.setEducationalQualification(importstudent.getFatherQualification());
		father.setOccupation(importstudent.getFatherOccupation());
		father.setOrganisation(importstudent.getFatherCompany());
		// father.setDesignation(importstudent.getFatherDesignation());
		// father.setEmergencyContact(importstudent.isEmergencyContact());

		Address address1 = new Address();
		address1.setAddress(importstudent.getAddress());
		address1.setCity(importstudent.getCity());
		address1.setState(importstudent.getState());
		address1.setZipcode(importstudent.getZipCode());
		address1.setPhone(importstudent.getFatherMobile());
		address1.setAddressType(AddressType.Home);
		father.setResidentialAddress(address1);

		if (!StringUtils.isEmpty(importstudent.getFatherWorkAddress())) {
			address = new Address();
			address.setAddress(importstudent.getFatherWorkAddress());
			address.setCity(importstudent.getCity());
			address.setState(importstudent.getState());
			address.setZipcode(importstudent.getZipCode());
			address.setPhone(importstudent.getFatherMobile());
			address.setAddressType(AddressType.Office);
			father.setOfficeAddress(address);
		}

		return Pair.of(mother, father);
	}

	private Student convertImportStudentToStudent(ImportStudentFull importStudent, List<StudentParent> parents) {
		Student student = new Student();
		student.setAdmissionNumber(importStudent.getAdmissionNumber());
		student.setCenter(centerRepository.findByCode(importStudent.getCenterCode()));
		student.setProgram(programRepository.findByCode(importStudent.getProgramCode()));
		student.setGroup(programGroupRepository.findByName(importStudent.getGroupName()));
		student.setActive(importStudent.isActive());
		student.setParents(parents);
		student.setExpectedIn(importStudent.getExpectedIn());
		student.setExpectedOut(importStudent.getExpectedOut());
		StudentProfile studentProfile = new StudentProfile();
		studentProfile.setAdmissionDate(importStudent.getAdmissionDate());
		studentProfile.setFirstName(importStudent.getFirstName());
		studentProfile.setLastName(importStudent.getLastName());
		studentProfile.setNickName(importStudent.getNickName());
		studentProfile.setDob(importStudent.getDob());
		studentProfile.setGender(Gender.valueOf(importStudent.getGender()));
		studentProfile.setNationality(importStudent.getNationality());
		studentProfile.setBloodGroup(importStudent.getBloodGroup());
		studentProfile.setProfile(importStudent.getProfile());
		studentProfile.setFamilyType(FamilyType.valueOf(importStudent.getFamilyType()));
		student.setProfile(studentProfile);
		student.setApprovalStatus(ApprovalStatus.Approved);
		return student;
	}

	/**
	 * Saves Student Fee in StudentFee entity, throws ValidationException if final
	 * fee in request is not matched with calculation in server from baseFee anf
	 * discount in request throws NotFoundException if request student not found in
	 * database.
	 *
	 * @param request
	 *            StudentFeeRequest for saving StudentFee
	 * @return saved {@code StudentFee}
	 */
	public StudentFee saveStudentFee(StudentFeeRequest request) {
		if (request.getBaseFee() == null) {
			throw new ValidationException("Base Fee not found");
		}
		if (request.getDiscount() == null) {
			throw new ValidationException("Discount not found");
		}
		if (request.getFinalFee() == null) {
			throw new ValidationException("Final Fee not found");
		}

		Student student = studentRepository.findOne(request.getStudentId());
		if (student == null) {
			throw new NotFoundException(String.format("Student not fount [%s]", request.getMaskedStudentId()));
		}

		StudentFee studentFee = studentFeeRepository.findByStudent(student);
		if (studentFee != null) {
			throw new ValidationException(String.format("Student fee already exits for student[admission_number=%s]",
					student.getAdmissionNumber()));
		}

		studentFee = request.toEntity(null);

		FeeUtils.validateStudentFee(studentFee);

		studentFee.setStudent(student);
		return studentFeeRepository.saveAndFlush(studentFee);
	}

	/**
	 * Updates {@code StudentFee} of Request having id (masked)
	 *
	 * @param request
	 *            StudentFeeRequest with id(Masked) to update
	 * @return updated {@code StudentFee}
	 */
	public StudentFee updateStudentFee(StudentFeeRequest request) {
		if (request.getBaseFee() == null) {
			throw new ValidationException("Base Fee not found");
		}
		if (request.getDiscount() == null) {
			throw new ValidationException("Discount not found");
		}
		if (request.getFinalFee() == null) {
			throw new ValidationException("Final Fee not found");
		}
		StudentFee studentFee = studentFeeRepository.findOne(request.getId());
		if (studentFee == null) {
			throw new NotFoundException(String.format("Student Fee not found [%s]", request.getMaskedId()));
		}

		studentFee = request.toEntity(studentFee);

		FeeUtils.validateStudentFee(studentFee);

		return studentFeeRepository.saveAndFlush(studentFee);
	}

	/**
	 * List fee slips and payments based on the parameters
	 *
	 * @param request
	 *            filter with center, year, quarter, month and period
	 * @return list of fee slips and payments for selected students, year, quarter
	 *         or month
	 */
	public List<StudentFeePaymentRequest> listFeeSlips(StudentFeeSlipRequest request) {

		FeeDuration period = FeeDuration.valueOf(request.getPeriod());
		QStudentFee qStudentFee = QStudentFee.studentFee;
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		query.select(qStudentFee).from(qStudentFee).where(qStudentFee.feeDuration.eq(period))
				.where(qStudentFee.student.approvalStatus.eq(ApprovalStatus.Approved))
				.where(qStudentFee.student.active.isTrue()).where(qStudentFee.student.corporate.isFalse())
				.where(qStudentFee.student.center.code.eq(request.getCenterCode()));
		List<StudentFee> feelist = query.fetch();

		List<StudentFeePaymentRequest> allslips = new LinkedList<>();
		int requestMonth = request.getMonth();
		int requestQuarter = request.getQuarter();
		int requestYear = request.getYear();

		for (StudentFee fee : feelist) {

			StudentFeePaymentRequest slip;
			switch (request.getPeriod()) {
			case "Monthly":
				slip = feePaymentRepository.findOneByStudentAndFeeDurationAndMonthAndYear(fee.getStudent(), period,
						requestMonth, requestYear);
				if (slip != null) {
					allslips.add(slip);
				}
				break;
			case "Quarterly":
				slip = feePaymentRepository.findOneByStudentAndFeeDurationAndQuarterAndYear(fee.getStudent(), period,
						requestQuarter, requestYear);
				if (slip != null) {
					allslips.add(slip);
				}
				break;
			case "Yearly":
				slip = feePaymentRepository.findOneByStudentAndFeeDurationAndYear(fee.getStudent(), period,
						requestYear);
				if (slip != null) {
					allslips.add(slip);
				}
				break;
			}
		}
		return allslips;
	}

	// shubham
	public List<StudentFeePaymentRequest> listFeeSlips2(StudentFeeSlipRequest request) {
		FeeDuration period = FeeDuration.valueOf(request.getPeriod());
		if (!request.getCenterCode().equals("All")) {
			int requestQuarter = request.getQuarter();
			int requestYear = request.getYear();
			return feePaymentRepository
					.findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode( period, requestQuarter, requestYear, request.getCenterCode());
		} else {
			int requestQuarter = request.getQuarter();
			int requestYear = request.getYear();
			return feePaymentRepository
					.findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(
							 period, requestQuarter, requestYear);
		}
	}

	public List<StudentFeeSlipResponse2> listFeeSlipsTable2(StudentFeeSlipRequest request) {

		List<StudentFeePaymentRequest> slips;
		FeeDuration period = FeeDuration.valueOf(request.getPeriod());
		if (request.getCenterCode().equals("All")) {
			slips = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(
							 period, request.getQuarter(), request.getYear());
		} else {
			slips = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(
							 period, request.getQuarter(), request.getYear(),
							request.getCenterCode());
		}

		if (slips != null && request.getConfirm() != null && request.getConfirm()) {
			for (StudentFeePaymentRequest slip : slips) {
				List<StudentFeePaymentRecord> result = slip.getPayments().stream()
						.filter(confirm -> confirm.getConfirmed() == true).collect(Collectors.toList()); // convert list
																											// to stream
				slip.setPayments(result);
			}
		} else if (slips != null && request.getConfirm() != null && !request.getConfirm()) {
			for (StudentFeePaymentRequest slip : slips) {
				List<StudentFeePaymentRecord> result = slip.getPayments().stream()
						.filter(confirm -> confirm.getConfirmed() == false).collect(Collectors.toList()); // convert
																											// list to
																											// stream
				slip.setPayments(result);
			}
		}
		return slips.stream().filter(payments -> payments.getPayments().size() > 0).map(StudentFeeSlipResponse2::new)
				.collect(Collectors.toList());
	}

	/**
	 * List fee slips and payments based on the parameters, if the slip is not yet
	 * generated then generate the slip
	 *
	 * @param request
	 *            filter with center, year, quarter, month and period
	 * @return list of fee slips and payments for selected students, year, quarter
	 *         or month
	 */
	@Transactional
	public List<StudentFeePaymentRequest> generateFeeSlips(StudentFeeSlipRequest request) {

		FeeDuration period = FeeDuration.valueOf(request.getPeriod());
		LocalDate today = LocalDate.now();
		// 1. list students (with fee)
		QStudentFee qStudentFee = QStudentFee.studentFee;
		JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
		query.select(qStudentFee).from(qStudentFee).where(qStudentFee.feeDuration.eq(period))
				.where(qStudentFee.student.approvalStatus.eq(ApprovalStatus.Approved))
				.where(qStudentFee.student.active.isTrue()).where(qStudentFee.student.corporate.isFalse())
				.where(qStudentFee.student.center.code.eq(request.getCenterCode()));
		List<StudentFee> feelist = query.fetch();

		List<StudentFeePaymentRequest> newslips = new LinkedList<>();
		List<StudentFeePaymentRequest> allslips = new LinkedList<>();

		int requestMonth = request.getMonth();
		int requestQuarter = request.getQuarter();
		int requestYear = request.getYear();

		// 2. locate their payment entry which is also fee slip for us
		switch (request.getPeriod()) {
		case "Monthly":
			// TODO date check to make sure fee slips are only generated += 15 days from
			// start of month
			validateMonth(request);
			for (StudentFee fee : feelist) {
				StudentFeePaymentRequest slip = feePaymentRepository.findOneByStudentAndFeeDurationAndMonthAndYear(
						fee.getStudent(), period, requestMonth, requestYear);
				if (slip == null) {
					BigDecimal baseFee = fee.getFinalFee();
					slip = new StudentFeePaymentRequest();
					slip.setStudent(fee.getStudent());
					slip.setBaseFee(baseFee);
					slip.setFeeDuration(fee.getFeeDuration());
					slip.setInvoiceDate(today.toDate());
					slip.setMonth(requestMonth);
					slip.setYear(requestYear);
					slip.setPaymentStatus(PaymentStatus.Raised);
					slip.setTotalFee(baseFee);
					slip.setReGenerateSlip(true);
					slip.setExtraCharge(ZERO);
					slip.setLatePaymentCharge(ZERO);
					slip.setCgst(fee.getCgst());
					slip.setSgst(fee.getSgst());
					slip.setIgst(fee.getIgst());
					if (requestMonth == 1) {
						CenterProgramFee programFee = centerProgramFeeRepository.findByProgramIdAndCenterId(
								slip.getStudent().getProgram().getId(), slip.getStudent().getCenter().getId());
						if (programFee == null) {
							continue;
						}
						BigDecimal annualFee = BigDecimal.valueOf(programFee.getAnnualFee());
						slip.setAnnualFee(annualFee);
					}
					slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
					newslips.add(slip);
				}
				allslips.add(slip);
			}
			break;
		case "Quarterly":
			// TODO date check to make sure fee slips are only generated += 15 days from
			// start of quarter
			validateQuarter(request);
			for (StudentFee fee : feelist) {
				StudentFeePaymentRequest slip = feePaymentRepository.findOneByStudentAndFeeDurationAndQuarterAndYear(
						fee.getStudent(), period, requestQuarter, requestYear);
				if (slip == null) {
					BigDecimal baseFee = fee.getFinalFee();
					slip = new StudentFeePaymentRequest();
					slip.setStudent(fee.getStudent());
					slip.setBaseFee(baseFee);
					slip.setFeeDuration(fee.getFeeDuration());
					slip.setInvoiceDate(today.toDate());
					slip.setYear(requestYear);
					slip.setPaymentStatus(PaymentStatus.Raised);
					slip.setQuarter(requestQuarter);
					slip.setTotalFee(baseFee);
					slip.setReGenerateSlip(true);
					slip.setExtraCharge(ZERO);
					slip.setLatePaymentCharge(ZERO);
					slip.setAnnualFee(ZERO);
					slip.setCgst(fee.getCgst());
					slip.setSgst(fee.getSgst());
					slip.setIgst(fee.getIgst());

					if (requestQuarter == 2) {
						CenterProgramFee programFee = centerProgramFeeRepository.findByProgramIdAndCenterId(
								slip.getStudent().getProgram().getId(), slip.getStudent().getCenter().getId());
						if (programFee == null) {
							continue;
						}
						BigDecimal annualFee = BigDecimal.valueOf(programFee.getAnnualFee());
						slip.setAnnualFee(annualFee);
					}
					calculateBaseFee(slip);
					slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
					newslips.add(slip);
				}
				allslips.add(slip);
			}
			break;
		case "Yearly":
			// TODO date check to make sure fee slips are only generated += 15 days from
			// start of year
			validateYear(request);
			for (StudentFee fee : feelist) {
				StudentFeePaymentRequest slip = feePaymentRepository
						.findOneByStudentAndFeeDurationAndYear(fee.getStudent(), period, requestYear);
				if (slip == null) {
					slip = new StudentFeePaymentRequest();
					slip.setStudent(fee.getStudent());
					slip.setBaseFee(fee.getFinalFee());
					slip.setFeeDuration(fee.getFeeDuration());
					slip.setInvoiceDate(today.toDate());
					slip.setYear(requestYear);
					slip.setPaymentStatus(PaymentStatus.Raised);
					slip.setTotalFee(fee.getFinalFee());
					slip.setReGenerateSlip(true);
					slip.setExtraCharge(new BigDecimal(0));
					slip.setLatePaymentCharge(new BigDecimal(0));
					slip.setCgst(fee.getCgst());
					slip.setSgst(fee.getSgst());
					slip.setIgst(fee.getIgst());
					slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
					newslips.add(slip);
				}
				allslips.add(slip);
			}
		}

		// 4. if payment entry is not present, create a new one and send across
		feePaymentRepository.save(newslips);
		return allslips;
	}

	@Transactional
	public List<StudentFeePaymentRequest> generateFinalFeeSlips(List<Long> slipIds) {
		List<StudentFeePaymentRequest> feeSlips = new LinkedList<>();
		for (Long slipId : slipIds) {
			Long unmasked = unmask(slipId);
			StudentFeePaymentRequest slip = feePaymentRepository.findOne(unmasked);
			if (slip != null) {
				slip.setGenerateActive(true);
				feePaymentRepository.saveAndFlush(slip);
				feeSlips.add(slip);
			}
		}
		return feeSlips;
	}

	private void calculateBaseFee(StudentFeePaymentRequest slip) {
		FeeDuration feeDuration = slip.getFeeDuration();
		Date admissionDate = slip.getStudent().getProfile().getAdmissionDate();
		if (admissionDate == null) {
			return;
		}
		LocalDate localDate = LocalDate.fromDateFields(admissionDate);
		int admissionYear = localDate.getYear();
		if (slip.getYear() > admissionYear) {
			return;
		}
		int admissionMonth = localDate.monthOfYear().get();
		int admissionDay = localDate.dayOfMonth().get();

		int quarterStartMonth = FeeUtils.quarterStartMonth(slip.getQuarter());

		BigDecimal newBaseFee = ZERO;
		BigDecimal factor = ZERO;
		switch (feeDuration) {
		case Quarterly:
			int dif = admissionMonth - quarterStartMonth;
			if (dif == 0) {
				newBaseFee = slip.getBaseFee();
			} else if (dif == 1 || dif == 2) {
				factor = BigDecimal.ONE.divide(new BigDecimal(3), 6, BigDecimal.ROUND_HALF_UP)
						.multiply(new BigDecimal(3 - dif));
				newBaseFee = factor.multiply(slip.getBaseFee());
				newBaseFee = newBaseFee.setScale(0, BigDecimal.ROUND_HALF_UP);
				if (!BigDecimalUtils.isZero(newBaseFee)) {
					slip.setAutoComments(
							String.format("BaseFee altered [%s] due to admission in running quarter.", newBaseFee));
				}
			}
			break;
		}
		slip.setBaseFee(newBaseFee);
	}

	private void validateYear(StudentFeeSlipRequest request) {
		int requestYear = request.getYear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		if (month != 1 && month != 12) {
			throw new ValidationException("Can only generate yearly slips in December or January");
		}

		if (month == 1 && day > 15 && requestYear != year) {
			throw new ValidationException("Can only generate yearly slips before January 15th for current year!");
		}

		if (month == 12 && day <= 15 && requestYear != year + 1) {
			throw new ValidationException("Can only generate yearly slips after December 15th for next year!");
		}
	}

	private void validateQuarter(StudentFeeSlipRequest request) {
		int requestQuarter = request.getQuarter();
		int reqYear = request.getYear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);

		switch (requestQuarter) {
		case 1:
			if (!((month == 1 && year == reqYear) || (month == 2 && year == reqYear) || (month == 3 && year == reqYear)
					|| (month == 12 && year + 1 == reqYear && day >= 15))) {
				throw new ValidationException(
						"Can only generate quarterly slips within quarter months or 15 days before quarter months.");
			}
			break;
		case 2:
			if (!((month == 4 && year == reqYear) || (month == 5 && year == reqYear) || (month == 6 && year == reqYear)
					|| (month == 3 && year == reqYear && day >= 15))) {
				throw new ValidationException(
						"Can only generate quarterly slips within quarter months or 15 days before quarter months.");
			}
			break;
		case 3:
			if (!((month == 7 && year == reqYear) || (month == 8 && year == reqYear) || (month == 9 && year == reqYear)
					|| (month == 6 && year == reqYear && day >= 15))) {
				throw new ValidationException(
						"Can only generate quarterly slips within quarter months or 15 days before quarter months.");
			}
			break;
		case 4:
			if (!((month == 10 && year == reqYear) || (month == 11 && year == reqYear)
					|| (month == 12 && year == reqYear) || (month == 9 && year == reqYear && day >= 15))) {
				throw new ValidationException(
						"Can only generate quarterly slips within quarter months or 15 days before quarter months.");
			}
			break;
		}
	}

	private void validateMonth(StudentFeeSlipRequest request) {
		int requestMonth = request.getMonth();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;

		if (day <= 15 && requestMonth != month) {
			throw new ValidationException(String.format("Invalid day[%d] for fee slip generation ..", day));
		}

		if (month < 12) {
			if (day > 15 && requestMonth != month + 1) {
				throw new ValidationException("Can only generate month slips within 15 days of start of month.");
			}
		} else {
			if (day > 15 && requestMonth != 1) {
				throw new ValidationException("Can only generate month slips within 15 days of start of month.");
			}
		}
	}

	public StudentFeePaymentRequest updateSlip(SaveFeeSlipRequest request) {
		StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());

		if (slip == null) {
			throw new NotFoundException("Missing slip");
		}

		if (slip.getPaymentStatus() == PaymentStatus.Paid) {
			throw new ValidationException("Already paid.");
		}

		slip.setAnnualFee(request.getAnnualFee());
		slip.setDeposit(request.getDeposit());
		slip.setExtraCharge(request.getExtraCharge());
		slip.setLatePaymentCharge(request.getLatePaymentCharge());
		slip.setBalance(request.getBalance());
		slip.setAdjust(request.getAdjust());
		slip.setComments(request.getComments());
		slip.setReGenerateSlip(true);
		slip.setReceiptFileName(null);
		slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
		return feePaymentRepository.saveAndFlush(slip);
	}

	public StudentFeePaymentRequest regenerateStudentSlip(StudentFeeSlipRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Slip id missing.");
		}

		StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());

		if (slip == null) {
			throw new ValidationException(String.format("Cannot locate slip[id=%s]", request.getMaskedId()));
		}

		if (slip.getPaymentStatus() == PaymentStatus.Paid) {
			throw new ValidationException("Already paid.");
		}

		StudentFee fee = studentFeeRepository.findByStudent(slip.getStudent());

		slip.setBaseFee(fee.getFinalFee());
		slip.setFeeDuration(fee.getFeeDuration());
		slip.setIgst(fee.getIgst());
		slip.setSgst(fee.getSgst());
		slip.setCgst(fee.getCgst());
		calculateBaseFee(slip);
		slip.setTotalFee(FeeUtils.calculateTotalFee(slip));

		List<StudentFeePaymentRecord> payments = slip.getPayments();

		if (!CollectionUtils.isEmpty(payments)) {
			BigDecimal paidAmount = ZERO;
			for (StudentFeePaymentRecord payment : payments) {
				paidAmount = paidAmount.add(payment.getPaidAmount());
			}

			if (slip.getTotalFee().subtract(paidAmount).signum() <= 0) {
				slip.setPaymentStatus(PaymentStatus.Paid);
			} else if (BigDecimalUtils.lessThen(paidAmount, slip.getTotalFee())) {
				slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
			} else {
				slip.setPaymentStatus(PaymentStatus.Paid);
			}
		}

		slip.setReGenerateSlip(true);
		slip.setSlipFileName(null);
		slip.setReceiptFileName(null);

		// slip.setReceiptSerial(null);
		// slip.setReceiptFileName(null);
		feePaymentRepository.saveAndFlush(slip);
		documentService.generateFeeSlipPdf(slip);
		return slip;
	}

	@Transactional
	public StudentFeePaymentRequest payFee(SaveFeeSlipRequest request) {
		if (request.getPaidAmount() == null) {
			throw new ValidationException("Paid amount is missing");
		}

		if (StringUtils.isEmpty(request.getPaymentMode())) {
			throw new ValidationException("Payment mode is missing.");
		}
		StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());

		if (slip == null) {
			throw new NotFoundException("Missing slip");
		}

		// BigDecimal total = slip.getBaseFee();

		int alreadypaid = (slip.getPayments() == null || slip.getPayments().isEmpty()) ? 0
				: slip.getPayments().stream().mapToInt(p -> p.getPaidAmount().intValue()).sum();

		/*
		 * if (request.getExtraCharge() != null) { total =
		 * total.add(request.getExtraCharge()); } if (request.getLatePaymentCharge() !=
		 * null) { total = total.add(request.getLatePaymentCharge()); } if
		 * (request.getDeposit() != null) { total = total.add(request.getDeposit()); }
		 * if (request.getAnnualFee() != null) { total =
		 * total.add(request.getAnnualFee()); }
		 */

		slip.setExtraCharge(request.getExtraCharge());
		slip.setLatePaymentCharge(request.getLatePaymentCharge());
		// slip.setTotalFee(total);
		slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
		slip.setComments(request.getComments());
		slip.setReGenerateSlip(true);
		slip.setReceiptSerial(null);
		slip.setReceiptFileName(null);

		if (slip.getTotalFee().intValue() <= (alreadypaid + request.getPaidAmount().intValue())) {
			slip.setPaymentStatus(PaymentStatus.Paid);
		} else {
			slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
		}

		StudentFeePaymentRecord record = new StudentFeePaymentRecord();
		record.setStudent(slip.getStudent());
		record.setRequest(slip);
		record.setPaidAmount(request.getPaidAmount());
		record.setPaymentMode(request.getPaymentMode());
		record.setTxnid(request.getTxnid());
		record.setPaymentStatus(PaymentStatus.Paid);
		record.setConfirmed(request.getConfirmed());
		record.setPaymentDate(request.getPaymentDate() == null ? new Date() : request.getPaymentDate());

		feePaymentRepository.saveAndFlush(slip);
		paymentRecordRepository.saveAndFlush(record);
		logger.info(String.format("Student Fee payment recoded successfully.%s", record));

		documentService.generateFeeReceiptPdf(slip);
		return feePaymentRepository.saveAndFlush(slip);
	}

	public StudentFeePaymentRecord updatePayFee(SaveFeeSlipRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Receipt id is required.");
		}

		StudentFeePaymentRecord receipt = paymentRecordRepository.findOne(request.getId());
		if (receipt == null) {
			throw new ValidationException(String.format("Cannot locate Receipt[id = %s]", mask(request.getId())));
		}

		if (receipt.getConfirmed() != null && receipt.getConfirmed()) {
			throw new ValidationException("Confirmed Receipt cannot update.");
		}

		if (request.getConfirmed())
			receipt.setConfirmed(request.getConfirmed());
		else {

			receipt.setActive(false);
		}
		return paymentRecordRepository.saveAndFlush(receipt);
	}

	public String exportRecords(String program) throws Exception {
		List<Student> students;
		if (!program.equalsIgnoreCase("ALL")) {
			students = studentRepository.findByProgramCodeAndActiveIsTrue(program);
		} else {
			students = studentRepository.findByActiveIsTrue();
		}
		Path dirpath = Paths.get(exportDirectory);
		String filename = dirpath.resolve(UUID.randomUUID().toString() + ".xls").toString();
		try (InputStream is = StudentService.class.getClassLoader().getResourceAsStream("student.xls")) {
			try (OutputStream os = new FileOutputStream(filename)) {
				Context context = new Context();
				context.putVar("students", students);
				JxlsHelper.getInstance().processTemplate(is, os, context);
			}
		}
		return filename;
	}

	public String getStudentImageData(Student student) {
		if (!StringUtils.isEmpty(student.getProfile().getImagePath())) {
			try {
				InputStream is = fileStore.getStream("STUDENTPIC", student.getProfile().getImagePath());
				byte[] bytes = ByteStreams.toByteArray(is);
				return Base64.getEncoder().encodeToString(bytes);
			} catch (IOException ioe) {
				logger.error("Error loading image from store! ", ioe);
			}
		}
		return null;
	}

	public void uploadStudentPic(StudentRequest request, MultipartFile file) {
		try {
			Student student = studentRepository.getOne(request.getId());

			if (student == null) {
				throw new NotFoundException("Cannot locate student");
			}

			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

			String extn = FilenameUtils.getExtension(file.getOriginalFilename());

			if (!in(extn, "JPG", "PNG", "JPEG", "GIF")) {
				throw new ValidationException("Only image files (gif, png, jpg) are allowed!");
			}

			String filename = UUID.randomUUID().toString() + ".png";
			File outfile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
			ImageIO.write(originalImage, "png", outfile);
			fileStore.store("STUDENTPIC", outfile);

			StudentProfile profile = student.getProfile();
			profile.setImagePath(filename);
			studentProfileRepository.saveAndFlush(profile);
		} catch (IOException e) {
			logger.error("Error in profile image upload!", e);
			throw new UploadException("Something went wrong, please try later");
		}
	}

	public void createParentAccount(ParentRequest request) {
		if (request.getId() == null) {
			throw new NotFoundException("Cannot locate parent record");
		}

		StudentParent parent = studentParentRepository.findOne(request.getId());
		if (parent == null) {
			throw new NotFoundException("Cannot locate parent record");
		}

		if (StringUtils.isEmpty(parent.getEmail())) {
			throw new ValidationException("Cannot locate email for this account, please save the record first");
		}

		if (parent.getEmail().equalsIgnoreCase("na")) {
			throw new ValidationException("Email is NA for this account, please save the record first");
		}

		if (StringUtils.isEmpty(parent.getMobile())) {
			throw new ValidationException("Cannot locate mobile for this account, please save the record first");
		}

		if (parent.getMobile().equalsIgnoreCase("na")) {
			throw new ValidationException("Mobile Number is NA for this account, please save the record first");
		}

		User user = userRepository.findByEmailAndActiveTrue(parent.getEmail());

		if (user != null) {
			throw new ValidationException(String.format("User already exist with email [%s]", parent.getEmail()));
		}

		Role parentRole = roleRepository.getOneByName("Parent");

		if (parentRole == null) {
			throw new ValidationException("Role not found for parent");
		}
		user = new User();
		user.setParent(parent);
		user.setActive(true);
		user.setEmail(parent.getEmail());
		user.setFirstname(parent.getFirstName());
		user.setLastname(parent.getLastName());
		user.setUserType(UserType.Parent);
		user.setPhone(parent.getMobile());
		user.getRoles().add(parentRole);
		String password = RandomStringUtils.randomAlphanumeric(8);
		user.setPasswordHash(encoder.encode(password));
		userRepository.saveAndFlush(user);
		parent.setAccount(true);
		studentParentRepository.saveAndFlush(parent);
		logger.info(String.format("Parent[%s],Password[%s] new account is created...", parent.getEmail(), password));
		communicationService.sendPasswordEmailAndSms(parent, password);
	}

	public List<Student> newStudentApprovals() {
		List<Center> centers = getUserCenters();
		return studentRepository.findByApprovalStatusAndCenterIn(ApprovalStatus.NewApproval, centers);
	}

	public List<Student> newStudentApprovals(CenterRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Center id is null.");
		}
		Center center = centerRepository.findOne(request.getId());
		if (center == null) {
			throw new NotFoundException(String.format("Center[%s] not found", request.getMaskId()));
		}
		User user = getFreshUser();
		List<Center> centers = getUserCenters();
		if (!centers.contains(center)) {
			throw new ValidationException(
					String.format("Unauthorized access to center[%s] user[%s]", request.getMaskId(), user.getEmail()));
		}
		return studentRepository.findByActiveTrueAndApprovalStatusAndCenter(ApprovalStatus.NewApproval, center);
	}

	public void approve(StudentRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Id is null");
		}
		Student student = studentRepository.findOne(request.getId());
		if (student == null) {
			throw new NotFoundException(String.format("Student not found %s", request.getMaskedId()));
		}
		List<Center> centers = getUserCenters();
		if (!centers.contains(student.getCenter())) {
			throw new ValidationException("Unauthorized access: student is not in user's centers");
		}
		student.setApprovalStatus(ApprovalStatus.Approved);
		studentRepository.saveAndFlush(student);

	}

	public void reject(StudentRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Id is null");
		}
		Student student = studentRepository.findOne(request.getId());
		if (student == null) {
			throw new NotFoundException(String.format("Student not found %s", request.getMaskedId()));
		}
		List<Center> centers = getUserCenters();
		if (!centers.contains(student.getCenter())) {
			throw new ValidationException("Unauthorized access: student is not in user's centers");
		}
		student.setApprovalStatus(ApprovalStatus.Rejected);
		studentRepository.saveAndFlush(student);
	}

	public SharingSheet getSharingSheet(SharingSheetRequest request) {
		SharingSheet sheet = sharingSheetRepository.findByStudentIdAndSharingDate(request.getStudentId(),
				request.getDate());

		if (sheet == null) {
			sheet = new SharingSheet();
			sheet.setStudent(studentRepository.getOne(request.getStudentId()));
			sheet.setSharingDate(request.getDate());
			sharingSheetRepository.saveAndFlush(sheet);
		}

		return sheet;
	}

	public ParentSharingSheet getParentSharingSheet(SharingSheetRequest request) {
		return parentSheetRepository.findByStudentIdAndSharingDate(request.getStudentId(), request.getDate());
	}

	public SharingSheet createSharingSheetEntry(SharingSheetEntryRequest request) {

		validateEntry(request);

		SharingSheet sharingSheet = sharingSheetRepository.findByStudentIdAndSharingDate(request.getStudentId(),
				request.getDate());

		if (sharingSheet == null) {
			sharingSheet = new SharingSheet();
			sharingSheet.setSharingDate(request.getDate());
			sharingSheet.setStudent(studentRepository.getOne(request.getStudentId()));
			sharingSheetRepository.saveAndFlush(sharingSheet);
		}

		if (request.getEntryType().equals(SharingSheetEntryType.Note.name())) {
			sharingSheet.setNotes(request.getNotes());
			sharingSheet.setNeeds(request.getNeeds());
			return sharingSheetRepository.saveAndFlush(sharingSheet);
		}

		SharingSheetEntry sse = request.toEntity();

		sse.setSharingSheet(sharingSheet);
		sharingSheetEntryRepository.saveAndFlush(sse);

		return sharingSheetRepository.getOne(sharingSheet.getId());
	}

	private void validateEntry(SharingSheetEntryRequest request) {

		if (request.getDate() == null) {
			throw new ValidationException("Provide entry date");
		}

		if (request.getStudentId() == null) {
			throw new ValidationException("Missing student info, cant create entry");
		}

		if (StringUtils.isEmpty(request.getEntryType()) || !SharingSheetEntryType.matches(request.getEntryType())) {
			throw new ValidationException("Missing entry type");
		}

		switch (SharingSheetEntryType.valueOf(request.getEntryType())) {
		case Food:
		case Snack:
		case Drink:
		case Lunch:
			// food specific validation

			if (StringUtils.isEmpty(request.getFood())) {
				throw new ValidationException("Missing Food");
			}

			if (request.getEntryAt() == null) {
				throw new ValidationException("Missing entry time");
			}

			if (StringUtils.isEmpty(request.getFeedQty()) || !SharingSheetFeedQty.matches(request.getFeedQty())) {
				throw new ValidationException("Missing Feed Qty");
			}

			break;
		case Diaper:
			if (request.getEntryAt() == null) {
				throw new ValidationException("Missing entry time");
			}
			break;
		case Nap:
			if (request.getEntryFrom() == null) {
				throw new ValidationException("Missing entry from");
			}

			if (request.getEntryFrom() == null) {
				throw new ValidationException("Missing entry to");
			}
			break;
		case Note:
			if (StringUtils.isEmpty(request.getNotes())) {
				throw new ValidationException("Missing notes!");
			}
			break;
		default:
			throw new ValidationException("Unknown entry type!");
		}
	}

	public StudentFee getStudentFee(StudentFeeRequest request) {
		Long id = request.getId();
		Long studentId = request.getStudentId();
		if (id == null && studentId == null) {
			throw new ValidationException("fee request is empty");
		}
		if (id != null) {
			return studentFeeRepository.findOne(id);
		} else {
			Student student = studentRepository.findOne(studentId);
			if (student == null) {
				throw new ValidationException(
						String.format("can not locate student[id=%s]", request.getMaskedStudentId()));
			}
			return studentFeeRepository.findByStudent(student);
		}
	}

	public void sendPaymentLink(SlipEmailRequest request) {
		List<StudentFeePaymentRequest> slips = new ArrayList<>();

		for (Long aLong : request.getSlipIds()) {
			StudentFeePaymentRequest slip = feePaymentRepository.findOne(unmask(aLong));
			if (slip == null) {
				throw new ValidationException(String.format("Cannot locate Slip[id=%s]", aLong));
			}
			slips.add(slip);
		}
		communicationService.sendPaymentLink(slips, request);
	}

	public StudentFeePaymentRecord getReceipt(SaveFeeSlipRequest request) {
		if (request.getId() == null) {
			throw new ValidationException("Receipt id is required.");
		}

		StudentFeePaymentRecord receipt = paymentRecordRepository.findOne(request.getId());
		if (receipt == null) {
			throw new ValidationException(String.format("Cannot locate Receipt[id = %s]", mask(request.getId())));
		}
		return receipt;
	}

	public List<Student> getStudentByCenterId(Center center) {
		return repository.findByActiveTrueAndCenter(center);
	}

	// -------------------------------------shubham
	// ----------------------------------------------------------------

	// shubham
	public List<StudentFeePaymentRequest> listFeeReport(FeeReportRequest request) {
		List<StudentFeePaymentRequest> slip2;
		FeeDuration period = FeeDuration.valueOf("Quarterly");
		if (request.getCenterCode().equals("All")) {
			slip2 = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(
							period, request.getQuarter(), request.getYear());
		} else {
			slip2 = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(
							 period, request.getQuarter(), request.getYear(),
							request.getCenterCode());
		}
		return slip2;
	}

	// shubham
	public List<StudentFeeSlipResponse3> listFeeReport2(FeeReportRequest request) {
		List<StudentFeePaymentRequest> slip2;
		FeeDuration period = FeeDuration.valueOf("Quarterly");
		if (request.getCenterCode().equals("All")) {
			slip2 = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYear(
							period, request.getQuarter(), request.getYear());
		} else {
			slip2 = feePaymentRepository
					.findByStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(
							period, request.getQuarter(), request.getYear(),
							request.getCenterCode());
		}
		return slip2.stream().map(StudentFeeSlipResponse3::new).collect(Collectors.toList());
	}
}