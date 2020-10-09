/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import com.imaginifer.mess.dto.Carrier;
import com.imaginifer.mess.dto.PageView;
import com.imaginifer.mess.service.MsgServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author imaginifer
 */
@Controller
public class AdministrationCtrl {
    
    private final MsgServiceImpl msg;
    
    @Autowired
    public AdministrationCtrl (MsgServiceImpl msg){
        this.msg = msg;
    }
    
    @RequestMapping(value = "/messaging/delete/{msgId}", method = RequestMethod.GET)
    public String hideMessage(@PathVariable("msgId") long msgId) {
        msg.deleteMsg(msgId, false);
        return "redirect:http://localhost:8080/messaging/thr/"+msg.getTopicId(msgId);
    }

    @RequestMapping(value = "/messaging/restore/{msgId}", method = RequestMethod.GET)
    public String restoreMessage(@PathVariable("msgId") long msgId) {
        msg.deleteMsg(msgId, true);
        return "redirect:http://localhost:8080/messaging/thr/"+msg.getTopicId(msgId);
    }

    @RequestMapping(value = "/messaging/deltop/{top}", method = RequestMethod.GET)
    public String removeTopic(@PathVariable("top") long top) {
        msg.removeTopic(top);
        return "redirect:http://localhost:8080/messaging";
    }
    
    @RequestMapping(value = "/messaging/mute/{msgId}", method = RequestMethod.GET)
    public String hideCommenter(@PathVariable("msgId") long msgId){
        //TODO
        return "redirect:http://localhost:8080/messaging/thr/"+msg.getTopicId(msgId);
    }
    
    @RequestMapping(value = "/messaging/unmute", method = RequestMethod.POST)
    public String unmuteCommenter(@ModelAttribute("message") Carrier cr){
        //TODO
        return "";
    }
    
    @RequestMapping(value = "/messaging/account", method = RequestMethod.GET)
    public String getProfile(Model mod){
        //TODO
        
        mod.addAttribute("cim", new PageView("Fiók"));
        return "";
    }
    
    @RequestMapping(value = "/messaging/reports", method = RequestMethod.GET)
    public String getComplaints(Model mod){
        //TODO
        
        mod.addAttribute("cim", new PageView("Feljelentések"));
        return "";
    }
    
    @RequestMapping(value = "/messaging/report/{msgId}", method = RequestMethod.GET)
    public String reportMessage(@PathVariable("msgId") long msgId, Model mod){
        //TODO
        return "";
    }
    
    @RequestMapping(value = "/messaging/report", method = RequestMethod.POST)
    public String sendReport(@ModelAttribute("message") Carrier cr){
        //TODO
        return "";
    }
    
    @RequestMapping(value = "/messaging/sanction/{msgId}", method = RequestMethod.GET)
    public String sanctionPage(@PathVariable("msgId") long msdId, Model mod){
        //TODO
        return "";
    }
    
    @RequestMapping(value = "/messaging/sanction", method = RequestMethod.POST)
    public String sendSanction(@ModelAttribute("message") Carrier cr){
        //TODO
        return "";
    }
}
