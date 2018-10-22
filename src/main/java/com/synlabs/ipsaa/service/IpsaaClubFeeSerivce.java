package com.synlabs.ipsaa.service;

import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.StudentFeePaymentRequestIpsaaClubRepository;
import com.synlabs.ipsaa.view.fee.StudentFeeSlipRequest;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipRequest;
import com.synlabs.ipsaa.view.student.IpsaaClubSlipResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class IpsaaClubFeeSerivce {

    @Autowired
    StudentFeePaymentRequestIpsaaClubRepository studentFeePaymentRequestIpsaaClubRepository;


    public List<StudentFeePaymentRequestIpsaaClub> listFeeSlips(IpsaaClubSlipRequest request)
    {
        List<StudentFeePaymentRequestIpsaaClub> allslips = new LinkedList<>();
        allslips = studentFeePaymentRequestIpsaaClubRepository.findAll();
        return allslips;
    }
    public IpsaaClubSlipResponce generateSlip(Long id) {
        StudentFeePaymentRequestIpsaaClub studentFeePaymentRequestIpsaaClub=studentFeePaymentRequestIpsaaClubRepository.findOne(id);
        if(studentFeePaymentRequestIpsaaClub!=null)

            // generate code here

            return new IpsaaClubSlipResponce(studentFeePaymentRequestIpsaaClub);
        else{
            throw new ValidationException("slip not found");
        }
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