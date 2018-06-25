package com.synlabs.ipsaa.store;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sushil on 30-08-2016.
 */
public interface FileStore
{

  void store(String category, File file);

  void store(String category, String filename, byte[] data) throws IOException;

  String download(String category, String filename) throws IOException;

  InputStream getStream(String category, String filename) throws IOException;

  boolean isExist(String category, String filename);
}
