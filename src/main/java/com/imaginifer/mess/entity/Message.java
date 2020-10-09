/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.*;


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
    @ManyToOne(optional = false)
    private Commenter commenter;
    private String text;
    private LocalDateTime date;
    private String ident;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msgId;
    private boolean deleted;
    private long nrInTopic;
    @ManyToOne(optional=false)
    private Topic topic;
    @OneToMany(mappedBy = "replyTo")
    private List<Message> replies;
    @ManyToOne
    private Message replyTo;

    public Message(Commenter author, String text, LocalDateTime dt, Topic topic, long nrInTopic, String ident) {
        this.commenter = author;
        this.text = text;
        this.date = dt;
        this.topic = topic;
        this.nrInTopic = nrInTopic;
        this.deleted = false;
        this.replyTo = null;
        this.ident = ident;
    }
    
    public Message(Commenter author, String text, LocalDateTime dt, Topic topic, long nrInTopic, 
            Message replyTo, String ident) {
        this.commenter = author;
        this.text = text;
        this.date = dt;
        this.topic = topic;
        this.nrInTopic = nrInTopic;
        this.deleted=false;
        this.replyTo = replyTo;
        this.ident = ident;
    }

    public Message() {
    }
    

    public Commenter getCommenter() {
        return commenter;
    }

    public String getText() {
        return text;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public long getMsgId() {
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
    
    public LocalDateTime getDate(){
        return date;
    }
    
    public long getNrInTopic(){
        return this.nrInTopic;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
    
    
    
    
    
    
    
    
    
    
    
}
