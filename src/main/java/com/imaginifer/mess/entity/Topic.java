/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.TopicAccess;
import com.imaginifer.mess.enums.TopicStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    private String text="";
    private String title="";
    private LocalDateTime created;
    private LocalDateTime lastUpdate;
    @Enumerated(EnumType.STRING)
    private TopicStatus status;
    @Enumerated(EnumType.STRING)
    private TopicAccess access;
    @OneToMany(cascade = REMOVE, mappedBy = "topic")
    private final List<Message> messages = new ArrayList<>();
    @ManyToOne(optional = false)
    private Forum forum;
    
    public Topic(String title, String text, Forum forum, boolean hidden){
        this.text=text.length() > 100 ? text.substring(0, 100) : text;
        this.title=title;
        this.created=LocalDateTime.now();
        this.lastUpdate=LocalDateTime.now();
        this.forum = forum;
        this.access = hidden ? 
                (forum.getAccess() == TopicAccess.ALL 
                || forum.getAccess() == TopicAccess.USER ? TopicAccess.USER : 
                TopicAccess.INTERNAL) : forum.getAccess();
        this.status = TopicStatus.STANDARD;
    }
    
    public Topic(){
    }

    public long getTopicId() {
        return topicId;
    }

    public String getText() {
        return text;
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
    
    
    public void update(){
        this.lastUpdate = LocalDateTime.now();
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public void setStatus(TopicStatus status) {
        this.status = status;
    }

    public TopicAccess getAccess() {
        return access;
    }

    public void setAccess(TopicAccess access) {
        this.access = access;
    }
    
    
}
