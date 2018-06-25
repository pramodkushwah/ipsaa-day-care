package com.synlabs.ipsaa.view.fee;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

//TODO map all the fields from post request

@Entity
public class PayuResponse extends BaseEntity
{

  private String status;
  private String firstname;
  private String lastname;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String country;
  private String zipcode;

  private String amount;
  private String txnid;

  @Column(name = "payu_hash")
  private String hash;

  @Column(name = "payu_key")
  private String key;
  private String productinfo;
  private String email;
  private String phone;
  private String salt;

  private String mihpayid;

  @Column(name="payment_mode")
  private String mode;

  private String error;
  private String bankcode;
  private String PG_TYPE;
  private String bank_ref_num;
  private String unmappedstatus;
  private String encryptedPaymentId;
  private String error_Message;
  private String name_on_card;
  private String cardnum;
  private String amount_split;
  private String payuMoneyId;
  private String discount;
  private String net_amount_debit;
  private String addedon;
  private String additionalCharges;
  private String comment;

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public void setFirstname(String firstname)
  {
    this.firstname = firstname;
  }

  public String getAmount()
  {
    return amount;
  }

  public void setAmount(String amount)
  {
    this.amount = amount;
  }

  public String getTxnid()
  {
    return txnid;
  }

  public void setTxnid(String txnid)
  {
    this.txnid = txnid;
  }

  public String getHash()
  {
    return hash;
  }

  public void setHash(String hash)
  {
    this.hash = hash;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getProductinfo()
  {
    return productinfo;
  }

  public void setProductinfo(String productinfo)
  {
    this.productinfo = productinfo;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getSalt()
  {
    return salt;
  }

  public void setSalt(String salt)
  {
    this.salt = salt;
  }

  public String getMihpayid()
  {
    return mihpayid;
  }

  public void setMihpayid(String mihpayid)
  {
    this.mihpayid = mihpayid;
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode(String mode)
  {
    this.mode = mode;
  }

  public String getError()
  {
    return error;
  }

  public void setError(String error)
  {
    this.error = error;
  }

  public String getBankcode()
  {
    return bankcode;
  }

  public void setBankcode(String bankcode)
  {
    this.bankcode = bankcode;
  }

  public String getPG_TYPE()
  {
    return PG_TYPE;
  }

  public void setPG_TYPE(String PG_TYPE)
  {
    this.PG_TYPE = PG_TYPE;
  }

  public String getBank_ref_num()
  {
    return bank_ref_num;
  }

  public void setBank_ref_num(String bank_ref_num)
  {
    this.bank_ref_num = bank_ref_num;
  }

  public String getLastname()
  {
    return lastname;
  }

  public void setLastname(String lastname)
  {
    this.lastname = lastname;
  }

  public String getAddress1()
  {
    return address1;
  }

  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }

  public String getAddress2()
  {
    return address2;
  }

  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public String getZipcode()
  {
    return zipcode;
  }

  public void setZipcode(String zipcode)
  {
    this.zipcode = zipcode;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public String getUnmappedstatus()
  {
    return unmappedstatus;
  }

  public void setUnmappedstatus(String unmappedstatus)
  {
    this.unmappedstatus = unmappedstatus;
  }

  public String getEncryptedPaymentId()
  {
    return encryptedPaymentId;
  }

  public void setEncryptedPaymentId(String encryptedPaymentId)
  {
    this.encryptedPaymentId = encryptedPaymentId;
  }

  public String getError_Message()
  {
    return error_Message;
  }

  public void setError_Message(String error_Message)
  {
    this.error_Message = error_Message;
  }

  public String getName_on_card()
  {
    return name_on_card;
  }

  public void setName_on_card(String name_on_card)
  {
    this.name_on_card = name_on_card;
  }

  public String getCardnum()
  {
    return cardnum;
  }

  public void setCardnum(String cardnum)
  {
    this.cardnum = cardnum;
  }

  public String getAmount_split()
  {
    return amount_split;
  }

  public void setAmount_split(String amount_split)
  {
    this.amount_split = amount_split;
  }

  public String getPayuMoneyId()
  {
    return payuMoneyId;
  }

  public void setPayuMoneyId(String payuMoneyId)
  {
    this.payuMoneyId = payuMoneyId;
  }

  public String getDiscount()
  {
    return discount;
  }

  public void setDiscount(String discount)
  {
    this.discount = discount;
  }

  public String getNet_amount_debit()
  {
    return net_amount_debit;
  }

  public void setNet_amount_debit(String net_amount_debit)
  {
    this.net_amount_debit = net_amount_debit;
  }

  public String getAddedon()
  {
    return addedon;
  }

  public void setAddedon(String addedon)
  {
    this.addedon = addedon;
  }

  public String getAdditionalCharges()
  {
    return additionalCharges;
  }

  public void setAdditionalCharges(String additionalCharges)
  {
    this.additionalCharges = additionalCharges;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("PayuResponse{");
    sb.append("status='").append(status).append('\'');
    sb.append(", firstname='").append(firstname).append('\'');
    sb.append(", lastname='").append(lastname).append('\'');
    sb.append(", address1='").append(address1).append('\'');
    sb.append(", address2='").append(address2).append('\'');
    sb.append(", city='").append(city).append('\'');
    sb.append(", state='").append(state).append('\'');
    sb.append(", country='").append(country).append('\'');
    sb.append(", zipcode='").append(zipcode).append('\'');
    sb.append(", amount='").append(amount).append('\'');
    sb.append(", txnid='").append(txnid).append('\'');
    sb.append(", hash='").append(hash).append('\'');
    sb.append(", key='").append(key).append('\'');
    sb.append(", productinfo='").append(productinfo).append('\'');
    sb.append(", email='").append(email).append('\'');
    sb.append(", phone='").append(phone).append('\'');
    sb.append(", salt='").append(salt).append('\'');
    sb.append(", mihpayid='").append(mihpayid).append('\'');
    sb.append(", mode='").append(mode).append('\'');
    sb.append(", error='").append(error).append('\'');
    sb.append(", bankcode='").append(bankcode).append('\'');
    sb.append(", PG_TYPE='").append(PG_TYPE).append('\'');
    sb.append(", bank_ref_num='").append(bank_ref_num).append('\'');
    sb.append(", unmappedstatus='").append(unmappedstatus).append('\'');
    sb.append(", encryptedPaymentId='").append(encryptedPaymentId).append('\'');
    sb.append(", error_Message='").append(error_Message).append('\'');
    sb.append(", name_on_card='").append(name_on_card).append('\'');
    sb.append(", cardnum='").append(cardnum).append('\'');
    sb.append(", amount_split='").append(amount_split).append('\'');
    sb.append(", payuMoneyId='").append(payuMoneyId).append('\'');
    sb.append(", discount='").append(discount).append('\'');
    sb.append(", net_amount_debit='").append(net_amount_debit).append('\'');
    sb.append(", addedon='").append(addedon).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public String getComment()
  {
    return comment;
  }
}
