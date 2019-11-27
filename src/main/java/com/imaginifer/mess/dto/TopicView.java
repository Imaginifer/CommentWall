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
    
    private int topicId;
    private String author;
    private String title;
    private List<MessageView> messages;

    public TopicView(int topicId, String author, String title, List<MessageView> messages) {
        this.topicId = topicId;
        this.author = author;
        this.title = title;
        this.messages = messages;
    }

    public TopicView() {
    }
    
    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
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

    public List<MessageView> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageView> messages) {
        this.messages = messages;
    }
    
    
}
