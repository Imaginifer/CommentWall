/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author imaginifer
 */
@Controller
public class SecuController {
    
    private InMemoryUserDetailsManager uds;
    
    @Autowired
    public SecuController(UserDetailsService uds) {
        this.uds = (InMemoryUserDetailsManager) uds;
    }
    
    @RequestMapping(value="/messaging/login",method=RequestMethod.GET)
    public String loginScreen(Model mod){
        mod.addAttribute("data", new LoginData());
        return "m4";
    }
    
    
    
    @RequestMapping(value="/messaging/register",method=RequestMethod.GET)
    public String regScreen(Model mod){
        mod.addAttribute("data", new RegData());
        return "m5";
    }
    
    
    
}
