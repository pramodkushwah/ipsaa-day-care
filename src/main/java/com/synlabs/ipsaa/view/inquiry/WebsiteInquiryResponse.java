package com.synlabs.ipsaa.view.inquiry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.synlabs.ipsaa.entity.inquiry.WebsiteInquiry;
import com.synlabs.ipsaa.entity.programs.Program;
import com.synlabs.ipsaa.enums.FormType;
import com.synlabs.ipsaa.enums.InquiryStatus;
import com.synlabs.ipsaa.view.center.ProgramResponse;
import com.synlabs.ipsaa.view.common.Response;

import java.util.Date;

public class WebsiteInquiryResponse implements Response {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String city;
    private String message;
    private InquiryStatus status;
    private FormType type;
    private ProgramResponse program;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "IST")
    private Date date;

    public WebsiteInquiryResponse(WebsiteInquiry inquiry){

        this.id=mask(inquiry.getId());
        this.name=inquiry.getName();
        this.phone=inquiry.getPhoneNumber();
        this.email=inquiry.getEmail();
        this.city=inquiry.getCity();
        this.message=inquiry.getMessage();
        this.status=inquiry.getStatus();
        this.type=inquiry.getFormType();
        if(inquiry.getProgram() !=null)
            this.program=new ProgramResponse(inquiry.getProgram());
        date=inquiry.getCreatedDate().toDate();
    }

    public String getName() { return name; }

    public String getPhone() { return phone; }

    public String getEmail() { return email; }

    public String getCity() { return city; }

    public String getMessage() { return message; }

    public InquiryStatus getStatus() { return status; }

    public void setStatus(InquiryStatus status) { this.status = status; }

    public FormType getType() { return type; }

    public void setType(FormType type) { this.type = type; }

    public ProgramResponse getProgram() { return program; }

    public void setProgram(ProgramResponse program) { this.program = program; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Date getDate() { return date; }
}
