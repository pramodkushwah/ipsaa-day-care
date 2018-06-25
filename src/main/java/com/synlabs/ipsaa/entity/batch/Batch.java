package com.synlabs.ipsaa.entity.batch;


import com.synlabs.ipsaa.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Batch extends BaseEntity
{

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 200)
    private String filename;

    @Column(nullable = true, length = 200)
    private String comments;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}


//TODO - questions
//what all batch imports are needed?
//what all data setup is needed to make a center live
//how do we progressivly roll out features?