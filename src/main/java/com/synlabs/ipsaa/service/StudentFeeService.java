package com.synlabs.ipsaa.service;


import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.ApprovalStatus;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeRequestV2;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipResponse;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static java.math.BigDecimal.ZERO;


@Service
public class StudentFeeService {

    @Autowired
    private CenterProgramFeeRepository centerProgramFeeRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramGroupRepository programGroupRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private StudentFeePaymentRecordRepository paymentRecordRepository;

    @Autowired
    private StudentFeePaymentRepository feePaymentRepository;


    @Autowired
    private EntityManager entityManager;

    @Transactional
    public StudentFee updateStudentFee(StudentFeeRequestV2 request) {
        if (request.getFinalBaseFee() == null)
        {
            throw new ValidationException("Base Fee not found");
        }
        if (request.getFinalAdmissionCharges() == null)
        {
            throw new ValidationException("Admission Fee not found");
        }
        if (request.getFinalAnnualFee() == null)
        {
            throw new ValidationException("Annual Fee not found");
        }
        if (request.getFinalSecurityDeposit() == null)
        {
            throw new ValidationException("Security Fee not found");
        }

        if (request.getDiscountBaseFee() == null)
        {
            throw new ValidationException(" Base Discount not found");
        }
        if (request.getFinalFee() == null)
        {
            throw new ValidationException("Final Fee not found");
        }

        Student student = studentRepository.findOne(request.getStudentId());
        if (student == null)
        {
            throw new NotFoundException(String.format("Student not fount [%s]", request.getMaskedStudentId()));
        }
        StudentFee studentFee = studentFeeRepository.findByStudent(student);
        if (studentFee == null)
        {
            throw new ValidationException(String.format("Student fee not exits for student[admission_number=%s]", student.getAdmissionNumber()));
        }
        CenterProgramFee centerProgramFee=centerProgramFeeRepository.findByProgramIdAndCenterId(student.getProgram().getId(),student.getCenter().getId());
        if(centerProgramFee==null){
            throw new NotFoundException("Center program fee not found ");
        }

        studentFee = request.toEntity(studentFee);
        studentFee.setStudent(student);
        FeeUtilsV2.validateStudentFee(studentFee,centerProgramFee);
        //return studentFee;
        return studentFeeRepository.saveAndFlush(studentFee);
    }
    @Transactional
    public StudentFee saveStudentFee(StudentFeeRequestV2 request) {
        if (request.getFinalBaseFee() == null)
        {
            throw new ValidationException("Base Fee not found");
        }
        if (request.getFinalAdmissionCharges() == null)
        {
            throw new ValidationException("Admission Fee not found");
        }
        if (request.getFinalAnnualFee() == null)
        {
            throw new ValidationException("Annual Fee not found");
        }
        if (request.getFinalSecurityDeposit() == null)
        {
            throw new ValidationException("Security Fee not found");
        }
        if (request.getDiscountBaseFee() == null)
        {
            throw new ValidationException(" Base Discount not found");
        }
        if (request.getFinalFee() == null)
        {
            throw new ValidationException("Final Fee not found");
        }
        Student student = studentRepository.findOne(request.getStudentId());
        if (student == null)
        {
            throw new NotFoundException(String.format("Student not fount [%s]", request.getMaskedStudentId()));
        }
        StudentFee studentFee = studentFeeRepository.findByStudent(student);
        if (studentFee != null)
        {
            throw new ValidationException(String.format("Student fee already exits for student[admission_number=%s]", student.getAdmissionNumber()));
        }
        CenterProgramFee centerProgramFee=centerProgramFeeRepository.findByProgramIdAndCenterId(student.getProgram().getId(),student.getCenter().getId());
        if(centerProgramFee==null){
            throw new NotFoundException("Center program fee not found");
        }

        studentFee = request.toEntity(null);
        studentFee.setStudent(student);
        FeeUtilsV2.validateStudentFee(studentFee,centerProgramFee);
        //return studentFee;
        return studentFeeRepository.saveAndFlush(studentFee);
    }
    private void add(BigDecimal a,BigDecimal b){
        a= a.add(b);
    }

    @Transactional
    public StudentFeePaymentRequest generateFeeSlip(Long studentId,int quarter,int year,boolean wantException) {
        StudentFee fee=studentFeeRepository.findByStudentId(studentId);
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not fount [%s]", studentId));
        }
        Calendar cal = Calendar. getInstance();
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());

        StudentFeePaymentRequest slip=null;
        Optional<StudentFeePaymentRequest> thisQuarterSlips=null;
        BigDecimal paidAmount=ZERO;
        BigDecimal totalAmount=ZERO;
        BigDecimal balance=ZERO;
        List<StudentFeePaymentRequest> slips = feePaymentRepository.findByStudentAndFeeDurationAndYear(fee.getStudent(),FeeDuration.Quarterly,year);
        if(slips!=null && !slips.isEmpty()){
        for(StudentFeePaymentRequest s:slips){
            for(StudentFeePaymentRecord p:s.getPayments()){
               paidAmount=paidAmount.add(p.getPaidAmount());
            }
            totalAmount=s.getTotalFee();
        }
        }
        balance=totalAmount.subtract(paidAmount);

        if(slips!=null && !slips.isEmpty())
            thisQuarterSlips =slips.stream().filter(s->s.getQuarter()==quarter).filter(s->s.getStudent().equals(fee.getStudent())).findFirst();

        if(!thisQuarterSlips.isPresent())
        {
            slip = new StudentFeePaymentRequest();
            slip.setStudent(fee.getStudent());
            slip.setFeeDuration(FeeDuration.Quarterly);
            slip.setInvoiceDate(LocalDate.now().toDate());
            slip.setYear(cal.get(Calendar.YEAR));
            slip.setPaymentStatus(PaymentStatus.Raised);
            slip.setQuarter(quarter);
            slip.setTotalFee(ZERO);
            slip.setGenerateActive(true);
            slip.setExtraCharge(ZERO);
            slip.setLatePaymentCharge(ZERO);
            slip.setCgst(fee.getCgst());
            slip.setSgst(fee.getSgst());
            slip.setIgst(fee.getIgst());
            slip.setBalance(balance);
            slip.setDeposit(fee.getDepositFee()==null?ZERO:fee.getDepositFee());
            slip.setFinalDepositFee(fee.getFinalDepositFee()==null?ZERO:fee.getFinalDepositFee());

            slip.setBaseFee(fee.getBaseFee());
            slip.setFinalBaseFee(fee.getFinalBaseFee());

            slip.setAnnualFee(ZERO);
            slip.setFinalAnnualCharges(ZERO);

            slip.setAdmissionFee(ZERO);
            slip.setFinalAdmissionFee(ZERO);

            slip.setAddmissionFeeDiscount(ZERO);
            slip.setAnnualFeeDiscount(ZERO);

            slip.setDepositFeeDiscount(fee.getDepositFeeDiscount()==null?ZERO:fee.getDepositFeeDiscount());
            slip.setBaseFeeDiscount(fee.getBaseFeeDiscount()==null?ZERO:fee.getBaseFeeDiscount());

            slip.setUniformCharges(fee.getUniformCharges()==null?ZERO:fee.getUniformCharges());
            slip.setStationary(fee.getStationary()==null?ZERO:fee.getStationary());
            slip.setTransportFee(fee.getTransportFee()==null?ZERO:fee.getTransportFee());

            slip.setGstAmount(fee.getGstAmount());
            BigDecimal baseFeeRatio=THREE;

            if(slips==null || slips.isEmpty()) // for checking first time genration or not
                {
                    baseFeeRatio=FeeUtilsV2.calculateFeeRatioForQuarter(slip.getStudent().getProfile().getAdmissionDate());

                    slip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount());
                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());

                    slip.setAnnualFee(fee.getAnnualCharges());
                    slip.setAdmissionFee(fee.getAdmissionFee());

                    slip.setFinalAnnualCharges(fee.getFinalAnnualCharges());
                    slip.setFinalAdmissionFee(fee.getFinalAdmissionFee());

                }else if(quarter == 2){

                    slip.setAdmissionFee(ZERO);

                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());

                    slip.setAnnualFee(fee.getFinalAnnualCharges());

                }else{
                    slip.setAdmissionFee(ZERO);
                    slip.setAnnualFee(ZERO);
                }
                slip.setTotalFee(FeeUtilsV2.calculateFinalFee(slip,baseFeeRatio));
                slip.setBalance(balance.add(slip.getTotalFee()));
                //feePaymentRepository.saveAndFlush(slip);
        }else if(thisQuarterSlips.get().isExpire()){

        }
        else{
            if(wantException)
                throw new NotFoundException(String.format("Pay Slip Already Exist", studentId));
            else
                return thisQuarterSlips.get();
        }
        return slip;
    }

    public StudentFeePaymentRequest generateFirstFeeSlip(Long studentId){
        Calendar cal = Calendar. getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH));
        int year=cal.get(Calendar.YEAR);
        return this.generateFeeSlip(studentId,quarter,year,true);
    }

    public List<StudentFeePaymentRequest> generateFeeSlips(StudentFeeSlipRequest request) {
        validateQuarter(request);
        QStudent qStudent = QStudent.student;
        JPAQuery<Student> query = new JPAQuery<>(entityManager);
        query.select(qStudent)
                .from(qStudent)
                .where(qStudent.approvalStatus.eq(ApprovalStatus.Approved))
                .where(qStudent.active.isTrue())
                .where(qStudent.corporate.isFalse())
                .where(qStudent.center.code.eq(request.getCenterCode()));

        List<Student> studentList = query.fetch();
        List<StudentFeePaymentRequest> allslips = new LinkedList<>();

        int requestQuarter = request.getQuarter();
        int requestYear = request.getYear();
        for(Student student:studentList){
          allslips.add(this.generateFeeSlip(student.getId(),requestQuarter,requestYear,false));
        }
        return allslips;
    }
    private void validateQuarter(StudentFeeSlipRequest request)
    {
        int requestQuarter = request.getQuarter();
        int reqYear = request.getYear();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        switch (requestQuarter)
        {
            case 1:
                if (!((month == 1 && year == reqYear)
                        || (month == 2 && year == reqYear)
                        || (month == 3 && year == reqYear)
                        || (month == 12 && year + 1 == reqYear && day >= 15)
                ))
                {
                    throw new ValidationException("Can only generate quarterly slips within quarter months or 15 days before quarter months.");
                }
                break;
            case 2:
                if (!((month == 4 && year == reqYear)
                        || (month == 5 && year == reqYear)
                        || (month == 6 && year == reqYear)
                        || (month == 3 && year == reqYear && day >= 15)
                ))
                {
                    throw new ValidationException("Can only generate quarterly slips within quarter months or 15 days before quarter months.");
                }
                break;
            case 3:
                if (!((month == 7 && year == reqYear)
                        || (month == 8 && year == reqYear)
                        || (month == 9 && year == reqYear)
                        || (month == 6 && year == reqYear && day >= 15)
                ))
                {
                    throw new ValidationException("Can only generate quarterly slips within quarter months or 15 days before quarter months.");
                }
                break;
            case 4:
                if (!((month == 10 && year == reqYear)
                        || (month == 11 && year == reqYear)
                        || (month == 12 && year == reqYear)
                        || (month == 9 && year == reqYear && day >= 15)
                ))
                {
                    throw new ValidationException("Can only generate quarterly slips within quarter months or 15 days before quarter months.");
                }
                break;
        }
    }
}
