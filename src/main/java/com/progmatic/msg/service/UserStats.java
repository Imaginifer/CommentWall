/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.entity.Commenter;
import java.util.HashMap;
import org.springframework.context.annotation.*;
import org.springframework.security.core.GrantedAuthority;
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
    //private String userName;
    //private HashMap<String, Integer> stats;

    /*public UserStats() {
        stats=new HashMap<>();
    }*/
    
    /*public void setUserName(String userName) {
        this.userName = userName;
        stats.putIfAbsent(userName, 0);
        stats.replace(userName, stats.get(userName)+1);
    }*/

    
    public boolean isAdmin(){
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            for (GrantedAuthority auth : SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities()) {
                if(auth.getAuthority().equals("ROLE_ADMIN")){
                    return true;
                }
            }
        }
        return false;
    }

    public String getCurrentUsername(){
        Commenter c = (Commenter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return c.getUsername();
    }
    
    
    
    
}