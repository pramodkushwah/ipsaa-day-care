package com.synlabs.ipsaa.view.msgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessageEmail
{
  private String  subject;
  private String  body;
  private boolean isHtml;
  private List<String>        to             = new ArrayList<>();
  private List<String>        cc             = new ArrayList<>();
  private List<String>        bcc            = new ArrayList<>();
  private List<File>          attachments    = new ArrayList<>();
  private List<InlineContent> inlineContents = new ArrayList<>();

  public MessageEmail()
  {
  }

  public MessageEmail(MessageEmail email)
  {
    subject = email.subject;
    body = email.body;
    isHtml = email.isHtml;
    attachments = email.attachments;
    inlineContents = email.inlineContents;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  public String getBody()
  {
    return body;
  }

  public void setBody(String body)
  {
    this.body = body;
  }

  public boolean isHtml()
  {
    return isHtml;
  }

  public void setHtml(boolean html)
  {
    isHtml = html;
  }

  public List<String> getTo()
  {
    return to;
  }

  public void setTo(List<String> to)
  {
    this.to = to;
  }

  public List<String> getCc()
  {
    return cc;
  }

  public void setCc(List<String> cc)
  {
    this.cc = cc;
  }

  public List<String> getBcc()
  {
    return bcc;
  }

  public void setBcc(List<String> bcc)
  {
    this.bcc = bcc;
  }

  public List<File> getAttachments()
  {
    return attachments;
  }

  public void setAttachments(List<File> attachments)
  {
    this.attachments = attachments;
  }

  public List<InlineContent> getInlineContents()
  {
    return inlineContents;
  }

  public void setInlineContents(List<InlineContent> inlineContents)
  {
    this.inlineContents = inlineContents;
  }

}
