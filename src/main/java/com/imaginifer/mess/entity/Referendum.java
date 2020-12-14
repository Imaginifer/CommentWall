/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author imaginifer
 */

@Entity
@NamedEntityGraph(name = "loadWithVoters", 
        attributeNodes = @NamedAttributeNode(value ="voted"))
public class Referendum implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long referendumId;
    private String title;
    private LocalDateTime creationDate;
    private boolean closed;
    @ManyToOne
    private Commenter initiator;
    @ManyToMany
    private Set<Commenter> voted;
    @OneToMany(mappedBy = "referendum", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Nominee> nominees;

    public Referendum(String title, Commenter initiator) {
        this.title = title;
        this.initiator = initiator;
        creationDate = LocalDateTime.now();
        voted = new HashSet<>();
        nominees = new HashSet<>();
        closed = false;
    }

    public Referendum() {
    }

    public long getReferendumId() {
        return referendumId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public boolean isClosed() {
        return closed;
    }

    public Commenter getInitiator() {
        return initiator;
    }

    public Set<Commenter> getVoted() {
        return voted;
    }

    public Set<Nominee> getNominees() {
        return nominees;
    }
    
    public boolean castVote(Commenter voter, long vote){
        if(closed || 
                voted.stream().anyMatch(v -> v.getCommenterId() == voter.getCommenterId()) ||
                nominees.stream().noneMatch(n -> n.getNomineeId() == vote)){
            return false;
        }
        voted.add(voter);
        return true;
    }
    
    
}
