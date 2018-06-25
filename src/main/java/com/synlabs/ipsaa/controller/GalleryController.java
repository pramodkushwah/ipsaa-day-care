package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.enums.PhotoType;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.GalleryService;
import com.synlabs.ipsaa.view.center.GalleryFilterRequest;
import com.synlabs.ipsaa.view.center.GalleryPhotoRequest;
import com.synlabs.ipsaa.view.center.GalleryPhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.GALLERY_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.GALLERY_WRITE;

@RestController
@RequestMapping("/api/gallery/")
public class GalleryController
{
  @Autowired
  private GalleryService galleryService;

  @PostMapping("/photo/filter/")
  @Secured(GALLERY_READ)
  public List<GalleryPhotoResponse> list(@RequestBody GalleryFilterRequest request)
  {
    return galleryService.list(request).stream().map(GalleryPhotoResponse::new).collect(Collectors.toList());
  }

  @GetMapping("/photo/{id}")
  @Secured(GALLERY_READ)
  public GalleryPhotoResponse get(@PathVariable("id") Long id)
  {
    return new GalleryPhotoResponse(galleryService.getPhoto(BaseService.unmask(id)));
  }

  @PostMapping("/photo/")
  @Secured(GALLERY_WRITE)
  public List<GalleryPhotoResponse> save(
      @RequestParam(value = "id", required = false) Long id,
      @RequestParam(value = "centerId", required = false) Long centerId,
      @RequestParam(value = "type", required = false) PhotoType type,
      @RequestParam(value = "studentId", required = false) Long studentId,
      @RequestParam(value = "comment", required = false) String comment,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException
  {
    GalleryPhotoRequest request = new GalleryPhotoRequest();
    request.setId(id);
    request.setCenterId(centerId);
    request.setStudentId(studentId);
    request.setType(type);
    request.setComment(comment);
    request.setFiles(files);
    return galleryService.savePhoto(request).stream().map(GalleryPhotoResponse::new).collect(Collectors.toList());
  }
}
