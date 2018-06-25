package com.synlabs.ipsaa.controller.parentportal;

import com.synlabs.ipsaa.entity.sharing.ParentSharingSheet;
import com.synlabs.ipsaa.entity.sharing.SharingSheet;
import com.synlabs.ipsaa.service.StudentPortalService;
import com.synlabs.ipsaa.util.PayUHelper;
import com.synlabs.ipsaa.view.FileResponse;
import com.synlabs.ipsaa.view.attendance.AttendanceResponse;
import com.synlabs.ipsaa.view.center.GalleryPhotoResponse;
import com.synlabs.ipsaa.view.center.GroupActivityResponse;
import com.synlabs.ipsaa.view.center.SupportRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeLedgerRequest;
import com.synlabs.ipsaa.view.fee.StudentFeeLedgerResponse;
import com.synlabs.ipsaa.view.fee.StudentFeeResponse;
import com.synlabs.ipsaa.view.food.FoodMenuResponse;
import com.synlabs.ipsaa.view.student.*;
import com.synlabs.ipsaa.view.support.SupportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PARENT;

@RestController
@RequestMapping("/api/pp/student/")
public class StudentPortalController
{

  @Autowired
  private StudentPortalService portalService;

  @Autowired
  private PayUHelper payUHelper;

  @GetMapping("me")
  @Secured(PARENT)
  public List<StudentSummaryResponse> me()
  {
    return portalService.profile().stream().filter(s -> s.isActive()).map(StudentSummaryResponse::new).collect(Collectors.toList());
  }

  @GetMapping("mydetails")
  @Secured(PARENT)
  public ParentDetails getMyDetails()
  {
    return new ParentDetails(portalService.getParent());
  }

  @GetMapping("{studentId}")
  @Secured(PARENT)
  public StudentResponse getStudent(@PathVariable("studentId") Long id)
  {
    return new StudentResponse(portalService.getStudent(new PortalRequest(id)));
  }

  @GetMapping("attendance/{studentId}")
  @Secured(PARENT)
  public List<AttendanceResponse> attendance(@PathVariable("studentId") Long studentId)
  {
    return portalService.getStudentAttendance(new PortalRequest(studentId)).stream().map(AttendanceResponse::new).collect(Collectors.toList());
  }

  @GetMapping("fee/{studentId}")
  @Secured(PARENT)
  public StudentFeeResponse fee(@PathVariable("studentId") Long studentId)
  {
    return new StudentFeeResponse(portalService.getStudentFee(new PortalRequest(studentId)));
  }

//  @GetMapping("feepayment/{studentId}")
//  @Secured(PARENT)
//  public FeePaymentResponse feepayment(@PathVariable("studentId") Long studentId)
//  {
//    FeePaymentRequest request = new FeePaymentRequest();
//    request.setStudentId(studentId);
//    Center center = portalService.preparePaymentRequest(request);
//    return payUHelper.processPaymentRequest(request, center);
//  }

  @GetMapping("feeledger/{studentId}")
  @Secured(PARENT)
  public StudentFeeLedgerResponse feeledger(@PathVariable("studentId") Long studentId)
  {
    return new StudentFeeLedgerResponse(portalService.getStudentFeeLedger(new PortalRequest(studentId)));
  }

  @GetMapping("support")
  @Secured(PARENT)
  public List<SupportResponse> listSupportQuery()
  {
    return portalService.supportQueryList().stream().map(SupportResponse::new).collect(Collectors.toList());
  }

  @GetMapping("support/{id}")
  @Secured(PARENT)
  public SupportResponse get(@PathVariable("id") Long id)
  {
    SupportRequest request = new SupportRequest(id);
    return new SupportResponse(portalService.supportQuery(request));
  }

  @PostMapping("support")
  @Secured(PARENT)
  public SupportResponse createQuery(@RequestBody SupportRequest request)
  {
    return new SupportResponse(portalService.createQuery(request));
  }

  @PostMapping("support/{id}/reply")
  @Secured(PARENT)
  public SupportResponse postReply(@RequestBody SupportRequest request, @PathVariable("id") Long id)
  {
    return new SupportResponse(portalService.postReply(request));
  }

  @PutMapping("profile")
  @Secured(PARENT)
  public StudentResponse upgdateProfile(@RequestBody StudentRequest request) throws ParseException
  {
    return new StudentResponse(portalService.updateStudentProfile(request));
  }

  @GetMapping("/sms/{parentId}/{enable}")
  @Secured(PARENT)
  public void smsNotification(@PathVariable("parentId") Long parentId, @PathVariable("enable") boolean enabled)
  {
    ParentRequest request = new ParentRequest();
    request.setId(parentId);
    request.setSmsEnabled(enabled);
    portalService.enableSms(request);
  }

  @GetMapping("/email/{parentId}/{enable}")
  @Secured(PARENT)
  public void emailNotification(@PathVariable("parentId") Long parentId, @PathVariable("enable") boolean enabled)
  {
    ParentRequest request = new ParentRequest();
    request.setId(parentId);
    request.setEmailEnabled(enabled);
    portalService.enableEmail(request);
  }

  @GetMapping("/ss/{date}/{studentId}")
  @Secured(PARENT)
  public SharingSheetResponse getSharingSheet(@PathVariable("date") String date, @PathVariable("studentId") Long studentId)
  {
    SharingSheet ss = portalService.getSharingSheet(date, new PortalRequest(studentId));
    return ss == null ? null : new SharingSheetResponse(ss);
  }

  @GetMapping("/pss/{date}/{studentId}")
  @Secured(PARENT)
  public ParentSharingSheetResponse getParentSharingSheet(@PathVariable String date, @PathVariable("studentId") Long studentId)
  {
    ParentSharingSheet ss = portalService.getParentSharingSheet(date, new PortalRequest(studentId));
    return ss == null ? null : new ParentSharingSheetResponse(ss);
  }

  @PostMapping("/pss/")
  @Secured(PARENT)
  public ParentSharingSheetResponse saveParentSharingSheet(@RequestBody ParentSharingSheetRequest request)
  {
    ParentSharingSheet ss = portalService.saveParentSharingSheet(request);
    return new ParentSharingSheetResponse(ss);
  }

  @PostMapping("/pssentry/")
  @Secured(PARENT)
  public ParentSharingSheetResponse saveParentSharingSheetEntry(@RequestBody SharingSheetEntryRequest request)
  {
    ParentSharingSheet ss = portalService.addParentSheetEntry(request);
    return new ParentSharingSheetResponse(ss);
  }

  @GetMapping("/feeslip/{ledgerId}")
  @Secured(PARENT)
  public FileResponse downloadFeeSlip(@PathVariable("ledgerId") Long id) throws IOException
  {
    StudentFeeLedgerRequest request = new StudentFeeLedgerRequest(null);
    request.setId(id);
    return portalService.downloadFeeSlip(request);
  }

  @GetMapping("/feereceipt/{ledgerId}")
  @Secured(PARENT)
  public FileResponse downloadFeeReceipt(@PathVariable("ledgerId") Long id) throws IOException
  {
    StudentFeeLedgerRequest request = new StudentFeeLedgerRequest(null);
    request.setId(id);
    return portalService.downloadFeeReceipt(request);
  }

  @GetMapping("/foodmenu/{studentId}")
  @Secured(PARENT)
  public List<FoodMenuResponse> getMonthlyFoodMenu(@PathVariable("studentId") Long studentId)
  {
    return portalService.getFoodMenu(new PortalRequest(studentId)).stream().map(FoodMenuResponse::new).collect(Collectors.toList());
  }

  @GetMapping("/activities/{studentId}")
  @Secured(PARENT)
  public List<GroupActivityResponse> getActivities(@PathVariable("studentId") Long studentId)
  {
    return portalService.getActivities(new PortalRequest(studentId)).stream().map(GroupActivityResponse::new).collect(Collectors.toList());
  }

  @GetMapping("/gallery/{studentId}/{page}")
  @Secured(PARENT)
  public List<GalleryPhotoResponse> getGalleryPhotos(@PathVariable("studentId") Long studentId,
                                                     @PathVariable("page") Long page)
  {
    return portalService.getGalleryPhotos(studentId, page).stream().map(GalleryPhotoResponse::new).collect(Collectors.toList());
  }
}