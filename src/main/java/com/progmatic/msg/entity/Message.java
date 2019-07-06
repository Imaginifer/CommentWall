/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Message implements Serializable{
    private String username;
    private String text;
    private LocalDateTime date;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int msgId;
    private boolean deleted;
    @ManyToOne(optional=false)
    private Topic topic;

    /*public Message(String username, String text, LocalDateTime dt, int id) {
        this.username = username;
        this.text = text;
        this.date = dt;
        this.msgId = id;
        this.deleted=false;
    }*/
    public Message(String username, String text, LocalDateTime dt, Topic topic) {
        this.username = username;
        this.text = text;
        this.date = dt;
        this.topic = topic;
        this.deleted=false;
    }

    public Message() {
    }
    

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public int getMsgId() {
        return msgId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Topic getTopic() {
        return topic;
    }
    
    public void setTopic(Topic topic){
        this.topic = topic;
    }
    
    
    
    
    
    
    
    
}
