package com.synlabs.ipsaa.auth;

public final class IPSAAAuth
{
  private IPSAAAuth()
  {
    throw new AssertionError("Not allowed");
  }

  public static final class Privileges
  {

    public static final String SELF_READ  = "ROLE_SELF_READ";
    public static final String SELF_WRITE = "ROLE_SELF_WRITE";

    public static final String USER_READ  = "ROLE_USER_READ";
    public static final String USER_WRITE = "ROLE_USER_WRITE";

    public static final String CENTER_READ   = "ROLE_CENTER_READ";
    public static final String CENTER_WRITE  = "ROLE_CENTER_WRITE";
    public static final String CENTER_DELETE = "ROLE_CENTER_DELETE";

    public static final String STUDENTFEE_READ  = "ROLE_STUDENTFEE_READ";
    public static final String STUDENTFEE_WRITE = "ROLE_STUDENTFEE_WRITE";

    public static final String STUDENTFEE_SLIP_READ  = "ROLE_STUDENTFEE_SLIP_READ";
    public static final String STUDENTFEE_SLIP_WRITE = "ROLE_STUDENTFEE_SLIP_WRITE";
    public static final String STUDENTFEE_REGENERATE_WRITE = "ROLE_STUDENTFEE_REGENERATE_WRITE";

    public static final String PAYSLIP_READ  = "ROLE_PAYSLIP_READ";
    // shubham
      public static final String PAYSLIP_LOCK  = "ROLE_PAYSLIP_LOCK";
    public static final String PAYSLIP_WRITE = "ROLE_PAYSLIP_WRITE";
    // shubham
    public static final String STUDENTFEE_RECEIPT_CONFIRM = "ROLE_STUDENTFEE_RECEIPT_CONFIRM";



    public static final String STUDENTFEE_RECEIPT_WRITE = "ROLE_STUDENTFEE_RECEIPT_WRITE";
    public static final String STUDENTFEE_RECEIPT_READ  = "ROLE_STUDENTFEE_RECEIPT_READ";

    public static final String CENTERFEE_READ  = "ROLE_CENTERFEE_READ";
    public static final String CENTERFEE_WRITE = "ROLE_CENTERFEE_WRITE";

    public static final String PROGRAM_READ  = "ROLE_PROGRAM_READ";
    public static final String PROGRAM_WRITE = "ROLE_PROGRAM_WRITE";

    public static final String STAFF_READ  = "ROLE_STAFF_READ";
    public static final String STAFF_WRITE = "ROLE_STAFF_WRITE";

    public static final String STUDENT_READ   = "ROLE_STUDENT_READ";
    public static final String STUDENT_WRITE  = "ROLE_STUDENT_WRITE";
    public static final String STUDENT_DELETE = "ROLE_STUDENT_DELETE";

    public static final String DASHBOARD_STATS = "ROLE_DASHBOARD_STATS";

    public static final String STUDENT_CLOCKINOUT = "ROLE_STUDENT_CLOCKINOUT";
    public static final String STAFF_CLOCKINOUT   = "ROLE_STAFF_CLOCKINOUT";

    public static final String IMPORT_WRITE = "ROLE_IMPORT_WRITE";
    public static final String IMPORT_READ  = "ROLE_IMPORT_READ";

    public static final String STUDENT_MESSAGE = "ROLE_STUDENT_MESSAGE";
    public static final String STAFF_MESSAGE   = "ROLE_STAFF_MESSAGE";

    public static final String SHARINGSHEET_READ  = "ROLE_SHARINGSHEET_READ";
    public static final String SHARINGSHEET_WRITE = "ROLE_SHARINGSHEET_WRITE";

    public static final String SUPPORT = "ROLE_SUPPORT";

    public static final String PARENT = "ROLE_PARENT";

    public static final String ADMIN_CENTER_LIST_READ = "ROLE_ADMIN_CENTER_LIST_READ";

    public static final String HYGIENE_READ  = "ROLE_HYGIENE_READ";
    public static final String HYGIENE_WRITE = "ROLE_HYGIENE_WRITE";

    public static final String STUDENT_APPROVAL_READ  = "ROLE_STUDENT_APPROVAL_READ";
    public static final String STUDENT_APPROVAL_WRITE = "ROLE_STUDENT_APPROVAL_WRITE";

    public static final String STAFF_APPROVAL_READ  = "ROLE_STAFF_APPROVAL_READ";
    public static final String STAFF_APPROVAL_WRITE = "ROLE_STAFF_APPROVAL_WRITE";

    public static final String GROUP_ACTIVITY_READ  = "ROLE_GROUP_ACTIVITY_READ";
    public static final String GROUP_ACTIVITY_WRITE = "ROLE_GROUP_ACTIVITY_WRITE";

    public static final String SALARY_READ  = "ROLE_SALARY_READ";
    public static final String SALARY_WRITE = "ROLE_SALARY_WRITE";

    public static final String MONTHLY_SALARY_READ  = "ROLE_MONTHLY_SALARY_READ";
    public static final String MONTHLY_SALARY_WRITE = "ROLE_MONTHLY_SALARY_WRITE";

    public static final String INQUIRY_READ  = "ROLE_INQUIRY_READ";
    public static final String INQUIRY_WRITE = "ROLE_INQUIRY_WRITE";

    public static final String FEE_REPORT              = "ROLE_FEE_REPORT";
    public static final String STD_ATTENDANCE_REPORT   = "ROLE_STD_ATTENDANCE_REPORT";
    public static final String STAFF_ATTENDANCE_REPORT = "ROLE_STAFF_ATTENDANCE_REPORT";
    public static final String INQUIRY_REPORT          = "ROLE_INQUIRY_REPORT";
    public static final String COLLECTION_FEE_REPORT   = "ROLE_COLLECTION_FEE_REPORT";

    public static final String FOLLOWUP_NOTIFICATION = "ROLE_FOLLOWUP_NOTIFICATION";

    public static final String STAFF_LEAVE_WRITE = "ROLE_STAFF_LEAVE_WRITE";
    public static final String STAFF_LEAVE_READ  = "ROLE_STAFF_LEAVE_READ";

    public static final String STAFF_ATTENDANCE_READ = "ROLE_STAFF_ATTENDANCE_READ";
    public static final String SELF_ATTENDANCE_READ  = "ROLE_SELF_ATTENDANCE_READ";

    public static final String GALLERY_READ  = "ROLE_GALLERY_READ";
    public static final String GALLERY_WRITE = "ROLE_GALLERY_WRITE";

    public static final String LEGAL_ENTITY_READ = "ROLE_LEGAL_ENTITY_READ";

    public static final String HR_ADMIN = "ROLE_HR_ADMIN";

    public static final String HOLIDAY_WRITE="ROLE_HOLIDAY_WRITE";

    public static final String STAFF_SALARY_REPORT="ROLE_STAFF_SALARY_REPORT";

  }
}
