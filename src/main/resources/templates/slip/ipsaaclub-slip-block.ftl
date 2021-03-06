<table style="margin: auto" class="sliptable">
  <tr>
    <th>Fee for the Days </th>
    <td><span> : from </span> ${slip.invoiceDate} to ${slip.expireDate}</div></td>
  </tr>
  <tr>
    <th>Name of the child</th>
    <td><span> : </span>${student.profile.fullName}</td>
  </tr>
  <tr>
    <th>Admission Number</th>
    <td><span> : </span>${student.admissionNumber}</td>
  </tr>
  <tr>
    <th>Program</th>
    <td><span> : </span>${student.program.name}</td>
  </tr>
  <tr>
    <th>Group</th>
    <td><span> : </span>${student.group.name}</td>
  </tr>
<#if isAnnualFee==true>
  <tr>
    <th>Annual Charges</th>
    <td><span> : </span>${slip.finalAnnualFee}</td>
  </tr>
</#if>

<#if isDeposit==true>
  <tr>
    <th>Security Deposit</th>
    <td><span> : </span>${slip.finalDepositFee}</td>
  </tr>
</#if>

  <tr>
    <th>Program Fee</th>
    <td><span> : </span>${slip.finalBaseFee?string["0"]}</td>
  </tr>
    <tr>
      <th>Total Days Fee</th>
      <td><span> : </span>${slip.totalDaysFee?string["0"]}</td>
    </tr>

  <!-- <tr>
    <th>Sub Total</th>
    <td><span> : </span> ${subTotal?string["0"]}</td>
  </tr> -->

<#if slip.gstAmount??>
  <tr>
    <th>GST 18%</th>
    <td><span> : ${slip.gstAmount}</span></td>
  </tr>
</#if>
<#if balance??>
  <tr>
    <th>Balance</th>
    <td><span> : ${balance}</span></td>
  </tr>
</#if>

<#if slip.extraCharge??>
  <tr>
    <th>Extra Charges</th>
    <td><span> : </span>${slip.extraCharge}</td>
  </tr>
</#if>
  <tr>
    <th>Total</th>
    <td><span> : </span>${slip.totalFee?string["0"]}</td>
  </tr>
</table>