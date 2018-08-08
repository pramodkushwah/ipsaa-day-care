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
    @Transactional
    public StudentFeeSlipResponse generateFirstFeeSlip(Long studentId,boolean isFirst) {
        StudentFee fee=studentFeeRepository.findByStudentId(studentId);
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not fount [%s]", studentId));
        }
        Calendar cal = Calendar. getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH));
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());
        StudentFeePaymentRequest slip;
        List<StudentFeePaymentRequest> slips = feePaymentRepository.findByStudentAndFeeDurationAndYear(fee.getStudent(),FeeDuration.Quarterly,cal.get(Calendar.YEAR));
        if(slips==null && slips.isEmpty()){
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
            slip.setAnnualFee(ZERO);
            slip.setCgst(fee.getCgst());
            slip.setSgst(fee.getSgst());
            slip.setIgst(fee.getIgst());

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

            slip.setUniformCharges(fee.getUniformCharges());
            slip.setStationary(fee.getStationary());
            slip.setTransportFee(fee.getTransportFee());

            slip.setGstAmount(fee.getGstAmount());
            BigDecimal baseFeeRatio=ZERO;
            if (isFirst)
            {
                baseFeeRatio=FeeUtilsV2.calculateFeeRatioForQuarter(slip.getStudent().getProfile().getAdmissionDate());

                slip.setAnnualFee(fee.getAnnualCharges());
                slip.setAdmissionFee(fee.getAdmissionFee());

                slip.setFinalAnnualCharges(fee.getFinalAnnualCharges());
                slip.setFinalAdmissionFee(fee.getFinalAdmissionFee());

            }else if(FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH)) == 2){
                slip.setAdmissionFee(ZERO);
                slip.setAnnualFee(fee.getFinalAnnualCharges());
            }else{
                slip.setAdmissionFee(ZERO);
                slip.setAnnualFee(ZERO);
            }
            slip.setTotalFee(FeeUtilsV2.calculateFinalFee(slip,baseFeeRatio));
            feePaymentRepository.saveAndFlush(slip);
        }else{
            throw new NotFoundException(String.format("Pay Slip Already Exist", studentId));
        }
        return new StudentFeeSlipResponse(slip);
    }
}
