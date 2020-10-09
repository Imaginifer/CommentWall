/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;


/**
 *
 * @author imaginifer
 */
public class TopicView {
    
    private long topicId;
    private String text;
    private String title;
    private long messages;
    private String lastUpdate;

    public TopicView(long topicId, String text, String title, long messages, String lastUpdate) {
        this.topicId = topicId;
        this.text = text;
        this.title = title;
        this.messages = messages;
        this.lastUpdate = lastUpdate;
    }
    
    public TopicView(long topicId, String title) {
        this.topicId = topicId;
        this.text = null;
        this.title = title;
        this.messages = 0;
        this.lastUpdate = null;
    }

    public TopicView(String author, String title) {
        this.topicId = 0;
        this.text = author;
        this.title = title;
        this.messages = 0;
        this.lastUpdate = null;
    }

    public TopicView() {
    }
    
    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    
    
}
