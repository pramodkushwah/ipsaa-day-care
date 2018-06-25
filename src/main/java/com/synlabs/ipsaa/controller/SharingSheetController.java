package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.service.StudentService;
import com.synlabs.ipsaa.view.student.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SHARINGSHEET_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SHARINGSHEET_WRITE;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.STUDENT_READ;

@RestController
@RequestMapping("/api/student/")
public class SharingSheetController
{

  @Autowired
  private StudentService studentService;

  @PostMapping("/ss/")
  @Secured(SHARINGSHEET_READ)
  public SharingSheetResponse get(@RequestBody SharingSheetRequest request)
  {
    SharingSheet sharingSheet = studentService.getSharingSheet(request);

    if (sharingSheet == null) return null;
    return new SharingSheetResponse(sharingSheet);
  }

  @PostMapping("/pss/")
  @Secured(SHARINGSHEET_READ)
  public ParentSharingSheetResponse getParentSharingSheet(@RequestBody SharingSheetRequest request)
  {
    ParentSharingSheet sharingSheet = studentService.getParentSharingSheet(request);

    if (sharingSheet == null) return null;
    return new ParentSharingSheetResponse(sharingSheet);
  }

  @PostMapping("/ss/se")
  @Secured(SHARINGSHEET_WRITE)
  public SharingSheetResponse addSharingSheetEntry(@RequestBody SharingSheetEntryRequest request) {
    return new SharingSheetResponse(studentService.createSharingSheetEntry(request));
  }

}
