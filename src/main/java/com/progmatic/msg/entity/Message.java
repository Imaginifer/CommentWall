/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author imaginifer
 */
@Entity
@NamedEntityGraphs(@NamedEntityGraph(
    name="loadWithReplies",
    attributeNodes= @NamedAttributeNode(value="replies")
))
public class Message implements Serializable{
    private String username;
    private String text;
    private LocalDateTime date;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int msgId;
    private boolean deleted;
    @JsonIgnore
    @ManyToOne(optional=false)
    private Topic topic;
    @JsonIgnore
    @OneToMany(mappedBy = "replyTo")
    private List<Message> replies;
    @JsonIgnore
    @ManyToOne
    private Message replyTo;

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
        this.deleted = false;
        this.replyTo = null;
    }
    
    public Message(String username, String text, LocalDateTime dt, Topic topic, Message replyTo) {
        this.username = username;
        this.text = text;
        this.date = dt;
        this.topic = topic;
        this.deleted=false;
        this.replyTo = replyTo;
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
    
    public boolean isReply(){
        return replyTo != null;
    }
    
    public Message getReplyTo(){
        return replyTo;
    }

    public List<Message> getReplies() {
        return replies;
    }
    
    public void editText(String text){
        this.text = text;
    }
    
    
    
    
    
    
    
    
    
}
