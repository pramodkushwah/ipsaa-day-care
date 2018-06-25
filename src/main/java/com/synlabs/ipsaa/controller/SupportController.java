package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.SupportService;
import com.synlabs.ipsaa.view.center.SupportRequest;
import com.synlabs.ipsaa.view.support.SupportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.SUPPORT;

@RestController
@RequestMapping("/api/support/")
public class SupportController
{

  @Autowired
  private SupportService supportService;

  @GetMapping
  @Secured(SUPPORT)
  public List<SupportResponse> listSupportQuery() {
    return supportService.supportQueryList().stream().map(SupportResponse::new).collect(Collectors.toList());
  }

  @GetMapping("all")
  @Secured(SUPPORT)
  public List<SupportResponse> listAllSupportQuery() {
    return supportService.supportQueryListAll().stream().map(SupportResponse::new).collect(Collectors.toList());
  }


  @GetMapping("/{id}")
  @Secured(SUPPORT)
  public SupportResponse get(@PathVariable("id") Long id)
  {
    SupportRequest request = new SupportRequest(id);
    return new SupportResponse(supportService.supportQuery(request));
  }

  @PostMapping("/{id}/reply")
  @Secured(SUPPORT)
  public SupportResponse postReply(@RequestBody SupportRequest request, @PathVariable("id") Long id)
  {
    return new SupportResponse(supportService.postReply(request));
  }

  @PostMapping("/{id}/close")
  @Secured(SUPPORT)
  public SupportResponse closeQuery(@PathVariable("id") Long id)
  {
    return new SupportResponse(supportService.closeQuery(new SupportRequest(id)));
  }
}
