/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import com.imaginifer.mess.service.MsgServiceImpl;
import com.imaginifer.mess.dto.*;
import com.imaginifer.mess.service.ControllerSupport;
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

    @RequestMapping(value = "/messaging/res", method = RequestMethod.GET)
    public String displayMsg(@RequestParam(name = "ord", defaultValue = "0") int order,
             @RequestParam(name = "ct", defaultValue = "0") int count,
             @RequestParam(name = "nm", defaultValue = "") String name,
             @RequestParam(name = "tx", defaultValue = "") String text,
             @RequestParam(name = "top", defaultValue = "0") long topic,
             @RequestParam(name = "only", defaultValue = "") String only,
             Model mod) {

        mod.addAttribute("messages", msg.getMsg(order, count, name, text, topic,
                 msg.isAdmin(), only));
        mod.addAttribute("search", true);
        return "front.html";
    }

    @RequestMapping(value = "/messaging/msg/{msgId}", method = RequestMethod.GET)
    public String displayOne(@PathVariable("msgId") long msgId, Model mod) {
        mod.addAttribute("messages", msg.pickMsg(msgId, true));
        long topicId=msg.getTopicId(msgId);
        mod.addAttribute("return", new TopicView(topicId, null, msg.getTopicName(topicId), 0));
        /*mod.addAttribute("returnId", topicId);
        mod.addAttribute("returnTitle", msg.getTopicName(topicId));*/
        return "front.html";
    }

    @RequestMapping(value = "/messaging/new", method = RequestMethod.GET)
    public String newMsgForm(@RequestParam(name = "reply", defaultValue="0") int reply,
            @RequestParam(name = "topic", defaultValue = "0") int topic, Model mod) {
        MessageData ms = new MessageData();
        ms.setTopics(msg.allTopicTitles());
        if(reply != 0){
            ms.setNewTopic("@ " + reply);
            ms.setReplied(reply);
        }
        mod.addAttribute("message", ms);
        mod.addAttribute("topic", topic);
        return "writenew.html";
    }

    @RequestMapping(path = "/messaging/new", method = RequestMethod.POST)
    public String newMsg(@Valid @ModelAttribute("message") MessageData ms, BindingResult bs) {
        if (bs.hasErrors()) {
            return "writenew.html";
        }
        //System.out.println("válasz erre: "+ms.getReplied());
        long end=0;
        if(ms.isResponse()){
            List<MessageView> x = msg.pickMsg(ms.getReplied(), false);
            if(!x.isEmpty()){
                msg.newReply(ms.getText(), x.get(0).getMsgId());
                end = msg.getTopicId(x.get(0).getMsgId());
                /*return "redirect:http://localhost:8080/messaging/thr/"+msg
                        .getTopicId(x.get(0).getMsgId());*/
            }
        } else {
            System.out.println(ms.getText());
            end = msg.addNew(ms.getText(), ms.getChosenTopic(), ms.getNewTopic());
        }
        return "redirect:http://localhost:8080/messaging/thr/"+end;
    }

    @RequestMapping(value = "/messaging/search", method = RequestMethod.GET)
    public String searchForm(Model mod) {
        Carrier c = new Carrier();
        /*List<String> tp = new ArrayList<>(Arrays.asList("Bármely topik"));
        tp.addAll(msg.allTopicTitles());
        c.setTopics(tp);*/
        mod.addAttribute("message", c);
        mod.addAttribute("topics", msg.allTopicTitlesWithIds());
        return "search.html";
    }

    @RequestMapping(path = "/messaging/search", method = RequestMethod.POST)
    public String searchRes(@ModelAttribute("message") Carrier cr) {
        return "redirect:http://localhost:8080/messaging/res" + ControllerSupport.searchUrl(cr);
    }

    @RequestMapping(value = "/messaging/problem", method = RequestMethod.GET)
    public String problem(@RequestParam(name = "err", defaultValue = "0") String err, Model mod) {
        mod.addAttribute("errortext", ControllerSupport.hiba(err));
        return "hiba.html";
    }

    @RequestMapping(value = "/messaging/delete/{msgId}", method = RequestMethod.GET)
    public String hideMessage(@PathVariable("msgId") long msgId, Model mod) {
        msg.deleteMsg(msgId, false);
        return "redirect:http://localhost:8080/messaging/thr/"+msg.getTopicId(msgId);
    }

    @RequestMapping(value = "/messaging/restore/{msgId}", method = RequestMethod.GET)
    public String restoreMessage(@PathVariable("msgId") long msgId, Model mod) {
        msg.deleteMsg(msgId, true);
        return "redirect:http://localhost:8080/messaging/thr/"+msg.getTopicId(msgId);
    }

    @RequestMapping(value = "/messaging/deltop/{top}", method = RequestMethod.GET)
    public String removeTopic(@PathVariable("top") long top) {
        msg.removeTopic(top);
        return "redirect:http://localhost:8080/messaging";
    }
    
    @GetMapping("/messaging")
    public String displayAllTopics(Model mod){
        mod.addAttribute("topics", msg.displayTopics());
        return "topics.html";
    }
    
    @GetMapping("/messaging/thr/{topicId}")
    public String displayTopic(@PathVariable("topicId") long topicId, Model mod){
        mod.addAttribute("messages", msg.getMsg(1, 3, "", "", topicId,
                 msg.isAdmin(), ""));
        mod.addAttribute("topicIdent", topicId);
        return "front.html";
    }
    
    @GetMapping("/messaging/ms")
    public String systemReply(@RequestParam(name = "n", defaultValue = "0") String n, Model mod){
        mod.addAttribute("reply", ControllerSupport.responseMsg(n));
        return "msg.html";
    }
    
    

}
