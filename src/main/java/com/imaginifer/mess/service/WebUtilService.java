/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Sanction;
import com.imaginifer.mess.enums.SanctionType;
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
        return (Commenter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    
    public boolean isAllowedHere(long location){
        if(!userHasLoggedIn()){
            return false;
        }
        Set<Sanction> bans = getCurrentUser().getSanctions();
        if(bans.isEmpty()){
            return true;
        }
        for (Sanction b : bans) {
            if(b.getType() == SanctionType.EXILE 
                    && b.getSanctionScope() == null || b.getSanctionScope().getForumId() == location){
                return false;
            }
        }
        return true;
    }
    
    public boolean isInvitedHere(long location){
        if(userHasLoggedIn()){
            Set<Sanction> sancions = getCurrentUser().getSanctions();
            if(!sancions.isEmpty()){
                for (Sanction s : sancions) {
                    if(s.getType() == SanctionType.INVITATION
                            && s.getSanctionScope().getForumId() == location){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isModeratorOrAdministratorHere(long location){
        if(userHasLoggedIn()){
            Set<Sanction> sanc = getCurrentUser().getSanctions();
            if(!sanc.isEmpty()){
                for (Sanction s : sanc) {
                    if(s.getType() == SanctionType.MODERATOR
                            || s.getType() == SanctionType.ADMINISTRATOR
                            && s.getSanctionScope().getForumId() == location){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
