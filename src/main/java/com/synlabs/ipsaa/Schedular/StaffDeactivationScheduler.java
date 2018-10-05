package com.synlabs.ipsaa.Schedular;

import com.synlabs.ipsaa.service.StaffService;
import com.synlabs.ipsaa.view.attendance.BioAttendance;
import com.synlabs.ipsaa.view.staff.StaffRequest;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Set;

@Component
public class StaffDeactivationScheduler {

    @Autowired
    private StaffService staffService;

    private static final Logger logger = LoggerFactory.getLogger(AttendancePullScheduler.class);
    @Scheduled(cron = "0 0 1 * * *")// every night at 1am
    public void pullAttendance() throws ParseException, SQLException, IOException
    {
        logger.info("staff deactivation scheduler");
        staffService.checkAndDelete();
    }

}
