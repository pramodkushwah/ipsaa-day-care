package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.GalleryPhoto;
import com.synlabs.ipsaa.enums.PhotoType;
import com.synlabs.ipsaa.view.common.Request;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class GalleryPhotoRequest implements Request
{
  private Long id;
  private Long studentId;
  private Long centerId;

  private String comment;

  private PhotoType type;

  private List<MultipartFile> files;

  public GalleryPhoto toEntity(GalleryPhoto galleryPhoto)
  {
    galleryPhoto = galleryPhoto == null ? new GalleryPhoto() : galleryPhoto;
    galleryPhoto.setType(type);
    galleryPhoto.setComment(comment);
    return galleryPhoto;
  }

  public List<MultipartFile> getFiles()
  {
    return files;
  }

  public void setFiles(List<MultipartFile> files)
  {
    this.files = files;
  }

  public boolean isNew()
  {
    return id == null;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getStudentId()
  {
    return unmask(studentId);
  }

  public void setStudentId(Long studentId)
  {
    this.studentId = studentId;
  }

  public Long getCenterId()
  {
    return unmask(centerId);
  }

  public void setCenterId(Long centerId)
  {
    this.centerId = centerId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public PhotoType getType()
  {
    return type;
  }

  public void setType(PhotoType type)
  {
    this.type = type;
  }

}
