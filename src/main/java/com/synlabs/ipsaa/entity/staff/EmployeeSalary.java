package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

@Entity
public class EmployeeSalary extends BaseEntity
{

  @ManyToOne(optional = false)
  private Employee employee;

  @Column(name = "deduce_pf")
  private boolean pfd;
  @Column(name = "deduce_esi")
  private boolean esid;
  @Column(name = "deduce_profession_tax")
  private boolean profd;

  @Column(precision = 16, scale = 2)
  private BigDecimal ctc;

  //earnings
  @Column(precision = 16, scale = 2)
  private BigDecimal basic;
  @Column(precision = 16, scale = 2)
  private BigDecimal bonus;
  @Column(precision = 16, scale = 2)
  private BigDecimal conveyance;
  @Column(precision = 16, scale = 2)
  private BigDecimal entertainment;
  @Column(precision = 16, scale = 2)
  private BigDecimal hra;
  @Column(precision = 16, scale = 2)
  private BigDecimal medical;
  @Column(precision = 16, scale = 2)
  private BigDecimal arrears;
  @Column(precision = 16, scale = 2)
  private BigDecimal shoes;
  @Column(precision = 16, scale = 2)
  private BigDecimal special;
  @Column(precision = 16, scale = 2)
  private BigDecimal tiffin;
  @Column(precision = 16, scale = 2)
  private BigDecimal uniform;
  @Column(precision = 16, scale = 2)
  private BigDecimal washing;

  @Column(precision = 16, scale = 2)
  private BigDecimal totalEarning;

  //deductions
  @Column(precision = 16, scale = 2)
  private BigDecimal esi;
  @Column(precision = 16, scale = 2)
  private BigDecimal pfe;
  @Column(precision = 16, scale = 2)
  private BigDecimal pfr;
  @Column(precision = 16, scale = 2)
  private BigDecimal retention;
  @Column(precision = 16, scale = 2)
  private BigDecimal tds;
  @Column(precision = 16, scale = 2)
  private BigDecimal advance;
  @Column(precision = 16, scale = 2)
  private BigDecimal professionalTax;

  @Column(precision = 16, scale = 2)
  private BigDecimal totalDeduction;

  @Column(precision = 16, scale = 2)
  private BigDecimal netSalary;
  @Column(precision = 16, scale = 2)
  private BigDecimal grossSalary;

  public BigDecimal getGrossSalary()
  {
    return grossSalary;
  }

  public void setGrossSalary(BigDecimal grossSalary)
  {
    this.grossSalary = grossSalary;
  }

  public BigDecimal getCtc()
  {
    return ctc;
  }

  public void setCtc(BigDecimal ctc)
  {
    this.ctc = ctc;
  }

  public boolean isPfd()
  {
    return pfd;
  }

  public void setPfd(boolean pfd)
  {
    this.pfd = pfd;
  }

  public boolean isEsid()
  {
    return esid;
  }

  public void setEsid(boolean esid)
  {
    this.esid = esid;
  }

  public boolean isProfd()
  {
    return profd;
  }

  public void setProfd(boolean profd)
  {
    this.profd = profd;
  }

  public BigDecimal getProfessionalTax()
  {
    return professionalTax;
  }

  public void setProfessionalTax(BigDecimal professionalTax)
  {
    this.professionalTax = professionalTax;
  }

  public BigDecimal getBasic()
  {
    return basic;
  }

  public void setBasic(BigDecimal basic)
  {
    this.basic = basic;
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

  public BigDecimal getTotalEarning()
  {
    return totalEarning;
  }

  public void setTotalEarning(BigDecimal totalEarning)
  {
    this.totalEarning = totalEarning;
  }

  public BigDecimal getEsi()
  {
    return esi;
  }

  public void setEsi(BigDecimal esi)
  {
    this.esi = esi;
  }

  public BigDecimal getPfe()
  {
    return pfe;
  }

  public void setPfe(BigDecimal pfe)
  {
    this.pfe = pfe;
  }

  public BigDecimal getPfr()
  {
    return pfr;
  }

  public void setPfr(BigDecimal pfr)
  {
    this.pfr = pfr;
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

  public BigDecimal getTotalDeduction()
  {
    return totalDeduction;
  }

  public void setTotalDeduction(BigDecimal totalDeduction)
  {
    this.totalDeduction = totalDeduction;
  }

  public BigDecimal getNetSalary()
  {
    return netSalary;
  }

  public void setNetSalary(BigDecimal netSalary)
  {
    this.netSalary = netSalary;
  }

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  @Transient
  public void update()
  {

    BigDecimal totalEarning = ZERO.add(bonus).add(grossSalary);

    if (esid)
    {
      totalDeduction = totalDeduction.add(esi == null ? ZERO : esi);
    }
    if (pfd)
    {
      totalDeduction = totalDeduction.add(pfe == null ? ZERO : pfe);
    }
    if (profd)
    {
      totalDeduction = totalDeduction.add(professionalTax == null ? ZERO : professionalTax);
    }
    totalDeduction = totalDeduction.setScale(0, ROUND_HALF_UP);

    netSalary = ZERO
        .add(totalEarning == null ? ZERO : totalEarning)
        .subtract(totalDeduction == null ? ZERO : totalDeduction)
        .setScale(0, ROUND_HALF_UP);

  }
}
