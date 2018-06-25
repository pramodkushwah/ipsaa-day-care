package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.CommunicationService;
import com.synlabs.ipsaa.view.msgs.EmailRequest;
import com.synlabs.ipsaa.view.msgs.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STAFF_MESSAGE;

@RestController
@RequestMapping("/api/send/staff/")
public class StaffMessageController
{
  @Autowired
  private CommunicationService communicationService;

  @PostMapping("email")
  @Secured(STAFF_MESSAGE)
  public void sendEmail(
      @RequestParam(value = "subject", required = false) String subject
      , @RequestParam(value = "emailcontent", required = false) String emailcontent
      , @RequestParam(value = "ids", required = false) Long[] ids
      , @RequestParam(value = "files", required = false) List<MultipartFile> attachments
      , @RequestParam(value = "images", required = false) List<MultipartFile> images
      , @RequestParam(value = "cids", required = false) List<String> cids) throws IOException
  {
    EmailRequest request = new EmailRequest();
    request.setSubject(subject);
    request.setEmailcontent(emailcontent);
    request.setAttachments(attachments);
    request.setIds(ids);
    request.setCids(cids);
    request.setImages(images);
    communicationService.sendStaffEmail(request);
  }

  @PostMapping("sms")
  @Secured(STAFF_MESSAGE)
  public void sendSMS(@RequestBody SMSRequest request)
  {
    communicationService.sendStaffSMS(request);
  }
}
