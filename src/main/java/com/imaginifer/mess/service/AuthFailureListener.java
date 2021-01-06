/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class AuthFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final SecurityService srv;
    private final WebUtilService wu;

    @Autowired
    public AuthFailureListener(SecurityService srv, WebUtilService wu) {
        this.srv = srv;
        this.wu = wu;
    }
    
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        //srv.loginFail(wu.getRequestIdent());
        srv.loginFail(wu.getRequestHash());
    }
    
}
