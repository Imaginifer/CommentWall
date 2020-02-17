/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;

import java.util.*;

/**
 *
 * @author imaginifer
 */
public class TopicView {
    
    private long topicId;
    private String author;
    private String title;
    private long messages;

    public TopicView(long topicId, String author, String title, long messages) {
        this.topicId = topicId;
        this.author = author;
        this.title = title;
        this.messages = messages;
    }

    public TopicView() {
    }
    
    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getMessages() {
        return messages;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }
    
    
}
