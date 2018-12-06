package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.InquiryService;
import com.synlabs.ipsaa.view.inquiry.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HttpServletBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.INQUIRY_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.INQUIRY_WRITE;

@RestController
@RequestMapping("/api/inquiry/")
public class InquiryController
{
  @Autowired
  private InquiryService inquiryService;

  @Autowired
  private HttpServletRequest request;

  @GetMapping("{id}")
  @Secured(INQUIRY_READ)
  public InquiryResponse getInquiry(@PathVariable("id") Long id)
  {
    InquiryRequest request = new InquiryRequest();
    request.setId(id);
    return new InquiryResponse(inquiryService.getInquiry(request));
  }

  @GetMapping
  @Secured(INQUIRY_READ)
  public List<InquirySummaryResponse> list(@RequestParam(value = "centerId", required = false) Long centerId)
  {
    InquiryRequest request = new InquiryRequest();
    request.setCenterId(centerId);
    return inquiryService.list(request).stream().map(InquirySummaryResponse::new).collect(Collectors.toList());
  }

  @PostMapping
  @Secured(INQUIRY_WRITE)
  public InquiryResponse saveInquiry(@RequestBody InquiryRequest request)
  {
    return new InquiryResponse(inquiryService.saveInquiry(request));
  }

  @PutMapping
  @Secured(INQUIRY_WRITE)
  public InquiryResponse updateInquiry(@RequestBody InquiryRequest request)
  {
    return new InquiryResponse(inquiryService.updateInquiry(request));
  }

  @PostMapping("/followUps/")
  @Secured(INQUIRY_READ)
  public List<InquiryEventLogResponse> getFollowUps(@RequestBody InquiryEventLogFilterRequest request)
  {
    return inquiryService.getFollowUps(request).stream().map(InquiryEventLogResponse::new).collect(Collectors.toList());
  }



  @GetMapping("website")
  public List<WebsiteInquiryResponse> getWebsiteInquiries(){
    return inquiryService.getWebsiteInquiry().stream().map(WebsiteInquiryResponse::new).collect(Collectors.toList());
  }

//  @PutMapping
//  public WebsiteInquiryResponse update(@RequestBody WebsiteInquiryRequest websiteInquiryRequest){
//    return inquiryService.updateInquiry(websiteInquiryRequest);
//  }


}
