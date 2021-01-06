/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.Felony;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Report implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;
    @ManyToOne (optional = false)
    private Commenter initiator;
    @ManyToOne (optional = false)
    private Message subject;
    @Enumerated (EnumType.STRING)
    private Felony felony;
    private boolean relevant;

    public Report(Commenter initiator, Message subject, Felony felony) {
        this.initiator = initiator;
        this.subject = subject;
        this.felony = felony;
        this.relevant = true;
    }

    public Report() {
    }

    public long getReportId() {
        return reportId;
    }

    public Commenter getInitiator() {
        return initiator;
    }

    public Message getSubject() {
        return subject;
    }

    public Felony getFelony() {
        return felony;
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }
    
    
}
