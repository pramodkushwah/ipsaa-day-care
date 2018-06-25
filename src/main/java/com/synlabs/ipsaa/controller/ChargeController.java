package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.FeeService;
import com.synlabs.ipsaa.view.fee.ChargeRequest;
import com.synlabs.ipsaa.view.fee.ChargeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTERFEE_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTERFEE_WRITE;

@RestController
@RequestMapping("/api/charge/")
public class ChargeController
{
  @Autowired
  private FeeService feeService;

  @GetMapping
  @Secured(CENTERFEE_READ)
  public List<ChargeResponse> list() {
    return feeService.listCharges().stream().map(ChargeResponse::new).collect(Collectors.toList());
  }

  @PostMapping
  @Secured(CENTERFEE_WRITE)
  public ChargeResponse saveCharge(@RequestBody ChargeRequest request){
    return new ChargeResponse(feeService.save(request));
  }

  @PutMapping
  @Secured(CENTERFEE_WRITE)
  public ChargeResponse saveUpdate(@RequestBody ChargeRequest request){
    return  new ChargeResponse(feeService.update(request));
  }

}
