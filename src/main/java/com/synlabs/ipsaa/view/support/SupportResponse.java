package com.synlabs.ipsaa.view.support;

import com.synlabs.ipsaa.entity.support.SupportQuery;
import com.synlabs.ipsaa.view.common.Response;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sushil on 06-06-2017.
 */
public class SupportResponse implements Response
{

  private Long id;

  private String title;

  private String description;

  private String status;

  private String center;

  private String created;

  private String raisedBy;

  private String lastAction;

  private String studentName;

  private List<SupportReplyResponse> replies;

  public SupportResponse() { }

  public SupportResponse(SupportQuery supportQuery)
  {
    this.id = mask(supportQuery.getId());
    this.center = supportQuery.getCenter().getCode();
    this.title = supportQuery.getTitle();
    this.description = supportQuery.getDescription();
    this.status = supportQuery.getStatus().name();
    this.raisedBy = supportQuery.getCreatedBy().getName();
    this.studentName = supportQuery.getStudent() == null ? "" : supportQuery.getStudent().getName();
    this.created = supportQuery.getCreatedDate().toString("yyyy-MM-dd HH:mm");
    this.lastAction = supportQuery.getLastModifiedDate() == null ? this.created : supportQuery.getLastModifiedDate().toString("yyyy-MM-dd HH:mm");
    if (supportQuery.getReplies() != null && !supportQuery.getReplies().isEmpty()) {
      this.replies = supportQuery.getReplies().stream().map(SupportReplyResponse::new).collect(Collectors.toList());
    }

  }

  public String getStudentName()
  {
    return studentName;
  }

  public String getCenter()
  {
    return center;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public String getStatus()
  {
    return status;
  }

  public Long getId()
  {
    return id;
  }

  public String getCreated()
  {
    return created;
  }

  public String getLastAction()
  {
    return lastAction;
  }

  public String getRaisedBy()
  {
    return raisedBy;
  }

  public List<SupportReplyResponse> getReplies()
  {
    return replies;
  }
}
