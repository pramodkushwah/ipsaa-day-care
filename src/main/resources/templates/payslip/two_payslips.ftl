<html>
<head>
  <style>
    .border {
      border-collapse: collapse;
    }

    .border > tbody > tr > td {
      border: 1px solid black;
    }
  </style>
</head>
<body style="font-family: arial;">
<table style="width: 100%;height: 100%; border-collapse: separate; border-spacing: 15px">
  <tr>
    <td style="border: 1px solid black; padding: 15px; border-collapse: collapse; width: 50%;">
      <table style="width: 100%; height: 100%;">
        <tr style="height: 100px;">
          <td style="width: 200px">
            <img src="http://portal.ipsaa.in/assets/img/Ipsaa-logo.png"/>
            <span style="width: 70%;text-align: right;display: inline-block;">
              <strong style="width: 50%;display: inline-block;">${paySlip.employerName}</strong><br>
              <span style="word-break: break-word;width: 50%;display: inline-block;">${paySlip.employerAddress}</span>
            </span>
          </td>
        </tr>
        <tr style="height: 20px;">
          <td style="text-align: center">
            <strong style="font-size: 20pt;">Pay Slip</strong><br>
            ${paySlip.month} / ${paySlip.year?c}
            <p style="margin:0;margin-top:5px;">Center copy</p>
          </td>
        </tr>
        <tr style="vertical-align: top;">
          <td>
          <#include "payslip_block.ftl">
          </td>
        </tr>
        <tr style="height: 120px;">
          <td>
          <#include "footer.ftl">
          </td>
        </tr>
      </table>
    </td>
    <td style="border: 1px solid black; padding: 15px; border-collapse: collapse; width: 50%;">
      <table style="width: 100%; height: 100%;">
        <tr style="height: 100px;">
          <td style="width: 200px">
            <img src="http://portal.ipsaa.in/assets/img/Ipsaa-logo.png"/>
            <span style="width: 70%;text-align: right;display: inline-block;">
              <strong style="width: 50%;display: inline-block;">${paySlip.employerName}</strong><br>
              <span style="word-break: break-word;width: 50%;display: inline-block;">${paySlip.employerAddress}</span>
            </span>
          </td>
        </tr>
        <tr style="height: 20px;">
          <td style="text-align: center">
            <strong style="font-size: 18pt;">Pay Slip</strong><br>
            ${paySlip.month} / ${paySlip.year?c}
            <p style="margin:0;margin-top:5px;">Employee copy</p>
          </td>
        </tr>
        <tr style="vertical-align: top;">
          <td>
          <#include "payslip_block.ftl">
          </td>
        </tr>
        <tr style="height: 120px;">
          <td>
          <#include "footer.ftl">
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>