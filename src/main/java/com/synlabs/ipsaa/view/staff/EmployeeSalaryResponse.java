package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeeSalary;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

/**
 * Created by ttn on 18/5/17.
 */
public class EmployeeSalaryResponse implements Response
{
  private String eid;
  private Long   employeeId;

  private String firstName;
  private String lastName;

  private String centerName;
  private String centerCode;

  private String employer;
  private String employerCode;

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
  private BigDecimal grossSalary;

  private BigDecimal retention;
  private BigDecimal tds;
  private BigDecimal advance;
  private BigDecimal professionalTax;
  private BigDecimal totalDeduction;
  private BigDecimal netSalary;

  public EmployeeSalaryResponse()
  {
  }

  public EmployeeSalaryResponse(EmployeeSalary employeeSalary)
  {
    Employee employee = employeeSalary.getEmployee();
    if (employee != null)
    {
      this.employeeId = employee.getId();
      this.firstName = employee.getFirstName();
      this.lastName = employee.getLastName();
      this.centerName = employee.getCenterName();
      this.centerCode = employee.getCostCenter().getCode();
      this.eid = employee.getEid();
      LegalEntity employer = employee.getEmployer();
      if (employer != null)
      {
        this.employer = employer.getName();
        this.employerCode = employer.getCode();
      }
    }

    this.ctc = employeeSalary.getCtc();
    this.pfd = employeeSalary.isPfd();
    this.esid = employeeSalary.isEsid();
    this.profd = employeeSalary.isProfd();

    this.basic = employeeSalary.getBasic();
    this.bonus = employeeSalary.getBonus();
    this.conveyance = employeeSalary.getConveyance();
    this.entertainment = employeeSalary.getEntertainment();
    this.hra = employeeSalary.getHra();
    this.medical = employeeSalary.getMedical();
    this.arrears = employeeSalary.getArrears();
    this.shoes = employeeSalary.getShoes();
    this.special = employeeSalary.getSpecial();
    this.tiffin = employeeSalary.getTiffin();
    this.uniform = employeeSalary.getUniform();
    this.washing = employeeSalary.getWashing();
    this.totalEarning = employeeSalary.getTotalEarning();
    this.esi = employeeSalary.getEsi();
    this.pfe = employeeSalary.getPfe();
    this.pfr = employeeSalary.getPfr();
    this.grossSalary = employeeSalary.getGrossSalary();
    this.retention = employeeSalary.getRetention();
    this.tds = employeeSalary.getTds();
    this.advance = employeeSalary.getAdvance();
    this.totalDeduction = employeeSalary.getTotalDeduction();
    this.netSalary = employeeSalary.getNetSalary();
    this.professionalTax = employeeSalary.getProfessionalTax();
    this.extraMonthlyAllowance=employeeSalary.getExtraMonthlyAllowance();
  }

  public BigDecimal getGrossSalary()
  {
    return grossSalary;
  }

  public BigDecimal getCtc()
  {
    return ctc;
  }

  public BigDecimal getProfessionalTax()
  {
    return professionalTax;
  }

  public boolean isPfd()
  {
    return pfd;
  }

  public boolean isEsid()
  {
    return esid;
  }

  public boolean isProfd()
  {
    return profd;
  }

  public String getEmployerCode()
  {
    return employerCode;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public void setCenterName(String centerName)
  {
    this.centerName = centerName;
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

  public BigDecimal getPfr()
  {
    return pfr;
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

  public String getEid()
  {
    return eid;
  }

  public void setEid(String eid)
  {
    this.eid = eid;
  }

  public String getEmployer()
  {
    return employer;
  }

  public void setEmployer(String employer)
  {
    this.employer = employer;
  }

  public Long getEmployeeId()
  {
    return employeeId;
  }

  public void setEmployeeId(Long employeeId)
  {
    this.employeeId = employeeId;
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
