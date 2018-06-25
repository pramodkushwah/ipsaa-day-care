package com.synlabs.ipsaa.Schedular;

import com.synlabs.ipsaa.ftl.model.ApprovalModel;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.service.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by itrs on 7/17/2017.
 */
@Component
public class ApprovalMailSchedule
{
  @Autowired
  private CommunicationService communicationService;

  @Autowired
  private CenterService centerService;

  @Scheduled(cron = "0 0 10 ? * MON")
//  @Scheduled(cron = "1/59 * * * * *")
  public void sendApprovalEmail()
  {
    List<ApprovalModel> pendingApprovals = centerService.getPendingApprovals();
    if (pendingApprovals.size() > 0)
    {
      communicationService.sendPendingApprovalsEmail(pendingApprovals);
    }
  }
}
