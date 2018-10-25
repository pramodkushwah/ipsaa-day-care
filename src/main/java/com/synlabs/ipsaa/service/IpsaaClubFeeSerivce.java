package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.*;
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
        allslips = studentFeePaymentRequestIpsaaClubRepository.findAll();
        return allslips;
    }

    @Transactional
    public IpsaaClubSlipResponce generateSlip(Long id) {
        StudentFee fee = feeRepository.findOne(id);
        if (fee == null)
           throw  new ValidationException("fee not found");
        StudentFeePaymentRequestIpsaaClub slip = studentFeePaymentRequestIpsaaClubRepository.findByStudentId(fee.getStudent().getId());
        if (slip == null) {
            // first fee

            slip = new StudentFeePaymentRequestIpsaaClub();
            slip.setInvoiceDate(new Date()); // for first slip start date
            slip.setStudent(fee.getStudent());

            slip.setAnnualFee(fee.getAnnualCharges());
            slip.setAnnualFeeDiscount(fee.getAnnualFeeDiscount());
            slip.setFinalAnnualFee(fee.getFinalAnnualCharges());

            slip.setBaseFee(ZERO);
            slip.setFinalBaseFee(ZERO);
            slip.setBaseFeeDiscount(ZERO);

            slip.setDeposit(fee.getDepositFee());
            slip.setDepositFeeDiscount(fee.getDepositFeeDiscount());
            slip.setFinalDepositFee(fee.getFinalDepositFee());
            slip.setBalance(ZERO);
        } else {

            if((slip.getInvoiceDate().getTime()-(new Date()).getTime())/60/60/24<1)
            {
              throw  new ValidationException("can not generate same day from last generate date");
            }
            // invoice date is not setting right now
            slip.setAnnualFee(BigDecimal.ZERO);
            slip.setAnnualFeeDiscount(BigDecimal.ZERO);
            slip.setFinalAnnualFee(BigDecimal.ZERO);

            slip.setBaseFee(fee.getBaseFee());
            slip.setFinalBaseFee(fee.getFinalBaseFee());
            slip.setBaseFeeDiscount(fee.getBaseFeeDiscount());

            slip.setDeposit(BigDecimal.ZERO);
            slip.setDepositFeeDiscount(BigDecimal.ZERO);
            slip.setFinalDepositFee(BigDecimal.ZERO);
        }
        if (fee != null) {
            slip.setPaymentStatus(PaymentStatus.Raised);

            Date expirDate=new Date();
            StudentFeePaymentRecordIpsaaClub record=new StudentFeePaymentRecordIpsaaClub();
            record.setStartDate(slip.getInvoiceDate());

            slip = generateFeeFrom(expirDate, fee, slip); // generate fee from invoice to today
            slip.setInvoiceDate(expirDate);               //invoice date is expire date of last slip

            record.setEndDate(expirDate);

            record.setTotalNoOfDays(slip.getTotalNoOfDays());
            record.setNoOfHalfDays(slip.getNoOfHalfDays());
            record.setNoOfFullDays(slip.getNoOfFullDays());

            record.setAnnualFee(slip.getFinalAnnualFee());
            record.setDepositFee(slip.getFinalDepositFee());
            record.setBaseFee(slip.getFinalBaseFee());
            record.setGstAmount(slip.getGstAmount());

            record.setBalance(slip.getBalance());

            record.setPaymentStatus(PaymentStatus.Raised);
            record.setRequest(slip);
            record.setStudent(slip.getStudent());

            slip.setTotalFee(FeeUtilsV2.calculateIpsaaClubTotalFee(slip));

            // calculate balance
            StudentFeePaymentRecordIpsaaClub lastRecord=null;
            if(slip.getPayments()!=null && !slip.getPayments().isEmpty())
             lastRecord= slip.getPayments().get(0);

            if(lastRecord!=null && lastRecord.getPaidAmount()!=null)
                record.setBalance(lastRecord.getTotalFee().subtract(lastRecord.getPaidAmount()));
            else{
                record.setBalance(ZERO);
            }

            record.setTotalFee(slip.getTotalFee().add(record.getBalance()));
            slip.setPayments(Arrays.asList(record));

            studentFeePaymentRequestIpsaaClubRepository.saveAndFlush(slip);

            return new IpsaaClubSlipResponce(slip);
        } else
            throw new NotFoundException("student fee not found");
    }

    private StudentFeePaymentRequestIpsaaClub generateFeeFrom(Date date, StudentFee fee, StudentFeePaymentRequestIpsaaClub slip) {
        Map<String, Integer> counts = getStudentAttendanceFromTo(slip.getInvoiceDate(), date, fee.getStudent()); // gettting attendace from invoice day to now
        slip.setTotalNoOfDays(counts.get("total"));
        slip.setNoOfFullDays(counts.get("fullday"));
        slip.setNoOfHalfDays(counts.get("halfday"));


        BigDecimal halfDayFee = fee.getBaseFee().multiply(new BigDecimal(slip.getNoOfHalfDays())).multiply(new BigDecimal(0.60));
        BigDecimal fullDayFee = fee.getBaseFee().multiply(new BigDecimal(slip.getNoOfFullDays()));

        BigDecimal totalFee = halfDayFee.add(fullDayFee);

        BigDecimal gst = FeeUtilsV2.calculateGST(totalFee,fee.getFinalAnnualCharges(), GST.IGST);

        slip.setGstAmount(gst);

        slip.setTotalFee(totalFee);
        return slip;
    }

    private Map<String, Integer> getStudentAttendanceFromTo(Date lastGenerationDate, Date today, Student student) {
        return studentAttendanceService.getAttendanceFromTo(lastGenerationDate, today, student);
    }

    public IpsaaClubSlipResponce getStudnetSlip(Long id) {
        StudentFeePaymentRequestIpsaaClub studentFeePaymentRequestIpsaaClub = studentFeePaymentRequestIpsaaClubRepository.findByStudentId(id);
        if (studentFeePaymentRequestIpsaaClub != null)
            return new IpsaaClubSlipResponce(studentFeePaymentRequestIpsaaClub);
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
        StudentFeePaymentRecordIpsaaClub record = studentFeePaymentRecordIpsaaClubRepository.findOne(request.getId());
        if (record == null) {
            throw new NotFoundException("Missing record");
        }
        record.setComments(request.getComments());
        record.setReceiptSerial(null);
        record.setReceiptFileName(null);
        BigDecimal alreadypaid = record.getPaidAmount()==null?ZERO:record.getPaidAmount();

        if (alreadypaid.doubleValue() <= request.getPaidAmount().doubleValue()) {
            record.setPaymentStatus(PaymentStatus.Paid);
        } else {
            record.setPaymentStatus(PaymentStatus.PartiallyPaid);
        }
        record.setActive(true);
        record.setPaidAmount(request.getPaidAmount());
        record.setPaymentMode(request.getPaymentMode());
        record.setTxnid(request.getTxnid());
        record.setConfirmed(request.getConfirmed());
        record.setPaymentDate(request.getPaymentDate() == null ? new Date() : request.getPaymentDate());
        studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(record);
        logger.info(String.format("Student Fee payment recoded successfully.%s", record));
        //documentService.generateFeeReceiptPdf(record);
        return record;
    }
    public StudentFeePaymentRecordIpsaaClub updateRecord(StudentFeeSlipRequestV2 request) {
        StudentFeePaymentRecordIpsaaClub slip = studentFeePaymentRecordIpsaaClubRepository.findOne(request.getId());
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
            slip.setExtraCharges(request.getExtraCharge());
            slip.setTotalFee(slip.getTotalFee().add(slip.getExtraCharges()));
        }
        slip.setTotalFee(slip.getTotalFee().add(slip.getBalance()));
        return studentFeePaymentRecordIpsaaClubRepository.saveAndFlush(slip);
    }

    public StudentFeePaymentRecordIpsaaClub updatePayFee(SaveFeeSlipRequest request) {

        if (request.getId() == null) {
            throw new ValidationException("Receipt id is required.");
        }

        StudentFeePaymentRecordIpsaaClub receipt = studentFeePaymentRecordIpsaaClubRepository.findOne(request.getId());
        if (receipt == null) {
            throw new ValidationException(String.format("Cannot locate Receipt[id = %s]", mask(request.getId())));
        }

        if (receipt.getConfirmed() != null && receipt.getConfirmed() && !receipt.getActive()) {
            throw new ValidationException("Confirmed Receipt cannot update.");
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
}