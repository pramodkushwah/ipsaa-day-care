package com.synlabs.ipsaa.entity.fee;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.HdfcResponseType;
import com.synlabs.ipsaa.enums.HdfcStatus;
import com.synlabs.ipsaa.service.BaseService;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Entity
public class HdfcResponse extends BaseEntity
{
  @ManyToOne
  private StudentFeePaymentRequest slip;

  @Column(length = 4112)
  private String encResponse;

  @Enumerated(EnumType.STRING)
  private HdfcStatus status;

  @Enumerated(EnumType.STRING)
  private HdfcResponseType type;

  private String orderId;
  private String trackingId;
  private String bankRefNo;
  private String orderStatus;
  private String failureMessage;
  private String paymentMode;
  private String cardName;
  private String statusCode;
  private String statusMessage;
  private String currency;
  private String amount;

  private String billingName;
  private String billingAddress;
  private String billingCity;
  private String billingState;
  private String billingZip;
  private String billingCountry;
  private String billingTel;
  private String billingEmail;

  private String deliveryName;
  private String deliveryAddress;
  private String deliveryCity;
  private String deliveryState;
  private String deliveryZip;
  private String deliveryCountry;
  private String deliveryTel;
  private String deliveryEmail;

  private String merchantParam1;
  private String merchantParam2;
  private String merchantParam3;
  private String merchantParam4;
  private String merchantParam5;

  private String vault;
  private String offerType;
  private String offerCode;
  private String discountValue;
  private String merAmount;
  private String eciValue;
  private String retry;
  private String responseCode;
  private String billingCode;
  private String transDate;
  private String binCountry;

  @Transient
  public void putDetails(String resp)
  {
    Map<String, String> map = new HashMap<>();
    BaseService.ampSplitter.split(resp).forEach(param -> {
      Iterator<String> iterator = BaseService.equalSplitter.split(param).iterator();
      if (iterator.hasNext())
      {
        String key = iterator.next();
        String value = iterator.hasNext() ? iterator.next() : "";
        map.put(key, value);
      }
    });
    putDetails(map);
  }

  @Transient
  public void putDetails(Map<String, String> map)
  {
    orderId = map.get("order_id");
    trackingId = map.get("tracking_id");
    bankRefNo = map.get("bank_ref_no");
    orderStatus = map.get("order_status");
    failureMessage = map.get("failure_message");
    paymentMode = map.get("payment_mode");
    cardName = map.get("card_name");
    statusCode = map.get("status_code");
    statusMessage = map.get("status_message");
    currency = map.get("currency");
    amount = map.get("amount");
    billingName = map.get("billing_name");
    billingAddress = map.get("billing_address");
    billingCity = map.get("billing_city");
    billingState = map.get("billing_state");
    billingZip = map.get("billing_zip");
    billingCountry = map.get("billing_country");
    billingTel = map.get("billing_tel");
    billingEmail = map.get("billing_email");
    deliveryName = map.get("delivery_name");
    deliveryAddress = map.get("delivery_address");
    deliveryCity = map.get("delivery_city");
    deliveryState = map.get("delivery_state");
    deliveryZip = map.get("delivery_zip");
    deliveryCountry = map.get("delivery_country");
    deliveryTel = map.get("delivery_tel");
    deliveryEmail = map.get("delivery_email");
    merchantParam1 = map.get("merchant_param1");
    merchantParam2 = map.get("merchant_param2");
    merchantParam3 = map.get("merchant_param3");
    merchantParam4 = map.get("merchant_param4");
    merchantParam5 = map.get("merchant_param5");
    vault = map.get("vault");
    offerType = map.get("offer_type");
    offerCode = map.get("offer_code");
    discountValue = map.get("discount_value");
    merAmount = map.get("mer_amount");
    eciValue = map.get("eci_value");
    retry = map.get("retry");
    responseCode = map.get("response_code");
    billingCode = map.get("billing_code");
    transDate = map.get("trans_date");
    binCountry = map.get("bin_country");

  }

  @Transient
  public Map<String, String> getAsMap(Map<String, String> map)
  {
    map = map == null ? new HashMap<>() : map;

    map.put("order_id", orderId);
    map.put("tracking_id", trackingId);
    map.put("bank_ref_no", bankRefNo);
    map.put("order_status", orderStatus);
    map.put("failure_message", failureMessage);
    map.put("payment_mode", paymentMode);
    map.put("card_name", cardName);
    map.put("status_code", statusCode);
    map.put("status_message", statusMessage);
    map.put("currency", currency);
    map.put("amount", amount);
    map.put("billing_name", billingName);
    map.put("billing_address", billingAddress);
    map.put("billing_city", billingCity);
    map.put("billing_state", billingState);
    map.put("billing_zip", billingZip);
    map.put("billing_country", billingCountry);
    map.put("billing_tel", billingTel);
    map.put("billing_email", billingEmail);
    map.put("delivery_name", deliveryName);
    map.put("delivery_address", deliveryAddress);
    map.put("delivery_city", deliveryCity);
    map.put("delivery_state", deliveryState);
    map.put("delivery_zip", deliveryZip);
    map.put("delivery_country", deliveryCountry);
    map.put("delivery_tel", deliveryTel);
    map.put("delivery_email", deliveryEmail);
    map.put("merchant_param1", merchantParam1);
    map.put("merchant_param2", merchantParam2);
    map.put("merchant_param3", merchantParam3);
    map.put("merchant_param4", merchantParam4);
    map.put("merchant_param5", merchantParam5);
    map.put("vault", vault);
    map.put("offer_type", offerType);
    map.put("offer_code", offerCode);
    map.put("discount_value", discountValue);
    map.put("mer_amount", merAmount);
    map.put("eci_value", eciValue);
    map.put("retry", retry);
    map.put("response_code", responseCode);
    map.put("billing_code", billingCode);
    map.put("trans_date", transDate);
    map.put("bin_country", binCountry);
    return map;
  }

  public HdfcResponseType getType()
  {
    return type;
  }

  public void setType(HdfcResponseType type)
  {
    this.type = type;
  }

  public HdfcStatus getStatus()
  {
    return status;
  }

  public void setStatus(HdfcStatus status)
  {
    this.status = status;
  }

  public StudentFeePaymentRequest getSlip()
  {
    return slip;
  }

  public void setSlip(StudentFeePaymentRequest slip)
  {
    this.slip = slip;
  }

  public String getEncResponse()
  {
    return encResponse;
  }

  public void setEncResponse(String encResponse)
  {
    this.encResponse = encResponse;
  }

  public String getOrderId()
  {
    return orderId;
  }

  public void setOrderId(String orderId)
  {
    this.orderId = orderId;
  }

  public String getTrackingId()
  {
    return trackingId;
  }

  public void setTrackingId(String trackingId)
  {
    this.trackingId = trackingId;
  }

  public String getBankRefNo()
  {
    return bankRefNo;
  }

  public void setBankRefNo(String bankRefNo)
  {
    this.bankRefNo = bankRefNo;
  }

  public String getOrderStatus()
  {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus)
  {
    this.orderStatus = orderStatus;
  }

  public String getFailureMessage()
  {
    return failureMessage;
  }

  public void setFailureMessage(String failureMessage)
  {
    this.failureMessage = failureMessage;
  }

  public String getPaymentMode()
  {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode)
  {
    this.paymentMode = paymentMode;
  }

  public String getCardName()
  {
    return cardName;
  }

  public void setCardName(String cardName)
  {
    this.cardName = cardName;
  }

  public String getStatusCode()
  {
    return statusCode;
  }

  public void setStatusCode(String statusCode)
  {
    this.statusCode = statusCode;
  }

  public String getStatusMessage()
  {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage)
  {
    this.statusMessage = statusMessage;
  }

  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency(String currency)
  {
    this.currency = currency;
  }

  public String getAmount()
  {
    return amount;
  }

  public void setAmount(String amount)
  {
    this.amount = amount;
  }

  public String getBillingName()
  {
    return billingName;
  }

  public void setBillingName(String billingName)
  {
    this.billingName = billingName;
  }

  public String getBillingAddress()
  {
    return billingAddress;
  }

  public void setBillingAddress(String billingAddress)
  {
    this.billingAddress = billingAddress;
  }

  public String getBillingCity()
  {
    return billingCity;
  }

  public void setBillingCity(String billingCity)
  {
    this.billingCity = billingCity;
  }

  public String getBillingState()
  {
    return billingState;
  }

  public void setBillingState(String billingState)
  {
    this.billingState = billingState;
  }

  public String getBillingZip()
  {
    return billingZip;
  }

  public void setBillingZip(String billingZip)
  {
    this.billingZip = billingZip;
  }

  public String getBillingCountry()
  {
    return billingCountry;
  }

  public void setBillingCountry(String billingCountry)
  {
    this.billingCountry = billingCountry;
  }

  public String getBillingTel()
  {
    return billingTel;
  }

  public void setBillingTel(String billingTel)
  {
    this.billingTel = billingTel;
  }

  public String getBillingEmail()
  {
    return billingEmail;
  }

  public void setBillingEmail(String billingEmail)
  {
    this.billingEmail = billingEmail;
  }

  public String getDeliveryName()
  {
    return deliveryName;
  }

  public void setDeliveryName(String deliveryName)
  {
    this.deliveryName = deliveryName;
  }

  public String getDeliveryAddress()
  {
    return deliveryAddress;
  }

  public void setDeliveryAddress(String deliveryAddress)
  {
    this.deliveryAddress = deliveryAddress;
  }

  public String getDeliveryCity()
  {
    return deliveryCity;
  }

  public void setDeliveryCity(String deliveryCity)
  {
    this.deliveryCity = deliveryCity;
  }

  public String getDeliveryState()
  {
    return deliveryState;
  }

  public void setDeliveryState(String deliveryState)
  {
    this.deliveryState = deliveryState;
  }

  public String getDeliveryZip()
  {
    return deliveryZip;
  }

  public void setDeliveryZip(String deliveryZip)
  {
    this.deliveryZip = deliveryZip;
  }

  public String getDeliveryCountry()
  {
    return deliveryCountry;
  }

  public void setDeliveryCountry(String deliveryCountry)
  {
    this.deliveryCountry = deliveryCountry;
  }

  public String getDeliveryTel()
  {
    return deliveryTel;
  }

  public void setDeliveryTel(String deliveryTel)
  {
    this.deliveryTel = deliveryTel;
  }

  public String getDeliveryEmail()
  {
    return deliveryEmail;
  }

  public void setDeliveryEmail(String deliveryEmail)
  {
    this.deliveryEmail = deliveryEmail;
  }

  public String getMerchantParam1()
  {
    return merchantParam1;
  }

  public void setMerchantParam1(String merchantParam1)
  {
    this.merchantParam1 = merchantParam1;
  }

  public String getMerchantParam2()
  {
    return merchantParam2;
  }

  public void setMerchantParam2(String merchantParam2)
  {
    this.merchantParam2 = merchantParam2;
  }

  public String getMerchantParam3()
  {
    return merchantParam3;
  }

  public void setMerchantParam3(String merchantParam3)
  {
    this.merchantParam3 = merchantParam3;
  }

  public String getMerchantParam4()
  {
    return merchantParam4;
  }

  public void setMerchantParam4(String merchantParam4)
  {
    this.merchantParam4 = merchantParam4;
  }

  public String getMerchantParam5()
  {
    return merchantParam5;
  }

  public void setMerchantParam5(String merchantParam5)
  {
    this.merchantParam5 = merchantParam5;
  }

  public String getVault()
  {
    return vault;
  }

  public void setVault(String vault)
  {
    this.vault = vault;
  }

  public String getOfferType()
  {
    return offerType;
  }

  public void setOfferType(String offerType)
  {
    this.offerType = offerType;
  }

  public String getOfferCode()
  {
    return offerCode;
  }

  public void setOfferCode(String offerCode)
  {
    this.offerCode = offerCode;
  }

  public String getDiscountValue()
  {
    return discountValue;
  }

  public void setDiscountValue(String discountValue)
  {
    this.discountValue = discountValue;
  }

  public String getMerAmount()
  {
    return merAmount;
  }

  public void setMerAmount(String merAmount)
  {
    this.merAmount = merAmount;
  }

  public String getEciValue()
  {
    return eciValue;
  }

  public void setEciValue(String eciValue)
  {
    this.eciValue = eciValue;
  }

  public String getRetry()
  {
    return retry;
  }

  public void setRetry(String retry)
  {
    this.retry = retry;
  }

  public String getResponseCode()
  {
    return responseCode;
  }

  public void setResponseCode(String responseCode)
  {
    this.responseCode = responseCode;
  }

  public String getBillingCode()
  {
    return billingCode;
  }

  public void setBillingCode(String billingCode)
  {
    this.billingCode = billingCode;
  }

  public String getTransDate()
  {
    return transDate;
  }

  public void setTransDate(String transDate)
  {
    this.transDate = transDate;
  }

  public String getBinCountry()
  {
    return binCountry;
  }

  public void setBinCountry(String binCountry)
  {
    this.binCountry = binCountry;
  }
}
