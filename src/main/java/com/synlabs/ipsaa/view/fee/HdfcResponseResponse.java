package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.fee.HdfcResponse;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.entity.student.StudentFeePaymentRequest;
import com.synlabs.ipsaa.enums.HdfcStatus;
import com.synlabs.ipsaa.view.common.Response;

public class HdfcResponseResponse implements Response
{
  private String trackingId;
  private String orderId;
  private String studentName;
  private String centerName;
  private String programName;

  private String billingName;
  private String billingAddress;
  private String billingCity;
  private String billingState;
  private String billingZip;
  private String billingCountry;
  private String billingTel;
  private String billingEmail;

  private String amount;
  private String currency;
  private HdfcStatus status;

  public HdfcResponseResponse(HdfcResponse response)
  {

    billingName = response.getBillingName();
    billingAddress = response.getBillingAddress();
    billingCity = response.getBillingCity();
    billingState = response.getBillingState();
    billingZip = response.getBillingZip();
    billingCountry = response.getBillingCountry();
    billingTel = response.getBillingTel();
    billingEmail = response.getBillingEmail();
    trackingId = response.getTrackingId();
    orderId = response.getOrderId();
    amount = response.getAmount();
    currency = response.getCurrency();
    status = response.getStatus();

    StudentFeePaymentRequest slip = response.getSlip();
    if (slip != null)
    {
      Student student = slip.getStudent();
      studentName = student.getName();
      centerName = student.getCenterName();
      programName = student.getProgramName();
    }
  }

  public HdfcStatus getStatus()
  {
    return status;
  }

  public String getTrackingId()
  {
    return trackingId;
  }

  public String getOrderId()
  {
    return orderId;
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

  public String getBillingName()
  {
    return billingName;
  }

  public String getBillingAddress()
  {
    return billingAddress;
  }

  public String getBillingCity()
  {
    return billingCity;
  }

  public String getBillingState()
  {
    return billingState;
  }

  public String getBillingZip()
  {
    return billingZip;
  }

  public String getBillingCountry()
  {
    return billingCountry;
  }


  public String getBillingTel()
  {
    return billingTel;
  }

  public String getBillingEmail()
  {
    return billingEmail;
  }

  public String getAmount()
  {
    return amount;
  }

  public String getCurrency()
  {
    return currency;
  }
}
