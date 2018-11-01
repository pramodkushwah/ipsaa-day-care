package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRecordIpsaaClubRepository;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRequestIpsaaClubRepository;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.fee.SaveFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequestV2;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipRequest;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipResponce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.synlabs.ipsaa.service.BaseService.mask;
import static com.synlabs.ipsaa.util.FeeUtils.ZERO;

@Service
public class IpsaaClubFeeSerivce {

    @Autowired
    StudentFeePaymentRequestIpsaaClubRepository studentFeePaymentRequestIpsaaClubRepository;
    @Autowired
    StudentFeePaymentRecordIpsaaClubRepository studentFeePaymentRecordIpsaaClubRepository;
    @Autowired
    StudentAttendanceService studentAttendanceService;
    @Autowired
    private DocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    StudentFeeRepository feeRepository;

    public List<StudentFeePaymentRequestIpsaaClub> listFeeSlips(IpsaaClubSlipRequest request) {
        List<StudentFeePaymentRequestIpsaaClub> allslips = new LinkedList<>();
        allslips = studentFeePaymentRequestIpsaaClubRepository.findByMonthAndYearOrderByCreatedDateDesc(10,2018);
        return allslips;
    }

    @Transactional
    public IpsaaClubSlipResponce generateSlip(Long id) {
        StudentFee fee = feeRepository.findByStudentId(id);
        if (fee == null)
           throw  new ValidationException("fee not found");

        List<StudentFeePaymentRequestIpsaaClub> slips = studentFeePaymentRequestIpsaaClubRepository.findByStudentIdOrderByCreatedDateDesc(fee.getStudent().getId());
        StudentFeePaymentRequestIpsaaClub oldSlip;

        if (slips == null || slips.isEmpty()) {
            // first fee
           StudentFeePaymentRequestIpsaaClub firstSlip= generateFirstSlip(fee);
            return new IpsaaClubSlipResponce(firstSlip);
        } else {
            // new slip
            oldSlip=slips.get(0);
            StudentFeePaymentRequestIpsaaClub newSlip=generateNewSlip(oldSlip,fee);
            return new IpsaaClubSlipResponce(newSlip);
        }
    }
    @Transactional
    private StudentFeePaymentRequestIpsaaClub generateNewSlip(StudentFeePaymentRequestIpsaaClub lastSlip, StudentFee fee) {
//        if((lastSlip.getInvoiceDate().getTime()-(new Date()).getTime())/60/60/24<1)
//        {
//            throw  new ValidationException("can not generate same day from last generate date");
//        }

        if (fee != null) {

            StudentFeePaymentRequestIpsaaClub newslip=startNewSession(lastSlip,fee);
            Date startDate=lastSlip.getExpireDate();

            newslip = generateFeeFrom(startDate, fee, newslip); // generate fee from invoice to today

            newslip.setGstAmount(FeeUtilsV2.calculateGST(newslip.getTotalDaysFee(),newslip.getFinalAnnualFee(),GST.IGST));
            newslip.setFinalFee(FeeUtilsV2.calculateIpsaaClubFinalFee(newslip));

            // check old payments is confrim or not
            boolean isConfirm=true;
            if(lastSlip!=null)
                isConfirm=this.isConfrom(lastSlip.getPayments());
            if(!isConfirm){
                throw new ValidationException("your old slip payment is not confirm");
            }else{
                BigDecimal balance=lastSlip.getTotalFee().subtract(lastSlip.getPaidAmount());
                newslip.setBalance(balance);
            }
            newslip.setTotalFee(newslip.getFinalFee().add(newslip.getBalance()));
            studentFeePaymentRequestIpsaaClubRepository.saveAndFlush(newslip);

            studentFeePaymentRequestIpsaaClubRepository.saveAndFlush(lastSlip);
            lastSlip.setExpire(true);
        return newslip;
        } else
            throw new NotFoundException("student fee not found");
    }

    private StudentFeePaymentRequestIpsaaClub startNewSession(StudentFeePaymentRequestIpsaaClub lastSlip,StudentFee fee) {
        StudentFeePaymentRequestIpsaaClub newSlip= new StudentFeePaymentRequestIpsaaClub();

        newSlip.setInvoiceDate(lastSlip.getExpireDate()); // for first newSlips start date
        newSlip.setExpireDate(new Date());

        newSlip.setStudent(fee.getStudent());

        newSlip.setFeeDuration(FeeDuration.Monthly);
        newSlip.setAnnualFee(ZERO);
        newSlip.setAnnualFeeDiscount(ZERO);
        newSlip.setFinalAnnualFee(ZERO);
        newSlip.setBaseFee(ZERO);
        newSlip.setFinalBaseFee(ZERO);
        newSlip.setBaseFeeDiscount(ZERO);

        newSlip.setDeposit(ZERO);
        newSlip.setDepositFeeDiscount(ZERO);
        newSlip.setFinalDepositFee(ZERO);

        newSlip.setMonth(LocalDate.now().getMonth().getValue());
        newSlip.setYear(LocalDate.now().getYear());


        newSlip.setBalance(ZERO);
        newSlip.setTotalDaysFee(ZERO);

        newSlip.setFinalFee(FeeUtilsV2.calculateIpsaaClubFinalFee(newSlip));
        newSlip.setTotalFee(newSlip.getFinalFee().add(newSlip.getBalance()));
        newSlip.setPaymentStatus(PaymentStatus.Raised);
        return newSlip;
    }

    private StudentFeePaymentRequestIpsaaClub generateFirstSlip(StudentFee fee) {

        StudentFeePaymentRequestIpsaaClub newSlip= new StudentFeePaymentRequestIpsaaClub();
        newSlip.setInvoiceDate(new Date()); // for first newSlips start date
        newSlip.setExpireDate(new Date());
        newSlip.setStudent(fee.getStudent());

        newSlip.setFeeDuration(FeeDuration.Monthly);

        newSlip.setAnnualFee(fee.getAnnualCharges());
        newSlip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());
        newSlip.setFinalAnnualFee(fee.getFinalAnnualCharges());

        newSlip.setBaseFee(ZERO);
        newSlip.setFinalBaseFee(ZERO);
        newSlip.setBaseFeeDiscount(ZERO);

        newSlip.setDeposit(fee.getDepositFee());
        newSlip.setDepositFeeDiscount(fee.getDepositFeeDiscount());
        newSlip.setFinalDepositFee(fee.getFinalDepositFee());

        newSlip.setMonth(LocalDate.now().getMonth().getValue());
        newSlip.setYear(LocalDate.now().getYear());

        newSlip.setBalance(ZERO);
        newSlip.setTotalDaysFee(ZERO);

        newSlip.setGstAmount(FeeUtilsV2.calculateGST(newSlip.getTotalDaysFee(),newSlip.getFinalAnnualFee(),GST.IGST));

        newSlip.setFinalFee(FeeUtilsV2.calculateIpsaaClubFinalFee(newSlip));
        newSlip.setTotalFee(newSlip.getFinalFee().add(newSlip.getBalance()));

        newSlip.setPaymentStatus(PaymentStatus.Raised);

        return studentFeePaymentRequestIpsaaClubRepository.saveAndFlush(newSlip);
    }

    private boolean isConfrom(List<StudentFeePaymentRecordIpsaaClub> payments) {
        for(StudentFeePaymentRecordIpsaaClub payment:payments){
            if(payment.getActive()){
                if(!payment.getConfirmed()){
                    return false;
                }
            }
        }
        return true;
    }

    private StudentFeePaymentRequestIpsaaClub generateFeeFrom(Date startdate, StudentFee fee, StudentFeePaymentRequestIpsaaClub slip) {

        Date endDate=new Date();
        Map<String, Integer> counts = getStudentAttendanceFromTo(startdate, endDate, fee.getStudent()); // gettting attendace from invoice day to now
        slip.setTotalNoOfDays(counts.get("total"));
        slip.setNoOfFullDays(counts.get("fullday"));
        slip.setNoOfHalfDays(counts.get("halfday"));

        slip.setNoOfHalfDays(4);
        slip.setNoOfFullDays(3);
        slip.setNoOfFullDays(7);

        slip.setBaseFeeDiscount(fee.getBaseFeeDiscount());
        slip.setBaseFee(fee.getBaseFee());
        slip.setFinalBaseFee(fee.getFinalBaseFee());
        BigDecimal halfDayFee = fee.getFinalBaseFee().multiply(new BigDecimal(slip.getNoOfHalfDays())).multiply(new BigDecimal(0.60));
        BigDecimal fullDayFee = fee.getFinalBaseFee().multiply(new BigDecimal(slip.getNoOfFullDays()));

        BigDecimal totalFee = halfDayFee.add(fullDayFee);
        slip.setTotalDaysFee(totalFee);
        return slip;
    }

    private Map<String, Integer> getStudentAttendanceFromTo(Date lastGenerationDate, Date today, Student student) {
        return studentAttendanceService.getAttendanceFromTo(lastGenerationDate, today, student);
    }

    public IpsaaClubSlipResponce getStudentSlip(Long id) {
        List<StudentFeePaymentRequestIpsaaClub> studentFeePaymentRequestIpsaaClub = studentFeePaymentRequestIpsaaClubRepository.findByStudentIdOrderByCreatedDateDesc(id);
        if (studentFeePaymentRequestIpsaaClub != null)
            return new IpsaaClubSlipResponce(studentFeePaymentRequestIpsaaClub.get(0));
        else {
            throw new ValidationException("slip not found");
        }
    }

    @Transactional
    public StudentFeePaymentRecordIpsaaClub payFee(SaveFeeSlipRequest request) {
        if (request.getPaidAmount() == null) {
            throw new ValidationException("Paid amount is missing");
        }

        if (StringUtils.isEmpty(request.getPaymentMode())) {
            throw new ValidationException("Payment mode is missing.");
        }


        if (!request.getPaymentMode().equals(PaymentMode.Cash)) {
            if (request.getTxnid() == null) {
                throw new ValidationException(" Reference number is missing.");
            }
        }
        StudentFeePaymentRequestIpsaaClub slip = studentFeePaymentRequestIpsaaClubRepository.findOne(request.getId());
        if (slip == null) {
            throw new NotFoundException("Missing slip");
        }
        slip.setComments(request.getComments());
        slip.setReceiptSerial(null);
        slip.setReceiptFileName(null);
        BigDecimal alreadyPaid = slip.getPaidAmount()==null?ZERO:slip.getPaidAmount();
        BigDecimal outStanding=slip.getTotalFee().subtract(alreadyPaid);

        if (request.getPaidAmount().doubleValue() >= outStanding.doubleValue()) {
            slip.setPaymentStatus(PaymentStatus.Paid);
        } else {
            slip.setPaymentStatus(PaymentStatus.PartiallyPaid);
        }
        StudentFeePaymentRecordIpsaaClub record=new StudentFeePaymentRecordIpsaaClub();
        record.setActive(true);
        record.setRequest(slip);
        record.setStudent(slip.getStudent());
        record.setPaidAmount(request.getPaidAmount());
        record.setPaymentMode(request.getPaymentMode());
        record.setTxnid(request.getTxnid());
        record.setPaymentStatus(PaymentStatus.Paid);
        record.setConfirmed(request.getConfirmed());
        record.setPaymentDate(request.getPaymentDate() == null ? new Date() : request.getPaymentDate());
        studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(record);
        logger.info(String.format("Student Fee payment recoded successfully.%s", slip));
        //documentService.generateFeeReceiptPdf(record);
        return record;
    }

    // use to confirm or reject payment
    public StudentFeePaymentRequestIpsaaClub updateSlip(StudentFeeSlipRequestV2 request) {
        StudentFeePaymentRequestIpsaaClub slip = studentFeePaymentRequestIpsaaClubRepository.findOne(request.getId());
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
        slip.setComments(request.getComments());
        if(request.getBalance()!=null){
            slip.setBalance(request.getBalance());
        }
        if(request.getExtraCharge()!=null){
            slip.setTotalFee(slip.getTotalFee().subtract(slip.getExtraCharge()==null?ZERO:slip.getExtraCharge()));
            slip.setExtraCharge(request.getExtraCharge());
            slip.setTotalFee(slip.getTotalFee().add(slip.getExtraCharge()));
        }
        return studentFeePaymentRequestIpsaaClubRepository.saveAndFlush(slip);
    }
    // use to confirm or reject payment
    public StudentFeePaymentRecordIpsaaClub updatePayFee(SaveFeeSlipRequest request) {

        if (request.getId() == null) {
            throw new ValidationException("Receipt id is required.");
        }

        StudentFeePaymentRecordIpsaaClub receipt = studentFeePaymentRecordIpsaaClubRepository.findOne(request.getId());
        StudentFeePaymentRequestIpsaaClub slip =receipt.getRequest();
        if (receipt == null) {
            throw new ValidationException(String.format("Cannot locate Receipt[id = %s]", mask(request.getId())));
        }

        if (receipt.getConfirmed() != null && receipt.getConfirmed() && !receipt.getActive()) {
            throw new ValidationException("Confirmed Receipt cannot update.");
        }

        if (request.getConfirmed()){
            receipt.setActive(true);
            receipt.setConfirmed(request.getConfirmed());
            logger.info(String.format("Student Fee payment confirm .%s",receipt.getId()));
        }
        else {
            receipt.setActive(false);
            if(request.getComments()==null){
                throw new ValidationException("Comment is missing");
            }
            receipt.setComment(request.getComments());
            if(receipt.getPaymentMode().equals(PaymentMode.Cheque)){
                slip.setTotalFee(slip.getTotalFee().subtract(slip.getExtraCharge()));
                slip.setExtraCharge(slip.getExtraCharge().add(FeeUtilsV2.CHEQUE_BOUNCE_CHARGE));
                if(slip.getAutoComments()==null)
                    slip.setAutoComments("200rs Cheque bounce charges added");
                else{
                    slip.setAutoComments(slip.getAutoComments()+",200rs Cheque bounce charges added");
                }
                slip.setTotalFee(slip.getTotalFee().add(slip.getExtraCharge()));
            }

            if (slip.getTotalFee().intValue() <= receipt.getRequest().getPaidAmount().intValue()) {
                receipt.setPaymentStatus(PaymentStatus.Paid);
            } else if (receipt.getRequest().getPaidAmount().intValue() == 0) {
                receipt.setPaymentStatus(PaymentStatus.Raised);
            } else {
                receipt.setPaymentStatus(PaymentStatus.PartiallyPaid);
            }

            logger.info(String.format("Student Fee payment rejected .%s",receipt.getId()));
        }
        studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(receipt);
        return receipt;
    }
}