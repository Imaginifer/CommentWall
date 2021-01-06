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
    private final PassRepository pr;
    private final DatabaseService ds;
    private final SanctionRepository sr;
    private final RequestRepository rq;

    @Autowired
    public AutoTasks(PassRepository cr, DatabaseService ds, SanctionRepository sr, RequestRepository rq) {
        this.pr = cr;
        this.ds = ds;
        this.sr = sr;
        this.rq = rq;
        ref = LocalDateTime.of(1970, 1, 1, 0, 0);
    }
    
    @Scheduled(fixedRate = 900000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void runTask(){
        System.out.println("AutoTasks runTask commenced.");
        LocalDateTime present = LocalDateTime.now();
        if(ref.getYear() == 1970){
            ds.checkPermits();
            ds.addAdmin();
            ds.fillerMessages();
        }
        if (ref.isBefore(present.minusHours(6))){
            ref = present;
            removeExpiredPasses(present);
            expireSanctions(present);
            purgeRequestLogs(present);
            //removeUnactivatedAccounts(present);
        }
    }
    
    private void removeExpiredPasses(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(6);
        List<Pass> passes = pr.getAllPasses();
        if(!passes.isEmpty()){
            passes.stream().filter(p -> p.getCreated()
                    .isBefore(deadline)).forEach(p -> pr.deletePass(p));
            /*for (Pass p : passes) {
                if(p.getCreated().isBefore(deadline)){
                    pr.deletePass(p);
                }
            }*/
        }
    }
    
    private void expireSanctions(LocalDateTime when){
        List<Sanction> sanctions = sr.getAllSanctions();
        if(!sanctions.isEmpty()){
            for (Sanction s : sanctions) {
                if(s.getExpires().isBefore(when)){
                    //cr.deleteSanction(s);
                    s.setValid(false);
                }
            }
        }
    }
    
    private void purgeRequestLogs(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(24);
        List<RequestLog> logs = rq.getAllRequestLogs();
        if(!logs.isEmpty()){
            logs.stream().filter(r -> r.getLastRequest().isBefore(deadline) || r.getLoginAttempts() == 0)
                    .forEach(r -> rq.deleteRequestLog(r));
        }
    }
    
    /*private void removeUnactivatedAccounts(LocalDateTime when){
        LocalDateTime deadline = when.minusHours(48);
        List<Commenter> x = pr.getUnactivatedCommenters();
        if(x.isEmpty()){
            return;
        }
        for (Commenter c : x) {
            if(c.getJoinDate().isBefore(deadline)){
                pr.deleteAccount(c);
            }
        }
    }*/
    
}
