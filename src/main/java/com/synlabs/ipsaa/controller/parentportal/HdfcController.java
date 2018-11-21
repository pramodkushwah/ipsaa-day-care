package com.synlabs.ipsaa.controller.parentportal;

import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.util.HdfcHelper;
import com.synlabs.ipsaa.view.fee.HdfcCheckoutDetails;
import com.synlabs.ipsaa.view.fee.HdfcResponseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/hdfc/")
public class HdfcController
{
  @Autowired
  private HdfcHelper hdfcHelper;

  @Value("${ipsaa.hdfc.payment.succcess}")
  private String success;

  @Value("${ipsaa.hdfc.payment.failure}")
  private String failure;

  @RequestMapping("/success")
  public void success(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    String encResp = req.getParameter("encResp");
    String orderNumber = req.getParameter("orderNo");
    Long id = hdfcHelper.recordPaymentSuccess(encResp, orderNumber);
    resp.sendRedirect(success+"/"+BaseService.mask(id));
  }

  @RequestMapping("/failure")
  public void failure(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    String encResp = req.getParameter("encResp");
    String orderNumber = req.getParameter("orderNo");
    Long id = hdfcHelper.recordPaymentFailure(encResp, orderNumber);
    resp.sendRedirect(failure+"/"+BaseService.mask(id));
  }

  @GetMapping("checkout/{slipId}/{parentId}")
  public HdfcCheckoutDetails getCheckoutDetails(@PathVariable("parentId") Long parentId, @PathVariable("slipId") Long slipId)
  {
    return hdfcHelper.getCheckoutDetails(BaseService.unmask(slipId), BaseService.unmask(parentId));
  }
  @GetMapping("checkout/ipsaaclub/{slipId}/{parentId}")
  public HdfcCheckoutDetails getCheckoutDetailsIpsaaclub(@PathVariable("parentId") Long parentId, @PathVariable("slipId") Long slipId)
  {
    return hdfcHelper.getCheckoutDetailsIpsaaclub(BaseService.unmask(slipId), BaseService.unmask(parentId));
  }

  @GetMapping("payment/{responseId}")
  public HdfcResponseResponse getHdfcResponse(@PathVariable("responseId") Long responseId)
  {
    return new HdfcResponseResponse(hdfcHelper.getHdfcResponse(BaseService.unmask(responseId)));
  }
}
