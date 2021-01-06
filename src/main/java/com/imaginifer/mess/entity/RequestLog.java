/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class RequestLog implements Serializable{
    
    @Id
    private int addressHash;
    private int loginAttempts;
    private LocalDateTime lastRequest;

    public RequestLog(int addressHash) {
        this.addressHash = addressHash;
        this.loginAttempts = 1;
        updateLastReq();
    }

    public RequestLog() {
    }
    
    private void updateLastReq(){
        this.lastRequest = LocalDateTime.now();
    }

    public int getAddressHash() {
        return addressHash;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public LocalDateTime getLastRequest() {
        return lastRequest;
    }
    
    public void newLoginFailure(){
        loginAttempts++;
        updateLastReq();
    }
    
    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    
}
