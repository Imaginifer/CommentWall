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
public class MessageView {
    private String username;
    private String text;
    private String date;
    private String ident;
    private long msgId;
    private String nrInTopic;
    private boolean deleted;
    private String topic;
    private long topicId;
    private String replyToNr;
    private long replyTo;

    public MessageView(String username, String text, String date, String ident, long msgId, 
            String nrInTopic, boolean deleted, String topic, long topicId, String replyToNr, long replyTo) {
        this.username = username;
        this.text = text;
        this.date = date;
        this.msgId = msgId;
        this.ident = ident;
        this.nrInTopic = nrInTopic;
        this.deleted = deleted;
        this.topic = topic;
        this.topicId = topicId;
        this.replyTo = replyTo;
        this.replyToNr = replyToNr;
    }

    public MessageView() {
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(long replyTo) {
        this.replyTo = replyTo;
    }
    
    public boolean isReply(){
        return replyTo != 0;
    }

    public String getNrInTopic() {
        return nrInTopic;
    }

    public void setNrInTopic(String nrInTopic) {
        this.nrInTopic = nrInTopic;
    }

    public String getReplyToNr() {
        return replyToNr;
    }

    public void setReplyToNr(String replyToNr) {
        this.replyToNr = replyToNr;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }
    
    
    
    
    
    
    
    
    
    
}
