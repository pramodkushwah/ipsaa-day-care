package com.synlabs.ipsaa.ccavenue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class AesCryptUtil
{

  private static final Logger logger = LoggerFactory.getLogger(AesCryptUtil.class);

  private Cipher ecipher;
  private Cipher dcipher;

  public AesCryptUtil(String key)
  {
    SecretKeySpec skey = new SecretKeySpec(getMD5(key), "AES");
    this.setupCrypto(skey);
  }

  private void setupCrypto(SecretKey key)
  {
    // Create an 8-byte initialization vector
    byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    try
    {
      ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      // CBC requires an initialization vector
      ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
      dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }
    catch (NoSuchPaddingException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | InvalidKeyException e)
    {
      logger.error("Error setting up crypto!", e);
    }
  }

  /**
   * Input is a string to encrypt.
   *
   * @return a Hex string of the byte array
   */
  public String encrypt(String plaintext)
  {
    try
    {
      byte[] ciphertext = ecipher.doFinal(plaintext.getBytes("UTF-8"));
      return byteToHex(ciphertext);
    }
    catch (BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e)
    {
      logger.error("{}", plaintext);
      logger.error("Error encrypting!", e);
      return null;
    }

  }

  /**
   * Input encrypted String represented in HEX
   *
   * @return a string decrypted in plain text
   */
  public String decrypt(String hexCipherText)
  {
    try
    {
      return new String(dcipher.doFinal(hexToByte(hexCipherText)), "UTF-8");
    }
    catch (BadPaddingException | UnsupportedEncodingException | IllegalBlockSizeException e)
    {
      logger.error("{}", hexCipherText);
      logger.error("Error decrypting!", e);
      return null;
    }
  }

  private static byte[] getMD5(String input)
  {
    try
    {
      byte[] bytesOfMessage = input.getBytes("UTF-8");
      MessageDigest md = MessageDigest.getInstance("MD5");
      return md.digest(bytesOfMessage);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  private static String byteToHex(byte[] raw)
  {
    if (raw == null)
    {
      return null;
    }
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < raw.length; i++)
    {
      result.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
    }
    return result.toString();
  }

  private static byte[] hexToByte(String hexString)
  {
    int len = hexString.length();
    byte[] ba = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
    {
      ba[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
    }
    return ba;
  }

}
