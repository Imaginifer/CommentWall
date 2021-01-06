/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class AuthSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final SecurityService srv;
    private final WebUtilService wu;

    @Autowired
    public AuthSuccessListener(SecurityService srv, WebUtilService wu) {
        this.srv = srv;
        this.wu = wu;
    }
    
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        //srv.loginSuccess(wu.getRequestIdent());
        srv.loginSuccess(wu.getRequestHash());
    }
    
}
