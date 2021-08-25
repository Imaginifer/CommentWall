/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Pass;
import com.imaginifer.mess.entity.RequestLog;
import com.imaginifer.mess.repo.PassRepository;
import com.imaginifer.mess.repo.RequestRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author imaginifer
 */
@Service
public class SecurityService {
    
    private final int upperLimit = 5;
    private final CacheManager ch;
    private final RequestRepository rqr;
    private final PassRepository pr;
    
    @Autowired
    public SecurityService(CacheManager ch, RequestRepository rqr, PassRepository pr){
        this.ch = ch;
        this.rqr = rqr;
        this.pr = pr;
    }
    
    public void loginSuccess(String addr){
        ch.getCache("reg").evict(addr);
    }
    
    private int nrOfPriorTries(String addr){
        try {
            ch.getCache("reg").get(addr);
        } catch (NullPointerException e) {
            
        }
        return 0;
    }
    
    public void loginFail(String addr){
        ch.getCache("reg").put(addr, nrOfPriorTries(addr)+1);
        
    }
    
    public boolean isBlocked(String addr){
        try {
            return ch.getCache("reg").get(addr, Integer.TYPE) <= upperLimit;
        } catch (NullPointerException e) {
            return false;
        }
    }
    
    //SQL változat
    @Transactional
    public void loginSuccess(int addr){
        RequestLog r = rqr.findOneRequestLogByAddressHash(addr);
        if (r != null){
            r.setLoginAttempts(0);
        }
    }
    
    //SQL változat
    @Transactional
    public void loginFail(int addr){
        RequestLog r = rqr.findOneRequestLogByAddressHash(addr);
        if(r == null){
            rqr.save(new RequestLog(addr));
        } else {
            r.newLoginFailure();
        }
    }
    
    //SQL változat
    public boolean isBlocked(int addr){
        RequestLog r = rqr.findOneRequestLogByAddressHash(addr);
        return r == null || r.getLoginAttempts() >= upperLimit;
    }
    
    @Transactional
    public long createRegistryPass(){
        Pass p = new Pass(makeValidator());
        pr.save(p);
        return p.getPassId();
    }
    
    public boolean tooEarlyOrMismatched(long passId, boolean[] boxes){
        LocalDateTime t = LocalDateTime.now();
        Pass p = pr.findOnePassByPassId(passId);
        return p == null || p.getCreated().isBefore(t.minusSeconds(20)) || mismatch(boxes, p.getValidator());
    }
    
    private boolean mismatch(boolean[] boxes, String s){
        HashSet<Integer> keys = disentangleKey(s);
        if(keys.isEmpty()){
            return true;
        }
        for (int i = 0; i < boxes.length; i++) {
            if((keys.contains(i) && !boxes[i]) || (!keys.contains(i) && boxes[i])){
                return true;
            }
        }
        return false;
    }
    
    private HashSet<Integer> disentangleKey(String s){
        List<String> parts = Arrays.asList(s.split("-"));
        HashSet<Integer> key = new HashSet<>();
        for (String part : parts) {
            try {
                key.add(Integer.parseInt(part));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return key;
    }
    
    private String makeValidator(){
        Random r = new Random();
        HashSet<Integer> keys = new HashSet<>();
        int nrOfIntegers  = r.nextInt(5)+1;
        while(keys.size()<nrOfIntegers){
            int n = r.nextInt(7);
            keys.add(n);
        }
        List<String> x = keys.stream().map(k -> k.toString()).collect(Collectors.toList());
        return String.join("-", x);
    }
    
}
