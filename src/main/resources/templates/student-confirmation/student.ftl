<table style="margin: auto;" class="sliptable">
    <caption><h3 style="background-color: #ddd;text-align: center">Child's Info</h3></caption>
	<tr><th></th></tr>
    <td>
        <tr>
            <#-- student details -->
            <td style="width:48%;">
                <table style="height: 280px; width: 96%; margin: auto">
                    <tr>
                        <th>First Name<span class="right"> : </span></th>
                        <td>
                            ${slip.firstName}</td>
                    </tr>

                    <tr>
                        <th>Addmission No.<span class="right"> : </span></th>
                        <td>
                            ${slip.admissionNumber}</td>
                    </tr>

                    <tr>
                        <th>Blood Group<span class="right"> : </span></th>
                        <td>
                            ${slip.bloodGroup}</td>
                    </tr>

                    <tr>
                        <th>Gender<span class="right"> : </span></th>
                        <td>
                            ${slip.gender}</td>
                    </tr>
                    <tr style="height: 15px;">
                        <td class="no-border"></td>
                    </tr>
                    <tr>
                        <th colspan="2"><h4 style="background-color: #ddd; text-align: center">Admission sought in</h4></th>
                    </tr>
                    <tr>
                        <th>Program<span class="right"> : </span></th>
                        <td>
                            ${slip.program.name}</td>
                    </tr>
                    <tr>
                        <th>Timing<span class="right"> : </span></th>
                        <td>
                            
                            <#if slip.expectedIn??>${slip.expectedIn?time}</#if> to
                            <#if slip.expectedOut??>${slip.expectedOut?time}</#if>
                        </td>
                    </tr>

                </table>
            </td>

            <td style="width:48%;" colspan="2">
                <table style="height: 280px; width: 96%; margin: auto">

                    <tr>
                        <th>Last Name<span class="right"> : </span></th>
                        <td>
                            ${slip.lastName}</td>
                    </tr>

                    <tr>
                        <th>Nick Name<span class="right"> : </span></th>
                        <td>
                            ${slip.nickName}</td>
                    </tr>

                    <tr>
                        <th>DOB<span class="right"> : </span></th>
                        <td>
                            ${slip.dob}</td>
                    </tr>

                    <tr>
                        <th>Nationality<span class="right"> : </span></th>
                        <td>
                            ${slip.nationality}</td>
                    </tr>

                    <tr style="height: 15px;">
                        <td class="no-border"></td>
                    </tr>
                    <tr>
                        <th colspan="2">
                            <h4>&nbsp;</h4>
                        </th>
                    </tr>
                    <tr>
                        <th>&nbsp;</th>
                        <td class="no-border">
                            <span> &nbsp; </span>&nbsp;</td>
                    </tr>
                    <tr>
                        <th>&nbsp;</th>
                        <td class="no-border">
                            <span> &nbsp; </span>
                              &nbsp;
                        </td>
                    </tr>
                </table>
            </td>

        </tr>

        <tr style="height: 25px">
            <td colspan="3" style="height: 25px"></td>
        </tr>

        <tr>

            <#-- father details -->
            <td style="width:48%;">
                <table style="height: 350px; width: 96%; margin: auto">
                    <caption>
                        <h3 style="background-color: #ddd; text-align: center">Father's Detail</h3>
                    </caption>

                    <tr><th></th></tr>
                    <tr>
                        <th>Full Name<span class="right"> : </span></th>
                        <td>
                            ${father.fullName}</td>
                    </tr>
                    <tr>
                        <th>Education Qualification<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.educationalQualification??>
                                ${father.educationalQualification}
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Occupation/Designation<span class="right"> : </span></th>
                        <td>
                            
                            ${father.occupation}</td>
                    </tr>
                    <tr>
                        <th>Designation<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.designation??>
                                ${father.designation}
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Name of Organisation<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.organisation??>${father.organisation}</#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Office Address<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.officeAddress.address??>${father.officeAddress.address}</#if>,
                            <#if father.officeAddress.city??>${father.officeAddress.city}</#if>,
                            <#if father.officeAddress.state??>${father.officeAddress.state}</#if>,
                            <#if father.officeAddress.zipcode??>${father.officeAddress.zipcode}</#if>,
                        </td>
                    </tr>
                    <tr>
                        <th>Contact Number<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.officeAddress.phone??>${father.officeAddress.phone}</#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Email<span class="right"> : </span></th>
                        <td>
                            
                            <#if father.officeAddress.email??>${father.officeAddress.email}</#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="no-border"></td>
                    </tr>
                </table>
                
            </td>

                <#-- mother details -->
            <td style="width:48%" colspan="2">
                <table style="height: 350px; width: 96%; margin: auto">
                    <caption>
                        <h3 style="background-color: #ddd; text-align: center">Mother's Detail</h3>
                    </caption>
					<tr><th></th></tr>
                    <tr>
                        <th>Full Name<span class="right"> : </span></th>
                        <td>
                            ${mother.fullName}</td>
                    </tr>
                    <tr>
                        <th>Education Qualification<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.educationalQualification??>
                                ${mother.educationalQualification}
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Occupation/Designation<span class="right"> : </span></th>
                        <td>
                            
                            ${mother.occupation}</td>
                    </tr>
                    <tr>
                        <th>Designation<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.designation??>
                                ${mother.designation}
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Name of Organisation<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.organisation??>${mother.organisation}</#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Office Address<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.officeAddress.address??>${mother.officeAddress.address}</#if>,
                            <#if mother.officeAddress.city??>${mother.officeAddress.city}</#if>,
                            <#if mother.officeAddress.state??>${mother.officeAddress.state}</#if>,
                            <#if mother.officeAddress.zipcode??>${mother.officeAddress.zipcode}</#if>,
                        </td>
                    </tr>
                    <tr>
                        <th>Contact Number<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.officeAddress.phone??>${mother.officeAddress.phone}</#if>
                        </td>
                    </tr>
                    <tr>
                        <th>Email<span class="right"> : </span></th>
                        <td>
                            
                            <#if mother.officeAddress.email??>${mother.officeAddress.email}</#if>
                        </td>
                    </tr>
                </table>
            </td>

        </tr>

    </td>
</table>