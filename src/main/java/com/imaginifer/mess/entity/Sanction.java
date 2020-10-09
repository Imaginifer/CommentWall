/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.SanctionType;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Sanction implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sanctionId;
    @Enumerated(EnumType.STRING)
    private SanctionType type;
    @ManyToOne(optional = false)
    private Commenter commenter;
    @ManyToOne
    private Forum sanctionScope;
    private LocalDateTime created;
    private LocalDateTime expires;
    private boolean valid;

    public Sanction(SanctionType type, Commenter comm, Forum sanctionScope, int duration) {
        this.type = type;
        this.commenter = comm;
        this.sanctionScope = sanctionScope;
        this.created = LocalDateTime.now();
        this.expires = LocalDateTime.now().plusHours(duration);
        this.valid = true;
    }

    public Sanction() {
    }

    public long getSanctionId() {
        return sanctionId;
    }

    public SanctionType getType() {
        return type;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public Forum getSanctionScope() {
        return sanctionScope;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    
    
}
