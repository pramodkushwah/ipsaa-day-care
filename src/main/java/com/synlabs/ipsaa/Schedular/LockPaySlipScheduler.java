package com.synlabs.ipsaa.Schedular;

import com.synlabs.ipsaa.service.PaySlipService;
import com.synlabs.ipsaa.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class LockPaySlipScheduler {

    @Autowired
    PaySlipService paySlipService;

    public static final Logger logger=LoggerFactory.getLogger(LockPaySlipScheduler.class);

   @Scheduled(cron = "0 0 0 20 * *")
    public void lockPaySlip(){
         logger.info("Locking PaySlip...");
         paySlipService.lockPaySlip();
    }
}
