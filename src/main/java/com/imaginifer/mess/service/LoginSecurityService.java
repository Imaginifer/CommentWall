/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

/**
 *
 * @author imaginifer
 */
@Service
public class LoginSecurityService {
    
    private final int upperLimit = 5;
    private final LoadingCache<String, Integer> attempts;
    
    public LoginSecurityService(){
        attempts = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, Integer>(){
            @Override
            public Integer load(String addr) throws Exception {
                return 0;
            }
                });
    }
    
    public void loginSuccess(String addr){
        attempts.invalidate(addr);
    }
    
    private int nrOfPriorTries(String addr){
        try {
            return attempts.get(addr);
        } catch (ExecutionException e) {
            return 0;
        }
    }
    
    public void loginFail(String k){
        attempts.put(k, nrOfPriorTries(k)+1);
    }
    
    public boolean isBlocked(String k){
        try {
            return attempts.get(k) >= upperLimit;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
