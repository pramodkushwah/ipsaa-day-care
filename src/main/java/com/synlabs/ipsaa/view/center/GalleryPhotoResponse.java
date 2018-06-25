package com.synlabs.ipsaa.view.center;

import com.synlabs.ipsaa.entity.center.GalleryPhoto;
import com.synlabs.ipsaa.enums.PhotoType;
import com.synlabs.ipsaa.view.common.Response;

public class GalleryPhotoResponse implements Response
{
  private Long id;
  private Long centerId;
  private Long studentId;

  private String centerCode;
  private String centerName;
  private String studentName;
  private String imagePath;
  private String comment;

  private PhotoType type;

  public GalleryPhotoResponse(GalleryPhoto galleryPhoto)
  {
    id = mask(galleryPhoto.getId());
    type = galleryPhoto.getType();
    comment = galleryPhoto.getComment();
    imagePath = galleryPhoto.getImagePath();

    centerId = mask(galleryPhoto.getCenter().getId());
    centerName = galleryPhoto.getCenter().getName();
    centerCode = galleryPhoto.getCenter().getCode();

    if (galleryPhoto.getStudent() != null)
    {
      studentId = mask(galleryPhoto.getStudent().getId());
      studentName = galleryPhoto.getStudent().getName();
    }
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public String getStudentName()
  {
    return studentName;
  }

  public Long getId()
  {
    return id;
  }

  public Long getCenterId()
  {
    return centerId;
  }

  public Long getStudentId()
  {
    return studentId;
  }

  public String getImagePath()
  {
    return imagePath;
  }

  public String getComment()
  {
    return comment;
  }

  public PhotoType getType()
  {
    return type;
  }
}
