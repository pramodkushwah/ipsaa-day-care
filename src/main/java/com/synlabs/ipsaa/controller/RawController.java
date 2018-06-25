package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.store.FileStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
@RequestMapping(value="/raw/")
public class RawController
{
  private static final Logger logger = LoggerFactory.getLogger(RawController.class);

  @Autowired
  private FileStore store;

  @GetMapping(value="/img/{folderName}/{filename:.+}")
  public void download(@PathVariable String folderName,
                       @PathVariable String filename,
                       HttpServletResponse response){
    try{
      final InputStream inputStream = store.getStream(folderName, filename);
      response.setContentType("image/png");
      BufferedImage bi = ImageIO.read(inputStream);
      OutputStream out = response.getOutputStream();
      ImageIO.write(bi, "png", out);
      response.flushBuffer();
      out.close();
    }catch (IOException e){
      logger.error("@getFile > error with request for img: " + filename, e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

}
