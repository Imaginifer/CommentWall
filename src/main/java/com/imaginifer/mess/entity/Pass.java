/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Pass implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long passId;
    private LocalDateTime created;
    @OneToOne(optional = false)
    private Commenter commenter;
    private int validator;

    public Pass(Commenter comm) {
        this.commenter = comm;
        this.created = LocalDateTime.now();
    }
    
    public Pass(int validator){
        this.validator = validator;
        this.created = LocalDateTime.now();
    }

    public Pass() {
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
    }

    public long getPassId() {
        return passId;
    }

    public int getValidator() {
        return validator;
    }

    public void setValidator(int validator) {
        this.validator = validator;
    }
    
    
    
    
    
    
    
    
}
