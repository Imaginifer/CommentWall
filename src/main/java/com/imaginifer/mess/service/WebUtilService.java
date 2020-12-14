/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Sanction;
import com.imaginifer.mess.enums.*;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class WebUtilService {
    
    private HttpServletRequest request;
    
    @Autowired
    public WebUtilService (HttpServletRequest req){
        this.request = req;
    }
    
    public boolean isDirector(){
        if (userHasLoggedIn()){
            for (GrantedAuthority auth : SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities()) {
                if(auth.getAuthority().equals("ROLE_DIRECTOR")){
                    return true;
                }
            }
        }
        return false;
    }

    public String getCurrentUsername(){
        if(userHasLoggedIn()){
            Commenter c = getCurrentUser();
            return c.getUsername();
        }
        return "null";
    }
    
    private boolean userHasLoggedIn(){
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
    
    public Commenter getCurrentUser(){
        if (userHasLoggedIn()){
            return (Commenter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
        
    }
    
    private String getRequestIP(){
        String s = "";
        
        if (request != null){
            s = request.getHeader("X-FORWARDED-FOR");
            if (s == null || s.isEmpty()){
                s = request.getRemoteAddr();
            }
        }
        return s;
    }
    
    public String getRequestIdent(){
        return Integer.toHexString(Math.abs(getRequestIP().hashCode()));
    }
    
    public boolean isSanctionedHere(SanctionType sanction, long location){
        if(userHasLoggedIn()){
            Set<Sanction> sanctions = getCurrentUser().getSanctions();
            if(!sanctions.isEmpty()){
                return sanctions.stream().anyMatch(s -> (s.isValid() 
                        && s.getType() == sanction && (s.getSanctionScope() == null 
                        || s.getSanctionScope().getForumId() == location)));
            }
        }
        return false;
    }
    
    public boolean isSanctionedHere(Commenter comm, SanctionType sanction, long location){
        if(comm != null){
            Set<Sanction> sanctions = comm.getSanctions();
            if(!sanctions.isEmpty()){
                return sanctions.stream().anyMatch(s -> (s.isValid() 
                        && s.getType() == sanction && (s.getSanctionScope() == null 
                        || s.getSanctionScope().getForumId() == location)));
            }
        }
        return false;
    }
    
    public boolean hasRank(UserRank rank){
        String s = "ROLE_"+rank.toString();
        if (userHasLoggedIn()){
            for (GrantedAuthority auth : SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities()) {
                if(auth.getAuthority().equals(s)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasRank(Commenter comm, UserRank rank){
        String s = "ROLE_"+rank.toString();
        if (comm != null){
            return comm.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(s));
        }
        return false;
    }
}
