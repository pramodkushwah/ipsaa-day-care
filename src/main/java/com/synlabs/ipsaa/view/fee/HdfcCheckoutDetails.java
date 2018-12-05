package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.common.Address;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequestIpsaaClub;
import com.synlabs.ipsaa.entity.student.StudentParent;
import com.synlabs.ipsaa.enums.FeeDuration;
import com.synlabs.ipsaa.enums.PaymentStatus;
import com.synlabs.ipsaa.util.FeeUtils;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

public class HdfcCheckoutDetails implements Response
{
  private String studentName = "";
  private String centerName  = "";
  private String programName = "";
  private String groupName   = "";

  private BigDecimal    feeAmount;
  private PaymentStatus paymentStatus;
  private FeeDuration   duration;
  private String        period;

  private String billing_name    = "";
  private String billing_address = "";
  private String billing_city    = "";
  private String billing_state   = "";
  private String billing_zip     = "";
  private String billing_country = "";
  private String billing_tel     = "";
  private String billing_email   = "";

  private String tnxId          = "";
  private String merchantId     = "";
  private String encRequest     = "";
  private String accessCode     = "";
  private String transactionUrl = "";
  private String checkoutDetailsUrl = "";
  private String orderId        = "";

  private String slipId = "";

  public void setParentDetails(StudentParent parent)
  {
    billing_name = parent.getFullName();
    Address rAddress = parent.getRAddress();
    billing_address = rAddress.getAddress();
    billing_city = rAddress.getCity();
    billing_state = rAddress.getState();
    billing_zip = rAddress.getZipcode();
    billing_country = "India";
    billing_tel = parent.getMobile();
    billing_email = parent.getEmail();

  }

  public void setSlipDetails(StudentFeePaymentRequest slip)
  {
    slipId = slip.getId() + "";
    feeAmount = slip.getTotalFee();
    paymentStatus = slip.getPaymentStatus();
    duration = slip.getFeeDuration();
    period = FeeUtils.getMonth(slip);

    Student student = slip.getStudent();
    studentName = student.getName();
    centerName = student.getCenterName();
    programName = student.getProgramName();
    groupName = student.getGroupName();
  }
  public void setSlipDetails(StudentFeePaymentRequestIpsaaClub slip)
  {
    slipId = slip.getId() + "";
    feeAmount = slip.getTotalFee();
    paymentStatus = slip.getPaymentStatus();
    duration = slip.getFeeDuration();
    period = FeeUtils.getMonth(slip);

    Student student = slip.getStudent();
    studentName = student.getName();
    centerName = student.getCenterName();
    programName = student.getProgramName();
    groupName = student.getGroupName();
  }

  public String getCheckoutDetailsUrl()
  {
    return checkoutDetailsUrl;
  }

  public void setCheckoutDetailsUrl(String checkoutDetailsUrl)
  {
    this.checkoutDetailsUrl = checkoutDetailsUrl;
  }

  public String getSlipId()
  {
    return slipId;
  }

  public void setFeeAmount(BigDecimal feeAmount)
  {
    this.feeAmount = feeAmount;
  }

  public FeeDuration getDuration()
  {
    return duration;
  }

  public String getPeriod()
  {
    return period;
  }

  public PaymentStatus getPaymentStatus()
  {
    return paymentStatus;
  }

  public String getOrderId()
  {
    return orderId;
  }

  public void setOrderId(String orderId)
  {
    this.orderId = orderId;
  }

  public String getTransactionUrl()
  {
    return transactionUrl;
  }

  public void setTransactionUrl(String transactionUrl)
  {
    this.transactionUrl = transactionUrl;
  }

  public void setTnxId(String tnxId)
  {
    this.tnxId = tnxId;
  }

  public void setMerchantId(String merchantId)
  {
    this.merchantId = merchantId;
  }

  public void setEncRequest(String encRequest)
  {
    this.encRequest = encRequest;
  }

  public void setAccessCode(String accessCode)
  {
    this.accessCode = accessCode;
  }

  public String getTnxId()
  {
    return tnxId;
  }

  public String getMerchantId()
  {
    return merchantId;
  }

  public String getEncRequest()
  {
    return encRequest;
  }

  public String getAccessCode()
  {
    return accessCode;
  }

  public String getStudentName()
  {
    return studentName;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public String getProgramName()
  {
    return programName;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public BigDecimal getFeeAmount()
  {
    return feeAmount;
  }

  public String getBilling_name()
  {
    return billing_name;
  }

  public String getBilling_address()
  {
    return billing_address;
  }

  public String getBilling_city()
  {
    return billing_city;
  }

  public String getBilling_state()
  {
    return billing_state;
  }

  public String getBilling_zip()
  {
    return billing_zip;
  }

  public String getBilling_country()
  {
    return billing_country;
  }

  public String getBilling_tel()
  {
    return billing_tel;
  }

  public String getBilling_email()
  {
    return billing_email;
  }
}
