/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import java.util.HashMap;
import org.springframework.context.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author imaginifer
 */
@Component
@Scope(scopeName=WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserStats {
    private String userName;
    private HashMap<String, Integer> stats;
    private User u;

    public UserStats() {
        stats=new HashMap<>();
        u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
        stats.putIfAbsent(userName, 0);
        stats.replace(userName, stats.get(userName)+1);
    }

    public String getUserName() {
        //return userName;
        return u.getUsername();
    }

    public HashMap<String, Integer> getStats() {
        stats=new HashMap<>();
        return (HashMap<String, Integer>) stats.clone();
        
    }
    
    
    
    
}
