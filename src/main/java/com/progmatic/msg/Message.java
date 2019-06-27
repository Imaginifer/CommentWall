/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.*;

/**
 *
 * @author imaginifer
 */
public class Message {
    private String username;
    @NotNull
    @Size(min=1, max=20, message="Needs more letters!")
    private String name;
    @NotNull
    @Size(min=1, message="Needs more letters!")
    private String text;
    private LocalDateTime date;
    private int msgId;

    public Message(String username, String text, LocalDateTime dt, int id) {
        this.username = username;
        this.text = text;
        this.date = dt;
        this.msgId = id;
    }

    public Message() {
        
    }
    

    public String getUsername() {
        return username+":";
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public int getMsgId() {
        return msgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }
    
    
    
    
    
    
}
