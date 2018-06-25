package com.synlabs.ipsaa.view;

public class FileResponse
{
  private Byte[] bytes;

  private String fileName;

  public FileResponse(byte[] bytes,String fileName)
  {
    this.bytes=new Byte[bytes.length];
    for (int x=0;x<bytes.length;x++)
    {
      this.bytes[x]=bytes[x];
    }
    this.fileName=fileName;
  }

  public String getFileName()
  {
    return fileName;
  }

  public Byte[] getBytes()
  {
    return bytes;
  }
}
