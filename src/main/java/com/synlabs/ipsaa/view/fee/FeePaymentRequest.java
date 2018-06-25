package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.view.student.PortalRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sushil on 11-04-2017.
 */
public class FeePaymentRequest extends PortalRequest
{
  private String transactionId;
  private String hash;
  private String hashString;
  private String action;

  private Map<String, String> params = new HashMap<>();

  public String getTransactionId()
  {
    return transactionId;
  }

  public void setTransactionId(String transactionId)
  {
    this.transactionId = transactionId;
  }

  public String getHash()
  {
    return hash;
  }

  public void setHash(String hash)
  {
    this.hash = hash;
  }

  public String getHashString()
  {
    return hashString;
  }

  public void setHashString(String hashString)
  {
    this.hashString = hashString;
  }

  public String getAction()
  {
    return action;
  }

  public void setAction(String action)
  {
    this.action = action;
  }

  public String get(String key)
  {
    return params.get(key);
  }

  public void set(String key, String value) {
    params.put(key, value);
  }
}
