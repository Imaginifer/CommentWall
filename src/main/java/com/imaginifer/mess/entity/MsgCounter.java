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
public class MsgCounter implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postCounterId;
    @ManyToOne(optional = false)
    private Commenter commenter;
    @ManyToOne(optional = false)
    private Forum forum;
    private int count;
    private LocalDateTime lastPost;

    public MsgCounter(Commenter commenter, Forum forum) {
        this.commenter = commenter;
        this.forum = forum;
        count = 1;
        lastPost = LocalDateTime.now();
    }

    public MsgCounter() {
    }

    public long getPostCounterId() {
        return postCounterId;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public Forum getForum() {
        return forum;
    }

    public int getCount() {
        return count;
    }

    public LocalDateTime getLastPost() {
        return lastPost;
    }
    
    public void update(){
        count++;
        lastPost = LocalDateTime.now();
    }
}
