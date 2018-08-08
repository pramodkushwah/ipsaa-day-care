package com.synlabs.ipsaa.service;


import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.QStudentFee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
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
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


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
        return studentFee;
        //return studentFeeRepository.saveAndFlush(studentFee);
    }

    @Transactional
    public List<StudentFeePaymentRequest> generateFeeSlips(StudentFeeSlipRequest request)
    {
        LocalDate today = LocalDate.now();
         QStudentFee qStudentFee = QStudentFee.studentFee;
        JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
        query.select(qStudentFee)
                .from(qStudentFee)
                .where(qStudentFee.feeDuration.eq(FeeDuration.Quarterly))
                .where(qStudentFee.student.approvalStatus.eq(ApprovalStatus.Approved))
                .where(qStudentFee.student.active.isTrue())
                .where(qStudentFee.student.corporate.isFalse())
                .where(qStudentFee.student.center.code.eq(request.getCenterCode()));
        List<StudentFee> feelist = query.fetch();

        List<StudentFeePaymentRequest> newslips = new LinkedList<>();
        List<StudentFeePaymentRequest> allslips = new LinkedList<>();

        int requestQuarter = request.getQuarter();
        int requestYear = request.getYear();
                //TODO date check to make sure fee slips are only generated += 15 days from start of quarter
                //validateQuarter(request);
                for (StudentFee fee : feelist)
                {
                    StudentFeePaymentRequest slip = feePaymentRepository.findOneByStudentAndFeeDurationAndQuarterAndYear(fee.getStudent(), FeeDuration.Quarterly, requestQuarter, requestYear);
                    if (slip == null)
                    {
                        BigDecimal baseFee = fee.getFinalFee();
                        slip = new StudentFeePaymentRequest();
                        slip.setStudent(fee.getStudent());
                        slip.setFeeDuration(FeeDuration.Quarterly);
                        slip.setInvoiceDate(today.toDate());
                        slip.setYear(requestYear);
                        slip.setPaymentStatus(PaymentStatus.Raised);
                        slip.setQuarter(requestQuarter);
                        slip.setTotalFee(baseFee);
                        slip.setReGenerateSlip(true);
                        slip.setExtraCharge(BigDecimal.ZERO);
                        slip.setLatePaymentCharge(BigDecimal.ZERO);
                        slip.setAnnualFee(BigDecimal.ZERO);
                        slip.setCgst(fee.getCgst());
                        slip.setSgst(fee.getSgst());
                        slip.setIgst(fee.getIgst());

                        slip.setAnnualFee(fee.getAnnualCharges());
                        slip.setFinalAnnualCharges(fee.getFinalAnnualCharges());

                        slip.setDeposit(fee.getDepositFee());
                        slip.setFinalDepositFee(fee.getFinalDepositFee());

                        slip.setBaseFee(baseFee);
                        slip.setFinalBaseFee(fee.getFinalBaseFee());

                        slip.setAdmissionFee(fee.getAdmissionFee());
                        slip.setFinalAdmissionFee(fee.getFinalAdmissionFee());

                        slip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount());
                        slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());
                        slip.setDepositFeeDiscount(fee.getDepositFeeDiscount());
                        slip.setBaseFeeDiscount(fee.getBaseFeeDiscount());

                        slip.setGstAmount(fee.getGstAmount());

                        if (requestQuarter == 2)
                        {
                            CenterProgramFee programFee = centerProgramFeeRepository.findByProgramIdAndCenterId(slip.getStudent().getProgram().getId(), slip.getStudent().getCenter().getId());
                            if (programFee == null)
                            {
                                continue;
                            }
                            BigDecimal annualFee = BigDecimal.valueOf(programFee.getAnnualFee());
                            slip.setAnnualFee(annualFee);

                        }
                      //  calculateBaseFee(slip);
                        slip.setTotalFee(FeeUtils.calculateTotalFee(slip));
                        newslips.add(slip);
                    }
                    allslips.add(slip);
                }
        //4. if payment entry is not present, create a new one and send across
        feePaymentRepository.save(newslips);
        return allslips;
    }

    public StudentFeeSlipResponse generateFirstFeeSlip(Long studentId) {
        StudentFee fee=studentFeeRepository.findByStudentId(studentId);
        if(fee==null){
            throw new NotFoundException(String.format("Student not fount [%s]", studentId));
        }
        Calendar cal = Calendar. getInstance();
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());
        StudentFeePaymentRequest slip = feePaymentRepository.findOneByStudentAndFeeDurationAndYear(fee.getStudent(),FeeDuration.Quarterly,cal.get(Calendar.YEAR));
        if(slip==null){
            BigDecimal finalFee = fee.getFinalFee();
            slip = new StudentFeePaymentRequest();
            slip.setStudent(fee.getStudent());
            slip.setFeeDuration(FeeDuration.Quarterly);
            slip.setInvoiceDate(LocalDate.now().toDate());
            slip.setYear(cal.get(Calendar.YEAR));
            slip.setPaymentStatus(PaymentStatus.Raised);
            slip.setQuarter(FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH)));
            slip.setTotalFee(finalFee);
            slip.setReGenerateSlip(true);
            slip.setExtraCharge(BigDecimal.ZERO);
            slip.setLatePaymentCharge(BigDecimal.ZERO);
            slip.setAnnualFee(BigDecimal.ZERO);
            slip.setCgst(fee.getCgst());
            slip.setSgst(fee.getSgst());
            slip.setIgst(fee.getIgst());

            slip.setAnnualFee(fee.getAnnualCharges());
            slip.setFinalAnnualCharges(fee.getFinalAnnualCharges());

            slip.setDeposit(fee.getDepositFee());
            slip.setFinalDepositFee(fee.getFinalDepositFee());

            slip.setBaseFee(fee.getBaseFee());
            slip.setFinalBaseFee(fee.getFinalBaseFee());

            slip.setAdmissionFee(fee.getAdmissionFee());
            slip.setFinalAdmissionFee(fee.getFinalAdmissionFee());

            slip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount());
            slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());
            slip.setDepositFeeDiscount(fee.getDepositFeeDiscount());
            slip.setBaseFeeDiscount(fee.getBaseFeeDiscount());

            slip.setGstAmount(fee.getGstAmount());
            // calculate feeslip
        }else{
            throw new NotFoundException(String.format("Pay Slip Already Exist", studentId));
        }
        return new StudentFeeSlipResponse(slip);
    }
}
