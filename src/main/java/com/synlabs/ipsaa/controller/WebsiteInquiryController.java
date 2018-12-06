package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.InquiryService;
import com.synlabs.ipsaa.view.inquiry.WebsiteInquiryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/websiteInquiry")
public class WebsiteInquiryController {

    @Autowired
    InquiryService inquiryService;

    //@CrossOrigin("https://ipsaa.in")
    @PostMapping()
    public void websiteInquiry(@RequestBody WebsiteInquiryRequest websiteInquiryRequest){
            inquiryService.save(websiteInquiryRequest);

    }

}
