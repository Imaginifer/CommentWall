/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Nominee implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nomineeId;
    private String title;
    private int votes;
    private boolean closed;
    @ManyToOne(optional = false)
    private Referendum referendum;

    public Nominee(String title, Referendum referendum) {
        this.title = title;
        this.referendum = referendum;
        votes = 0;
        closed = false;
    }

    public Nominee() {
    }

    public long getNomineeId() {
        return nomineeId;
    }

    public String getTitle() {
        return title;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isClosed() {
        return closed;
    }

    public Referendum getReferendum() {
        return referendum;
    }
    
    
    
}
