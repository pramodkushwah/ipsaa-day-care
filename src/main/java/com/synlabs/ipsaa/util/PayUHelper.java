package com.synlabs.ipsaa.util;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.fee.PayuSetting;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.jpa.PayuSettingRepository;
import com.synlabs.ipsaa.view.fee.FeePaymentRequest;
import com.synlabs.ipsaa.view.fee.FeePaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PayUHelper
{

  @Value("${ipsaa.payu.baseurl}")
  private String baseUrl;

  @Autowired
  private PayuSettingRepository payuSettingRepository;

  public String hashCal(String type, String str)
  {
    byte[] hashseq = str.getBytes();
    StringBuilder hexString = new StringBuilder();
    try
    {
      MessageDigest algorithm = MessageDigest.getInstance(type);
      algorithm.reset();
      algorithm.update(hashseq);
      byte messageDigest[] = algorithm.digest();
      for (byte aMessageDigest : messageDigest)
      {
        String hex = Integer.toHexString(0xFF & aMessageDigest);
        if (hex.length() == 1)
        {
          hexString.append("0");
        }
        hexString.append(hex);
      }

    }
    catch (NoSuchAlgorithmException nsae)
    {
    }
    return hexString.toString();
  }

  public FeePaymentResponse processPaymentRequest(FeePaymentRequest request, Center center) {

    if (center == null) return null;

    PayuSetting setting = payuSettingRepository.findOneByCenter(center);

    FeePaymentResponse response = new FeePaymentResponse(request.getTransactionId());
    String salt = setting.getSalt();
    String hashString = "";

    String optionalSequence = "phone|surl|furl|lastname|curl|address1|address2|city|state|country|zipcode|pg";
    String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";

    String[] hashVariableSequence = hashSequence.split("\\|");

    for (String hashVariable : hashVariableSequence)
    {
      if (hashVariable.equals("txnid"))
      {
        hashString = hashString + request.getTransactionId();
      }
      else
      {
        hashString = (StringUtils.isEmpty(request.get(hashVariable))) ? hashString.concat("") : hashString.concat(request.get(hashVariable).trim());
        response.set(hashVariable, request.get(hashVariable));
      }
      hashString = hashString.concat("|");
    }

    for (String optionalVariable : optionalSequence.split("\\|")) {
      response.set(optionalVariable, request.get(optionalVariable));
    }

    hashString = hashString.concat(salt);
    String hash = hashCal("SHA-512", hashString);
    String action = baseUrl.concat("/_payment");

    response.setAction(action);
    response.setHash(hash);
    //response.setHashString(hashString);
    return response;
  }

}
