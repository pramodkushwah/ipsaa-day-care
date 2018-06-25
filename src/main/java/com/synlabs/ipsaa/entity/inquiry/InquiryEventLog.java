package com.synlabs.ipsaa.entity.inquiry;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.enums.CallDisposition;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Transient;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sushil on 10/07/2017.
 */
@Entity
public class InquiryEventLog extends BaseEntity
{
  @Enumerated(EnumType.STRING)
  private CallDisposition callDisposition;

  @ManyToOne
  private Inquiry inquiry;

  private Date callBack;

  private String callBackNumber;

  @Column(length = 500)
  private String comment;

  private boolean done = false;

  public boolean isDone()
  {
    return done;
  }

  public void setDone(boolean done)
  {
    this.done = done;
  }

  public String getCallBackNumber()
  {
    return callBackNumber;
  }

  public void setCallBackNumber(String callBackNumber)
  {
    this.callBackNumber = callBackNumber;
  }

  public Inquiry getInquiry()
  {
    return inquiry;
  }

  public void setInquiry(Inquiry inquiry)
  {
    this.inquiry = inquiry;
  }

  public Date getCallBack()
  {
    return callBack;
  }

  public void setCallBack(Date callBack)
  {
    this.callBack = callBack;
  }

  public CallDisposition getCallDisposition()
  {
    return callDisposition;
  }

  public void setCallDisposition(CallDisposition callDisposition)
  {
    this.callDisposition = callDisposition;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  @Transient
  public boolean isEmpty()
  {
    return callDisposition == null
        && callBack == null
        && StringUtils.isEmpty(callBackNumber)
        && StringUtils.isEmpty(comment)
        ;
  }
}
