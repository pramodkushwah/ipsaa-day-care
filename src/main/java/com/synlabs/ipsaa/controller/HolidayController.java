package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.HolidayService;
import com.synlabs.ipsaa.view.center.HolidayFilterRequest;
import com.synlabs.ipsaa.view.center.HolidayRequest;
import com.synlabs.ipsaa.view.center.HolidayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;

/**
 * Created by ttn on 10/6/17.
 */
@RestController
@RequestMapping("/api/holiday/")
public class HolidayController
{

  @Autowired
  private HolidayService holidayService;

  @Secured(CENTER_READ)
  @PostMapping
  public List<HolidayResponse> list(@RequestBody HolidayFilterRequest request)
  {
    return holidayService.list(request).stream().map(HolidayResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTER_WRITE)
  @DeleteMapping(path = "{id}")
  public void delete(@PathVariable Long id) throws Exception
  {
    HolidayRequest holidayRequest = new HolidayRequest();
    holidayRequest.setId(id);
    holidayService.delete(holidayRequest);
  }

  @Secured(CENTER_WRITE)
  @PostMapping(path = "save")
  public HolidayResponse save(@RequestBody HolidayRequest holidayRequest)
  {
    return new HolidayResponse(holidayService.save(holidayRequest));
  }
}
