/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;

import javax.validation.constraints.*;


/**
 *
 * @author imaginifer
 */
public class RegData {
    @NotNull(message = "Meg kell adnia egy nevet!")
    @Size(min=3, max=25, message="3 és 25 karakter közötti hosszúságú kell legyen!")
    private String name;
    @NotNull(message = "Meg kell adnia egy jelszót!")
    @Size(min=8, message="Legalább 8 karakter hosszú kell legyen!")
    private String pwd1;
    @NotNull(message = "Meg kell adnia ugyanazt a jelszót!")
    @Size(min=8, message="Legalább 8 karakter hosszú kell legyen!")
    private String pwd2;
    @NotNull(message="Meg kell adnia egy drótposta-címet!")
    @Email(message="Meg kell adnia egy drótposta-címet!")
    private String mail;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd1() {
        return pwd1;
    }

    public void setPwd1(String pwd1) {
        this.pwd1 = pwd1;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd2() {
        return pwd2;
    }

    public void setPwd2(String pwd2) {
        this.pwd2 = pwd2;
    }
    
   

    
    
}
