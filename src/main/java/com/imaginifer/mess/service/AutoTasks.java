/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.*;
import com.imaginifer.mess.repo.*;
import java.time.LocalDateTime;
import java.util.*;
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
    private final CustomCommenterRepoImpl cr;
    private final CustomMsgRepoImpl mr;
    private final TopicRepository tr;
    private final PasswordEncoder pwd;

    @Autowired
    public AutoTasks(CustomCommenterRepoImpl cr, CustomMsgRepoImpl mr, TopicRepository tr, PasswordEncoder pwd) {
        this.cr = cr;
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
        if (ref.isBefore(present.minusHours(6))){
            ref = present;
            removeExpiredPasses(present);
            expireSanctions(present);
            removeUnactivatedAccounts(present);
        }
    }
    
    private void checkPermits(){
        if(cr.noPermits()){
            cr.addNewPermit(new Permit("ROLE_DIRECTOR"));
            cr.addNewPermit(new Permit("ROLE_USER"));
        }
    }
    
    private void addAdmin(){
        if(cr.noDirector()){
            cr.registerNew(new Commenter("Imaginifer",pwd.encode("pa ss wo rd 12 34"),"address@host.domain"
                    ,LocalDateTime.of(1980, 1, 1, 9, 15),cr.getPermitByName("ROLE_DIRECTOR")));
        }
    }
    
    private List<Commenter> fillerUsers(){
        List<Commenter> comm = new ArrayList<>();
        comm.add(new Commenter("Tank Aranka", pwd.encode("qwertzuiop"), "tankari@citromail.com",
                LocalDateTime.of(1980, 1, 2, 12, 15),cr.getPermitByName("ROLE_USER")));
        comm.add(new Commenter("Citad Ella", pwd.encode("qwertzuiop"), "cella@citromail.com",
                LocalDateTime.of(1980, 1, 2, 12, 35),cr.getPermitByName("ROLE_USER")));
        comm.add(new Commenter("Techno Kolos", pwd.encode("qwertzuiop"), "teko@citromail.com",
                LocalDateTime.of(1980, 1, 2, 14, 45),cr.getPermitByName("ROLE_USER")));
        comm.add(new Commenter("Feles Elek", pwd.encode("qwertzuiop"), "felelek@citromail.com",
                LocalDateTime.of(1980, 1, 3, 9, 25),cr.getPermitByName("ROLE_USER")));
        for (Commenter commenter : comm) {
            cr.registerNew(commenter);
        }
        return comm;
    }
    
    private void fillerMessages(){
        if(cr.nrOfAllCommenters() == 0 && mr.countNrOfMessages() == 0){
            List<Commenter> com = fillerUsers();
            Forum f = new Forum("Filler","Próbaüzenetek", false);
            tr.newForum(f);
            Topic t = tr.newTopic(new Topic("Lorem ipsum", "Lorem ipsum dolor sit amet, "
                    + "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", f, false));
            mr.addNew(new Message(com.get(2), "Lorem ipsum dolor sit amet, consectetur "
                    + "adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    LocalDateTime.of(1998, 6, 20, 9, 15, 9), t, 1, "123456"));
            mr.addNew(new Message(com.get(3), "Ut enim ad minim veniam, quis nostrud "
                    + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 
                    LocalDateTime.of(2008, 11, 27, 23, 6, 54), t, 2, "1a2b3c"));
            mr.addNew(new Message(com.get(1), "Duis aute irure dolor in reprehenderit "
                    + "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", 
                    LocalDateTime.of(2011, 9, 5, 3, 43, 21), t, 3, "000a1e"));
            mr.addNew(new Message(com.get(0), "Excepteur sint occaecat cupidatat non "
                    + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", 
                    LocalDateTime.of(2017, 3, 14, 2, 55, 7), t, 4,"555eee"));
            t.setLastUpdate(LocalDateTime.of(2017, 3, 14, 2, 55, 7));
            Topic t2 = tr.newTopic(new Topic("Nyelvtörő", "A szerencsés csősz cserszömörcés sört szörcsöl.", f, true));
            mr.addNew(new Message(com.get(0), "A szerencsés csősz cserszömörcés sört szörcsöl.", 
                    LocalDateTime.of(2002, 8, 1, 9, 8, 47), t2, 1, "000000"));
            Message m1 = new Message(com.get(1), "Sepsiszentgyörgyi szájsebész-asszisztensre sincs szükségem.", 
                    LocalDateTime.of(2002, 8, 3, 17, 32, 11), t2, 2, "aaabbb"); 
            mr.addNew(m1);
            Message m2 = new Message(com.get(3), "Fekete bikapata pattog a patika pepita kövén.", 
                    LocalDateTime.of(2007, 1, 20, 6, 14, 33), t2, 3, m1, "4d5e6f");
            mr.addNew(m2);
            mr.addNew(new Message(com.get(2), "Az ibafai papnak fapipája van, így az "
                    + "ibafai papi pipa papi fapipa.", 
                    LocalDateTime.of(2015, 5, 14, 20, 3, 14), t2, 4, m2, "ffffff"));
            t2.setLastUpdate(LocalDateTime.of(2015, 5, 14, 20, 3, 14));        
        }
    }
    
    private void removeExpiredPasses(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(48);
        List<Pass> passes = cr.getAllPasses();
        if(!passes.isEmpty()){
            for (Pass p : passes) {
                if(p.getCreated().isBefore(deadline)){
                    cr.deletePass(p);
                }
            }
        }
    }
    
    private void expireSanctions(LocalDateTime when){
        List<Sanction> sanctions = cr.getAllSanctions();
        if(!sanctions.isEmpty()){
            for (Sanction s : sanctions) {
                if(s.getExpires().isBefore(when)){
                    //cr.deleteSanction(s);
                    s.setValid(false);
                }
            }
        }
    }
    
    private void removeUnactivatedAccounts(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(48);
        List<Commenter> x = cr.getUnactivatedCommenters();
        if(x.isEmpty()){
            return;
        }
        for (Commenter c : x) {
            if(c.getJoinDate().isBefore(deadline)){
                cr.deleteAccount(c);
            }
        }
    }
    
}
