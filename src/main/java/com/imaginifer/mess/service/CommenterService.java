/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.CommenterView;
import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Pass;
import com.imaginifer.mess.enums.UserRank;
import com.imaginifer.mess.repo.CustomCommenterRepoImpl;
import com.imaginifer.mess.repo.PassRepository;
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
    
    private final CustomCommenterRepoImpl cr;
    private final PasswordEncoder pwd;
    private final MailingService ms;
    private final SecurityService ss;
    private final WebUtilService wu;
    private final PassRepository pr;
    
    @Autowired
    public CommenterService(CustomCommenterRepoImpl ub, PasswordEncoder pwd, 
            MailingService ms, SecurityService ss, WebUtilService wu, PassRepository pr) {
        this.cr = ub;
        this.pwd = pwd;
        this.ms = ms;
        this.wu = wu;
        this.ss = ss;
        this.pr = pr;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*if(ss.isBlocked(wu.getRequestIdent())){
            throw new UsernameNotFoundException("Túl sokat próbálkozott!");
        }*/
        if(ss.isBlocked(wu.getRequestHash())){
            throw new UsernameNotFoundException("Túl sokat próbálkozott!");
        }
        UserDetails user  = cr.loadUserByUsername(username);
        if (user == null || !user.isAccountNonLocked()){
            throw new UsernameNotFoundException(user == null ? 
                    "Ilyen nevű felhasználó nincs!":
                    "A fiókot felfüggesztették!");
        }
        return user;
    }
    
    @Transactional(readOnly = false)
    public int registerNew(RegData reg){
        //checkPermits();
        if(cr.nameOccupied(reg.getName())){
            return 1;
        }
        if(!reg.getPwd1().equals(reg.getPwd2())){
            return 2;
        }
        Commenter com = new Commenter(reg.getName(), pwd.encode(reg.getPwd1()), reg.getMail().toLowerCase()
                , LocalDateTime.now()/*,ub.getPermitByName("ROLE_USER")*/);
        cr.registerNew(com);
        Pass p = new Pass(com);
        pr.newPass(p);
        //System.out.println(reg.getName() + " belépőjének száma: " + p.getPassId());
        ms.sendValidatorLink(reg.getMail(), p.getPassId());
        return 0;
    }
    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public boolean promoteOrDemote(String id){
        int q;
        Commenter c;
        try {
            q=Integer.parseInt(id);
            c=cr.findCommenterById(q);
            c.getCommenterId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        
        if(c.isDirector()){
            c.removeAuthority(cr.getPermitByName("ROLE_DIRECTOR"));
        }else{
            c.grantAuthority(cr.getPermitByName("ROLE_DIRECTOR"));
        }
        return true;
    }
    
    @PreAuthorize("hasRole('DIRECTOR')")
    public List<CommenterView> listCommenters(){
        return ControllerSupport.convertCommenter(cr.listCommenters());
    }
    
    
    @Transactional(readOnly = false)
    public boolean validateCommenter(String activator){
        Pass p;
        long passId;
        try {
            passId = Long.parseLong(activator);
            p = pr.findOnePassByPassId(ms.disentangleActivator(passId));
            p.getPassId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        p.getCommenter().grantAuthority(cr.getPermitByName("ROLE_USER"));
        pr.deletePass(p);
        return true;
    }
    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public boolean banOrRehabilitate(long id){
        Commenter c = cr.findCommenterById(id);
        if(c == null){
            return false;
        }
        c.setEnabled(!c.isEnabled());
        return true;
    }
    
    
}
