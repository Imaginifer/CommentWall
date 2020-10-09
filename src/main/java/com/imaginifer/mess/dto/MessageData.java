/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;

import java.util.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author imaginifer
 */
public class MessageData {
    
    @NotNull(message="Több betű kell!")
    @Size(min=1, message="Több betű kell!")
    private String text;
    private List<String> topics=new ArrayList<>();
    private long chosenTopic;
    private String newTopic = "";
    private long replied = 0;
    private long forumId = 0;
    private boolean notUpdating = true;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTopics() {
        return topics;
    }

    public long getChosenTopic() {
        return chosenTopic;
    }

    public void setChosenTopic(long chosenTopic) {
        this.chosenTopic = chosenTopic;
    }

    public String getNewTopic() {
        return newTopic;
    }

    public void setNewTopic(String newTopic) {
        this.newTopic = newTopic;
    }
    
    public void setTopics(List<String> topics){
        this.topics=topics;
    }

    public long getReplied() {
        return replied;
    }

    public void setReplied(long replied) {
        this.replied = replied;
    }
    
    public boolean isResponse(){
        return replied != 0;
    }

    public boolean isNotUpdating() {
        return notUpdating;
    }

    public void setNotUpdating(boolean notUpdating) {
        this.notUpdating = notUpdating;
    }

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }
    
    
    
    
   
    
    
    
    
    
}
