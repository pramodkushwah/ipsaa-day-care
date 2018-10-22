package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.attendance.StudentAttendance;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.enums.GST;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRequestIpsaaClubRepository;
import com.synlabs.ipsaa.jpa.StudentFeeRepository;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipRequest;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class IpsaaClubFeeSerivce {

    @Autowired
    StudentFeePaymentRequestIpsaaClubRepository studentFeePaymentRequestIpsaaClubRepository;
    @Autowired
    StudentAttendanceService studentAttendanceService;

    @Autowired
    StudentFeeRepository feeRepository;

    public List<StudentFeePaymentRequestIpsaaClub> listFeeSlips(IpsaaClubSlipRequest request)
    {
        List<StudentFeePaymentRequestIpsaaClub> allslips = new LinkedList<>();
        allslips = studentFeePaymentRequestIpsaaClubRepository.findAll();
        return allslips;
    }
    public IpsaaClubSlipResponce generateSlip(Long id) {
        StudentFeePaymentRequestIpsaaClub slip=studentFeePaymentRequestIpsaaClubRepository.findOne(id);
        if(slip!=null) {
            StudentFee fee = feeRepository.findByStudent(slip.getStudent());
                if (fee != null) {
                    slip = generateFeeFrom(new Date(), fee, slip);
                    return new IpsaaClubSlipResponce(studentFeePaymentRequestIpsaaClubRepository.save(slip));
                } else
                    throw new NotFoundException("student fee not found");
            }
        else{
            throw new ValidationException("slip not found");
        }
    }

    private StudentFeePaymentRequestIpsaaClub generateFeeFrom(Date date, StudentFee fee, StudentFeePaymentRequestIpsaaClub slip) {
        Map<String,Integer> counts= getStudentAttendanceFromTo(slip.getLastGenerationDate(),date,fee.getStudent());
        slip.setTotalNoOfDays(counts.get("total"));
        slip.setNoOfFullDays(counts.get("fullday"));
        slip.setNoOfHalfDays(counts.get("halfday"));
        BigDecimal halfDayFee=fee.getBaseFee();
        BigDecimal fullDayFee=fee.getBaseFee();
        BigDecimal toalFee=halfDayFee.add(fullDayFee);
        BigDecimal gst=FeeUtilsV2.calculateGST(toalFee,GST.IGST);
        toalFee=toalFee.add(gst);
        slip.setTotalFee(toalFee);
        slip.setInvoiceDate(date);
        s
        return slip;
    }

    private Map<String,Integer> getStudentAttendanceFromTo(Date lastGenerationDate, Date today, Student student) {
        return studentAttendanceService.getAttendanceFromTo(lastGenerationDate,today,student);
    }

    public IpsaaClubSlipResponce getStudnetSlip(Long id) {
        StudentFeePaymentRequestIpsaaClub studentFeePaymentRequestIpsaaClub=studentFeePaymentRequestIpsaaClubRepository.findByStudentId(id);
        if(studentFeePaymentRequestIpsaaClub!=null)
        return new IpsaaClubSlipResponce(studentFeePaymentRequestIpsaaClub);
        else{
            throw new ValidationException("slip not found");
        }
    }
}