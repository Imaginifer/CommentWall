/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.dto;

import java.util.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author imaginifer
 */
public class MessageData {
    @NotNull
    @Size(min=1, max=20, message="Needs more letters!")
    private String name;
    @NotNull
    @Size(min=1, message="Needs more letters!")
    private String text;
    private List<String> topics=new ArrayList<>();
    private String chosenTopic;
    private String newTopic;
    private String replied = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getChosenTopic() {
        return chosenTopic;
    }

    public void setChosenTopic(String chosenTopic) {
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

    public String getReplied() {
        return replied;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }
    
    public boolean isResponse(){
        return !replied.isEmpty();
    }
    
    
   
    
    
    
    
    
}
