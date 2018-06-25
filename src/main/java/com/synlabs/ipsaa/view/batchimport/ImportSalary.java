package com.synlabs.ipsaa.view.batchimport;

import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.util.Utils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import static com.synlabs.ipsaa.util.SalaryUtils.*;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

/**
 * Created by abhishek on 17/4/17.
 */
public class ImportSalary
{
  private String eid;
  private String pfd;
  private String esid;
  private String profd;

  private BigDecimal ctc;
  private BigDecimal basic;
  private BigDecimal hra;
  private BigDecimal conveyance;
  private BigDecimal bonus;
  private BigDecimal special;
  private BigDecimal pfe;
  private BigDecimal pfr;
  private BigDecimal esie;
  private BigDecimal esir;
  private BigDecimal professionalTax;
  private BigDecimal netSalary;

  public String validate()
  {
    if (StringUtils.isEmpty(this.eid))
    {
      return "Not able to locate user.";
    }

    if (pfd == null)
    {
      return "pf deduction eligibility component missing";
    }
    if (esid == null)
    {
      return "esi deduction eligibility component missing";
    }
    if (profd == null)
    {
      return "prof deduction eligibility component missing";
    }
    if (ctc == null)
    {
      return "ctc component missing.";
    }
//    if (basic == null)
//    {
//      return "basic component missing.";
//    }
//    if (hra == null)
//    {
//      return "hra component missing.";
//    }
    if (conveyance == null)
    {
      return "conveyance component missing.";
    }
    if (bonus == null)
    {
      return "bonus component missing.";
    }
//    if (special == null)
//    {
//      return "special component missing.";
//    }
//    if (pfe == null)
//    {
//      return "PF employee component missing.";
//    }
//    if (pfr == null)
//    {
//      return "PF employer component missing.";
//    }
//    if (esie == null)
//    {
//      return "ESI employee component missing.";
//    }
//    if (esir == null)
//    {
//      return "ESI employer component missing.";
//    }
//    if (professionalTax == null)
//    {
//      return "professionTax component missing.";
//    }

    return "OK";
  }

  public EmployeeSalary toEntity(EmployeeSalary salary)
  {
    salary = salary == null ? new EmployeeSalary() : salary;

    salary.setEntertainment(ZERO);
    salary.setMedical(ZERO);
    salary.setArrears(ZERO);
    salary.setShoes(ZERO);
    salary.setTiffin(ZERO);
    salary.setUniform(ZERO);
    salary.setWashing(ZERO);
    salary.setEsi(ZERO);
    salary.setPfe(ZERO);
    salary.setPfr(ZERO);
    salary.setProfessionalTax(ZERO);
    salary.setRetention(ZERO);
    salary.setTds(ZERO);
    salary.setAdvance(ZERO);
    salary.setTotalEarning(ZERO);
    salary.setTotalDeduction(ZERO);

    salary.setCtc(ctc == null ? ZERO : ctc.setScale(0, ROUND_HALF_UP));
    salary.setBasic(calculateBasic(salary.getCtc()));
    salary.setHra(calculateHra(salary.getBasic()));
    salary.setConveyance(conveyance == null ? ZERO : conveyance.setScale(0, ROUND_HALF_UP));
    salary.setBonus(bonus == null ? ZERO : bonus.setScale(0, ROUND_HALF_UP));
    salary.setSpecial(calculateSpecial(salary));

    salary.setPfd(Utils.getBooleanValue(pfd));
    salary.setEsid(Utils.getBooleanValue(esid));
    salary.setProfd(Utils.getBooleanValue(profd));

    if (salary.isPfd())
    {
      salary.setPfe(calculatePfe(salary));
      salary.setPfr(calculatePfr(salary));
    }

    salary.setGrossSalary(calculateGross(salary));

    if(salary.isEsid())
    {
      salary.setEsi(calculateEsi(salary, ESI_PERCENT));
    }
    if (salary.isProfd())
    {
      salary.setProfessionalTax(PROFESSIONAL_TAX);
    }

    salary.update();
    return salary;
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

  public String getPfd()
  {
    return pfd;
  }

  public void setPfd(String pfd)
  {
    this.pfd = pfd;
  }

  public String getEsid()
  {
    return esid;
  }

  public void setEsid(String esid)
  {
    this.esid = esid;
  }

  public String getProfd()
  {
    return profd;
  }

  public void setProfd(String profd)
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

  public BigDecimal getBasic()
  {
    return basic;
  }

  public void setBasic(BigDecimal basic)
  {
    this.basic = basic;
  }

  public BigDecimal getHra()
  {
    return hra;
  }

  public void setHra(BigDecimal hra)
  {
    this.hra = hra;
  }

  public BigDecimal getConveyance()
  {
    return conveyance;
  }

  public void setConveyance(BigDecimal conveyance)
  {
    this.conveyance = conveyance;
  }

  public BigDecimal getBonus()
  {
    return bonus;
  }

  public void setBonus(BigDecimal bonus)
  {
    this.bonus = bonus;
  }

  public BigDecimal getSpecial()
  {
    return special;
  }

  public void setSpecial(BigDecimal special)
  {
    this.special = special;
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

  public BigDecimal getEsie()
  {
    return esie;
  }

  public void setEsie(BigDecimal esie)
  {
    this.esie = esie;
  }

  public BigDecimal getEsir()
  {
    return esir;
  }

  public void setEsir(BigDecimal esir)
  {
    this.esir = esir;
  }

  public BigDecimal getNetSalary()
  {
    return netSalary;
  }

  public void setNetSalary(BigDecimal netSalary)
  {
    this.netSalary = netSalary;
  }
}
