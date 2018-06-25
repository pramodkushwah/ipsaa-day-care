package com.synlabs.ipsaa.view.fee;

public class HdfcPaymentResponse
{
  private String merchantId;
  private String accessCode;
  private String encRequest;
  private String paymentUrl;

  public HdfcPaymentResponse(String merchantId, String accessCode, String encRequest, String paymentUrl)
  {
    this.merchantId = merchantId;
    this.accessCode = accessCode;
    this.encRequest = encRequest;
    this.paymentUrl = paymentUrl;
  }

  public String getMerchantId()
  {
    return merchantId;
  }

  public String getAccessCode()
  {
    return accessCode;
  }

  public String getEncRequest()
  {
    return encRequest;
  }

  public String getPaymentUrl()
  {
    return paymentUrl;
  }
}
