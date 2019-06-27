/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class UserRepo {
    
    private HashMap<String,Commenter> users;

    public UserRepo() {
        users = new HashMap<>();
    }
    
    public boolean alreadyExists(String name){
        return users.containsKey(name);
    }
    
    public boolean correctPassword(LoginData login){
        return users.get(login.getName()).getPwd().equals(login.getPwd());
    }
    
    public void newUser(RegData reg){
        users.put(reg.getName(), new Commenter(reg.getName(), reg.getPwd()
                , reg.getMail(), reg.getBlood()
                , reg.getMother(), reg.getShoe(), LocalDateTime.now()));
    }
    
}
