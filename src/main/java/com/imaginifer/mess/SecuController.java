/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess;

import com.imaginifer.mess.dto.RegData;
import com.imaginifer.mess.repo.UserBase;
import com.imaginifer.mess.service.CommenterService;
import com.imaginifer.mess.service.ConvertDTO;
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
    
    private CommenterService cs;
    
    @Autowired
    public SecuController(CommenterService cs) {
        this.cs = cs;
    }
    
    @RequestMapping(value="/messaging/login",method=RequestMethod.GET)
    public String loginScreen(){
        cs.addAdmin();
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
        if(q != 0){
            return "redirect:http://localhost:8080/messaging/problem?err=" + q;
        }
        /*if(cs.nameOccupied(reg.getName())){
            return "redirect:http://localhost:8080/messaging/problem?err=1";
        }
        if(!reg.getPwd1().equals(reg.getPwd2())){
            return "redirect:http://localhost:8080/messaging/problem?err=2";
        }
        cs.registerNew(reg);*/
        return "redirect:http://localhost:8080/messaging/login";
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
        return "hiteles.html";
    }
    
   
    
   
    
    
    
    
    
}
