/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.service.CommenterService;
import com.imaginifer.mess.service.ConvertDTO;
import com.imaginifer.mess.service.MailingService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author imaginifer
 */
@Controller
public class SecuController {
    
    private final CommenterService cs;
    private MailingService ms;
    
    @Autowired
    public SecuController(CommenterService cs, MailingService ms) {
        this.cs = cs;
        this.ms = ms;
    }
    
    @RequestMapping(value="/messaging/login",method=RequestMethod.GET)
    public String loginScreen(){
        //cs.addAdmin();
        return "signin.html";
    }
    
    
    
    @RequestMapping(value="/messaging/register",method=RequestMethod.GET)
    public String regScreen(Model mod){
        mod.addAttribute("data", new RegData());
        return "regpage.html";
    }
    
    @RequestMapping(path = "/messaging/register", method = RequestMethod.POST)
    public String regNewUser(@Valid @ModelAttribute("data") RegData reg, BindingResult bs){
        if(bs.hasErrors()){
            return "regpage.html";
        }
        int q = cs.registerNew(reg);
        if(q > 0){
            return "redirect:http://localhost:8080/messaging/problem?err=" + q;
        }
        
        return "redirect:http://localhost:8080/messaging/ms?n=1";
    }
    
    @GetMapping("/messaging/commenters")
    public String displayAllUsers(
            @RequestParam(name = "id", defaultValue = "") String id, Model mod){
        if(!id.isEmpty()){
            cs.promoteOrDemote(id);
        }
        mod.addAttribute("users", ConvertDTO.convertCommenter(cs.listCommenters()));
        return "userlist.html";
    }
    
    @GetMapping("/messaging/valid")
    public String validateNew(@RequestParam(name = "n", defaultValue = "0") String n){
        if (cs.validateCommenter(n)){
            return "redirect:http://localhost:8080/messaging/ms?n=2";
        }
        return "redirect:http://localhost:8080/messaging/problem?err=3";
    }
    
    @GetMapping("/messaging/sendtestmail")
    public String sendTestMail(){
        ms.sendSimpleTestMail();
        return "redirect:http://localhost:8080/messaging/";
    }
    
   
    
   
    
    
    
    
    
}
