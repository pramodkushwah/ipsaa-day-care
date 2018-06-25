package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.view.center.ZoneRequest;
import com.synlabs.ipsaa.view.center.ZoneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;

@RestController
@RequestMapping("/api/zone/")
public class ZoneController
{
  @Autowired
  private CenterService centerService;

  @GetMapping
  @Secured(CENTER_READ)
  public List<ZoneResponse> list()
  {
    return centerService.listZones().stream().map(ZoneResponse::new).collect(Collectors.toList());
  }

  @PostMapping
  @Secured(CENTER_WRITE)
  public ZoneResponse save(@RequestBody ZoneRequest request)
  {
    return new ZoneResponse(centerService.saveZone(request));
  }

  @PutMapping
  @Secured(CENTER_WRITE)
  public ZoneResponse update(@RequestBody ZoneRequest request)
  {
    return new ZoneResponse(centerService.updateZone(request));
  }

  @DeleteMapping("{zoneId}")
  @Secured(CENTER_WRITE)
  public void delete(@PathVariable Long zoneId)
  {
    centerService.deleteZone(new ZoneRequest(zoneId));
  }

}
