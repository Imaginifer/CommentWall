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
import com.imaginifer.mess.repo.CustomCommenterRepoImpl;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
@Transactional(readOnly = true)
public class CommenterService implements UserDetailsService{
    
    private final CustomCommenterRepoImpl ub;
    private final PasswordEncoder pwd;
    private final MailingService ms;
    private final LoginSecurityService ls;
    private final WebUtilService wu;
    
    @Autowired
    public CommenterService(CustomCommenterRepoImpl ub, PasswordEncoder pwd, 
            MailingService ms, LoginSecurityService ls, WebUtilService wu) {
        this.ub = ub;
        this.pwd = pwd;
        this.ms = ms;
        this.wu = wu;
        this.ls = ls;
    }
    
    @Transactional(readOnly = false)
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
    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public boolean promoteOrDemote(String id){
        int q;
        Commenter c;
        try {
            q=Integer.parseInt(id);
            c=ub.findCommenterById(q);
            c.getCommenterId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        
        if(c.isDirector()){
            c.removeAuthority(ub.getPermitByName("ROLE_DIRECTOR"));
        }else{
            c.grantAuthority(ub.getPermitByName("ROLE_DIRECTOR"));
        }
        return true;
    }
    
    @PreAuthorize("hasRole('DIRECTOR')")
    public List<Commenter> listCommenters(){
        return ub.listCommenters();
    }
    
    @Transactional(readOnly = false)
    public boolean validateCommenter(String activator){
        Pass p;
        long passId;
        try {
            passId = Long.parseLong(activator);
            p = ub.findPassById(ms.disentangleActivator(passId));
            p.getPassId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        p.getCommenter().grantAuthority(ub.getPermitByName("ROLE_USER"));
        ub.deletePass(p);
        return true;
    }
    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public boolean banOrRehabilitate(long id){
        Commenter c = ub.findCommenterById(id);
        if(c == null){
            return false;
        }
        c.setEnabled(!c.isEnabled());
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(ls.isBlocked(wu.getRequestIdent())){
            throw new RuntimeException("Túl sokat próbálkozott!");
        }
        return ub.loadUserByUsername(username);
    }
    
}
