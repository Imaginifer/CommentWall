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
public class LoginData {
    @NotNull
    @Size(min=3, max=25, message="Needs to be between 3 and 25 characters long!")
    private String name;
    @NotNull
    @Size(min=8, message="Needs more characters!")
    private String pwd;

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
    
    
}
