<html>
<head>
<#--<link rel="stylesheet" href="style.css">-->
  <style>
  <#include "style.ftl">
  </style>
</head>
<body>
<#--<div class="left"><img style="height: 80px;" src="${appt_letter_image_url}"/></div>-->
<p>&nbsp;</p>
<p>&nbsp;</p>
<p class="header"><strong><u>APPOINTMENT LETTER</u></strong></p>
<p style="text-align: left;"><strong>${emp.name}</strong></p>
<p style="text-align: left; max-width: 350px"><strong>
${emp.profile.address.address},
${emp.profile.address.city},
${emp.profile.address.state},
${emp.profile.address.zipcode}
</strong></p>
<p style="text-align: right;"><strong>Date:</strong><strong> ${emp.profile.doj}</strong></p>
<p><strong>&nbsp;</strong></p>
<p><strong>Dear ${emp.name}</strong><strong>,</strong></p>
<p>On behalf of<strong> IPSAA Holdings Private Limited</strong>, (the &ldquo;Company&rdquo;) it is our pleasure to offer you the post of<strong>
  &lsquo;${emp.designation}&rsquo; </strong>in our company with effect from <strong>${emp.profile.doj}</strong><strong>&nbsp;</strong>on the following terms and conditions. &nbsp;
</p>
<ol class="ol_one_padding">
  <li><strong>COMPENSATION AND BENEFITS</strong></li>
</ol>
<p>As a member of the Company&rsquo;s team, your compensation and benefits will be as follows:</p>
<p>Your CTC will be <strong>Rs. ${salary} (${salaryWords})</strong>, inclusive of all allowances.</p>
<p>You will be eligible for benefits which will be subject to Company&rsquo;s policies/rules in this behalf made from time to time and applicable to you.</p>
<p>Your compensation and benefits package has been customized as discussed with you after taking into account your qualifications, quality and relevance of experience, your role at
  the Company and your last drawn package etc. You are requested to maintain complete confidentiality about your package.</p>
<p>The above package will be reviewed periodically according to the policy of the Company. The revision is discretionary and will be subject to your performance and conduct during
  the review period.</p>
<ol start="2" class="ol_one_padding">
  <li><strong>DESIGNATION</strong></li>
</ol>
<p>Your designation is merely indicative of the responsibilities, which you are required to carry out. The Company shall be entitled to require you, at any time, to perform any
  other administrative, managerial, supervisory, or other functions related to the Company&rsquo;s activities and you will be bound to carry out such functions.</p>

</body>
</html>