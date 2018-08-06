package com.synlabs.ipsaa.service;


import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFee;
import com.synlabs.ipsaa.ex.NotFoundException;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.util.FeeUtilsV2;
import com.synlabs.ipsaa.view.fee.StudentFeeRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeRequestV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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



    public StudentFee studentFeeService(StudentFeeRequestV2 request) {
        if (request.getBaseFee() == null)
        {
            throw new ValidationException("Base Fee not found");
        }
        if (request.getDiscount() == null)
        {
            throw new ValidationException("Discount not found");
        }
        if (request.getFinalFee() == null)
        {
            throw new ValidationException("Final Fee not found");
        }

        if(request.getCenterProgramFeeId()==null){
            throw new ValidationException("Center program fee not found");
        }

        CenterProgramFee centerProgramFee=centerProgramFeeRepository.findOne(request.getCenterProgramFeeId());

        if(centerProgramFee==null){
            throw new NotFoundException(String.format("Center program fee not found [%s]", request.getCenterProgramFeeId()));
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
        studentFee = request.toEntity(null);

        FeeUtilsV2.validateStudentFee(studentFee,centerProgramFee);

        studentFee.setStudent(student);
        return studentFeeRepository.saveAndFlush(studentFee);
    }
}
