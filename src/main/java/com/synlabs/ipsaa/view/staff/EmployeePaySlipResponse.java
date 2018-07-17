package com.synlabs.ipsaa.view.staff;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.entity.common.LegalEntity;
import com.synlabs.ipsaa.entity.staff.Employee;
import com.synlabs.ipsaa.entity.staff.EmployeePaySlip;
import com.synlabs.ipsaa.entity.staff.EmployeeProfile;
import com.synlabs.ipsaa.view.common.Response;

import java.math.BigDecimal;

/**
 * Created by Rakesh on 06-04-2018.
 */
public class EmployeePaySlipResponse implements Response
{
  private Long    id;
  private String  serial;
  private boolean master;

  private Integer year;
  private Integer month;

  private String eid;
  private String empName;
  private String empDesignation;
  private String employerCode;
  private String employerName;
  private String employerAddress;
  private String employerPhone;

  private String centerName;
  private String centerCode;
  private String autoComment;
  private String comment;

  private BigDecimal totalEarning;
  private BigDecimal netSalary;
  private BigDecimal totalDeduction;

  private BigDecimal totalDays;
  private BigDecimal presents;

  //earnings
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
  private BigDecimal otherAllowances;

  //deductions
  private BigDecimal esi;
  private BigDecimal pfe;
  private BigDecimal pfr;
  private BigDecimal retention;
  private BigDecimal tds;
  private BigDecimal advance;
  private BigDecimal professionalTax;
  private BigDecimal otherDeductions;

  private BigDecimal grossSalary;

  private String centerAccountName;
  private String centerAccountNumber;

  private String pan;
  private String uan;
  private String pfan;
  private String esin;
  private String pran;
  private String ban;
  private String aadharNumber;
  private boolean islock;


  public EmployeePaySlipResponse(EmployeePaySlip slip)
  {
    Employee employee = slip.getEmployee();
    if (employee != null)
    {

      this.eid = employee.getEid();
      this.empName = employee.getName();
      this.empDesignation = employee.getDesignation() == null ? "" : employee.getDesignation();
      this.aadharNumber = employee.getAadharNumber() == null ? "" : employee.getAadharNumber();
      EmployeeProfile profile = employee.getProfile();
      if (profile != null)
      {
        this.pan = profile.getPan() == null ? "" : profile.getPan();
        this.uan = profile.getUan() == null ? "" : profile.getUan();
        this.pfan = profile.getPfan() == null ? "" : profile.getPfan();
        this.esin = profile.getEsin() == null ? "" : profile.getEsin();
        this.pran = profile.getPran() == null ? "" : profile.getPran();
        this.ban = profile.getBan() == null ? "" : profile.getBan();
      }
    }
    this.islock=slip.isLock();
    this.employerCode = "";
    this.employerName = "";
    this.employerAddress = "";
    this.employerPhone = "";
    LegalEntity employer = slip.getEmployer();
    if (employer != null)
    {
      this.employerCode = employer.getCode();
      this.employerName = employer.getName();
      this.employerAddress = employer.getAddressString();
      this.employerPhone = employer.getAddress() == null ? "" : employer.getAddress().getPhone();
    }

    Center center = slip.getCenter();
    if (center != null)
    {
      centerName = center.getName();
      centerCode = center.getCode();
      centerAccountName = center.getAccountName();
      centerAccountNumber = center.getAccountNumber();
    }
    this.id = mask(slip.getId());
    this.master = slip.isMaster();
    this.year = slip.getYear();
    this.month = slip.getMonth();

    this.totalDays = slip.getTotalDays();
    this.presents = slip.getPresents();

    this.basic = slip.getBasic();
    this.bonus = slip.getBonus();
    this.conveyance = slip.getConveyance();
    this.entertainment = slip.getEntertainment();
    this.hra = slip.getHra();
    this.medical = slip.getMedical();
    this.arrears = slip.getArrears();
    this.shoes = slip.getShoes();
    this.special = slip.getSpecial();
    this.tiffin = slip.getTiffin();
    this.uniform = slip.getUniform();
    this.washing = slip.getWashing();
    this.totalEarning = slip.getTotalEarning();
    this.esi = slip.getEsi();
    this.pfe = slip.getPfe();
    this.pfr = slip.getPfr();
    this.grossSalary = slip.getGrossSalary();
    this.retention = slip.getRetention();
    this.tds = slip.getTds();
    this.advance = slip.getAdvance();
    this.professionalTax = slip.getProfessionalTax();
    this.otherDeductions = slip.getOtherDeductions();
    this.totalDeduction = slip.getTotalDeduction();
    this.netSalary = slip.getNetSalary();
    this.serial = slip.getSerial();
    this.otherAllowances = slip.getOtherAllowances();
    this.autoComment = slip.getAutoComment();
    this.comment = slip.getComment();
  }

  public boolean isIslock() {
    return islock;
  }

  public void setIslock(boolean islock) {
    this.islock = islock;
  }

  public BigDecimal getPfe()
  {
    return pfe;
  }

  public BigDecimal getPfr()
  {
    return pfr;
  }

  public BigDecimal getGrossSalary()
  {
    return grossSalary;
  }

  public String getBan()
  {
    return ban;
  }

  public BigDecimal getTotalDays()
  {
    return totalDays;
  }

  public BigDecimal getPresents()
  {
    return presents;
  }

  public BigDecimal getProfessionalTax()
  {
    return professionalTax;
  }

  public String getEmployerAddress()
  {
    return employerAddress;
  }

  public String getEmployerPhone()
  {
    return employerPhone;
  }

  public String getAadharNumber()
  {
    return aadharNumber;
  }

  public String getPan()
  {
    return pan;
  }

  public String getUan()
  {
    return uan;
  }

  public String getPfan()
  {
    return pfan;
  }

  public String getEsin()
  {
    return esin;
  }

  public String getPran()
  {
    return pran;
  }

  public String getCenterAccountName()
  {
    return centerAccountName;
  }

  public String getCenterAccountNumber()
  {
    return centerAccountNumber;
  }

  public String getAutoComment()
  {
    return autoComment;
  }

  public String getComment()
  {
    return comment;
  }

  public BigDecimal getOtherAllowances()
  {
    return otherAllowances;
  }

  public String getEmpDesignation()
  {
    return empDesignation;
  }

  public Long getId()
  {
    return id;
  }

  public String getSerial()
  {
    return serial;
  }

  public String getCenterName()
  {
    return centerName;
  }

  public String getCenterCode()
  {
    return centerCode;
  }

  public boolean isMaster()
  {
    return master;
  }

  public Integer getYear()
  {
    return year;
  }

  public Integer getMonth()
  {
    return month;
  }

  public BigDecimal getBasic()
  {
    return basic;
  }

  public BigDecimal getBonus()
  {
    return bonus;
  }

  public BigDecimal getConveyance()
  {
    return conveyance;
  }

  public BigDecimal getEntertainment()
  {
    return entertainment;
  }

  public BigDecimal getHra()
  {
    return hra;
  }

  public BigDecimal getMedical()
  {
    return medical;
  }

  public BigDecimal getArrears()
  {
    return arrears;
  }

  public BigDecimal getShoes()
  {
    return shoes;
  }

  public BigDecimal getSpecial()
  {
    return special;
  }

  public BigDecimal getTiffin()
  {
    return tiffin;
  }

  public BigDecimal getUniform()
  {
    return uniform;
  }

  public BigDecimal getWashing()
  {
    return washing;
  }

  public BigDecimal getTotalEarning()
  {
    return totalEarning;
  }

  public BigDecimal getEsi()
  {
    return esi;
  }

  public BigDecimal getOtherDeductions()
  {
    return otherDeductions;
  }

  public BigDecimal getRetention()
  {
    return retention;
  }

  public BigDecimal getTds()
  {
    return tds;
  }

  public BigDecimal getAdvance()
  {
    return advance;
  }

  public BigDecimal getTotalDeduction()
  {
    return totalDeduction;
  }

  public BigDecimal getNetSalary()
  {
    return netSalary;
  }

  public String getEid()
  {
    return eid;
  }

  public String getEmpName()
  {
    return empName;
  }

  public String getEmployerCode()
  {
    return employerCode;
  }

  public String getEmployerName()
  {
    return employerName;
  }
}
