package com.synlabs.ipsaa.entity.inquiry;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.enums.FormType;
import com.synlabs.ipsaa.enums.InquiryStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WebsiteInquiry extends BaseEntity {

    @Column(length = 50)
    private String name;

    @Column(nullable = false,length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String city;

    @Column(length = 150)
    private String message;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    @Enumerated(EnumType.STRING)
    private FormType formType;

    @ManyToOne
    private Program program;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public InquiryStatus getStatus() { return status; }

    public void setStatus(InquiryStatus status) { this.status = status; }

    public FormType getFormType() { return formType; }

    public void setFormType(FormType formType) { this.formType = formType; }

    public Program getProgram() { return program; }

    public void setProgram(Program program) { this.program = program; }
}
