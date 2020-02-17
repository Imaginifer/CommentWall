/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.*;
import static javax.persistence.CascadeType.REMOVE;


/**
 *
 * @author imaginifer
 */
@Entity
@NamedEntityGraphs(@NamedEntityGraph(
    name="loadWithMessages",
    attributeNodes= @NamedAttributeNode(value="messages")
))
public class Topic implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long topicId;
    private String author="";
    private String title="";
    private LocalDateTime created;
    private LocalDateTime lastUpdate;
    @OneToMany(cascade = REMOVE, mappedBy = "topic")
    private List<Message> messages = new ArrayList<>();
    
    public Topic(String author, String title){
        this.author=author;
        this.title=title;
        this.created=LocalDateTime.now();
        this.lastUpdate=LocalDateTime.now();
    }
    
    public Topic(){
    }

    public long getTopicId() {
        return topicId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getFormattedCreated(){
        return created.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
    
    public String getFormattedLastUpdate(){
        return lastUpdate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
    
    public void update(){
        this.lastUpdate = LocalDateTime.now();
    }
    
    
}
