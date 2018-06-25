package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.jpa.GalleryPhotoRepository;
import com.synlabs.ipsaa.service.BaseService;
import com.synlabs.ipsaa.service.GalleryService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/s3/")
public class LocalS3Controller
{
  @Autowired
  private GalleryService galleryService;

  @GetMapping("/gallery/photo/{id}")
  private void getPhotoImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException
  {
    InputStream is = galleryService.getPhotoImage(BaseService.unmask(id));

    response.setContentType("image/*");
    ServletOutputStream out = response.getOutputStream();
    IOUtils.copy(is, out);



  }
}
