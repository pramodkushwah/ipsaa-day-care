<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>New Query</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body style="margin: 0; padding: 0;">
<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
  <tr>
    <td align="center" bgcolor="#ffffff" style="padding: 40px 0 40px 0;">
      <img src="https://ipsaa.in/wp-content/uploads/2017/03/Ipsaa-logo.png" alt="NEW" style="display: block;"/>
    </td>
  <tr>
    <td bgcolor="#ffffff" style="padding: 40px 30px 40px 30px;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td style="color: #153643; font-family: Arial, sans-serif; font-size: 24px;">
            <b>Student ${student.profile.fullName} added in ${student.center.name}, ${student.program.name}, ${student.group.name}</b>
          </td>
        </tr>
        <tr>
          <td style="color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;">
            <br>
            Please click
            <a href="${baseURL}/mis/index.html#!/app/student_approval/${maskedId?c}">here</a>
            to approve student.
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td bgcolor="#ffcc02" style="padding: 30px 30px 30px 30px; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px;">
      <i>from</i><br>
      <strong>--IPSAA Autobot</strong>
    </td>
  </tr>
</table>
</body>
</html>