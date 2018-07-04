<table style="margin: auto" class="sliptable">
 <td style="width:30%">
  <table>
   <caption>
    <b><u>Child's Info</u></b>
   </caption>
   <tr>
    <th>First Name</th>
    <td>
     <span> : </span>${slip.firstName}</td>
   </tr>
   <tr>
    <th>Last Name</th>
    <td>
     <span> : </span>${slip.lastName}</td>

   </tr>
   <tr>
    <th>Nick Name</th>
    <td>
     <span> : </span>${slip.admissionNumber}</td>
   </tr>
   <tr>
    <th>Nick Name</th>
    <td>
     <span> : </span>${slip.nickName}</td>
   </tr>
   <tr>
    <th>Blood Group</th>
    <td>
     <span> : </span>${slip.bloodGroup}</td>
   </tr>
   <tr>
    <th>Gender</th>
    <td>
     <span> : </span>${slip.gender}</td>
   </tr>
   <tr>
    <th>DOB</th>
    <td>
     <span> : </span>${slip.dob}</td>
   </tr>
   <tr>
    <th>Gender</th>
    <td>
     <span> : </span>${slip.gender}</td>
   </tr>
   <tr>
    <th>Nationality</th>
    <td>
     <span> : </span>${slip.nationality}</td>
   </tr>
  </table>
  <table>
   <caption>
    <b><u>Admission sought in</u></b>
   </caption>
   <tr>
    <th>Program</th>
    <td>
     <span> : </span>${slip.program.name}</td>
   </tr>
   <tr>
    <th>Timing</th>
    <td>
     <span> : </span>
     <#if slip.expectedIn??>${slip.expectedIn?time}</#if> to
     <#if slip.expectedOut??>${slip.expectedOut?time}</#if>
    </td>
   </tr>
  </table>
 </td>
 <td style="width:30%">
  <table>
   <caption>
    <b><u>Father's Detail</u></b>
   </caption>
   <tr>
    <th>Full Name</th>
    <td>
     <span> : </span>${father.fullName}</td>
   </tr>
   <tr>
    <th>Education Qualification</th>
    <td>
     <span> : </span>
     <#if father.educationalQualification??>
      ${father.educationalQualification}
     </#if>
    </td>
   </tr>
   <tr>
    <th>Occupation/Designation</th>
    <td>
     <span> : </span>
     ${father.occupation}</td>
   </tr>
   <tr>
    <th>Designation</th>
    <td>
     <span> : </span>
     <#if father.designation??>
      ${father.designation}
     </#if>
    </td>
   </tr>
   <tr>
    <th>Name of Organisation</th>
    <td>
     <span> : </span>
     <#if father.organisation??>${father.organisation}</#if>
    </td>
   </tr>
   <tr>
    <th>Office Address</th>
    <td>
     <span> : </span>
     <#if father.officeAddress.address??>${father.officeAddress.address}</#if>,
     <#if father.officeAddress.city??>${father.officeAddress.city}</#if>,
     <#if father.officeAddress.state??>${father.officeAddress.state}</#if>,
     <#if father.officeAddress.zipcode??>${father.officeAddress.zipcode}</#if>,
    </td>
   </tr>
   <tr>
    <th>Contact Number</th>
    <td>
     <span> : </span>
     <#if father.officeAddress.phone??>${father.officeAddress.phone}</#if>
    </td>
   </tr>
   <tr>
    <th>Email</th>
    <td>
     <span> : </span>
     <#if father.officeAddress.email??>${father.officeAddress.email}</#if>
    </td>
   </tr>
  </table>
 </td>
 <td style="width:40%">
  <table>
   <caption>
    <b><u>Mother's Detail</u></b>
   </caption>
   <tr>
    <th>Full Name</th>
    <td>
     <span> : </span>${mother.fullName}</td>
   </tr>
   <tr>
    <th>Education Qualification</th>
    <td>
     <span> : </span>
     <#if mother.educationalQualification??>
      ${mother.educationalQualification}
     </#if>
    </td>
   </tr>
   <tr>
    <th>Occupation/Designation</th>
    <td>
     <span> : </span>
     ${mother.occupation}</td>
   </tr>
   <tr>
    <th>Designation</th>
    <td>
     <span> : </span>
     <#if mother.designation??>
      ${mother.designation}
     </#if>
    </td>
   </tr>
   <tr>
    <th>Name of Organisation</th>
    <td>
     <span> : </span>
     <#if mother.organisation??>${mother.organisation}</#if>
    </td>
   </tr>
   <tr>
    <th>Office Address</th>
    <td>
     <span> : </span>
     <#if mother.officeAddress.address??>${mother.officeAddress.address}</#if>,
     <#if mother.officeAddress.city??>${mother.officeAddress.city}</#if>,
     <#if mother.officeAddress.state??>${mother.officeAddress.state}</#if>,
     <#if mother.officeAddress.zipcode??>${mother.officeAddress.zipcode}</#if>,
    </td>
   </tr>
   <tr>
    <th>Contact Number</th>
    <td>
     <span> : </span>
     <#if mother.officeAddress.phone??>${mother.officeAddress.phone}</#if>
    </td>
   </tr>
   <tr>
    <th>Email</th>
    <td>
     <span> : </span>
     <#if mother.officeAddress.email??>${mother.officeAddress.email}</#if>
    </td>
   </tr>
  </table>


 </td>
</table>