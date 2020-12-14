/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.dto.CommenterView;
import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.dto.TopicView;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Forum;
import com.imaginifer.mess.entity.Muting;
import com.imaginifer.mess.entity.Nominee;
import com.imaginifer.mess.entity.Pass;
import com.imaginifer.mess.entity.Referendum;
import com.imaginifer.mess.entity.Sanction;
import com.imaginifer.mess.enums.SanctionType;
import com.imaginifer.mess.enums.UserRank;
import com.imaginifer.mess.repo.CustomCommenterRepoImpl;
import com.imaginifer.mess.repo.TopicRepository;
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
    private final LoginSecurityService ls;
    private final WebUtilService wu;
    private final TopicRepository tr;
    
    @Autowired
    public CommenterService(CustomCommenterRepoImpl ub, PasswordEncoder pwd, 
            MailingService ms, LoginSecurityService ls, WebUtilService wu, TopicRepository tr) {
        this.cr = ub;
        this.pwd = pwd;
        this.ms = ms;
        this.wu = wu;
        this.ls = ls;
        this.tr = tr;
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
        cr.newPass(p);
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
    public boolean castVote(long nomineeId){
        if(!wu.hasRank(UserRank.ELECTOR)){
            return false;
        }
        Nominee n = cr.findNomineeById(nomineeId);
        Referendum r = cr.getReferendumWithVotes(n.getReferendum().getReferendumId());
        if(!r.isClosed() && r.castVote(wu.getCurrentUser(), nomineeId)){
            n.vote();
            return Boolean.TRUE;
        }
        return false;
    }
    
    public List<TopicView> displayReferendum(long referendumId){
        Referendum r = cr.getReferendumWithVotes(referendumId);
        return ControllerSupport.convertReferendum(r, true);
    }
    
    @Transactional(readOnly = false)
    public boolean validateCommenter(String activator){
        Pass p;
        long passId;
        try {
            passId = Long.parseLong(activator);
            p = cr.findPassById(ms.disentangleActivator(passId));
            p.getPassId();
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        p.getCommenter().grantAuthority(cr.getPermitByName("ROLE_USER"));
        cr.deletePass(p);
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
    
    @Transactional(readOnly = false)
    public void newSanction(long commenterId, long forumId, SanctionType type, int duration){
        if(wu.hasRank(UserRank.DIRECTOR) || wu.hasRank(UserRank.SUBJECTOR) 
                || wu.hasRank(UserRank.ARBITRATOR) 
                || wu.isSanctionedHere(SanctionType.ADMINISTRATOR, forumId) 
                || wu.isSanctionedHere(SanctionType.MODERATOR, forumId)){
            Forum scope = forumId == 0 ? null : tr.getForumById(forumId);
            Sanction s = new Sanction(type, cr.findCommenterById(commenterId), 
                    wu.getCurrentUser(), scope, duration);
            cr.newSanction(s);
        }
    }
    
    @PreAuthorize("hasRole('DIRECTOR') OR hasRole('ARBITRATOR')")
    @Transactional(readOnly = false)
    public void liftSanction(long sanctionId){
        cr.findSanction(sanctionId).setValid(false);
    }
    
    @Transactional(readOnly = false)
    public void muteUser(long id){
        Muting m = new Muting(wu.getCurrentUser(), cr.findCommenterById(id));
        cr.newMuting(m);
    }
    
    @Transactional(readOnly = false)
    public void unmuteUser(long mutingId){
        cr.removeMuting(cr.findMuting(mutingId));
    }
    
    public List<TopicView> listMutings(){
        return ControllerSupport.convertMuting(cr.listCommentersMutings(wu.getCurrentUser().getCommenterId()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(ls.isBlocked(wu.getRequestIdent())){
            throw new RuntimeException("Túl sokat próbálkozott!");
        }
        return cr.loadUserByUsername(username);
    }
    
}
