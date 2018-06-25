<table style="width: 100%;border-collapse: collapse;" class="receipttable">
  <tr>
    <th>Date</th>
    <th>Mode</th>
    <th>Amount in words</th>
    <th>Received Amount</th>
  </tr>
<#list payments as payment>
  <tr>
    <td>${payment.paymentDate}</td>
    <td>${payment.paymentMode}</td>
    <td>${payment.paidAmountWords?upper_case} Rupees Only</td>
    <td>${payment.paidAmount}</td>
  </tr>
</#list>


  <tr style="margin-top: 10px">
    <th colspan="3" style="text-align: right">Paid Amount:</th>
    <td>${paidAmount}</td>
  </tr>

  <tr>
    <th colspan="3" style="text-align: right">Total Amount:</th>
    <td>${totalAmount}</td>
  </tr>
  <tr>
    <th colspan="3" style="text-align: right">Due Amount</th>
    <td>${dueAmount}</td>
  </tr>

</table>