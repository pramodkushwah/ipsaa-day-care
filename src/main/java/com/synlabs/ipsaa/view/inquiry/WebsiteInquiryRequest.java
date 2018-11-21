package com.synlabs.ipsaa.view.inquiry;

import com.synlabs.ipsaa.entity.inquiry.WebsiteInquiry;
import com.synlabs.ipsaa.enums.FormType;
import com.synlabs.ipsaa.enums.InquiryStatus;
import com.synlabs.ipsaa.view.common.Request;
import org.joda.time.LocalDate;


public class WebsiteInquiryRequest implements Request {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String city;
    private String message;
    private FormType formType;
    private String program;

    public WebsiteInquiry toEntity(WebsiteInquiry inquiry){

        inquiry.setName(name);
        inquiry.setPhoneNumber(phone);
        inquiry.setEmail(email);
        inquiry.setCity(city);
        inquiry.setMessage(message);
        inquiry.setStatus(InquiryStatus.New);
        return inquiry;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public FormType getFormType() { return formType; }

    public void setFormType(FormType formType) { this.formType = formType; }

    public String getProgram() { return program; }

    public void setProgram(String program) { this.program = program; }
}
