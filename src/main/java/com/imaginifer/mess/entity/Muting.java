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
public class Muting implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mutingId;
    private LocalDateTime creationDate;
    @ManyToOne(optional = false)
    private Commenter whoMutes;
    @ManyToOne(optional = false)
    private Commenter whoIsMuted;

    public Muting(Commenter whoMutes, Commenter whoIsMuted) {
        this.whoMutes = whoMutes;
        this.whoIsMuted = whoIsMuted;
        creationDate = LocalDateTime.now();
    }

    public Muting() {
    }

    public long getMutingId() {
        return mutingId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Commenter getWhoMutes() {
        return whoMutes;
    }

    public Commenter getWhoIsMuted() {
        return whoIsMuted;
    }
    
    
}
