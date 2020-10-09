/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;

import java.time.LocalDateTime;

/**
 *
 * @author imaginifer
 */
public class PageView {
    
    private String title;
    private int year;
    
    public PageView(){
        
    }

    public PageView(String title) {
        this.title = title;
        this.year = LocalDateTime.now().getYear();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getYear(){
        return year;
    }
    
    public void setYear(int year){
        this.year = year;
    }
    
    public boolean isNotMain(){
        return !title.equals("Főoldal");
    }
    
    public boolean isNotSearch(){
        return !title.equals("Keresés");
    }
    
    public boolean isNotEntry(){
        return !title.equals("Belépés");
    }
    
    public boolean isNotCommenters(){
        return !title.equals("Felhasználók");
    }
    
    public boolean isNotAccount(){
        return !title.equals("Fiók");
    }
    
    public boolean isNotReports(){
        return !title.equals("Feljelentések");
    }
    
    public boolean isSearch(){
        return title.equals("Találatok");
    }
}
