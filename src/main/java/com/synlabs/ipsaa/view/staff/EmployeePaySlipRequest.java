package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

public class EmployeePaySlipRequest implements Request
{
  private Long   id;
  private String comment;
// shubham
  private BigDecimal noOfPresent;
  //earnings
  private BigDecimal bonus;
  private BigDecimal conveyance;
  private BigDecimal entertainment;
  private BigDecimal hra;
  private BigDecimal medical;
  private BigDecimal arrears;
  private BigDecimal shoes;
  private BigDecimal special;
  private BigDecimal tiffin;
  private BigDecimal uniform;
  private BigDecimal washing;
  private BigDecimal otherAllowances;

  //deductions
  private BigDecimal otherDeductions;
  private BigDecimal retention;
  private BigDecimal tds;
  private BigDecimal advance;
  // shubham
  private boolean lock;

  public EmployeePaySlip toEntity(EmployeePaySlip slip)
  {
    slip.setBonus(bonus);
    slip.setConveyance(conveyance);
    slip.setEntertainment(entertainment);
    slip.setHra(hra);
    slip.setMedical(medical);
    slip.setArrears(arrears);
    slip.setShoes(shoes);
    slip.setSpecial(special);
    slip.setTiffin(tiffin);
    slip.setUniform(uniform);
    slip.setWashing(washing);
    slip.setOtherAllowances(otherAllowances);
    slip.setPresents(noOfPresent);
    slip.setOtherDeductions(otherDeductions);
    slip.setRetention(retention);
    slip.setTds(tds);
    slip.setAdvance(advance);

    slip.setComment(comment);
    return slip;
  }

  public boolean isLock() {
    return lock;
  }
  public void setLock(boolean lock) {
    this.lock = lock;
  }

  public BigDecimal getNoOfPresent() {
    return noOfPresent;
  }

  public void setNoOfPresent(BigDecimal noOfPresent) {
    this.noOfPresent = noOfPresent;
  }

  public Long getId()
  {
    return unmask(id);
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public BigDecimal getBonus()
  {
    return bonus;
  }

  public void setBonus(BigDecimal bonus)
  {
    this.bonus = bonus;
  }

  public BigDecimal getConveyance()
  {
    return conveyance;
  }

  public void setConveyance(BigDecimal conveyance)
  {
    this.conveyance = conveyance;
  }

  public BigDecimal getEntertainment()
  {
    return entertainment;
  }

  public void setEntertainment(BigDecimal entertainment)
  {
    this.entertainment = entertainment;
  }

  public BigDecimal getHra()
  {
    return hra;
  }

  public void setHra(BigDecimal hra)
  {
    this.hra = hra;
  }

  public BigDecimal getMedical()
  {
    return medical;
  }

  public void setMedical(BigDecimal medical)
  {
    this.medical = medical;
  }

  public BigDecimal getArrears()
  {
    return arrears;
  }

  public void setArrears(BigDecimal arrears)
  {
    this.arrears = arrears;
  }

  public BigDecimal getShoes()
  {
    return shoes;
  }

  public void setShoes(BigDecimal shoes)
  {
    this.shoes = shoes;
  }

  public BigDecimal getSpecial()
  {
    return special;
  }

  public void setSpecial(BigDecimal special)
  {
    this.special = special;
  }

  public BigDecimal getTiffin()
  {
    return tiffin;
  }

  public void setTiffin(BigDecimal tiffin)
  {
    this.tiffin = tiffin;
  }

  public BigDecimal getUniform()
  {
    return uniform;
  }

  public void setUniform(BigDecimal uniform)
  {
    this.uniform = uniform;
  }

  public BigDecimal getWashing()
  {
    return washing;
  }

  public void setWashing(BigDecimal washing)
  {
    this.washing = washing;
  }

  public BigDecimal getOtherAllowances()
  {
    return otherAllowances;
  }

  public void setOtherAllowances(BigDecimal otherAllowances)
  {
    this.otherAllowances = otherAllowances;
  }

  public BigDecimal getOtherDeductions()
  {
    return otherDeductions;
  }

  public void setOtherDeductions(BigDecimal otherDeductions)
  {
    this.otherDeductions = otherDeductions;
  }

  public BigDecimal getRetention()
  {
    return retention;
  }

  public void setRetention(BigDecimal retention)
  {
    this.retention = retention;
  }

  public BigDecimal getTds()
  {
    return tds;
  }

  public void setTds(BigDecimal tds)
  {
    this.tds = tds;
  }

  public BigDecimal getAdvance()
  {
    return advance;
  }

  public void setAdvance(BigDecimal advance)
  {
    this.advance = advance;
  }
}
