package com.synlabs.ipsaa.controller.parentportal;

import com.synlabs.ipsaa.service.StudentPortalService;
import com.synlabs.ipsaa.view.fee.PayuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payu/")
public class PayUController
{

  @Value("${ipsaa.payu.finalurl}")
  private String finalurl;

  @Autowired
  private StudentPortalService portalService;

  @PostMapping(value = "success", consumes="application/x-www-form-urlencoded")
  public String paymentSuccess(PayuResponse payuresponse)
  {
    portalService.recordSuccess(payuresponse);
    return "redirect:" + finalurl;
  }

  @PostMapping(value="failure", consumes="application/x-www-form-urlencoded")
  public String paymentFailure(PayuResponse payuresponse)
  {
    portalService.recordFailure(payuresponse);
    return "redirect:" + finalurl;
  }
}
