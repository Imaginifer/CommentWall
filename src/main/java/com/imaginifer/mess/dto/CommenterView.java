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
public class CommenterView {
    private String username;
    private long id;
    private boolean director;
    private String formattedJoinDate;

    public CommenterView(String username, long id, boolean director, String formattedJoinDate) {
        this.username = username;
        this.id = id;
        this.director = director;
        this.formattedJoinDate = formattedJoinDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDirector() {
        return director;
    }

    public void setDirector(boolean director) {
        this.director = director;
    }

    public String getFormattedJoinDate() {
        return formattedJoinDate;
    }

    public void setFormattedJoinDate(String formattedJoinDate) {
        this.formattedJoinDate = formattedJoinDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
    
}
