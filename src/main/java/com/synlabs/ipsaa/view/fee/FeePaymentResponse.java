package com.synlabs.ipsaa.view.fee;

import java.util.HashMap;
import java.util.Map;

public class FeePaymentResponse
{
  private String transactionId;
  private String hash;
  private String hashString;
  private String action;

  private Map<String, String> params = new HashMap<>();
  private String key;

  public FeePaymentResponse(String transactionId)
  {
    this.transactionId = transactionId;
  }

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

  public Map<String, String> getParams()
  {
    return params;
  }

  public void setParams(Map<String, String> params)
  {
    this.params = params;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }
}
