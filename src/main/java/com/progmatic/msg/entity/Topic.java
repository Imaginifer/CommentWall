/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.entity;

import java.io.Serializable;
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
    private int topicId;
    private String author="";
    private String title="";
    @OneToMany(cascade = REMOVE, mappedBy = "topic")
    private List<Message> messages = new ArrayList<>();
    
    public Topic(String author, String title){
        this.author=author;
        this.title=title;
        
    }
    
    public Topic(){
    }

    public int getTopicId() {
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
    
    
}
