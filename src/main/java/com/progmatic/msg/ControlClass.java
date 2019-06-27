/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import com.progmatic.msg.service.*;

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
public class ControlClass {
    
    
    private final MsgServiceImpl msg;
    private final UserStats us;
    private final UserRepo usr;
    
    @Autowired
    public ControlClass(MsgServiceImpl msg, UserStats us, UserRepo usr) {
        this.msg = msg;
        this.us = us;
        this.usr = usr;
    }
    
    @RequestMapping(value="/messaging",method=RequestMethod.GET)
    public String displayMsg(@RequestParam(name="ord", defaultValue="0") String order
            ,@RequestParam(name="ct", defaultValue="full") String count
            ,@RequestParam(name="nm", defaultValue="") String name
            ,@RequestParam(name="tx", defaultValue="") String text
            ,Model mod){
        
        mod.addAttribute("messages",msg.getMsg(order, count, name, text));
        return "m1";
    }
    
    @RequestMapping(value="/messaging/{msgId}",method=RequestMethod.GET)
    public String displayOne(@PathVariable("msgId") String msgId, Model mod){
        mod.addAttribute("messages", msg.pickMsg(msgId));
        return "m1";
    }
    @RequestMapping(value="/messaging/new",method=RequestMethod.GET)
    public String newMsgForm(Model mod){
        Message ms = new Message();
        ms.setName(us.getUserName());
        mod.addAttribute("message", ms);
        mod.addAttribute("stats", us.getStats());
        //mod.addAttribute("message", new Message());
        return "m2";
    }
    
    @RequestMapping(path = "/messaging/new", method = RequestMethod.POST)
    public String newMsg(@Valid @ModelAttribute("message") Message ms, BindingResult bs){
        if(bs.hasErrors()){
            return "m2";
        }
        //us.setUserName(ms.getName());
        //msg.addNew(ms.getName(), ms.getText());
        msg.addNew(us.getUserName(), ms.getText());
        return "redirect:http://localhost:8080/messaging";
    }
    
    @RequestMapping(value="/messaging/search",method=RequestMethod.GET)
    public String searchForm(Model mod){
        mod.addAttribute("message", new Carrier());
        return "m3";
    }
    
    @RequestMapping(path = "/messaging/search", method = RequestMethod.POST)
    public String searchRes(@ModelAttribute("message") Carrier cr){
        return "redirect:http://localhost:8080/messaging"+msg.searchUrl(cr);
    }
    
    
    
    
    
    

}
