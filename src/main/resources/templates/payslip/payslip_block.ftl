<div>&nbsp;</div>

<table style="width: 100%" class="">
  <tbody>
  <tr>
    <td style="width: 180px;">EId</td>
    <td style="width: 280px;">:&nbsp;&nbsp;${paySlip.eid}</td>
    <td style="width: 210px;">Aadhar No.</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.aadharNumber}</td>
  </tr>
  <tr>
    <td style="width: 180px;">Name</td>
    <td style="width: 280px;">:&nbsp;&nbsp;${paySlip.empName}</td>
    <td style="width: 210px;">UAN</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.uan}</td>
  </tr>
  <tr>
    <td style="width: 180px;">Designation</td>
    <td style="width: 280px;">:&nbsp;&nbsp;${paySlip.empDesignation}</td>
    <td style="width: 210px;">ESI Number</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.esin}</td>
  </tr>
  <tr>
    <td style="width: 180px;">Month and year</td>
    <td style="width: 280px;">:&nbsp;&nbsp;${paySlip.month} / ${paySlip.year?c}</td>
    <td style="width: 210px;">Bank Acc. No.</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.ban}</td>
  </tr>
  <tr>
    <td style="width: 180px;">Date Of Joining</td>
    <td style="width: 280px;">:&nbsp;&nbsp;${paySlip.month} / ${paySlip.year?c}</td>
    <td style="width: 210px;">PRAN</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.pran}</td>
  </tr>
  <tr>
    <td style="width: 180px;vertical-align: top;">Location</td>
    <td style="width: 280px;vertical-align: top;">:&nbsp;&nbsp;${paySlip.centerName}</td>
    <td style="width: 210px;">PAN</td>
    <td style="width: 350px;">:&nbsp;&nbsp;${paySlip.pan}</td>
  </tr>
  <tr>
    <td style="width: 210px;vertical-align: top;">Presents/Total</td>
    <td style="width: 210px;vertical-align: top;">:&nbsp;&nbsp;${paySlip.presents}/${paySlip.totalDays}</td>
    <td style="width: 210px;vertical-align: top;">PF Acc No.</td>
    <td style="width: 350px;vertical-align: top;">:&nbsp;&nbsp;${paySlip.pfan}</td>
  </tr>
  </tbody>
</table><#--Employee details table-->

<div>&nbsp;</div>

<table style="width: 100%;" class="border">
  <tbody>
  <tr>
    <td style="width: 237px;"><strong>Earnings</strong></td>
    <td style="width: 100px;"><strong>&nbsp;</strong></td>
    <td style="width: 195px;"><strong>Deductions</strong></td>
    <td style="width: 121px;">&nbsp;</td>
  </tr>
  <tr>
    <td style="width: 237px;">Basic</td>
    <td style="width: 100px;">${paySlip.basic}</td>
    <td style="width: 195px;">PF</td>
    <td style="width: 121px;">${paySlip.pfe}</td>
  </tr>
  <tr>
    <td style="width: 237px;">Conveyance</td>
    <td style="width: 100px;">${paySlip.conveyance}</td>
    <td style="width: 195px;">ESI</td>
    <td style="width: 121px;">${paySlip.esi}</td>
  </tr>
  <tr>
    <td style="width: 237px;">HRA</td>
    <td style="width: 100px;">${paySlip.hra}</td>
    <td style="width: 195px;">Professional Tax</td>
    <td style="width: 121px;">${paySlip.professionalTax}</td>
  </tr>
  <tr>
    <td style="width: 237px;">Bonus</td>
    <td style="width: 100px;">${paySlip.bonus}</td>
    <td style="width: 195px;">TDS</td>
    <td style="width: 121px;">${paySlip.tds}</td>
  </tr>
  <tr>
    <td style="width: 237px;">Other Allowance</td>
    <td style="width: 100px;">${paySlip.otherAllowances}</td>
    <td style="width: 195px;">Other Deductions</td>
    <td style="width: 121px;">${paySlip.otherDeductions}</td>
  </tr>
  <tr>
    <td style="width: 195px;">Special</td>
    <td style="width: 121px;">${paySlip.special}</td>
    <td style="width: 237px;">&nbsp;</td>
    <td style="width: 100px;">&nbsp;</td>
  </tr>
  <tr>
    <td style="width: 195px;">Medical</td>
    <td style="width: 121px;">${paySlip.medical}</td>
    <td style="width: 237px;">&nbsp;</td>
    <td style="width: 100px;">&nbsp;</td>
  </tr>
  <tr>
    <td style="width: 237px;">Total Earning</td>
    <td style="width: 100px;">${paySlip.totalEarning}</td>
    <td style="width: 195px;">Total Deduction</td>
    <td style="width: 121px;">${paySlip.totalDeduction}</td>
  </tr>
  <tr>
    <td style="width: 237px;">&nbsp;</td>
    <td style="width: 100px;">&nbsp;</td>
    <td style="width: 195px;"><strong>NET Salary</strong></td>
    <td style="width: 121px;">${paySlip.netSalary}</td>
  </tr>
  </tbody>
</table>

<div>&nbsp;</div>

<table style="width: 100%;">
  <tbody>
  <tr>
    <td style="text-align: center;">Amount (in words)</td>
    <td style="text-align: center;"><strong>for ${paySlip.employerName}</strong></td>
  </tr>
  <tr>
    <td>${amountWords}</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td style="text-align: center;">Authorized Signatory</td>
  </tr>
  </tbody>
</table>
