package com.synlabs.ipsaa.store;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.synlabs.ipsaa.config.Dev;
import com.synlabs.ipsaa.config.Prod;
import com.synlabs.ipsaa.service.BaseService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Prod
@Dev
@Service
public class AwsS3FileStore extends BaseService implements FileStore
{

  private static final Logger logger = LoggerFactory.getLogger(AwsS3FileStore.class);

  @Value("${ipsaa.s3.bucket}")
  private String s3Bucket;

  private AmazonS3 s3Client;

  @Value("${ipsaa.upload.location}")
  protected String filedir;

  @PostConstruct
  public void init()
  {
    s3Client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new SystemPropertiesCredentialsProvider())
        .withRegion("ap-south-1")
        .build();
  }

  @Override
  public void store(String category, File file)
  {

    logger.info("Storing [{}] {}", category, file.getName());
    if (file.canRead())
    {
      try
      {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.length());
        PutObjectRequest putObjectRequest = new PutObjectRequest(
            s3Bucket,
            file.getName(),
            new FileInputStream(file), objectMetadata);
        PutObjectResult result = s3Client.putObject(putObjectRequest);
      }
      catch (FileNotFoundException e)
      {
        logger.error("Error!", e);
      }
    }
  }

  @Override
  public void store(String category, String filename, byte[] data) throws IOException
  {
    if (data.length > 0)
    {
      try
      {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        PutObjectRequest putObjectRequest = new PutObjectRequest(s3Bucket, filename, inputStream, new ObjectMetadata());
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectRequest);
        IOUtils.closeQuietly(inputStream);
      }
      catch (Exception e)
      {
        logger.error("Error!", e);
      }
    }

  }

  @Override
  public String download(String category, String filename) throws IOException
  {
    Path dirpath = Paths.get(filedir);
    String tempFilename = dirpath.resolve(UUID.randomUUID().toString() + filename).toString();
    S3Object object = s3Client.getObject(new GetObjectRequest(s3Bucket, filename));
    InputStream reader = new BufferedInputStream(object.getObjectContent());
    FileOutputStream out = new FileOutputStream(tempFilename);
    IOUtils.copy(reader, out);
    out.flush();
    reader.close();
    return tempFilename;
  }

  @Override
  public InputStream getStream(String type, String filename) throws IOException
  {
    logger.info("Fetching {} - {}", type, filename);
    GetObjectRequest getObjectRequest = new GetObjectRequest(s3Bucket, filename);
    S3Object s3Object = s3Client.getObject(getObjectRequest);
    return s3Object.getObjectContent();
  }

  @Override
  public boolean isExist(String category, String filename)
  {
    try
    {
      s3Client.getObjectMetadata(s3Bucket, filename);
    }
    catch (AmazonS3Exception e)
    {
      if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND)
      {
        // bucket/key does not exist
        return false;
      }
      else
      {
        throw e;
      }
    }
    return true;
  }

}
