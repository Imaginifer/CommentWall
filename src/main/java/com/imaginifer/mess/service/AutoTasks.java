/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.*;
import com.imaginifer.mess.repo.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
public class AutoTasks {
    
    private LocalDateTime ref;
    private final UserBase ub;
    private final CustomMsgRepoImpl mr;
    private final TopicRepository tr;
    private final PasswordEncoder pwd;

    @Autowired
    public AutoTasks(UserBase ub, CustomMsgRepoImpl mr, TopicRepository tr, PasswordEncoder pwd) {
        this.ub = ub;
        this.mr = mr;
        this.tr = tr;
        this.pwd = pwd;
        ref = LocalDateTime.of(1970, 1, 1, 0, 0);
    }
    
    @Scheduled(fixedRate = 900000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void runTask(){
        System.out.println("AutoTasks runTask commenced.");
        LocalDateTime present = LocalDateTime.now();
        if(ref.getYear() == 1970){
            checkPermits();
            addAdmin();
            fillerMessages();
        }
        if (ref.isBefore(present.minusHours(12))){
            ref = present;
            removeExpiredPasses(present);
            removeUnactivatedAccounts(present);
        }
    }
    
    private void checkPermits(){
        if(ub.noPermits()){
            ub.addNewPermit(new Permit("ROLE_ADMIN"));
            ub.addNewPermit(new Permit("ROLE_USER"));
        }
    }
    
    private void addAdmin(){
        if(ub.noAdmin()){
            ub.registerNew(new Commenter("admin",pwd.encode("pa ss wo rd 12 34"),""
                    ,LocalDateTime.of(1980, 1, 1, 9, 15),ub.getPermitByName("ROLE_ADMIN")));
        }
    }
    
    private void fillerMessages(){
        if(mr.msgListTooShort()){
            Topic t = tr.newTopic("admin", "Filler");
            mr.addNew(new Message("Techno Kolos", "Lorem ipsum dolor sit amet, consectetur "
                    + "adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    LocalDateTime.of(2008, 11, 27, 23, 6, 54), t));
            mr.addNew(new Message("Feles Elek", "Ut enim ad minim veniam, quis nostrud "
                    + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 
                    LocalDateTime.of(2017, 3, 14, 2, 55, 7), t));
            mr.addNew(new Message("Citad Ella", "Duis aute irure dolor in reprehenderit "
                    + "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", 
                    LocalDateTime.of(1998, 6, 20, 9, 15, 9), t));
            mr.addNew(new Message("Tank Aranka", "Excepteur sint occaecat cupidatat non "
                    + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 
                    LocalDateTime.of(2011, 9, 5, 3, 43, 21), t));
        }
    }
    
    private void removeExpiredPasses(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(48);
        List<Pass> passes = ub.getAllPasses();
        if(passes.isEmpty()){
            return;
        }
        for (Pass p : passes) {
            if(p.getCreated().isBefore(deadline)){
                ub.deletePass(p);
            }
        }
    }
    
    private void removeUnactivatedAccounts(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(48);
        List<Commenter> x = ub.getUnactivatedCommenters();
        if(x.isEmpty()){
            return;
        }
        for (Commenter c : x) {
            if(c.getJoinDate().isBefore(deadline)){
                ub.deleteAccount(c);
            }
        }
    }
    
}
