/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Pass;
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
    
    private final UserBase ub;
    private final PasswordEncoder pwd;
    private final MailingService ms;
    
    @Autowired
    public CommenterService(UserBase ub, PasswordEncoder pwd, MailingService ms) {
        this.ub = ub;
        this.pwd = pwd;
        this.ms = ms;
    }
    
    @Transactional
    public int registerNew(RegData reg){
        //checkPermits();
        if(ub.nameOccupied(reg.getName())){
            return 1;
        }
        if(!reg.getPwd1().equals(reg.getPwd2())){
            return 2;
        }
        Commenter com = new Commenter(reg.getName(), pwd.encode(reg.getPwd1()), reg.getMail().toLowerCase()
                , LocalDateTime.now()/*,ub.getPermitByName("ROLE_USER")*/);
        ub.registerNew(com);
        Pass p = new Pass(com);
        ub.newPass(p);
        //System.out.println(reg.getName() + " belépőjének száma: " + p.getPassId());
        ms.sendValidator(reg.getMail(), p.getPassId());
        return 0;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public boolean promoteOrDemote(String id){
        int q;
        Commenter c;
        try {
            q=Integer.parseInt(id);
            c=ub.findCommenterById(q);
            c.getId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        
        if(c.isAdmin()){
            c.removeAuthority(ub.getPermitByName("ROLE_ADMIN"));
        }else{
            c.grantAuthority(ub.getPermitByName("ROLE_ADMIN"));
        }
        return true;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public List<Commenter> listCommenters(){
        return ub.listCommenters();
    }
    
    @Transactional
    public boolean validateCommenter(String activator){
        Pass p;
        int passId;
        try {
            passId = Integer.parseInt(activator);
            p = ub.findPassById(ms.disentangleActivator(passId));
            p.getPassId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        p.getComm().grantAuthority(ub.getPermitByName("ROLE_USER"));
        ub.deletePass(p);
        return true;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public boolean banOrRehabilitate(String id){
        int q;
        Commenter c;
        try {
            q=Integer.parseInt(id);
            c=ub.findCommenterById(q);
            c.getId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        
        c.setEnabled(!c.isEnabled());
        return true;
    }
    
}
