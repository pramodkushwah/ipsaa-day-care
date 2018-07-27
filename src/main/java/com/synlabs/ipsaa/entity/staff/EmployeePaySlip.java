package com.synlabs.ipsaa.entity.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.util.SalaryUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.SalaryUtils.*;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

@Entity
public class EmployeePaySlip extends BaseEntity
{

  @ManyToOne(optional = false)
  private Employee employee;

  @ManyToOne(optional = false)
  private LegalEntity employer;

  @ManyToOne(optional = false)
  private Center center;

  //is this the master record, or monthly entry
  private boolean master;

  @Column(nullable = false)
  private Integer year;
  @Column(nullable = false)
  private Integer month;

  @Column(nullable = false)
  private String serial;

  private String file;

  @Column(name = "deduce_pf")
  private boolean pfd;
  @Column(name = "deduce_esi")
  private boolean esid;
  @Column(name = "deduce_profession_tax")
  private boolean profd;

  @Column(precision = 16, scale = 2)
  private BigDecimal presents;

  private BigDecimal totalDays;

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
  private BigDecimal otherAllowances;

  @Column(precision = 16, scale = 2, nullable = false)
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
  private BigDecimal otherDeductions;

  @Column(precision = 16, scale = 2, nullable = false)
  private BigDecimal totalDeduction;

  @Column(precision = 16, scale = 2, nullable = false)
  private BigDecimal netSalary;
  @Column(precision = 16, scale = 2, nullable = false)
  private BigDecimal grossSalary;

  private boolean isLock=false;

  private String comment;

  private String autoComment;

  public boolean isLock() {
    return isLock;
  }

  public void setLock(boolean lock) {
    isLock = lock;
  }

  public BigDecimal getPresents()
  {
    return presents;
  }

  public void setPresents(BigDecimal presents)
  {
    this.presents = presents;
  }

  public BigDecimal getTotalDays()
  {
    return totalDays;
  }

  public void setTotalDays(BigDecimal totalDays)
  {
    this.totalDays = totalDays;
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

  public BigDecimal getCtc()
  {
    return ctc;
  }

  public void setCtc(BigDecimal ctc)
  {
    this.ctc = ctc;
  }

  public BigDecimal getProfessionalTax()
  {
    return professionalTax;
  }

  public void setProfessionalTax(BigDecimal professionalTax)
  {
    this.professionalTax = professionalTax;
  }

  public String getAutoComment()
  {
    return autoComment;
  }

  public void setAutoComment(String autoComment)
  {
    this.autoComment = autoComment;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public String getSerial()
  {
    return serial;
  }

  public void setSerial(String serial)
  {
    this.serial = serial;
  }

  public String getFile()
  {
    return file;
  }

  public void setFile(String file)
  {
    this.file = file;
  }

  public Center getCenter()
  {
    return center;
  }

  public void setCenter(Center center)
  {
    this.center = center;
  }

  public LegalEntity getEmployer()
  {
    return employer;
  }

  public void setEmployer(LegalEntity employer)
  {
    this.employer = employer;
  }

  public boolean isMaster()
  {
    return master;
  }

  public void setMaster(boolean master)
  {
    this.master = master;
  }

  public Integer getYear()
  {
    return year;
  }

  public void setYear(Integer year)
  {
    this.year = year;
  }

  public Integer getMonth()
  {
    return month;
  }

  public void setMonth(Integer month)
  {
    this.month = month;
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

  public BigDecimal getGrossSalary()
  {
    return grossSalary;
  }

  public void setGrossSalary(BigDecimal grossSalary)
  {
    this.grossSalary = grossSalary;
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

  public BigDecimal getOtherAllowances()
  {
    return otherAllowances;
  }

  public void setOtherAllowances(BigDecimal otherAllowances)
  {
    this.otherAllowances = otherAllowances;
  }

  @Transient
  public void update(EmployeeSalary employeeSalary, BigDecimal totalDays, BigDecimal presents)
  {
    this.totalDays = totalDays;
    this.presents = presents;

    BigDecimal ratio = presents.divide(totalDays, 6, BigDecimal.ROUND_CEILING);

    this.ctc = ratio.multiply(employeeSalary.getCtc());
    this.basic = calculateBasic(ctc);
    this.hra = calculateHra(basic).multiply(ratio);
    this.conveyance = ratio.multiply(employeeSalary.getConveyance());
    this.bonus = ratio.multiply(employeeSalary.getBonus());
    this.special = calculateSpecial(ctc, basic, hra, conveyance, bonus);

    this.entertainment = employeeSalary.getEntertainment();
    this.medical = employeeSalary.getMedical();
    this.arrears = employeeSalary.getArrears();
    this.shoes = employeeSalary.getShoes();
    this.tiffin = employeeSalary.getTiffin();
    this.uniform = employeeSalary.getUniform();
    this.washing = employeeSalary.getWashing();
    if (this.otherAllowances == null)
    {
      this.otherAllowances = ZERO;
    }
    if (this.otherDeductions == null)
    {
      this.otherDeductions = ZERO;
    }

    pfd = employeeSalary.isPfd();
    esid = employeeSalary.isEsid();
    profd = employeeSalary.isProfd();

    this.pfe = employeeSalary.isPfd() ?
               calculatePfe(basic) :
               ZERO;
    this.pfr = employeeSalary.isPfd() ?
               calculatePfr(basic) :
               ZERO;
// shubham update by shubham with v2
    this.grossSalary = calculateGrossV2(employeeSalary.getExtraMonthlyAllowance(),basic, hra, conveyance, special, pfr);

    this.esi = employeeSalary.isEsid() ?
               calculateEsi(employeeSalary.isEsid(), grossSalary, SalaryUtils.ESI_PERCENT) :
               ZERO;

    this.professionalTax = employeeSalary.isProfd() ?
                           SalaryUtils.PROFESSIONAL_TAX :
                           ZERO;

    this.retention = employeeSalary.getRetention();
    this.tds = employeeSalary.getTds();
    this.advance = employeeSalary.getAdvance();

    this.update();
  }

  @Transient
  public void update()
  {
    totalEarning = ZERO.add(bonus).add(grossSalary).add(otherAllowances == null ? ZERO : otherAllowances);

    totalDeduction = ZERO;
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
    totalDeduction = totalDeduction.add(otherDeductions == null ? ZERO : otherDeductions);
    totalDeduction = totalDeduction.setScale(0, ROUND_HALF_UP);

    netSalary = ZERO
        .add(totalEarning == null ? ZERO : totalEarning)
        .subtract(totalDeduction == null ? ZERO : totalDeduction)
        .setScale(0, ROUND_HALF_UP);
  }

  @Transient
  public void roundOff()
  {
    basic = basic.setScale(0, BigDecimal.ROUND_HALF_UP);
    conveyance = conveyance.setScale(0, BigDecimal.ROUND_HALF_UP);
    hra = hra.setScale(0, BigDecimal.ROUND_HALF_UP);
    bonus = bonus.setScale(0, BigDecimal.ROUND_HALF_UP);
    special = special.setScale(0, BigDecimal.ROUND_HALF_UP);
    totalEarning = totalEarning.setScale(0, BigDecimal.ROUND_HALF_UP);
  }
}
