<html>
<head>
  <style>
    .sliptable th{
      text-align: left;
    }
    .receipttable tr{
      border: 1px solid black;
    }
    .receipttable td{
      border: 1px solid black;
      text-align: center;
    }
    .receipttable th{
      border: 1px solid black;
      text-align: center;
    }

  </style>
</head>
<body style="font-family: arial;">
<table style="width: 100%;height: 100%; border-collapse: separate; border-spacing: 15px">
  <tr>
    <td style="border: 1px solid black; padding: 15px; border-collapse: collapse; width: 50%">

      <table style="width: 100%; height: 100%;">
        <tr style="height: 100px;">
          <td style="width: 200px">
            <img src="http://portal.ipsaa.in/assets/img/Ipsaa-logo.png"/>
          </td>
          <td style="text-align: center;font-size: 20pt;">
            <strong>Fee Receipt</strong>
          </td>
          <td style="width: 220px">
            <div id="serial"><strong>S.No.</strong> : ${slip.receiptSerial}</div>
            <div id="date"><strong>Date</strong> : ${date}</div>
          </td>
        </tr>
        <tr style="height: 20px"><td colspan="3" style="text-align: center">
          Student copy
        </td></tr>
        <tr>
          <td colspan="3">
          <#include "ipsaaclub-slip-block.ftl">
          <#include "new-receipt-blick.ftl">
          </td>
        </tr>
        <tr style="height: 120px;">
          <td colspan="3">
          <#include "footer.ftl">
          </td>
        </tr>
      </table>

    </td>
    <td style="border: 1px solid black; padding: 15px; border-collapse: collapse; width: 50%">

      <table style="width: 100%; height: 100%;">
        <tr style="height: 100px;">
          <td style="width: 200px">
            <img src="http://portal.ipsaa.in/assets/img/Ipsaa-logo.png"/>
          </td>
          <td style="text-align: center;font-size: 20pt;">
            <strong>Fee Receipt</strong>
          </td>
          <td style="width: 220px">
            <div id="serial"><strong>S.No.</strong> : ${slip.receiptSerial}</div>
            <div id="date"><strong>Date</strong> : ${date}</div>
          </td>
        </tr>
        <tr style="height: 20px"><td colspan="3" style="text-align: center">
          Center copy
        </td></tr>
        <tr>
          <td colspan="3">
          <#include "ipsaaclub-slip-block.ftl">
          <#include "new-receipt-blick.ftl">
          </td>
        </tr>
        <tr style="height: 120px;">
          <td colspan="3">
          <#include "footer.ftl">
          </td>
        </tr>
      </table>

    </td>
  </tr>
</table>
</body>
</html>