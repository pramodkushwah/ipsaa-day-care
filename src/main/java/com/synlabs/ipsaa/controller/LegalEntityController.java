package com.synlabs.ipsaa.controller;

/**
 * Created by sushil on 02-04-2018.
 */

import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.view.common.LegalEntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.LEGAL_ENTITY_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.PROGRAM_READ;

@RestController
@RequestMapping("/api/le/")
public class LegalEntityController
{

  @Autowired
  private CenterService centerService;

  @GetMapping
  @Secured(LEGAL_ENTITY_READ)
  public List<LegalEntityResponse> listPrograms()
  {
    return centerService.listEntities().stream().map(LegalEntityResponse::new).collect(Collectors.toList());
  }

}
