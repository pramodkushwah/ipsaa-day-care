<table style="margin: auto" class="sliptable">
  <tr>
    <th>Fee for the month of</th>
    <td><span> : </span>${month}</div></td>
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
    <td><span> : </span>${slip.finalAnnualCharges}</td>
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
    <th>Admission Fee</th>
    <td><span> : </span><#if slip.finalAdmissionFee??>${slip.finalAdmissionFee}</#if></td>
  </tr>

  <!-- <tr>
    <th>Sub Total</th>
    <td><span> : </span> ${subTotal?string["0"]}</td>
  </tr> -->

<#if sgst?? || cgst??>
  <tr>
    <th>GST (${sgst+cgst}%)</th>
    <td><span> : ${sgstAmount+cgstAmount}</span></td>
  </tr>
</#if>
<#if slip.gstAmount??>
  <tr>
    <th>I-GST (${igst}%)</th>
    <td><span> : ${slip.gstAmount}</span></td>
  </tr>
</#if>

<#if adjust??>
  <tr>
    <th>Adjust Amount</th>
    <td><span> : ${adjust}</span></td>
  </tr>
</#if>
<#if balance??>
  <tr>
    <th>Balance</th>
    <td><span> : ${balance}</span></td>
  </tr>
</#if>

  <tr>
    <th>Transport Fee</th>
    <td><span> : </span>${slip.finalTransportFee}</td>
  </tr>

  <tr>
    <th>Uniform</th>
    <td><span> : </span>${slip.uniformCharges?string["0"]}</td>
  </tr>

  <tr>
    <th>Stationary</th>
    <td><span> : </span><#if slip.stationary??>${slip.stationary?string["0"]}</#if></td>
  </tr>

  <tr>
    <th>Extra Charges</th>
    <td><span> : </span>${slip.extraCharge}</td>
  </tr>
  <tr>
    <th>Late Payment</th>
    <td><span> : </span>${slip.latePaymentCharge}</td>
  </tr>
  <tr>
    <th>Total</th>
    <td><span> : </span>${slip.totalFee?string["0"]}</td>
  </tr>
</table>