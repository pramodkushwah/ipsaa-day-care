package com.synlabs.ipsaa.service;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.synlabs.ipsaa.Schedular.StudentFeeResetScheduler;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.QCenter;
import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.QEmployee;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.*;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.util.QuarterYearUtil;
import com.synlabs.ipsaa.view.fee.*;
import com.synlabs.ipsaa.view.student.PaymentHistoryResponce;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.spi.PersistenceProvider;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.synlabs.ipsaa.service.BaseService.mask;
import static com.synlabs.ipsaa.service.BaseService.unmask;
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
    private StudentAttendanceService attendanceService;

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
//        if (studentFee == null)
//        {
//            throw new ValidationException(String.format("Student fee not exits for student[admission_number=%s]", student.getAdmissionNumber()));
//        }
        if(studentFee!=null)
        if(studentFee.getFeeDuration().equals(FeeDuration.Monthly) || studentFee.getFeeDuration().equals(FeeDuration.Yearly)){
            studentFee.setFeeDuration(FeeDuration.Quarterly);
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
    public StudentFee saveStudentFee(StudentFee fee) {

        CenterProgramFee centerProgramFee=centerProgramFeeRepository.findByProgramIdAndCenterId(fee.getStudent().getProgram().getId(),fee.getStudent().getCenter().getId());
        if(centerProgramFee==null){
            throw new NotFoundException("Center program fee not found");
        }
        FeeUtilsV2.validateStudentFee(fee,centerProgramFee);
        //return studentFee;
        return studentFeeRepository.saveAndFlush(fee);
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

            allslips = feePaymentRepository. findByStudentActiveIsTrueAndStudentCorporateIsFalseAndFeeDurationAndQuarterAndYearAndStudentCenterCode(FeeDuration.Quarterly, requestQuarter, requestYear,request.getCenterCode());
        return allslips;
    }

    public List<StudentFeePaymentRequest> unpaidList(Long feeId,int quarter,int year,boolean wantException){
        StudentFee fee=studentFeeRepository.findOne(feeId);
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", feeId));
        }
        Calendar cal = Calendar. getInstance();
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());
        List<StudentFeePaymentRequest> unPaidList=new ArrayList<>();
        List<StudentFeePaymentRequest> slips = feePaymentRepository.findByStudentAndFeeDuration(fee.getStudent(),FeeDuration.Quarterly);

        if(slips!=null && !slips.isEmpty()){
                for(StudentFeePaymentRequest s:slips){
                for(StudentFeePaymentRecord p:s.getPayments()){
                    if(p.getConfirmed()==null || !p.getConfirmed()){
                        unPaidList.add(s);
                        }
                }
            }
        }
        return unPaidList;
    }

    @Transactional
    public StudentFeePaymentRequest generateFeeSlip(Long feeId,int quarter,int year,boolean wantException) {
        StudentFee fee=studentFeeRepository.findOne(feeId);
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", feeId));
        }
        Calendar cal = Calendar. getInstance();
        cal.setTime(fee.getStudent().getProfile().getAdmissionDate());

        int slipCount=feePaymentRepository.countByStudentAndFeeDuration(fee.getStudent(),FeeDuration.Quarterly);
        StudentFeePaymentRequest slip=null;
        StudentFeePaymentRequest thisQuarterSlip=feePaymentRepository.findByStudentAndFeeDurationAndQuarterAndYear(fee.getStudent(),FeeDuration.Quarterly,quarter,year);
        StudentFeePaymentRequest lastQuarterSlip=feePaymentRepository.findByStudentAndFeeDurationAndQuarterAndYear(fee.getStudent(),FeeDuration.Quarterly,FeeUtilsV2.getLastQuarter(quarter,year).get("quarter"),FeeUtilsV2.getLastQuarter(quarter,year).get("year"));
        BigDecimal paidAmount=ZERO;
        BigDecimal balance=ZERO;
        boolean isAllConfirm=false;

        int unConfirmCount=0;
        if(lastQuarterSlip!=null){
            for(StudentFeePaymentRecord p:lastQuarterSlip.getPayments()){

                if(p.getActive())
                    paidAmount=paidAmount.add(p.getPaidAmount());

                    if(p.getConfirmed()==null || !p.getConfirmed()) {
                    if( p.getActive()){
                        unConfirmCount++;
                    }
                }
            }
        }
        if(unConfirmCount>0)
            isAllConfirm=false;
        else if(unConfirmCount==0){ // to check there is no old  slip found
            isAllConfirm=true;
        }
        if(lastQuarterSlip!=null)
        balance=lastQuarterSlip.getTotalFee().subtract(paidAmount);

        if(thisQuarterSlip==null && isAllConfirm) // checking old payments status too if yes then generate
        {
            slip = new StudentFeePaymentRequest();
            double extraHours=attendanceService.getLastQuarterExtraHours(fee.getStudent(),quarter,year);
            slip.setExtraHours(new BigDecimal(extraHours));
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
            slip.setAdjust(slip.getAdjust()==null?ZERO:slip.getAdjust());

            slip.setBaseFee(fee.getBaseFee());
            slip.setFinalBaseFee(fee.getFinalBaseFee());

            slip.setAnnualFee(ZERO);
            slip.setFinalAnnualCharges(ZERO);

            slip.setDeposit(ZERO);
            slip.setFinalDepositFee(ZERO);

            slip.setAdmissionFee(ZERO);
            slip.setFinalAdmissionFee(ZERO);

            slip.setAddmissionFeeDiscount(ZERO);
            slip.setAnnualFeeDiscount(ZERO);

            slip.setBaseFeeDiscount(fee.getBaseFeeDiscount()==null?ZERO:fee.getBaseFeeDiscount());

            slip.setUniformCharges(ZERO);
            slip.setStationary(ZERO);
            slip.setTransportFee(fee.getTransportFee()==null?ZERO:fee.getTransportFee());


            slip.setGstAmount(fee.getGstAmount());
            BigDecimal baseFeeRatio=THREE;
            slip.setFeeRatio(baseFeeRatio);
            if(lastQuarterSlip==null && slipCount==0) // for checking first time genration or not
                {
                    baseFeeRatio=FeeUtilsV2.calculateFeeRatioForQuarter(slip.getStudent().getProfile().getAdmissionDate(),quarter);
                    slip.setFeeRatio(baseFeeRatio);
                    slip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount()==null?ZERO:fee.getAddmissionFeeDiscount());
                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());


                    slip.setDepositFeeDiscount(fee.getDepositFeeDiscount()==null?ZERO:fee.getDepositFeeDiscount());
                    slip.setDeposit(fee.getDepositFee()==null?ZERO:fee.getDepositFee());
                    slip.setFinalDepositFee(fee.getFinalDepositFee()==null?ZERO:fee.getFinalDepositFee());

                    slip.setAnnualFee(fee.getAnnualCharges()==null?ZERO:fee.getAnnualCharges());
                    slip.setAdmissionFee(fee.getAdmissionFee()==null?ZERO:fee.getAdmissionFee());

                    slip.setFinalAnnualCharges(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());
                    slip.setFinalAdmissionFee(fee.getFinalAdmissionFee()==null?ZERO:fee.getFinalAdmissionFee());

                }else if(quarter == 2){

                    slip.setDeposit(ZERO);
                    slip.setFinalDepositFee(ZERO);
                    slip.setDepositFeeDiscount(ZERO);

                    slip.setAdmissionFee(ZERO);
                    slip.setFinalAdmissionFee(ZERO);

                    slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());

                    slip.setAnnualFee(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());

                }else{
                    slip.setDeposit(ZERO);
                    slip.setFinalDepositFee(ZERO);

                    slip.setAdmissionFee(ZERO);
                    slip.setFinalAdmissionFee(ZERO);

                    slip.setAnnualFee(ZERO);
                    slip.setFinalAnnualCharges(ZERO);
                }

                slip.setBalance(balance);
                slip.setTotalFee(FeeUtilsV2.calculateSaveFinalFee(slip,baseFeeRatio).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                slip.setFinalFee(slip.getTotalFee());
                slip.setTotalFee(slip.getTotalFee().add(balance));

                  // expiring all old slips
                     if(lastQuarterSlip!=null && !lastQuarterSlip.isExpire()){
                        lastQuarterSlip.setExpire(true);
                        feePaymentRepository.saveAndFlush(lastQuarterSlip);
                    }
            return  feePaymentRepository.saveAndFlush(slip);
        }
            else {
            if (wantException)
                throw new NotFoundException(String.format("Pay Slip Already Exist", feeId));
                return thisQuarterSlip;
            }
    }
    public StudentFeePaymentRequest regenerateFeeSlip(StudentFeeSlipRequestV2 request) {
        StudentFeePaymentRequest thisQuarterSlip=null;
        thisQuarterSlip = feePaymentRepository.findOne(request.getId());
        if(thisQuarterSlip==null){
            throw new NotFoundException(String.format("Payslip not found [%s]", request.getStudentId()));
        }
        int slipCount=feePaymentRepository.countByStudentId(thisQuarterSlip.getStudent().getId());

        StudentFee fee=studentFeeRepository.findByStudentId(thisQuarterSlip.getStudent().getId());
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", request.getStudentId()));
        }
        if (thisQuarterSlip.getPaymentStatus() == PaymentStatus.Paid ||thisQuarterSlip.getPaymentStatus() == PaymentStatus.PartiallyPaid)
        {
            throw new ValidationException("Already paid some or full amount.");
        }
         if(thisQuarterSlip.isExpire()){
             throw new ValidationException("Can Not Regenerate Expire Slip.");
         }

        if(thisQuarterSlip!=null)
        {
            thisQuarterSlip.setStudent(fee.getStudent());
            thisQuarterSlip.setReGenerateSlip(true);
            thisQuarterSlip.setExtraCharge(thisQuarterSlip.getExtraCharge()==null?ZERO:thisQuarterSlip.getExtraCharge());
            thisQuarterSlip.setLatePaymentCharge(thisQuarterSlip.getLatePaymentCharge()==null?ZERO:thisQuarterSlip.getLatePaymentCharge());

            thisQuarterSlip.setCgst(fee.getCgst());
            thisQuarterSlip.setSgst(fee.getSgst());
            thisQuarterSlip.setIgst(fee.getIgst());


            thisQuarterSlip.setBaseFee(fee.getBaseFee());
            thisQuarterSlip.setFinalBaseFee(fee.getFinalBaseFee()==null?ZERO:fee.getFinalBaseFee());

            thisQuarterSlip.setBaseFeeDiscount(fee.getBaseFeeDiscount()==null?ZERO:fee.getBaseFeeDiscount());

            thisQuarterSlip.setUniformCharges(ZERO);
            thisQuarterSlip.setStationary(ZERO);

            thisQuarterSlip.setTransportFee(fee.getTransportFee()==null?ZERO:fee.getTransportFee());

            thisQuarterSlip.setGstAmount(fee.getGstAmount());
            thisQuarterSlip.setAdjust(thisQuarterSlip.getAdjust()==null?ZERO:thisQuarterSlip.getAdjust());

            if(thisQuarterSlip.getFinalDepositFee()!=null && thisQuarterSlip.getFinalDepositFee().doubleValue()>0){
                // one time only
                thisQuarterSlip.setDepositFeeDiscount(fee.getDepositFeeDiscount()==null?ZERO:fee.getDepositFeeDiscount());
                thisQuarterSlip.setDeposit(fee.getDepositFee()==null?ZERO:fee.getDepositFee());
                thisQuarterSlip.setFinalDepositFee(fee.getFinalDepositFee()==null?ZERO:fee.getFinalDepositFee());
            }else{
                // one time only
                thisQuarterSlip.setDepositFeeDiscount(ZERO);
                thisQuarterSlip.setDeposit(ZERO);
                thisQuarterSlip.setFinalDepositFee(ZERO);
            }
            BigDecimal baseFeeRatio=THREE;
            thisQuarterSlip.setFeeRatio(baseFeeRatio);
            // to check is it new addmission
            if(slipCount==1){
                    baseFeeRatio=FeeUtilsV2.calculateFeeRatioForQuarter(thisQuarterSlip.getStudent().getProfile().getAdmissionDate(),thisQuarterSlip.getQuarter());
                    thisQuarterSlip.setFeeRatio(baseFeeRatio);
                 thisQuarterSlip.setAdmissionFee(fee.getAdmissionFee()==null?ZERO:fee.getAdmissionFee());
                 thisQuarterSlip.setAddmissionFeeDiscount(fee.getAddmissionFeeDiscount()==null?ZERO:fee.getAddmissionFeeDiscount());
                 thisQuarterSlip.setFinalAdmissionFee(fee.getFinalAdmissionFee()==null?ZERO:fee.getFinalAdmissionFee());
             }else{
                 thisQuarterSlip.setAdmissionFee(ZERO);
                 thisQuarterSlip.setAddmissionFeeDiscount(ZERO);
                 thisQuarterSlip.setFinalAdmissionFee(ZERO);
             }
             if(thisQuarterSlip.getQuarter()==2 || (thisQuarterSlip.getFinalAnnualCharges()!=null &&thisQuarterSlip.getFinalAnnualCharges().intValue()>0)  ){
                 thisQuarterSlip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount()==null?ZERO:fee.getAnnualFeeDiscount());
                 thisQuarterSlip.setAnnualFee(fee.getAnnualCharges()==null?ZERO:fee.getAnnualCharges());
                 thisQuarterSlip.setFinalAnnualCharges(fee.getFinalAnnualCharges()==null?ZERO:fee.getFinalAnnualCharges());
             }else{
                 thisQuarterSlip.setAnnualFee(ZERO);
                 thisQuarterSlip.setAnnualFeeDiscount(ZERO);
                 thisQuarterSlip.setFinalAnnualCharges(ZERO);
             }

             thisQuarterSlip.setTotalFee(FeeUtilsV2.calculateReGenrateFinalFee(thisQuarterSlip,thisQuarterSlip.getFeeRatio()));
             thisQuarterSlip.setFinalFee(thisQuarterSlip.getTotalFee()); // without balance
             thisQuarterSlip.setTotalFee(thisQuarterSlip.getTotalFee()
                            .add(thisQuarterSlip.getBalance()==null?ZERO:thisQuarterSlip.getBalance()));

             return  feePaymentRepository.saveAndFlush(thisQuarterSlip);
        }
        else {
                throw new NotFoundException(String.format("Pay Slip not found", request.getStudentId()));
        }
    }

    public StudentFeePaymentRequest regenerateFeeSlipV2(StudentFeeSlipRequestV2 request){
        StudentFeePaymentRequest thisQuarterSlip=null;
        thisQuarterSlip = feePaymentRepository.findOne(request.getId());
        if(thisQuarterSlip==null){
            throw new NotFoundException(String.format("Payslip not found [%s]", request.getStudentId()));
        }
        StudentFee fee=studentFeeRepository.findByStudentId(thisQuarterSlip.getStudent().getId());
        if(fee==null){
            throw new NotFoundException(String.format("Student fee not found [%s]", request.getStudentId()));
        }
        if(thisQuarterSlip.isExpire()){
            throw new ValidationException("Can Not Regenerate Expire Slip.");
        }


        Date regenrationDate;
        if(request.getSpaceifyRegenrationDate()==null){
            Calendar cal =Calendar.getInstance();
            regenrationDate=cal.getTime();
        }else{
            try {
                regenrationDate=request.parseDate(request.getSpaceifyRegenrationDate());
            } catch (ParseException e) {
                throw new ValidationException("error in date");
            }
        }

        // test case
        Calendar cal =Calendar.getInstance();
        cal.set(2018,9-1,14);
        regenrationDate=cal.getTime();
        BigDecimal nextRatio=FeeUtilsV2.calculateNextFeeRatioForQuarter(regenrationDate);
        // hander fee chnage or discount change in running quarter
        int newBaseFee=fee.getBaseFee().intValue();
        int oldBaseFee=thisQuarterSlip.getBaseFee().intValue();

        int newFinalBaseFee=fee.getFinalBaseFee().divide(THREE,2,BigDecimal.ROUND_CEILING).intValue();
        int oldFinalBaseFee=thisQuarterSlip.getFinalBaseFee().divide(thisQuarterSlip.getFeeRatio(),2,BigDecimal.ROUND_CEILING).intValue();

        thisQuarterSlip.setTotalFee(thisQuarterSlip.getTotalFee().subtract(thisQuarterSlip.getFinalFee()));

        if(newBaseFee!=oldBaseFee ||
                newFinalBaseFee!=oldFinalBaseFee){
            // base fee change handel
            thisQuarterSlip.setFinalBaseFee(thisQuarterSlip.getFinalBaseFee().divide(thisQuarterSlip.getFeeRatio(),2,BigDecimal.ROUND_CEILING));


            thisQuarterSlip.setFinalFee(thisQuarterSlip.getFinalFee()
                        .subtract(thisQuarterSlip.getFinalBaseFee().multiply(nextRatio))
                );
            thisQuarterSlip.setFinalFee(thisQuarterSlip.getFinalFee().add(fee.getFinalBaseFee().multiply(nextRatio)));
            BigDecimal gstAmmount=ZERO;

            // handel chnage in gst
            if (fee.getIgst() != null && fee.getIgst().intValue() != 0) {
                gstAmmount = FeeUtilsV2.calculateGST(thisQuarterSlip.getFinalBaseFee(), null, GST.IGST);
                fee.setGstAmount(thisQuarterSlip.getGstAmount().subtract(gstAmmount.multiply(nextRatio)));
                gstAmmount = FeeUtilsV2.calculateGST(fee.getFinalBaseFee(), null, GST.IGST);
                fee.setGstAmount(thisQuarterSlip.getGstAmount().add(gstAmmount.multiply(nextRatio)));
            } else {
                fee.setGstAmount(ZERO);
            }

            thisQuarterSlip.setAutoComments(thisQuarterSlip.getAutoComments()+" "+"change in transport fee" +thisQuarterSlip.getBaseFee() +" to "+ fee.getBaseFee());
        }
        if(fee.getTransportFee().intValue()!=thisQuarterSlip.getTransportFee().intValue()){
        // transport fee change handel
            thisQuarterSlip.setFinalFee(thisQuarterSlip.getFinalFee()
                    .subtract(thisQuarterSlip.getTransportFee().multiply(nextRatio))
            );
            thisQuarterSlip.setFinalFee(thisQuarterSlip.getFinalFee().add(fee.getTransportFee().multiply(nextRatio)));
            thisQuarterSlip.setTransportFee(fee.getTransportFee());
            thisQuarterSlip.setFinalTransportFee(fee.getTransportFee().multiply(thisQuarterSlip.getFeeRatio()));

            thisQuarterSlip.setAutoComments(thisQuarterSlip.getAutoComments()+" "+"change in transport fee" +thisQuarterSlip.getTransportFee() +" to " +fee.getTransportFee());
        }
        thisQuarterSlip.setTotalFee(thisQuarterSlip.getTotalFee().add(thisQuarterSlip.getFinalFee()));
        return  feePaymentRepository.saveAndFlush(thisQuarterSlip);
    }


    public StudentFeePaymentRequest generateFirstFeeSlip(Long slipId){
        Calendar cal = Calendar.getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH)+1);
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
                .where(qStudentFee.student.active.isTrue())
                .where(qStudentFee.student.corporate.isFalse())
                .where(qStudentFee.student.center.code.eq(request.getCenterCode()));

        List<StudentFee> feelist = query.fetch();
        List<StudentFeePaymentRequest> allslips = new LinkedList<>();

        int requestQuarter = request.getQuarter();
        int requestYear = request.getYear();

        for(StudentFee studentFee:feelist){
//            if(studentFee.getStudent().getProgram().getId()!=FeeUtilsV2.IPSAA_CLUB_REGULAR_PROGRAM_ID &&
//                    studentFee.getStudent().getProgram().getId()!=FeeUtilsV2.IPSAA_CLUB_PROGRAM_ID){
                StudentFeePaymentRequest req=this.generateFeeSlip(studentFee.getId(),requestQuarter,requestYear,false);
                if(req!=null)
                allslips.add(req);
   //         }
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
    public void regenerateStudentSlipAll(List<Long> ids) {
        for(Long id:ids){
            StudentFeeSlipRequestV2 request=new StudentFeeSlipRequestV2();
            request.setId(id);
            try{
                this.regenerateFeeSlip(request);
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }
    }

    public List<StudentFee> listAllFee() {
        QStudentFee fee = QStudentFee.studentFee;
        JPAQuery<StudentFee> query = new JPAQuery<>(entityManager);
        query.select(fee)
                .from(fee)
                .where(fee.student.active.isTrue())
                .where(fee.feeDuration.eq(FeeDuration.Quarterly))
                .where(fee.student.corporate.isFalse());
            return query.fetch();
    }
    @Transactional
    public void resetStudentFee(){
        List<StudentFee> fees=listAllFee();
        for(StudentFee fee:fees){
            // it will not reset ipsa club student fee
            if(fee.getStudent().getProgram().getId()!=FeeUtilsV2.IPSAA_CLUB_PROGRAM_ID && fee.getStudent().getProgram().getId()!=FeeUtilsV2.IPSAA_CLUB_PROGRAM_ID){
            CenterProgramFee centerProgramFee=centerProgramFeeRepository.findByProgramIdAndCenterId(fee.getStudent().getProgram().getId(),fee.getStudent().getCenter().getId());
            try{
                if(centerProgramFee==null){
                    throw new NotFoundException("Center program fee not found");
                }

                fee.setBaseFee(new BigDecimal(centerProgramFee.getFee()));
                fee.setAdmissionFee(centerProgramFee.getAddmissionFee()==null?ZERO:centerProgramFee.getAddmissionFee());
                fee.setDepositFee(new BigDecimal(centerProgramFee.getDeposit()));
                fee.setAnnualCharges(new BigDecimal(centerProgramFee.getAnnualFee()));
                fee.setFinalFee(ZERO);

                fee.setBaseFeeDiscount(ZERO);
                fee.setAnnualFeeDiscount(ZERO);
                fee.setAddmissionFeeDiscount(ZERO);
                fee.setDepositFeeDiscount(ZERO);

                fee.setFinalBaseFee(fee.getBaseFee());
                fee.setFinalDepositFee(fee.getDepositFee());
                fee.setFinalAdmissionFee(fee.getAdmissionFee());
                fee.setFinalAnnualCharges(fee.getAnnualCharges());

                if(fee.getStudent().isFormalSchool())
                    fee.setFinalFee(FeeUtilsV2.calculateFinalFee(fee,true));
                else
                    fee.setFinalFee(FeeUtilsV2.calculateFinalFee(fee,false));
                    studentFeeRepository.save(fee);
            }catch(Exception e){
                logger.error(String.format("Student Fee scheduler, program center not found error .%s",fee));
            }
            }
        }
    }
    public StudentFeePaymentRequest regenerateStudentSlip(StudentFeeSlipRequestV2 request){
        if(request.getSpaceifyRegenrationDate()==null){
            return this.regenerateFeeSlip(request);
        }else{
            return this.regenerateFeeSlipV2(request);
        }
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
        if(slip.isExpire()){
            throw new ValidationException("You can't update expire pay slip.");
        }
        slip.setUniformCharges(request.getUniformCharges()==null?ZERO:request.getUniformCharges());
        slip.setStationary(request.getStationary()==null?ZERO:request.getStationary());
        slip.setComments(request.getComments());
        if(request.getBalance()!=null){
            slip.setBalance(request.getBalance());
        }
        if(request.getExtraCharge()!=null)
            slip.setExtraCharge(request.getExtraCharge());
        if(request.getLatePaymentCharge()!=null)
            slip.setLatePaymentCharge(request.getLatePaymentCharge());
        if(request.getAdjust()!=null){
            slip.setAdjust(request.getAdjust());
        }

        slip.setTotalFee(FeeUtilsV2.calculateSaveFinalFee(slip,slip.getFeeRatio()));
        slip.setTotalFee(slip.getTotalFee().add(slip.getBalance()));
       return feePaymentRepository.saveAndFlush(slip);
    }
    public StudentFeePaymentRequest updateSlipHard(StudentFeeSlipRequestV2 request) {
        StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());
        if (slip == null)
        {
            throw new NotFoundException("Missing slip");
        }

        if (slip.getPaymentStatus() == PaymentStatus.Paid)
        {
            throw new ValidationException("Already paid.");
        }
        slip.setUniformCharges(request.getUniformCharges()==null?ZERO:request.getUniformCharges());
        slip.setStationary(request.getStationary()==null?ZERO:request.getStationary());

        if(request.getExtraCharge()!=null)
            slip.setExtraCharge(request.getExtraCharge());
        if(request.getLatePaymentCharge()!=null)
            slip.setLatePaymentCharge(request.getLatePaymentCharge());
        if(request.getAdjust()!=null){
            slip.setAdjust(request.getAdjust());
        }

        slip.setTotalFee(FeeUtilsV2.calculateSaveFinalFee(slip,slip.getFeeRatio()));
        slip.setTotalFee(slip.getTotalFee().add(slip.getBalance()));
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


        if(!request.getPaymentMode().equals(PaymentMode.Cash)){
            if(request.getTxnid()==null){
                throw new ValidationException(" Reference number is missing.");
            }
        }

        StudentFeePaymentRequest slip = feePaymentRepository.findOne(request.getId());

        if (slip == null)
        {
            throw new NotFoundException("Missing slip");
        }
        if(slip.isExpire()){
            throw new ValidationException("can not update expire slip");
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
             alreadypaid+=payments.getPaidAmount().doubleValue();;

            if(payments.getUniformPaidAmount()!=null && payments.getActive())
             uniformPaid+=payments.getUniformPaidAmount().doubleValue();

            if(payments.getStationaryPaidAmount()!=null && payments.getActive())
            stationaryPaid+=payments.getStationaryPaidAmount().doubleValue();;

            if(payments.getTransportPaidAmount()!=null && payments.getActive())
            transportPaid+=payments.getTransportPaidAmount().doubleValue();;

            if(payments.getAddmissionPaidAmount()!=null && payments.getActive())
            admissionPaid=+payments.getAddmissionPaidAmount().doubleValue();;

            if(payments.getAnnualPaidAmount()!=null && payments.getActive())
            annualPaid+=payments.getAnnualPaidAmount().doubleValue();;

            if(payments.getProgramPaidAmount()!=null && payments.getActive())
            programFeePaid+=payments.getProgramPaidAmount().doubleValue();;

            if(payments.getDepositPaidAmount()!=null && payments.getActive())
            depositePaid+=payments.getDepositPaidAmount().doubleValue();;
        }
        slip.setComments(request.getComments());
        slip.setReGenerateSlip(true);
        slip.setReceiptSerial(null);
        slip.setReceiptFileName(null);

        if (slip.getTotalFee().doubleValue() <= (alreadypaid + request.getPaidAmount().doubleValue()))
        {
            slip.setPaymentStatus(PaymentStatus.Paid);
            if(alreadypaid + request.getPaidAmount().intValue()>slip.getTotalFee().intValue()){
                //slip.setBalance(slip.getBalance().add(request.getPaidAmount().add(new BigDecimal(alreadypaid)).subtract(slip.getTotalFee())));
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

        if(slip.getUniformCharges().doubleValue()>(uniformPaid) || uniformPaid==0){
            BigDecimal amount=slip.getUniformCharges().subtract(new BigDecimal(uniformPaid));
                if(amount.subtract(paidAmount).doubleValue()<0){
                    record.setUniformPaidAmount(amount);
                    paidAmount=paidAmount.subtract(amount);
                }else{
                    record.setUniformPaidAmount(paidAmount);
                    paidAmount=ZERO;
                }
        }
        slip.setStationary(slip.getStationary()==null?ZERO:slip.getStationary());
        if(slip.getStationary().doubleValue()>(stationaryPaid) || stationaryPaid==0 ){
            BigDecimal amount=slip.getStationary().subtract(new BigDecimal(stationaryPaid));
            if(amount.subtract(paidAmount).doubleValue()<0){
                record.setStationaryPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setStationaryPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setFinalTransportFee(slip.getFinalTransportFee()==null?ZERO:slip.getFinalTransportFee());
        if(slip.getFinalTransportFee().doubleValue()>(transportPaid) || transportPaid==0) {
            BigDecimal amount = slip.getFinalTransportFee().subtract(new BigDecimal(transportPaid));
            if (amount.subtract(paidAmount).doubleValue() < 0) {
                record.setTransportPaidAmount(amount);
                paidAmount = paidAmount.subtract(amount);
            } else {
                record.setTransportPaidAmount(paidAmount);
                paidAmount = ZERO;
            }
        }

        slip.setFinalAnnualCharges(slip.getFinalAnnualCharges()==null?ZERO:slip.getFinalAnnualCharges());
        if(slip.getFinalAnnualCharges().doubleValue()>(annualPaid)|| annualPaid==0)  {
            BigDecimal amount = slip.getFinalAnnualCharges().subtract(new BigDecimal(annualPaid));
            if (amount.subtract(paidAmount).doubleValue() < 0) {
                record.setAnnualPaidAmount(amount);
                paidAmount = paidAmount.subtract(amount);
            } else {
                record.setAnnualPaidAmount(paidAmount);
                paidAmount = ZERO;
            }
        }

        slip.setFinalAdmissionFee(slip.getFinalAdmissionFee()==null?ZERO:slip.getFinalAdmissionFee());
        if(slip.getFinalAdmissionFee().doubleValue()>(admissionPaid) || admissionPaid==0){
            BigDecimal amount=slip.getFinalAdmissionFee().subtract(new BigDecimal(admissionPaid));
            if (amount.subtract(paidAmount).doubleValue() < 0) {
                record.setAddmissionPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setAddmissionPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setFinalBaseFee(slip.getFinalBaseFee()==null?ZERO:slip.getFinalBaseFee());
        if(slip.getFinalBaseFee().doubleValue()>(programFeePaid) || programFeePaid==0){  // final base fee
            BigDecimal amount=slip.getFinalBaseFee().subtract(new BigDecimal(programFeePaid));
            if (amount.subtract(paidAmount).doubleValue() < 0) {
                record.setProgramPaidAmount(amount);
                paidAmount=paidAmount.subtract(amount);
            }else{
                record.setProgramPaidAmount(paidAmount);
                paidAmount=ZERO;
            }
        }

        slip.setFinalDepositFee(slip.getFinalDepositFee()==null?ZERO:slip.getFinalDepositFee());
        if(slip.getFinalDepositFee().doubleValue()>(depositePaid) || depositePaid==0){
            BigDecimal amount=slip.getFinalDepositFee().subtract(new BigDecimal(depositePaid));
            if (amount.subtract(paidAmount).doubleValue() < 0) {
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

        if (receipt.getConfirmed() != null && receipt.getConfirmed() && !receipt.getActive()) {
            throw new ValidationException("Confirmed Receipt cannot update.");
        }


        StudentFeePaymentRequest slip = feePaymentRepository.findOne(receipt.getRequest().getId());

        if(slip==null){
            throw  new ValidationException("slip not found");
        }

        if (request.getConfirmed()){
            receipt.setActive(true);
            receipt.setConfirmed(request.getConfirmed());
            logger.info(String.format("Student Fee payment confirm .%s",slip));
        }
        else {
            receipt.setActive(false);
            if(request.getComments()==null){
                throw new ValidationException("Comment is missing");
            }
            receipt.setComment(request.getComments());
            if(receipt.getPaymentMode().equals(PaymentMode.Cheque)){
                slip.setExtraCharge(slip.getExtraCharge().add(FeeUtilsV2.CHEQUE_BOUNCE_CHARGE));
                if(slip.getAutoComments()==null)
                slip.setAutoComments("200rs Cheque bounce charges added");
                else{
                    slip.setAutoComments(slip.getAutoComments()+",200rs Cheque bounce charges added");
                }
                slip.setTotalFee(FeeUtilsV2.calculateFinalFee(slip));;
            }

            paymentRecordRepository.saveAndFlush(receipt);

            if (slip.getTotalFee().intValue() <= receipt.getRequest().getPaidAmount().intValue()) {
                slip.setPaymentStatus(PaymentStatus.Paid);
            } else if (receipt.getRequest().getPaidAmount().intValue() == 0) {
                slip.setPaymentStatus(PaymentStatus.Raised);
            } else {
                slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
            }
            logger.info(String.format("Student Fee payment rejected .%s",slip));
        }
        //slip.setComments(request.getComments());

        feePaymentRepository.saveAndFlush(slip);
        return receipt;
    }

    public StudentFeePaymentRequest getStudentBalance(Student student) {
        Calendar cal = Calendar. getInstance();
        int quarter=FeeUtilsV2.getQuarter(cal.get(Calendar.MONTH));
        int year=cal.get(Calendar.YEAR);
        return feePaymentRepository.findOneByStudentAndFeeDurationAndQuarterAndYear(student,FeeDuration.Quarterly,quarter,year);
    }

    public PaymentHistoryResponce getStudentPaymentHistory(Long studentId) {
        Student student=studentRepository.findOne(studentId);
        if(student==null){
           throw new NotFoundException(String.format("Student no found [%s]",studentId));
        }
        List<StudentFeePaymentRequest> slips = feePaymentRepository.findByStudentAndFeeDuration(student,FeeDuration.Quarterly);
        return new PaymentHistoryResponce(slips);
    }

    public void updateExtraHours() {

        QStudentFeePaymentRequest qStudentFeePaymentRequest = QStudentFeePaymentRequest.studentFeePaymentRequest;
        JPAQuery<StudentFeePaymentRequest> query = new JPAQuery<>(entityManager);
        query.select(qStudentFeePaymentRequest)
                .from(qStudentFeePaymentRequest)
                .where(qStudentFeePaymentRequest.quarter.eq(4)
                        .and(qStudentFeePaymentRequest.year.eq(2018)));

        List<StudentFeePaymentRequest> feelist = query.fetch();

        feelist.forEach(new Consumer<StudentFeePaymentRequest>() {
            @Override
            public void accept(StudentFeePaymentRequest studentFeePaymentRequest) {
                double extraHours=attendanceService.getLastQuarterExtraHours(studentFeePaymentRequest.getStudent(),4,2018);
                System.out.println("from "+studentFeePaymentRequest.getExtraHours() +" to "+ extraHours);
                studentFeePaymentRequest.setExtraHours(new BigDecimal(extraHours));
                feePaymentRepository.saveAndFlush(studentFeePaymentRequest);
            }
        });
    }

//    public static final QStudentFeePaymentRequest fees=new QStudentFeePaymentRequest("fees");

//    public void setExpire(){
//
//        JPAQuery<StudentFeePaymentRequest> query = new JPAQuery<>(entityManager);
//        QStudentFeePaymentRequest fees = QStudentFeePaymentRequest.studentFeePaymentRequest;
//        QStudent student=QStudent.student;
//        QCenter center=QCenter.center;
//
//
//        List<Long> centers= new ArrayList<>();
//        centers.add(Long.valueOf(54));
//
////        List<StudentFeePaymentRequest> feeList= query.select(fees).from(fees).innerJoin(student).innerJoin(center)
////                .where(fees.year.eq(2018).and(fees.quarter.eq(3).and(center.id.in(centers))))
////                .fetch();
////
//        List<StudentFeePaymentRequest> feeList=feePaymentRepository.findByStudentCenterIdInAndQuarterAndYear(centers,3,2018);
//        int count=0;
//        System.out.println(feeList.size());
//        for(StudentFeePaymentRequest fee:feeList){
//            System.out.println(count);
//            if(fee.isExpire()){
//                fee.setExpire(false);
//                feePaymentRepository.saveAndFlush(fee);
//                count++;
//            }
//        }
//
//
//
//    }
}
