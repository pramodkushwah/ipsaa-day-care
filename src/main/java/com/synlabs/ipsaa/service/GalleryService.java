package com.synlabs.ipsaa.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.center.GalleryPhoto;
import com.synlabs.ipsaa.entity.center.QGalleryPhoto;
import com.synlabs.ipsaa.entity.student.Student;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.CenterRepository;
import com.synlabs.ipsaa.jpa.GalleryPhotoRepository;
import com.synlabs.ipsaa.jpa.StudentRepository;
import com.synlabs.ipsaa.store.FileStore;
import com.synlabs.ipsaa.view.center.GalleryFilterRequest;
import com.synlabs.ipsaa.view.center.GalleryPhotoRequest;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GalleryService extends BaseService
{
  @Autowired
  private GalleryPhotoRepository galleryPhotoRepository;

  @Autowired
  private CenterRepository centerRepository;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private FileStore fileStore;

  @Autowired
  private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(GalleryService.class);

  public List<GalleryPhoto> savePhoto(GalleryPhotoRequest request) throws IOException
  {
    validate(request);
    List<GalleryPhoto> galleryPhotos = get(request);

    galleryPhotoRepository.save(galleryPhotos);
    return galleryPhotos;
  }

  private List<GalleryPhoto> get(GalleryPhotoRequest request) throws IOException
  {
    Center center = null;
    Student student = null;
    switch (request.getType())
    {
      case Center:
        center = centerRepository.findOne(request.getCenterId());
        if (center == null)
        {
          throw new ValidationException(String.format("Cannot locate Center[id = %s]", mask(request.getCenterId())));
        }
        break;
      case Student:
        student = studentRepository.findOne(request.getStudentId());
        if (student == null)
        {
          throw new ValidationException(String.format("Cannot locate Student[id = %s]", mask(request.getStudentId())));
        }
        center = student.getCenter();
        break;
    }
    List<GalleryPhoto> photos = new ArrayList<>();
    if (!request.isNew())
    {
      GalleryPhoto photo = galleryPhotoRepository.findOne(request.getId());
      if (photo == null)
      {
        throw new ValidationException(String.format("Cannot locate photo[id=%s]", mask(request.getId())));
      }
      if (!CollectionUtils.isEmpty(request.getFiles()))
      {
        photo.setImagePath(getPath(request.getFiles().get(0)));
      }
      request.toEntity(photo);
      photos.add(photo);
    }
    else
    {
      if (!CollectionUtils.isEmpty(request.getFiles()))
      {
        for (MultipartFile file : request.getFiles())
        {
          GalleryPhoto photo = request.toEntity(null);
          photo.setCenter(center);
          photo.setStudent(student);
          photo.setImagePath(getPath(file));
          photos.add(photo);
        }
      }
    }

    return photos;
  }

  private String getPath(MultipartFile file) throws IOException
  {
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    String filename = UUID.randomUUID().toString() + "." + extension;
    fileStore.store("GALLERY", filename, file.getBytes());
    return filename;
  }

  private boolean validate(GalleryPhotoRequest request)
  {
    if (request.getType() == null)
    {
      throw new ValidationException("Photo type is required.");
    }
    if (request.isNew() && CollectionUtils.isEmpty(request.getFiles()))
    {
      throw new ValidationException("At least One Image File is required.");
    }
    switch (request.getType())
    {
      case Center:
        if (request.getCenterId() == null)
        {
          throw new ValidationException("Center id required for Photo type Center.");
        }
        break;
      case Student:
        if (request.getStudentId() == null)
        {
          throw new ValidationException("Student id required for Photo type Student.");
        }
        break;
    }
    return true;
  }

  public List<GalleryPhoto> list(GalleryFilterRequest request)
  {
    JPAQuery<GalleryPhoto> query = new JPAQuery<>(entityManager);
    QGalleryPhoto photo = QGalleryPhoto.galleryPhoto;
    query.select(photo).from(photo)
         .orderBy(photo.createdDate.desc());

    if (request.getFrom() != null)
    {
      LocalDate localDate = LocalDate.fromDateFields(request.getFrom());
      query.where(photo.createdDate.after(localDate.toDate()));
    }

    if (request.getTo() != null)
    {
      LocalDate localDate = LocalDate.fromDateFields(request.getTo()).plusDays(1);
      logger.info(String.format("Fetching gallery photo [from=%s,to=%s,center=%s,student=%s]",
                                LocalDate.fromDateFields(request.getFrom()),
                                localDate,
                                mask(request.getStudentId()),
                                mask(request.getCenterId())));
      query.where(photo.createdDate.before(localDate.toDate()));
    }

    if (request.getCenterId() != null)
    {
      query.where(photo.center.id.eq(request.getCenterId()));
    }

    if (request.getStudentId() != null)
    {
      query.where(photo.student.id.eq(request.getStudentId()));
    }

    return query.fetch();
  }

  public GalleryPhoto getPhoto(Long id)
  {
    GalleryPhoto one = galleryPhotoRepository.findOne(id);
    if (one == null)
    {
      throw new ValidationException(String.format("Cannot locate Photo[id=%s]", mask(id)));
    }

    if (hasCenter(one.getCenter().getId()) == null)
    {
      throw new ValidationException("User is not authorized to access this photo.");
    }

    return one;
  }

  public InputStream getPhotoImage(Long id) throws IOException
  {
    GalleryPhoto one = galleryPhotoRepository.findOne(id);
    if (one == null)
    {
      throw new ValidationException("Cannot locate photo");
    }
    return fileStore.getStream("GALLERY", one.getImagePath());
  }
}
