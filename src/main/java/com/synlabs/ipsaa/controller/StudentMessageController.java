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

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STUDENT_MESSAGE;

@RestController
@RequestMapping("/api/send/student/")
public class StudentMessageController
{

  @Autowired
  private CommunicationService communicationService;

  @PostMapping("email")
  @Secured(STUDENT_MESSAGE)
  public void sendEmail(
      @RequestParam(value = "subject", required = false) String subject
      , @RequestParam(value = "emailcontent", required = false) String emailcontent
      , @RequestParam(value = "ids", required = false) Long[] ids
      , @RequestParam(value = "files", required = false) List<MultipartFile> attachments
      , @RequestParam(value = "images", required = false) List<MultipartFile> images
      , @RequestParam(value = "cids", required = false) List<String> cids
      ,@RequestParam(value = "cc", required = false) List<String> cc
      ) throws IOException
  {
    EmailRequest request = new EmailRequest();
    request.setSubject(subject);
    request.setEmailcontent(emailcontent);
    request.setAttachments(attachments);
    request.setIds(ids);
    request.setCc(cc);
    request.setCids(cids);
    request.setImages(images);
    communicationService.sendStudentEmail(request);
  }

  @PostMapping("sms")
  @Secured(STUDENT_MESSAGE)
  public void sendSMS(@RequestBody SMSRequest request) {
    communicationService.sendStudentSMS(request);
  }
}
