/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Sanction;
import com.imaginifer.mess.enums.*;
import java.util.Arrays;
import java.util.HashSet;
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
    
    public int getRequestHash(){
        return getRequestIP().hashCode();
    }
    
    public boolean isSanctionedHere(Commenter comm, SanctionType sanction, long location){
        if(comm != null && sanction != null){
            Set<Sanction> sanctions = comm.getSanctions();
            if(!sanctions.isEmpty()){
                return sanctions.stream().anyMatch(s -> (s.isValid() 
                        && s.getType() == sanction && (s.getSanctionScope() == null 
                        || s.getSanctionScope().getForumId() == location)));
            }
        }
        return false;
    }
    
    public boolean isSanctionedHere(Commenter comm, SanctionType[] sanction, long location){
        if(comm != null && sanction != null && sanction.length > 0){
            Set<Sanction> sanctions = comm.getSanctions();
            Set<SanctionType> types = new HashSet<>(Arrays.asList(sanction));
            if(!sanctions.isEmpty()){
                return sanctions.stream().anyMatch(s -> (s.isValid() 
                        && types.contains(s.getType()) && (s.getSanctionScope() == null 
                        || s.getSanctionScope().getForumId() == location)));
            }
        }
        return false;
    }
    
    public boolean hasRank(Commenter c, UserRank rank){
        return c == null || rank == null ? false : c.getAuthorities().stream()
                .anyMatch(a -> UserRank.valueOf(a.getAuthority()) == rank);
    }
    
    public boolean hasRank(Commenter c, UserRank[] ranks){
        if(c == null || ranks == null || ranks.length == 0){
            return false;
        }
        Set<UserRank> x = new HashSet<>(Arrays.asList(ranks));
        return c.getAuthorities().stream()
                .anyMatch(a -> x.contains(UserRank.valueOf(a.getAuthority())));
    }
    
    public UserRank highestRank(Commenter c){
        UserRank[] r = UserRank.values();
        UserRank highest = UserRank.ROLE_USER;
        for (int i = 0; i < r.length; i++) {
            if(hasRank(c, r[i])){
                highest = r[i];
            }  
        }
        return highest;
    }
    
    public String commenterStatus(Commenter c){
        if (!c.isEnabled()){
            return "Kikapcsolt";
        }
        if(!c.isAccountNonLocked()){
            return "Felfüggesztett";
        }
        return "Működő";
    }
    
}
