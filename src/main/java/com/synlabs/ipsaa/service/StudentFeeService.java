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
import com.synlabs.ipsaa.util.QuarterYearUtil;
import com.synlabs.ipsaa.view.fee.*;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.synlabs.ipsaa.service.BaseService.mask;
import static com.synlabs.ipsaa.util.BigDecimalUtils.THREE;
import static java.math.BigDecimal.ZERO;


@Service
public class StudentFeeService {

    private static final Logger logger = LoggerFactory.getLogger(StudentFeeService.class);
    @Autowired
    private DocumentService documentService;
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

        studentFee = request.toEntity(studentFee,centerProgramFee);
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

        studentFee = request.toEntity(null,centerProgramFee);
        studentFee.setStudent(student);
        FeeUtilsV2.validateStudentFee(studentFee,centerProgramFee);
        //return studentFee;
        return studentFeeRepository.saveAndFlush(studentFee);
    }
    public List<StudentFeePaymentRequest> listFeeSlips(StudentFeeSlipRequest request)
    {
        List<StudentFeePaymentRequest> allslips = new LinkedList<>();
        int requestQuarter = request.getQuarter();
        int requestYear = request.getYear();

            allslips = feePaymentRepository. findByStudentActiveIsTrueAndStudentApprovalStatusAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(ApprovalStatus.Approved, FeeDuration.Quarterly, requestQuarter, requestYear,request.getCenterCode());
        return allslips;
    }

    @Transactional
    public StudentFeePaymentRequest generateFeeSlip(Long feeId,int quarter,int year,boolean wantException) {
        StudentFee fee=studentFeeRepository.findOne(feeId);
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", feeId));
        }
        Calendar cal = Calendar. getInstance();
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());

        StudentFeePaymentRequest slip=null;
        StudentFeePaymentRequest thisQuarterSlips=null;
        List<StudentFeePaymentRequest> lastQuarterSlips= new ArrayList<>();
        List<StudentFeePaymentRequest> unPaidList=new ArrayList<>();
        BigDecimal paidAmount=ZERO;
        BigDecimal totalAmount=ZERO;
        BigDecimal balance=ZERO;
        List<StudentFeePaymentRequest> slips = feePaymentRepository.findByStudentAndFeeDuration(fee.getStudent(),FeeDuration.Quarterly);
        boolean isAllConfirm=true;
        if(slips!=null && !slips.isEmpty()){
        for(StudentFeePaymentRequest s:slips){
            for(StudentFeePaymentRecord p:s.getPayments()){
                if(p.getActive())
                    paidAmount=paidAmount.add(p.getPaidAmount());
                if(p.getConfirmed()==null || !p.getConfirmed()){
                    unPaidList.add(s);
                    if(isAllConfirm)
                        isAllConfirm=false;
                }
            }
            totalAmount=totalAmount.add(s.getTotalFee());
        }
        }
        balance=totalAmount.subtract(paidAmount);

        if(slips!=null && !slips.isEmpty()){
            List<StudentFeePaymentRequest> list=slips.stream()
                                        .filter(s->s.getQuarter()==quarter)
                                        .filter(s->s.getStudent().equals(fee.getStudent()))
                                        .filter(s->s.getYear()==year)
                                        .collect(Collectors.toList());
            if(list!=null && !list.isEmpty())
                thisQuarterSlips=list.get(0);
        }
        if(slips!=null && !slips.isEmpty()){
            lastQuarterSlips=slips.stream().collect(Collectors.toList());
            lastQuarterSlips.removeIf(req->req.getQuarter()==quarter && req.getYear()==year);
        }
        if(thisQuarterSlips==null && isAllConfirm)
        {
            slip = new StudentFeePaymentRequest();
            slip.setStudent(fee.getStudent());
            slip.setFeeDuration(FeeDuration.Quarterly);
            slip.setInvoiceDate(LocalDate.now().toDate());
            slip.setYear(year);
            slip.setPaymentStatus(PaymentStatus.Raised);
            slip.setQuarter(quarter);
            slip.setTotalFee(ZERO);
           // slip.setGenerateActive(true);
            slip.setExtraCharge(ZERO);
            slip.setLatePaymentCharge(ZERO);
            slip.setCgst(fee.getCgst());
            slip.setSgst(fee.getSgst());
            slip.setIgst(fee.getIgst());
            slip.setLastQuarterBalance(balance);
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
            slip.setFeeRatio(baseFeeRatio);
            if(slips==null || slips.isEmpty()) // for checking first time genration or not
                {
                    baseFeeRatio=FeeUtilsV2.calculateFeeRatioForQuarter(slip.getStudent().getProfile().getAdmissionDate());
                    slip.setFeeRatio(baseFeeRatio);
                    slip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount()==null?ZERO:fee.getAddmissionFeeDiscount());
                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());

                    slip.setAnnualFee(fee.getAnnualCharges()==null?ZERO:fee.getAnnualCharges());
                    slip.setAdmissionFee(fee.getAdmissionFee()==null?ZERO:fee.getAdmissionFee());

                    slip.setFinalAnnualCharges(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());
                    slip.setFinalAdmissionFee(fee.getFinalAdmissionFee()==null?ZERO:fee.getFinalAdmissionFee());

                }else if(quarter == 2){

                    slip.setAdmissionFee(ZERO);
                    slip.setFinalAdmissionFee(ZERO);

                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());

                    slip.setAnnualFee(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());

                }else{
                    slip.setAdmissionFee(ZERO);
                    slip.setFinalAdmissionFee(ZERO);
                    slip.setAnnualFee(ZERO);
                    slip.setFinalAnnualCharges(ZERO);
                }
                slip.setBalance(balance);
                slip.setTotalFee(FeeUtilsV2.calculateFinalFee(slip,baseFeeRatio));

                slip.setTotalFee(slip.getTotalFee().add(balance));

                for(StudentFeePaymentRequest lastQuarter:lastQuarterSlips){  // expiring all old slips
                     if(lastQuarter!=null && !lastQuarter.isExpire()){
                        lastQuarter.setExpire(true);
                        feePaymentRepository.saveAndFlush(lastQuarter);
                    }
                }
            return  feePaymentRepository.saveAndFlush(slip);
        }
        else {
            if (wantException)
                throw new NotFoundException(String.format("Pay Slip Already Exist", feeId));
                return thisQuarterSlips;
            }
    }

    public StudentFeePaymentRequest regenerateFeeSlip(StudentFeeSlipRequestV2 request, int quarter, int year) {
        long id=request.getId();
        StudentFeePaymentRequest thisQuarterSlip=null;
        thisQuarterSlip = feePaymentRepository.findOne(request.getId());

        StudentFee fee=studentFeeRepository.findByStudentId(thisQuarterSlip.getStudent().getId());
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", request.getStudentId()));
        }
        if(thisQuarterSlip!=null)
        {
            thisQuarterSlip.setStudent(fee.getStudent());
            //thisQuarterSlip.setFeeDuration(FeeDuration.Quarterly);
            //thisQuarterSlip.setInvoiceDate(LocalDate.now().toDate());
            //thisQuarterSlip.setYear(year);
            //thisQuarterSlip.setPaymentStatus(PaymentStatus.Raised);
            //thisQuarterSlip.setQuarter(quarter);
            //thisQuarterSlip.setTotalFee(ZERO);
            //thisQuarterSlip.setLastQuarterBalance(balance);
            thisQuarterSlip.setReGenerateSlip(true);
            thisQuarterSlip.setExtraCharge(request.getExtraCharge()==null?ZERO:request.getExtraCharge());
            thisQuarterSlip.setLatePaymentCharge(request.getLatePaymentCharge()==null?ZERO:request.getLatePaymentCharge());

            thisQuarterSlip.setCgst(fee.getCgst());
            thisQuarterSlip.setSgst(fee.getSgst());
            thisQuarterSlip.setIgst(fee.getIgst());
            thisQuarterSlip.setDeposit(fee.getDepositFee()==null?ZERO:fee.getDepositFee());
            thisQuarterSlip.setFinalDepositFee(fee.getFinalDepositFee()==null?ZERO:fee.getFinalDepositFee());

            thisQuarterSlip.setBaseFee(fee.getBaseFee());
            thisQuarterSlip.setFinalBaseFee(fee.getFinalBaseFee()==null?ZERO:fee.getFinalBaseFee());

            thisQuarterSlip.setDepositFeeDiscount(fee.getDepositFeeDiscount()==null?ZERO:fee.getDepositFeeDiscount());
            thisQuarterSlip.setBaseFeeDiscount(fee.getBaseFeeDiscount()==null?ZERO:fee.getBaseFeeDiscount());

            thisQuarterSlip.setUniformCharges(fee.getUniformCharges()==null?ZERO:fee.getUniformCharges());
            thisQuarterSlip.setStationary(fee.getStationary()==null?ZERO:fee.getStationary());
            thisQuarterSlip.setTransportFee(fee.getTransportFee()==null?ZERO:fee.getTransportFee());

            thisQuarterSlip.setGstAmount(fee.getGstAmount());

            thisQuarterSlip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount()==null?ZERO:fee.getAddmissionFeeDiscount());
             thisQuarterSlip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());

             thisQuarterSlip.setAnnualFee(fee.getAnnualCharges()==null?ZERO:fee.getAnnualCharges());
             thisQuarterSlip.setAdmissionFee(fee.getAdmissionFee()==null?ZERO:fee.getAdmissionFee());

             thisQuarterSlip.setFinalAnnualCharges(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());
             thisQuarterSlip.setFinalAdmissionFee(fee.getFinalAdmissionFee()==null?ZERO:fee.getFinalAdmissionFee());

             thisQuarterSlip.setTotalFee(FeeUtilsV2.calculateFinalFee(thisQuarterSlip,thisQuarterSlip.getFeeRatio()));
             thisQuarterSlip.setTotalFee(thisQuarterSlip.getTotalFee()
                            .add(thisQuarterSlip.getLastQuarterBalance()==null?ZERO:thisQuarterSlip.getLastQuarterBalance()));

             return  feePaymentRepository.saveAndFlush(thisQuarterSlip);
        }
        else {
                throw new NotFoundException(String.format("Pay Slip not found", request.getStudentId()));
        }
    }
    public StudentFeePaymentRequest generateFirstFeeSlip(Long slipId){
        Calendar cal = Calendar. getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH));
        int year=cal.get(Calendar.YEAR);
        return this.generateFeeSlip(slipId,quarter,year,true);
    }

    public List<StudentFeePaymentRequest> generateFeeSlips(StudentFeeSlipRequest request) {
        validateQuarter(request);
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
        List<StudentFeePaymentRequest> allslips = new LinkedList<>();

        int requestQuarter = request.getQuarter();
        int requestYear = request.getYear();
        for(StudentFee studentFee:feelist){
          allslips.add(this.generateFeeSlip(studentFee.getId(),requestQuarter,requestYear,false));
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

    public StudentFeePaymentRequest regenerateStudentSlip(StudentFeeSlipRequestV2 request) {
        Calendar cal = Calendar. getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH));
        int year=cal.get(Calendar.YEAR);
        return this.regenerateFeeSlip(request,quarter,year);
    }

    public StudentFeePaymentRequest updateSlip(StudentFeeSlipRequestV2 request) {
        StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());
        if (slip == null)
        {
            throw new NotFoundException("Missing slip");
        }

        if (slip.getPaymentStatus() == PaymentStatus.Paid)
        {
            throw new ValidationException("Already paid.");
        }
        if(request.getExtraCharge()!=null)
            slip.setExtraCharge(request.getExtraCharge());
        if(request.getLatePaymentCharge()!=null)
            slip.setLatePaymentCharge(request.getLatePaymentCharge());
        slip.setTotalFee(FeeUtilsV2.calculateFinalFee(slip,slip.getFeeRatio()));
       return feePaymentRepository.saveAndFlush(slip);
    }

    @Transactional
    public StudentFeePaymentRequest payFee(SaveFeeSlipRequest request)
    {
        if (request.getPaidAmount() == null)
        {
            throw new ValidationException("Paid amount is missing");
        }

        if (StringUtils.isEmpty(request.getPaymentMode()))
        {
            throw new ValidationException("Payment mode is missing.");
        }
        StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());

        if (slip == null)
        {
            throw new NotFoundException("Missing slip");
        }
        double alreadypaid=0;
        double uniformPaid=0;
        double stationaryPaid=0;
        double transportPaid=0;
        double admissionPaid=0;
        double annualPaid=0;
        double programFeePaid=0;
        double depositePaid=0;

        for(StudentFeePaymentRecord payments:slip.getPayments()){
            if(payments.getPaidAmount()!=null && payments.getActive())
             alreadypaid+=payments.getPaidAmount().intValue();
            if(payments.getUniformPaidAmount()!=null && payments.getActive())
             uniformPaid+=payments.getUniformPaidAmount().intValue();
            if(payments.getStationaryPaidAmount()!=null && payments.getActive())
            stationaryPaid+=payments.getStationaryPaidAmount().intValue();
            if(payments.getTransportPaidAmount()!=null && payments.getActive())
            transportPaid+=payments.getTransportPaidAmount().intValue();
            if(payments.getAddmissionPaidAmount()!=null && payments.getActive())
            admissionPaid=+payments.getAddmissionPaidAmount().intValue();
            if(payments.getAnnualPaidAmount()!=null && payments.getActive())
            annualPaid+=payments.getAnnualPaidAmount().intValue();
            if(payments.getProgramPaidAmount()!=null && payments.getActive())
            programFeePaid+=payments.getProgramPaidAmount().intValue();
            if(payments.getDepositPaidAmount()!=null && payments.getActive())
            depositePaid+=payments.getDepositPaidAmount().intValue();
        }
        slip.setComments(request.getComments());
        slip.setReGenerateSlip(true);
        slip.setReceiptSerial(null);
        slip.setReceiptFileName(null);

        if (slip.getTotalFee().intValue() <= (alreadypaid + request.getPaidAmount().intValue()))
        {
            slip.setPaymentStatus(PaymentStatus.Paid);
            if(alreadypaid + request.getPaidAmount().intValue()>slip.getTotalFee().intValue()){
                slip.setBalance(slip.getBalance().add(request.getPaidAmount().add(new BigDecimal(alreadypaid)).subtract(slip.getTotalFee())));
            }
        }
        else
        {
            slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
        }

        StudentFeePaymentRecord record = new StudentFeePaymentRecord();
        record.setActive(true);

        BigDecimal paidAmount=request.getPaidAmount();

        slip.setUniformCharges(slip.getUniformCharges()==null?ZERO:slip.getUniformCharges());
        if(slip.getUniformCharges().intValue()>(uniformPaid) || uniformPaid==0){
            BigDecimal amount=slip.getUniformCharges().subtract(new BigDecimal(uniformPaid));
                if(amount.subtract(paidAmount).intValue()<0){
                    record.setUniformPaidAmount(amount);
                    paidAmount=paidAmount.subtract(amount);
                }else{
                    record.setUniformPaidAmount(paidAmount);
                    paidAmount=ZERO;
                }
        }


        slip.setStationary(slip.getStationary()==null?ZERO:slip.getStationary());
        if(slip.getStationary().intValue()>(stationaryPaid) || stationaryPaid==0 ){
            BigDecimal amount=slip.getStationary().subtract(new BigDecimal(stationaryPaid));
            if(amount.subtract(paidAmount).intValue()<0){
                record.setStationaryPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setStationaryPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setTransportFee(slip.getTransportFee()==null?ZERO:slip.getTransportFee());
        if(slip.getTransportFee().intValue()>(transportPaid) || transportPaid==0) {
            BigDecimal amount = slip.getTransportFee().subtract(new BigDecimal(transportPaid));
            if (amount.subtract(paidAmount).intValue() < 0) {
                record.setTransportPaidAmount(amount);
                paidAmount = paidAmount.subtract(amount);
            } else {
                record.setTransportPaidAmount(paidAmount);
                paidAmount = ZERO;
            }
        }

        slip.setFinalAnnualCharges(slip.getFinalAnnualCharges()==null?ZERO:slip.getFinalAnnualCharges());
        if(slip.getFinalAnnualCharges().intValue()>(annualPaid)|| annualPaid==0)  {
            BigDecimal amount = slip.getFinalAnnualCharges().subtract(new BigDecimal(annualPaid));
            if (amount.subtract(paidAmount).intValue() < 0) {
                record.setAnnualPaidAmount(amount);
                paidAmount = paidAmount.subtract(amount);
            } else {
                record.setAnnualPaidAmount(paidAmount);
                paidAmount = ZERO;
            }
        }

        slip.setFinalAdmissionFee(slip.getFinalAdmissionFee()==null?ZERO:slip.getFinalAdmissionFee());
        if(slip.getFinalAdmissionFee().intValue()>(admissionPaid) || admissionPaid==0){
            BigDecimal amount=slip.getFinalAdmissionFee().subtract(new BigDecimal(admissionPaid));
            if (amount.subtract(paidAmount).intValue() < 0) {
                record.setAddmissionPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setAddmissionPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setFinalBaseFee(slip.getFinalBaseFee()==null?ZERO:slip.getFinalBaseFee());
        if(slip.getFinalBaseFee().intValue()>(programFeePaid) || programFeePaid==0){  // final base fee
            BigDecimal amount=slip.getFinalBaseFee().subtract(new BigDecimal(programFeePaid));
            if (amount.subtract(paidAmount).intValue() < 0) {
                record.setProgramPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setProgramPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setFinalDepositFee(slip.getFinalDepositFee()==null?ZERO:slip.getFinalDepositFee());
        if(slip.getFinalDepositFee().intValue()>(depositePaid) || depositePaid==0){
            BigDecimal amount=slip.getFinalDepositFee().subtract(new BigDecimal(depositePaid));
            if (amount.subtract(paidAmount).intValue() < 0) {
                record.setDepositPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setDepositPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }
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
        return slip;
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

        if (!request.getConfirmed())
            receipt.setConfirmed(request.getConfirmed());
        else {
            receipt.setActive(false);
            if(request.getComments()!=null)
            receipt.setComment(request.getComments());
            paymentRecordRepository.saveAndFlush(receipt);

            StudentFeePaymentRequest slip = feePaymentRepository.findOne(receipt.getRequest().getId());

            if (slip.getTotalFee().intValue() <= receipt.getRequest().getPaidAmount().intValue()) {
                slip.setPaymentStatus(PaymentStatus.Paid);
            } else if (receipt.getRequest().getPaidAmount().intValue() == 0) {
                slip.setPaymentStatus(PaymentStatus.Raised);
            } else {
                slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
            }
        slip.setAutoComments(request.getComments());
        feePaymentRepository.saveAndFlush(slip);
    }
        return receipt;
    }

}
