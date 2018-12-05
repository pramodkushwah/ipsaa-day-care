package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.ccavenue.AesCryptUtil;
import com.synlabs.ipsaa.entity.fee.HdfcResponse;
import com.synlabs.ipsaa.entity.hdfc.HdfcApiDetails;
import com.synlabs.ipsaa.entity.student.*;
import com.synlabs.ipsaa.enums.HdfcResponseType;
import com.synlabs.ipsaa.enums.HdfcStatus;
import com.synlabs.ipsaa.enums.PaymentMode;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.*;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.HdfcApiDetailService;
import com.synlabs.ipsaa.view.fee.HdfcCheckoutDetails;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HdfcHelper extends BaseService
{
//  @Value("${ipsaa.hdfc.accesscode}")
//  private String accesscode;
//
//  @Value("${ipsaa.hdfc.tid}")
//  private String tid;
//
//  @Value("${ipsaa.hdfc.merchant_id}")
//  private String merchantId;
//
//  @Value("${ipsaa.hdfc.workingkey}")
//  private String workingkey;

  @Value("${ipsaa.hdfc.baseurl}")
  private String baseurl;

  @Value("${ipsaa.hdfc.payment.baseurl}")
  private String checkoutBaseUrl;

  @Value("${ipsaa.hdfc.successurl}")
  private String successurl;

  @Value("${ipsaa.hdfc.failureurl}")
  private String failureurl;

  @Autowired
  HdfcApiDetailService hdfcApiDetailService;

  @Autowired
  private StudentFeePaymentRepository slipRepository;
  @Autowired
  private StudentFeePaymentRequestIpsaaClubRepository ipsaaclubSlipRepository;

  @Autowired
  private StudentFeePaymentRecordRepository receiptRepository;
  @Autowired
  private StudentFeePaymentRecordIpsaaClubRepository ipsaaclubReceiptRepository;

  @Autowired
  private StudentParentRepository parentRepository;

  @Autowired
  private HdfcResponseRepository hdfcResponseRepository;

  private static final Logger logger = LoggerFactory.getLogger(HdfcHelper.class);

  @Transactional
  public Long recordPaymentSuccess(String encResp, String orderNumber)
  {
    //decrypt response
    HdfcApiDetails hdfcApiDetails=hdfcApiDetailService.findByOrderId(orderNumber);
    AesCryptUtil aesUtil = new AesCryptUtil(hdfcApiDetails.getWorkingKey());
    String resp = aesUtil.decrypt(encResp);
    HdfcResponse hdfcResponse = new HdfcResponse();
    hdfcResponse.setType(HdfcResponseType.Success);
    hdfcResponse.setEncResponse(encResp);
    if (resp == null)
    {
      hdfcResponse.setOrderId(orderNumber);
      hdfcResponse.setStatus(HdfcStatus.DecryptionError);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      throw new ValidationException("Unable to decrypt success gateway response");
    }

   // saving pg response to database
    hdfcResponse.putDetails(resp);
    hdfcResponseRepository.saveAndFlush(hdfcResponse);

    if (!hdfcResponse.getOrderStatus().equalsIgnoreCase("success"))
    {
      hdfcResponse.setStatus(HdfcStatus.Failure);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      throw new ValidationException(String.format("Transaction Failed: %s", hdfcResponse.getStatusMessage()));
    }

//    if (!StringUtils.isEmpty(hdfcResponse.getMerchantParam5()))
//    {
//      StudentFeePaymentRequest studentSlip;
//      studentSlip = slipRepository.findOne(Long.parseLong(hdfcResponse.getMerchantParam5()));
//    }
//    if {
    StudentFeePaymentRequestIpsaaClub ipsaaClubSlip=null;
    StudentFeePaymentRequest studentSlip = slipRepository.findOneByTnxid(orderNumber);
    if(studentSlip==null){
      ipsaaClubSlip=ipsaaclubSlipRepository.findOneByTnxid(orderNumber);
    }

    if (studentSlip == null && ipsaaClubSlip==null)
    {
      hdfcResponse.setStatus(HdfcStatus.MissingSlip);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      throw new ValidationException("Cannot locate fee slip.");
    }
    if(studentSlip!=null){
      hdfcResponse.setSlip(studentSlip);
      hdfcResponse.setStatus(HdfcStatus.NotRecorded);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);

      //recording payment
      BigDecimal paidAmount = new BigDecimal(hdfcResponse.getAmount());
      BigDecimal payableAmount = getPayableAmount(studentSlip);
      StudentFeePaymentRecord receipt = new StudentFeePaymentRecord();
      receipt.setPaymentMode(PaymentMode.Hdfc);
      receipt.setPaymentDate(new Date());
      receipt.setRequest(studentSlip);
      receipt.setStudent(studentSlip.getStudent());
      receipt.setPaidAmount(paidAmount);
      //TODO : set transaction as order_id or tracking_id from response.
      receipt.setTxnid(hdfcResponse.getTrackingId());
      if (payableAmount.intValue() == paidAmount.intValue())
      {
        receipt.setPaymentStatus(PaymentStatus.Paid);
        studentSlip.setPaymentStatus(PaymentStatus.Paid);
        receipt.setPaymentMode(PaymentMode.Hdfc);
      }
      else
      {
        receipt.setPaymentStatus(PaymentStatus.PartiallyPaid);
        studentSlip.setPaymentStatus(PaymentStatus.PartiallyPaid);
        receipt.setPaymentMode(PaymentMode.Hdfc);
      }
      receiptRepository.saveAndFlush(receipt);
      slipRepository.saveAndFlush(studentSlip);
      hdfcResponse.setStatus(HdfcStatus.Success);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      return hdfcResponse.getId();
    }else{
      hdfcResponse.setSlip(ipsaaClubSlip);
      hdfcResponse.setStatus(HdfcStatus.NotRecorded);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);

      //recording payment
      BigDecimal paidAmount = new BigDecimal(hdfcResponse.getAmount());
      BigDecimal payableAmount = getPayableAmount(ipsaaClubSlip);
      StudentFeePaymentRecordIpsaaClub receipt = new StudentFeePaymentRecordIpsaaClub();
      receipt.setPaymentMode(PaymentMode.Hdfc);
      receipt.setPaymentDate(new Date());
      receipt.setRequest(ipsaaClubSlip);
      receipt.setStudent(ipsaaClubSlip.getStudent());
      receipt.setPaidAmount(paidAmount);
      //TODO : set transaction as order_id or tracking_id from response.
      receipt.setTxnid(hdfcResponse.getTrackingId());
      if (payableAmount.intValue() == paidAmount.intValue())
      {
        receipt.setPaymentStatus(PaymentStatus.Paid);
        ipsaaClubSlip.setPaymentStatus(PaymentStatus.Paid);
        receipt.setPaymentMode(PaymentMode.Hdfc);
      }
      else
      {
        receipt.setPaymentStatus(PaymentStatus.PartiallyPaid);
        ipsaaClubSlip.setPaymentStatus(PaymentStatus.PartiallyPaid);
        receipt.setPaymentMode(PaymentMode.Hdfc);
      }
      ipsaaclubReceiptRepository.saveAndFlush(receipt);
      ipsaaclubSlipRepository.saveAndFlush(ipsaaClubSlip);
      hdfcResponse.setStatus(HdfcStatus.Success);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      return hdfcResponse.getId();
    }





  }

  @Transactional
  public Long recordPaymentFailure(String encResp, String orderNumber)
  {

    HdfcApiDetails hdfcApiDetails=hdfcApiDetailService.findByOrderId(orderNumber);
    AesCryptUtil aesUtil = new AesCryptUtil(hdfcApiDetails.getWorkingKey());
    String resp = aesUtil.decrypt(encResp);
    HdfcResponse hdfcResponse = new HdfcResponse();
    hdfcResponse.setType(HdfcResponseType.Failure);
    hdfcResponse.setEncResponse(encResp);
    if (resp == null)
    {
      hdfcResponse.setOrderId(orderNumber);
      hdfcResponse.setStatus(HdfcStatus.DecryptionError);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      throw new ValidationException("Unable to decrypt failure gateway response");
    }
    hdfcResponse.putDetails(resp);
    hdfcResponseRepository.saveAndFlush(hdfcResponse);
    StudentFeePaymentRequest slip = null;
    if (!StringUtils.isEmpty(hdfcResponse.getMerchantParam5()))
    {
      slip = slipRepository.findOne(Long.parseLong(hdfcResponse.getMerchantParam5()));
    }
    else
    {
      slip = slipRepository.findOneByTnxid(orderNumber);
    }
    if (slip == null)
    {
      hdfcResponse.setStatus(HdfcStatus.MissingSlip);
      hdfcResponseRepository.saveAndFlush(hdfcResponse);
      throw new ValidationException("Cannot locate fee slip.");
    }
    hdfcResponse.setSlip(slip);
    hdfcResponse.setStatus(HdfcStatus.Failure);
    hdfcResponseRepository.saveAndFlush(hdfcResponse);

    return hdfcResponse.getId();
  }

  public HdfcResponse getHdfcResponse(Long id)
  {
    HdfcResponse response = hdfcResponseRepository.findOne(id);
    if (response == null)
    {
      throw new ValidationException("Cannot locate Transaction Response.");
    }
    return response;
  }

  public HdfcCheckoutDetails getCheckoutDetails(Long slipId, Long parentId)
  {
    StudentParent parent = parentRepository.findOne(parentId);
    if (parent == null)
    {
      throw new ValidationException("Cannot load parent details.");
    }

    StudentFeePaymentRequest slip = slipRepository.findOne(slipId);
    if (slip == null)
    {
      throw new ValidationException("Missing checkout details.");
    }
    if(slip.isExpire()){
      throw new ValidationException("This slip is expired ! Please contact tech support.");
    }

    HdfcApiDetails hdfcApiDetails=hdfcApiDetailService.getDetailsByCenter(slip.getStudent().getCenter());

    if(hdfcApiDetails==null){
      hdfcApiDetails=hdfcApiDetailService.findDefaultOne();
        if(hdfcApiDetails==null)
          throw new ValidationException("Can not find gateway details for this center! contact tech support.");
    }

    HdfcCheckoutDetails details = new HdfcCheckoutDetails();

    String tnxid = slip.getTnxid();
    tnxid = RandomStringUtils.randomNumeric(16);
    slip.setTnxid(tnxid);
    slipRepository.saveAndFlush(slip);
    details.setTnxId(tnxid);
    details.setOrderId(tnxid);
    details.setAccessCode(hdfcApiDetails.getAccessCode());
    details.setMerchantId(hdfcApiDetails.getVsa());
    details.setCheckoutDetailsUrl(checkoutBaseUrl);

    details.setSlipDetails(slip);
    details.setParentDetails(parent);
    details.setTransactionUrl(baseurl);
    details.setFeeAmount(getPayableAmount(slip));

    StringBuilder params = new StringBuilder();
    Map<String, String> map = putBillingDetails(null, details);
    map.put("slip_id", slip.getId() + "");
    map.forEach((k, v) -> {
      params.append(k)
            .append("=")
            .append(v)
            .append("&");
    });
    params.deleteCharAt(params.length() - 1);

    AesCryptUtil aesUtil = new AesCryptUtil(hdfcApiDetails.getWorkingKey());
    String encryptedParams = aesUtil.encrypt(params.toString());
    details.setEncRequest(encryptedParams);

    return details;
  }

  @Transactional
  public HdfcCheckoutDetails getCheckoutDetailsIpsaaclub(Long slipId, Long parentId)
  {
    StudentParent parent = parentRepository.findOne(parentId);
    if (parent == null)
    {
      throw new ValidationException("Cannot load parent details.");
    }

    StudentFeePaymentRequestIpsaaClub slip = ipsaaclubSlipRepository.findOne(slipId);
    if (slip == null)
    {
      throw new ValidationException("Missing checkout details.");
    }
    if(slip.isExpire()){
      throw new ValidationException("This slip is expired ! Please contact tech support.");
    }

    HdfcApiDetails hdfcApiDetails=hdfcApiDetailService.getDetailsByCenter(slip.getStudent().getCenter());

    if(hdfcApiDetails==null){
      hdfcApiDetails=hdfcApiDetailService.findDefaultOne();
      if(hdfcApiDetails==null)
        throw new ValidationException("Can not find gateway details for this center! contact tech support.");
    }

    HdfcCheckoutDetails details = new HdfcCheckoutDetails();

    String tnxid = slip.getTnxid();
    tnxid = RandomStringUtils.randomNumeric(16);
    slip.setTnxid(tnxid);
    ipsaaclubSlipRepository.saveAndFlush(slip);
    details.setTnxId(tnxid);
    details.setOrderId(tnxid);
    details.setAccessCode(hdfcApiDetails.getAccessCode());
    details.setMerchantId(hdfcApiDetails.getVsa());
    details.setCheckoutDetailsUrl(checkoutBaseUrl);

    details.setSlipDetails(slip);
    details.setParentDetails(parent);
    details.setTransactionUrl(baseurl);
    details.setFeeAmount(getPayableAmount(slip));

    StringBuilder params = new StringBuilder();
    Map<String, String> map = putBillingDetails(null, details);
    map.put("slip_id", slip.getId() + "");
    map.forEach((k, v) -> {
      params.append(k)
              .append("=")
              .append(v)
              .append("&");
    });
    params.deleteCharAt(params.length() - 1);

    AesCryptUtil aesUtil = new AesCryptUtil(hdfcApiDetails.getWorkingKey());
    String encryptedParams = aesUtil.encrypt(params.toString());
    details.setEncRequest(encryptedParams);

    return details;
  }
  private BigDecimal getPayableAmount(StudentFeePaymentRequest slip)
  {
    List<StudentFeePaymentRecord> receipts = receiptRepository.findByRequest(slip);
    BigDecimal payableAmount = slip.getTotalFee().add(BigDecimal.ZERO);
    if (!CollectionUtils.isEmpty(receipts))
    {
      for (StudentFeePaymentRecord receipt : receipts)
      {
        if(receipt.getActive())
        payableAmount = payableAmount.subtract(receipt.getPaidAmount());
      }
    }
    return payableAmount;
  }
  private BigDecimal getPayableAmount(StudentFeePaymentRequestIpsaaClub slip)
  {
    List<StudentFeePaymentRecordIpsaaClub> receipts = ipsaaclubReceiptRepository.findByRequest(slip);
    BigDecimal payableAmount = slip.getTotalFee().add(BigDecimal.ZERO);
    if (!CollectionUtils.isEmpty(receipts))
    {
      for (StudentFeePaymentRecordIpsaaClub receipt : receipts)
      {
        if(receipt.getActive())
        payableAmount = payableAmount.subtract(receipt.getPaidAmount());
      }
    }
    return payableAmount;
  }

  private Map<String, String> putBillingDetails(Map<String, String> req, HdfcCheckoutDetails details)
  {
    req = req == null ? new HashMap<>() : req;

    req.put("merchant_id", details.getMerchantId());
    req.put("currency", "INR");
    req.put("redirect_url", successurl);
    req.put("cancel_url", failureurl);
    req.put("language", "EN");
    req.put("tid", details.getTnxId());
    req.put("order_id", details.getTnxId());
    req.put("amount", details.getFeeAmount().toString());

    req.put("merchant_param5", details.getSlipId());

    req.put("billing_name", details.getBilling_name());
    req.put("billing_address", details.getBilling_address());
    req.put("billing_city", details.getBilling_city());
    req.put("billing_state", details.getBilling_state());
    req.put("billing_zip", details.getBilling_zip());
    req.put("billing_tel", details.getBilling_tel());
    req.put("billing_email", details.getBilling_email());
    req.put("billing_country", "India");

    req.put("delivery_name", details.getBilling_name());
    req.put("delivery_address", details.getBilling_address());
    req.put("delivery_city", details.getBilling_city());
    req.put("delivery_state", details.getBilling_state());
    req.put("delivery_zip", details.getBilling_zip());
    req.put("delivery_tel", details.getBilling_tel());
    req.put("delivery_email", details.getBilling_email());
    req.put("delivery_country", "India");

    return req;
  }
}
