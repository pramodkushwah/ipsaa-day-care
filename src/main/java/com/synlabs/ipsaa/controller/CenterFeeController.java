package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.fee.CenterProgramFee;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.FeeService;
import com.synlabs.ipsaa.util.ExcelExporterCenterPrograms;
import com.synlabs.ipsaa.view.center.*;
import com.synlabs.ipsaa.view.common.Request;
import com.synlabs.ipsaa.view.fee.SaveFeeSlipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTERFEE_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTERFEE_WRITE;

@RestController
@RequestMapping("/api/center/")
public class CenterFeeController
{
  @Autowired
  private FeeService feeService;

  @Secured(CENTERFEE_READ)
  @GetMapping("{centerId}/fee")
  public List<CenterFeeResponse> listFee(@PathVariable Long centerId)
  {
    return feeService.listCenterFee(new CenterFeeRequest(centerId)).stream().map(CenterFeeResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTERFEE_READ)
  @GetMapping("{centerId}/charge")
  public List<CenterChargeResponse> listCharge(@PathVariable Long centerId)
  {
    return feeService.listCenterCharge(new CenterFeeRequest(centerId)).stream().map(CenterChargeResponse::new).collect(Collectors.toList());
  }

  @Secured(CENTERFEE_READ)
  @GetMapping("export")
  public String listCenterExport()
  {
    //System.out.print("get");
    try{
    ExcelExporterCenterPrograms c=  new ExcelExporterCenterPrograms();
     c.createExcel(feeService.listCenterProgramFee());
      return "done";
    }catch (Exception e){
      return  "nothing";
    }
  }

  @Secured(CENTERFEE_WRITE)
  @PostMapping("charge")
  public CenterChargeResponse save(@RequestBody CenterChargeRequest request)
  {
    return new CenterChargeResponse(feeService.save(request));
  }

  @Secured(CENTERFEE_WRITE)
  @PutMapping("charge")
  public CenterChargeResponse update(@RequestBody CenterChargeRequest request)
  {
    return new CenterChargeResponse(feeService.update(request));
  }

  @Secured(CENTERFEE_WRITE)
  @DeleteMapping("charge/{id}")
  public void delete(@PathVariable("id") Long id)
  {
    CenterChargeRequest request = new CenterChargeRequest();
    request.setId(id);
    feeService.deleteCenterCharge(request);
  }

  @Secured(CENTERFEE_WRITE)
  @PostMapping("program/fee/")
  public CenterProgramFeeResponse saveCenterProgramFee(@RequestBody CenterProgramFeeRequest request)
  {
    return new CenterProgramFeeResponse(feeService.saveCenterProgramFee(request));
  }

  @Secured(CENTERFEE_WRITE)
  @PutMapping("program/fee/")
  public CenterProgramFeeResponse updateCenterProgramFee(@RequestBody CenterProgramFeeRequest request)
  {
    return new CenterProgramFeeResponse(feeService.updateCenterProgramFee(request));
  }

  @Secured(CENTERFEE_WRITE)
  @DeleteMapping("program/fee/{id}")
  public void deleteCenterProgramFee(@PathVariable("id") Long id)
  {
    Request request = new Request()
    {
    };
      feeService.deleteCenterProgramFee(request.unmask(id));
  }

  @PostMapping("/fee/")
  @Secured(CENTERFEE_READ)
  public CenterProgramFeeResponse programFee(@RequestBody CenterProgramFeeRequest request)
  {
    CenterProgramFee programFee = feeService.getProgramFee(request);
    CenterProgramFeeResponse fee = new CenterProgramFeeResponse(programFee);
    fee.setCenter(null);
    fee.setProgram(null);
    return fee;
  }

  @GetMapping("/program/fee/slip/{id}")
  @Secured(CENTERFEE_READ)
  public CenterProgramFeeResponse getProgramFee(@PathVariable("id") Long slipId)
  {
    SaveFeeSlipRequest request = new SaveFeeSlipRequest();
    request.setId(slipId);
    return new CenterProgramFeeResponse(feeService.getProgramFee(request));
  }
}
