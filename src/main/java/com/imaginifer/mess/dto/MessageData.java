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
    private int chosenTopic;
    private String newTopic;
    private int replied = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTopics() {
        return topics;
    }

    public int getChosenTopic() {
        return chosenTopic;
    }

    public void setChosenTopic(int chosenTopic) {
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

    public int getReplied() {
        return replied;
    }

    public void setReplied(int replied) {
        this.replied = replied;
    }
    
    public boolean isResponse(){
        return replied != 0;
    }
    
    
   
    
    
    
    
    
}
