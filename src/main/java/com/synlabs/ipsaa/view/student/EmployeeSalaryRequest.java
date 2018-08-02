package com.synlabs.ipsaa.view.student;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.util.SalaryUtils;
import com.synlabs.ipsaa.util.SalaryUtilsV2;
import com.synlabs.ipsaa.view.common.Request;

import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.SalaryUtils.calculateGross;
import static com.synlabs.ipsaa.util.SalaryUtils.calculateGrossV2;
import static java.math.BigDecimal.ZERO;

/**
 * Created by ttn on 22/5/17.
 */
public class EmployeeSalaryRequest implements Request
{
  private String eid;

  private boolean pfd;
  private boolean esid;
  private boolean profd;

  private BigDecimal ctc;
  private BigDecimal basic;
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
  private BigDecimal totalEarning;
  private BigDecimal esi;
  private BigDecimal pfe;
  private BigDecimal pfr;
  private BigDecimal retention;
  private BigDecimal tds;
  private BigDecimal advance;
  private BigDecimal totalDeduction;
  private BigDecimal netSalary;
  private BigDecimal professionalTax;

  public EmployeeSalary toEntity(EmployeeSalary salary)
  {
    salary = salary == null ? new EmployeeSalary() : salary;

    salary.setCtc(ctc == null ? ZERO : ctc);
    salary.setBasic(basic == null ? ZERO : basic);
    salary.setBonus(bonus == null ? ZERO : bonus);
    salary.setConveyance(conveyance == null ? ZERO : conveyance);
    salary.setEntertainment(entertainment == null ? ZERO : entertainment);
    salary.setHra(hra == null ? ZERO : hra);
    salary.setMedical(medical == null ? ZERO : medical);
    salary.setArrears(arrears == null ? ZERO : arrears);
    salary.setShoes(shoes == null ? ZERO : shoes);
    salary.setSpecial(special == null ? ZERO : special);
    salary.setTiffin(tiffin == null ? ZERO : tiffin);
    salary.setUniform(uniform == null ? ZERO : uniform);
    salary.setWashing(washing == null ? ZERO : washing);
    salary.setTotalEarning(totalEarning == null ? ZERO : totalEarning);
    salary.setRetention(retention == null ? ZERO : retention);
    salary.setTds(tds == null ? ZERO : tds);
    salary.setAdvance(advance == null ? ZERO : advance);
    salary.setTotalDeduction(totalDeduction == null ? ZERO : totalDeduction);
    salary.setNetSalary(netSalary == null ? ZERO : netSalary);


    salary.setEsid(esid);
    salary.setPfd(pfd);
    salary.setProfd(profd);
    salary.setEsi(esi == null ? ZERO : esi);

    //modify by shubham calculateGrossV2 by calculateGross
    salary.setExtraMonthlyAllowance(extraMonthlyAllowance==null ?ZERO:extraMonthlyAllowance);
    salary=SalaryUtilsV2.calculateCTC(salary);
    return salary;
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

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
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


  //----------------------------------shubham -------------------------------------------------------------
  // shubham
  private BigDecimal extraMonthlyAllowance;

  public BigDecimal getExtraMonthlyAllowance() {
    return extraMonthlyAllowance;
  }

  public void setExtraMonthlyAllowance(BigDecimal extraMonthlyAllowance) {
    this.extraMonthlyAllowance = extraMonthlyAllowance;
  }
}
