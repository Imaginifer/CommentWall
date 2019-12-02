/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Permit;
import com.imaginifer.mess.repo.UserBase;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
public class CommenterService {
    
    private UserBase ub;
    private PasswordEncoder pwd;
    
    @Autowired
    public CommenterService(UserBase ub, PasswordEncoder pwd) {
        this.ub = ub;
        this.pwd = pwd;
    }
    
    @Transactional
    public void addAdmin(){
        checkPermits();
        if(ub.noAdmin()){
            ub.registerNew(new Commenter("admin",pwd.encode("pa ss wo rd 12 34"),""
                    ,LocalDateTime.of(1980, 1, 1, 9, 15),ub.getPermitByName("ROLE_ADMIN")));
        }
    }
    
    private void checkPermits(){
        if(ub.noPermits()){
            ub.addNewPermit(new Permit("ROLE_ADMIN"));
            ub.addNewPermit(new Permit("ROLE_USER"));
        }
    }
    
    @Transactional
    public int registerNew(RegData reg){
        checkPermits();
        if(ub.nameOccupied(reg.getName())){
            return 1;
        }
        if(!reg.getPwd1().equals(reg.getPwd2())){
            return 2;
        }
        ub.registerNew(new Commenter(reg.getName(), pwd.encode(reg.getPwd1()), reg.getMail()
                , LocalDateTime.now(),ub.getPermitByName("ROLE_USER")));
        return 0;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void promoteOrDemote(String id){
        int q=0;
        try {
            q=Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return;
        }
        Commenter c=ub.findCommenterById(q);
        if(c.isAdmin()){
            c.removeAuthority(ub.getPermitByName("ROLE_ADMIN"));
        }else{
            c.grantAuthority(ub.getPermitByName("ROLE_ADMIN"));
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public List<Commenter> listCommenters(){
        return ub.listCommenters();
    }
}
