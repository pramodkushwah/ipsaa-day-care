<html>
<head>
  <style>
    .sliptable th, .sliptable caption{
      text-align: left;
    }
  </style>
</head>
<body style="font-family: arial;">
<table style="width: 100%;height: 100%; border-collapse: separate; border-spacing: 15px">
  <tr>
    <td style="border: 1px solid black; padding: 15px; border-collapse: collapse; width: 100%;">
      <table style="width: 100%; height: 100%;">
        <tr style="height: 100px;">
          <td style="width: 200px">
            <img src="http://portal.ipsaa.in/assets/img/Ipsaa-logo.png"/>
          </td>
          <td style="text-align: center;font-size: 24pt;">
            <strong>Confirmation</strong>
          </td>
          <td style="width: 200px">
            <div id="serial"><strong>S.No.</strong> : ${slip.admissionNumber}</div>
            <div id="date"><strong>Date</strong> : ${slip.admissionDate}</div>
          </td>
        </tr>
        <tr style="height: 20px">
			<td colspan="3" style="text-align: center">
			  Student Information
			</td>
		</tr>
        <tr>
          <td colspan="3">
          <#include "student.ftl">
          </td>
        </tr>
        <tr style="height: 120px;">
          <td colspan="3">
          
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>