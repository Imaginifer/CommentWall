/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import com.progmatic.msg.dto.*;
import com.progmatic.msg.service.UserBase;
import javax.persistence.*;
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
    
    @PersistenceContext
    EntityManager em;
    
    private UserBase ub;
    
    @Autowired
    public SecuController(UserBase ub) {
        this.ub = ub;
    }
    
    @RequestMapping(value="/messaging/login",method=RequestMethod.GET)
    public String loginScreen(){
        //ub.addAdmin(ub.noAdmin());
        return "m4";
    }
    
    
    
    @RequestMapping(value="/messaging/register",method=RequestMethod.GET)
    public String regScreen(Model mod){
        mod.addAttribute("data", new RegData());
        return "m5";
    }
    
    @RequestMapping(path = "/messaging/register", method = RequestMethod.POST)
    public String regNewUser(@Valid @ModelAttribute("data") RegData reg, BindingResult bs){
        if(bs.hasErrors()){
            return "m5";
        }
        if(ub.nameOccupied(reg.getName())){
            return "redirect:http://localhost:8080/messaging/problem?err=1";
        }
        
        ub.registerNew(reg);
        return "redirect:http://localhost:8080/login";
    }
    
    @GetMapping("/messaging/commenters")
    public String displayAllUsers(
            @RequestParam(name = "id", defaultValue = "") String id, Model mod){
        if(!id.isEmpty()){
            ub.promoteOrDemote(id);
        }
        mod.addAttribute("users", ub.listCommenters());
        return "m7";
    }
    
    
   
    
    
    
    
    
}
