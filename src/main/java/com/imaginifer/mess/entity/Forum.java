/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.TopicAccess;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
@NamedEntityGraph(name = "loadWithSanctions", attributeNodes = @NamedAttributeNode("sanctions"))
public class Forum implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long forumId;
    @Enumerated(EnumType.STRING)
    private TopicAccess access;
    private String title;
    private String text;
    private long totalNrOfPosts;
    private long totalNrOfTopics;
    private LocalDateTime created;
    private LocalDateTime lastUpdate;
    @OneToMany(cascade = REMOVE, mappedBy = "forum")
    private final List<Topic> topics = new ArrayList<>();
    @OneToMany(cascade = REMOVE, mappedBy = "sanctionScope")
    private final Set<Sanction> sanctions = new HashSet<>();

    public Forum(String title, String text, boolean hidden) {
        this.title = title;
        this.text = text;
        this.access = hidden ? TopicAccess.USER : TopicAccess.ALL;
        this.created = this.lastUpdate = LocalDateTime.now();
    }

    public Forum() {
    }

    public long getForumId() {
        return forumId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public TopicAccess getAccess() {
        return access;
    }

    public void setAccess(TopicAccess access) {
        this.access = access;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTotalNrOfPosts() {
        return totalNrOfPosts;
    }

    public void setTotalNrOfPosts(long totalNrOfPosts) {
        this.totalNrOfPosts = totalNrOfPosts;
    }

    public long getTotalNrOfTopics() {
        return totalNrOfTopics;
    }

    public void setTotalNrOfTopics(long totalNrOfTopics) {
        this.totalNrOfTopics = totalNrOfTopics;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<Sanction> getSanctions() {
        return sanctions;
    }
    
    
    
    public void newTopic(){
        totalNrOfTopics++;
        totalNrOfPosts++;
        lastUpdate = LocalDateTime.now();
    }
    
    public void newMessage(){
        totalNrOfPosts++;
        lastUpdate = LocalDateTime.now();
    }
    
    
}
