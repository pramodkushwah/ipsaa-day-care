package com.synlabs.ipsaa.view.msgs;

import com.synlabs.ipsaa.view.common.Request;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailRequest implements Request
{

  private List<String> cc=new ArrayList<>();
  private Long[] ids;
  private String subject;
  private String emailcontent;
  private List<MultipartFile> attachments = new ArrayList<>();
  private List<String> cids=new ArrayList<>();
  private List<MultipartFile> images=new ArrayList<>();

  public List<String> getCc() {
    return cc;
  }

  public void setCc(List<String> cc) {
    this.cc = cc;
  }

  public List<String> getCids()
  {
    return cids;
  }

  public void setCids(List<String> cids)
  {
    this.cids = cids;
  }

  public List<MultipartFile> getImages()
  {
    return images;
  }

  public void setImages(List<MultipartFile> images)
  {
    this.images = images;
  }

  public List<MultipartFile> getAttachments()
  {
    return attachments;
  }

  public void setAttachments(List<MultipartFile> attachments)
  {
    this.attachments = attachments;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  public String getEmailcontent()
  {
    return emailcontent;
  }

  public void setEmailcontent(String emailcontent)
  {
    this.emailcontent = emailcontent;
  }

  public Long[] getIds()
  {
    return Arrays.stream(ids).map(this::unmask).toArray(Long[]::new);
  }

  public void setIds(Long[] ids)
  {
    this.ids = ids;
  }
}
