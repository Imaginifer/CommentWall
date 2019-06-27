/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import javax.validation.constraints.*;


/**
 *
 * @author imaginifer
 */
public class RegData {
    @NotNull
    @Size(min=3, max=25, message="Needs to be between 3 and 25 characters long!")
    private String name;
    @NotNull
    @Size(min=8, message="Needs more characters!")
    private String pwd;
    @NotNull
    @Size(min=3, message="You have to give your e-mail address!")
    @Email(message="You have to give your e-mail address!")
    private String mail;
    @NotNull
    @Size(min=1, message="You have to give your blood type!")
    private String blood;
    @NotNull
    @Size(min=3, message="You have to give your mother's name!")
    private String mother;
    @NotNull
    @Size(min=1, message="You have to give your shoe size!")
    private String shoe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getShoe() {
        return shoe;
    }

    public void setShoe(String shoe) {
        this.shoe = shoe;
    }
    
}
