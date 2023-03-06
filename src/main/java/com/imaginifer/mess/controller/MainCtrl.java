/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.controller;

import com.imaginifer.mess.service.MsgServiceImpl;
import com.imaginifer.mess.dto.*;
import com.imaginifer.mess.service.ControllerSupport;
import com.imaginifer.mess.service.WebUtilService;
import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author imaginifer
 */
@Controller
public class MainCtrl {

    private final MsgServiceImpl msg;
    private final WebUtilService wu;
    
    @Autowired
    public MainCtrl(MsgServiceImpl msg, WebUtilService wu) {
        this.msg = msg;
        this.wu = wu;
    }

    @RequestMapping(value = "/messaging/res", method = RequestMethod.GET)
    public String displayMsg(@RequestParam(name = "ord", defaultValue = "0") int order,
             @RequestParam(name = "ct", defaultValue = "0") int count,
             @RequestParam(name = "opt", defaultValue = "0") int txtOption,
             @RequestParam(name = "nm", defaultValue = "") String name,
             @RequestParam(name = "tx", defaultValue = "") String text,
             @RequestParam(name = "ttl", defaultValue = "") String title,
             @RequestParam(name = "top", defaultValue = "0") long topic,
             @RequestParam(name = "only", defaultValue = "") String only,
             Model mod) {

        mod.addAttribute("messages", msg.getMsg(order, count, txtOption, name, text, title, 
                topic, only));
        mod.addAttribute("cim", new PageView("Találatok"));
        return "front.html";
    }

    @RequestMapping(value = "/messaging/msg/{msgId}", method = RequestMethod.GET)
    public String displayOne(@PathVariable("msgId") long msgId, Model mod) {
        List<MessageView> m = msg.pickMsg(msgId, true);
        mod.addAttribute("messages", m);
        mod.addAttribute("return", new TopicView(m.get(0).getTopicId(), 
                m.get(0).getTopic()));
        /*long topicId=msg.getTopicId(msgId);
        mod.addAttribute("return", new TopicView(topicId, null, msg.getTopicName(topicId), 0, null));*/
        mod.addAttribute("cim", new PageView(m.get(0).getTopic()));
        return "front.html";
    }

    @RequestMapping(value = "/messaging/new", method = RequestMethod.GET)
    public String newMsgForm(@RequestParam(name = "reply", defaultValue="0") long reply,
            @RequestParam(name = "topic", defaultValue = "0") long topic, Model mod) {
        MessageData ms = new MessageData();
        //ms.setTopics(msg.allTopicTitles());
        if(reply != 0){
            ms.setNewTopic("@ " + reply);
            ms.setReplied(reply);
        }
        ms.setChosenTopic(topic);
        mod.addAttribute("message", ms);
        mod.addAttribute("cim", new PageView("Üzenetírás"));
        return "writenew.html";
    }

    @RequestMapping(path = "/messaging/new", method = RequestMethod.POST)
    public String newMsg(@Valid @ModelAttribute("message") MessageData ms, BindingResult bs) {
        if (bs.hasErrors()) {
            return "writenew.html";
        }
        long end=0;
        if(ms.isResponse()){
            List<MessageView> x = msg.pickMsg(ms.getReplied(), false);
            if(!x.isEmpty()){
                msg.newReply(ms.getText(), x.get(0).getMsgId());
                end = msg.getTopicId(x.get(0).getMsgId());
            }
        } else {
            end = msg.addNew(ms);
        }
        return "redirect:http://localhost:8080/messaging/thr/"+end;
    }

    @RequestMapping(value = "/messaging/search", method = RequestMethod.GET)
    public String searchForm(Model mod) {
        
        mod.addAttribute("message", new Carrier());
        mod.addAttribute("topics", msg.allTopicTitlesWithIds());
        mod.addAttribute("limits", ControllerSupport.listOfSearchLimits());
        mod.addAttribute("orders", ControllerSupport.listOfOrderings());
        mod.addAttribute("filters", ControllerSupport.listOfStatus());
        mod.addAttribute("cim", new PageView("Keresés"));
        return "search.html";
    }

    @RequestMapping(path = "/messaging/search", method = RequestMethod.POST)
    public String searchRes(@ModelAttribute("message") Carrier cr) {
        return "redirect:http://localhost:8080/messaging/res" + ControllerSupport.searchUrl(cr);
    }

    @RequestMapping(value = "/messaging/problem", method = RequestMethod.GET)
    public String problem(@RequestParam(name = "err", defaultValue = "0") String err, Model mod) {
        mod.addAttribute("reply", ControllerSupport.hiba(err));
        mod.addAttribute("cim", new PageView("Hiba!"));
        mod.addAttribute("alcim", "Gond van");
        return "msg.html";
    }
    
    @GetMapping("/messaging")
    public String displayAllTopics(Model mod){
        mod.addAttribute("topics", msg.displayTopics(1));
        mod.addAttribute("cim", new PageView("Főoldal"));
        return "front.html";
    }
    
    @GetMapping("/messaging/thr/{topicId}")
    public String displayTopic(@PathVariable("topicId") long topicId, Model mod){
        List<MessageView> m = msg.getTopic(topicId);
        mod.addAttribute("messages", m);
        mod.addAttribute("topicIdent", topicId);
        mod.addAttribute("cim", new PageView(m.get(0).getTopic()));
        return "front.html";
    }
    
    @GetMapping("/messaging/ms")
    public String systemReply(@RequestParam(name = "n", defaultValue = "0") String n, Model mod){
        mod.addAttribute("reply", ControllerSupport.responseMsg(n));
        mod.addAttribute("cim", new PageView("Üzenet"));
        mod.addAttribute("alcim", "Üzenet");
        return "msg.html";
    }
    
    @RequestMapping("/messaging/imagesavetest")
    public String imageUploadTest(MultipartFile img){
        return msg.imageUploadTest(img);
    }
    

}
