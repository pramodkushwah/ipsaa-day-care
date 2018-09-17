package com.synlabs.ipsaa.Schedular;

import com.synlabs.ipsaa.service.StudentFeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StudentFeeResetScheduler {

    @Autowired
    StudentFeeService studentFeeService;
    private static final Logger logger = LoggerFactory.getLogger(StudentFeeResetScheduler.class);

    @Scheduled(cron = "0 0 0 1 4 ?") // on april 1 at midnight
    //@Scheduled(cron = "0 * * * * *") // on april 1 at midnight
    public void run(){
        logger.info(("Student Fee reset scheduler"));
        studentFeeService.resetStudentFee();
    }
}
