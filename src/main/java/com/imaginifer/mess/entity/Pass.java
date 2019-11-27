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
    private int passId;
    private LocalDateTime created;
    @OneToOne
    private Commenter comm;

    public Pass(Commenter comm) {
        this.comm = comm;
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

    public Commenter getComm() {
        return comm;
    }

    public void setComm(Commenter comm) {
        this.comm = comm;
    }

    public int getPassId() {
        return passId;
    }
    
    
    
    
    
    
}
