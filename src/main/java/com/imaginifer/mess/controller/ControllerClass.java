/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import com.imaginifer.mess.service.MsgServiceImpl;
import com.imaginifer.mess.dto.Carrier;
import com.imaginifer.mess.dto.MessageView;
import com.imaginifer.mess.dto.MessageData;
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
public class ControllerClass {

    private final MsgServiceImpl msg;
    
    @Autowired
    public ControllerClass(MsgServiceImpl msg) {
        this.msg = msg;
    
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
                 msg.isAdmin(), only));
        return "front.html";
    }

    @RequestMapping(value = "/messaging/{msgId}", method = RequestMethod.GET)
    public String displayOne(@PathVariable("msgId") String msgId, Model mod) {
        mod.addAttribute("messages", msg.pickMsg(msgId, msg.isAdmin(), true));
        return "front.html";
    }

    @RequestMapping(value = "/messaging/new", method = RequestMethod.GET)
    public String newMsgForm(@RequestParam(name = "reply", defaultValue="") String reply , Model mod) {
        MessageData ms = new MessageData();
        ms.setTopics(msg.allTopicTitles());
        if(!reply.isEmpty()){
            ms.setNewTopic("@ " + reply);
            ms.setReplied(reply);
        }
        mod.addAttribute("message", ms);
        return "writenew.html";
    }

    @RequestMapping(path = "/messaging/new", method = RequestMethod.POST)
    public String newMsg(@Valid @ModelAttribute("message") MessageData ms, BindingResult bs) {
        if (bs.hasErrors()) {
            return "writenew.html";
        }
        System.out.println("válasz erre: "+ms.getReplied());
        if(ms.isResponse()){
            List<MessageView> x = msg.pickMsg(ms.getReplied(), msg.isAdmin(), false);
            if(!x.isEmpty()){
                msg.newReply(ms.getText(), x.get(0).getMsgId());
            }
        } else {
            System.out.println(ms.getText());
            msg.addNew(ms.getText(), ms.getChosenTopic(), ms.getNewTopic());
        }
        return "redirect:http://localhost:8080/messaging";
    }

    @RequestMapping(value = "/messaging/search", method = RequestMethod.GET)
    public String searchForm(Model mod) {
        Carrier c = new Carrier();
        List<String> tp = new ArrayList<>(Arrays.asList("Bármely topik"));
        tp.addAll(msg.allTopicTitles());
        c.setTopics(tp);
        mod.addAttribute("message", c);
        return "search.html";
    }

    @RequestMapping(path = "/messaging/search", method = RequestMethod.POST)
    public String searchRes(@ModelAttribute("message") Carrier cr) {
        return "redirect:http://localhost:8080/messaging" + msg.searchUrl(cr);
    }

    @RequestMapping(value = "/messaging/problem", method = RequestMethod.GET)
    public String problem(@RequestParam(name = "err", defaultValue = "0") String err, Model mod) {
        mod.addAttribute("errortext", msg.hiba(err));
        return "hiba.html";
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
        return "topics.html";
    }
    
    @GetMapping("/messaging/ms")
    public String systemReply(@RequestParam(name = "n", defaultValue = "0") String n, Model mod){
        mod.addAttribute("reply", msg.responseMsg(n));
        return "msg.html";
    }
    
    

}
