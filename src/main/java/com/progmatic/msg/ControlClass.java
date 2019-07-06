/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

import com.progmatic.msg.dto.*;
import com.progmatic.msg.service.*;
import java.util.*;
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

    @Autowired
    public ControlClass(MsgServiceImpl msg, UserStats us) {
        this.msg = msg;
        this.us = us;

    }

    @RequestMapping(value = "/messaging", method = RequestMethod.GET)
    public String displayMsg(@RequestParam(name = "ord", defaultValue = "0") String order,
             @RequestParam(name = "ct", defaultValue = "full") String count,
             @RequestParam(name = "nm", defaultValue = "") String name,
             @RequestParam(name = "tx", defaultValue = "") String text,
             @RequestParam(name = "top", defaultValue = "") String topic,
             @RequestParam(name = "only", defaultValue = "") String only,
             Model mod) {

        mod.addAttribute("messages", msg.getMsg(order, count, name, text, topic,
                 us.isAdmin(), only));
        return "m1";
    }

    @RequestMapping(value = "/messaging/{msgId}", method = RequestMethod.GET)
    public String displayOne(@PathVariable("msgId") String msgId, Model mod) {
        mod.addAttribute("messages", msg.pickMsg(msgId));
        return "m1";
    }

    @RequestMapping(value = "/messaging/new", method = RequestMethod.GET)
    public String newMsgForm(Model mod) {
        MessageData ms = new MessageData();
        //User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //ms.setName(u.getUsername());
        ms.setName(us.getCurrentUsername());
        ms.setTopics(msg.allTopicTitles());
        mod.addAttribute("message", ms);
        //mod.addAttribute("stats", us.getStats());
        //mod.addAttribute("message", new Message()); //web1.0
        return "m2";
    }

    @RequestMapping(path = "/messaging/new", method = RequestMethod.POST)
    public String newMsg(@Valid @ModelAttribute("message") MessageData ms, BindingResult bs) {
        if (bs.hasErrors()) {
            return "m2";
        }
        //us.setUserName(ms.getName());
        //msg.addNew(ms.getName(), ms.getText());
        //User u = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //msg.addNew(u.getUsername(), ms.getText());  
        msg.addNew(us.getCurrentUsername(), ms.getText(), ms.getChosenTopic(), ms.getNewTopic());
        return "redirect:http://localhost:8080/messaging";
    }

    @RequestMapping(value = "/messaging/search", method = RequestMethod.GET)
    public String searchForm(Model mod) {
        Carrier c = new Carrier();
        List<String> tp = new ArrayList<>(Arrays.asList("Bármely topik"));
        tp.addAll(msg.allTopicTitles());
        c.setTopics(tp);
        mod.addAttribute("message", c);
        return "m3";
    }

    @RequestMapping(path = "/messaging/search", method = RequestMethod.POST)
    public String searchRes(@ModelAttribute("message") Carrier cr) {
        return "redirect:http://localhost:8080/messaging" + msg.searchUrl(cr);
    }

    @RequestMapping(value = "/messaging/problem", method = RequestMethod.GET)
    public String problem(@RequestParam(name = "err", defaultValue = "0") String err, Model mod) {
        mod.addAttribute("errortext", msg.hiba(err));
        return "hiba";
    }

    @RequestMapping(value = "/messaging/delete/{msgId}", method = RequestMethod.GET)
    public String hideMessage(@PathVariable("msgId") String msgId, Model mod) {
        msg.deleteMsg(msgId, false);
        return "redirect:http://localhost:8080/messaging";
    }

    @RequestMapping(value = "/messaging/restore/{msgId}", method = RequestMethod.GET)
    public String restoreMessage(@PathVariable("msgId") String msgId, Model mod) {
        msg.deleteMsg(msgId, true);
        return "redirect:http://localhost:8080/messaging";
    }

    @RequestMapping(value = "/messaging/deltop/", method = RequestMethod.GET)
    public String removeTopic(@RequestParam(name = "top") String top) {
        msg.removeTopic(top);
        return "redirect:http://localhost:8080/messaging";
    }
    
    @GetMapping("/messaging/topics")
    public String displayAllTopics(Model mod){
        mod.addAttribute("topics", msg.displayTopics());
        return "m6";
    }
    

}
