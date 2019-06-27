/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import java.time.LocalDateTime;

/**
 *
 * @author imaginifer
 */
public class Commenter {
    
    private final String name;
    private String pwd;
    private String mail;
    private String blood;
    private String mother;
    private String shoe;
    private final LocalDateTime joinTime;

    public Commenter(String name, String pwd, String mail, String blood, String mother, String shoe, LocalDateTime joinTime) {
        this.name = name;
        this.pwd = pwd;
        this.mail = mail;
        this.blood = blood;
        this.mother = mother;
        this.shoe = shoe;
        this.joinTime = joinTime;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getMail() {
        return mail;
    }

    public String getBlood() {
        return blood;
    }

    public String getMother() {
        return mother;
    }

    public String getShoe() {
        return shoe;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setShoe(String shoe) {
        this.shoe = shoe;
    }

    
    
    
}
