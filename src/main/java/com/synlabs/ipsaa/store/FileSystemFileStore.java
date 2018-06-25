package com.synlabs.ipsaa.store;

import com.synlabs.ipsaa.config.Local;
import com.synlabs.ipsaa.ex.DownloadException;
import com.synlabs.ipsaa.ex.UploadException;
import com.synlabs.ipsaa.service.BaseService;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Local
@Service
public class FileSystemFileStore extends BaseService implements FileStore
{

  private static Logger logger = LoggerFactory.getLogger(FileSystemFileStore.class);

  @Value("${ipsaa.upload.location}")
  private String uploaddir;

  private Path uploadpath;

  @PostConstruct
  public void init()
  {
    uploadpath = Paths.get(uploaddir);
    if (uploadpath.toFile().exists())
    {
      if (!uploadpath.toFile().isDirectory())
      {
        logger.error("{} is not a dir, cant continue", uploaddir);
        throw new UploadException(uploaddir + "is not a dir, cant continue");
      }
    }
    else
    {
      if (!uploadpath.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", uploaddir);
        throw new UploadException(uploaddir + "cannot create upload directory!");
      }
    }
  }

  @Override
  public void store(String category, File file)
  {
    Path finalLocation = uploadpath.resolve(category);
    if (!finalLocation.toFile().exists())
    {
      if (!finalLocation.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", finalLocation);
        throw new UploadException(finalLocation + "cannot create upload directory!");
      }
    }

    try
    {
      Files.copy(Paths.get(file.getAbsolutePath()), finalLocation.resolve(file.getName()));
    }
    catch (IOException e)
    {
      logger.error("Error copying file!", e);
      throw new UploadException(e);
    }
  }

  @Override
  public boolean isExist(String category, String filename)
  {
    Path finalLocation = uploadpath.resolve(category);
    if (!finalLocation.toFile().exists())
    {
      return false;
    }
    Path resolve = finalLocation.resolve(filename);
    return resolve.toFile().exists();
  }

  @Override
  public void store(String category, String filename, byte[] data) throws IOException
  {
    Path finalLocation = uploadpath.resolve(category);
    if (!finalLocation.toFile().exists())
    {
      if (!finalLocation.toFile().mkdirs())
      {
        logger.error("cannot create upload directory", finalLocation);
        throw new UploadException(finalLocation + "cannot create upload directory!");
      }
    }
    String filepath = finalLocation.resolve(filename).toString();
    BufferedOutputStream stream =
        new BufferedOutputStream(new FileOutputStream(new File(filepath)));
    stream.write(data);
    stream.close();
  }

  @Override
  public String download(String category, String filename) throws IOException
  {
    Path finalLocation = uploadpath.resolve(category);
    Path dirpath = Paths.get(uploaddir);
    String tempFilename = dirpath.resolve(UUID.randomUUID().toString() + filename).toString();
    if (!finalLocation.toFile().exists())
    {
      throw new DownloadException("location not found");
    }
    String filepath = finalLocation.resolve(filename).toString();
    BufferedInputStream stream =
        new BufferedInputStream(new FileInputStream(new File(filepath)));
    FileOutputStream out = new FileOutputStream(tempFilename);
    IOUtils.copy(stream, out);
    out.flush();
    stream.close();
    return tempFilename;
  }

  @Override
  public InputStream getStream(String category, String filename) throws IOException
  {
    String finalName = uploaddir + "/" + category + "/" + filename;
    if (Files.exists(Paths.get(finalName)))
    {
      return new FileInputStream(finalName);
    }
    throw new FileNotFoundException("Cannot locate " + finalName);
  }

}
