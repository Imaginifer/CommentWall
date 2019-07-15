/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.entity.*;
import com.progmatic.msg.dto.*;
import com.progmatic.msg.repo.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import com.progmatic.msg.annot.*;

/**
 *
 * @author imaginifer
 */
@Service
@LogAnnot
public class MsgServiceImpl {
    //private int nrId;
    //private final List<Message> m;
    private CustomMsgRepoImpl msgrepo;
    private TopicRepository topicrepo;

    
    @Autowired
    public MsgServiceImpl(TopicRepository topicrepo, CustomMsgRepoImpl msgrepo) {
        //nrId=0;
        //m=fillerMsg();
        this.topicrepo = topicrepo;
        this.msgrepo = msgrepo;
        
    }

    @Transactional
    public void addNew(String name, String text, String topicTitle, String newTopic) {
        //m.add(new Message(name, text, LocalDateTime.now(),++nrId));
        msgrepo.addNew(name, text, topicHandling(name, topicTitle, newTopic));
        
    }

    @Transactional
    private Topic topicHandling(String name, String topicTitle, String newTitle) {
        if (allTopicTitles().contains(newTitle)) {
            topicTitle = newTitle;
            newTitle = "";
        }
        if (newTitle.isEmpty()) {
            return topicrepo.findByTitle(topicTitle);
        }
        return topicrepo.newTopic(name, newTitle);
    }

    public List<String> allTopicTitles() {
        List<String> x = topicrepo.findAllTopicTitle();
        return x;
    }

    @Transactional
    public List<Message> getMsg(String order, String count, String name,
             String text, String topic, boolean allowed, String only) {
        if (msgrepo.ifMsgListTooShort()) {
            msgrepo.fillerMsg();
        }
        List<Message> m = msgrepo.getMessages();
        return countMsg(count, orderMsg(order, findMsg(name, text,
                 filterTopic(topic, filterDeleted(allowed, only, m)))));

    }
    
    
    public List<Message> orderMsg(String order, List<Message> y) {
        int q;
        List<Message> x = new ArrayList<>();
        y.forEach((Message ms) -> x.add(ms));
        try {
            q = Integer.parseInt(order);
        } catch (NumberFormatException e) {
            q = 0;
        }
        switch (q) {
            case 1:
                x.sort((ms1, ms2) -> {
                    return ms1.getDate().compareTo(ms2.getDate());
                });
                break;
            case 2:
                x.sort((ms1, ms2) -> {
                    return ms2.getDate().compareTo(ms1.getDate());
                });
                break;
            case 3:
                x.sort((ms1, ms2) -> {
                    return ms1.getUsername().compareTo(ms2.getUsername());
                });
                break;
            case 4:
                x.sort((ms1, ms2) -> {
                    return ms2.getUsername().compareTo(ms1.getUsername());
                });
                break;
            case 6:
                x.sort((ms1, ms2) -> {
                    return ms2.getMsgId() - ms1.getMsgId();
                });
                break;
            default:
                break;
        }
        return x;
    }

    public List<Message> countMsg(String count, List<Message> ls) {
        List<Message> x = new ArrayList<>();
        int n = 0;
        if (count.equals("full") || ls.size() < 2) {
            return ls;
        } else {
            while (n < Math.max(2, ls.size() / 2)) {
                x.add(ls.get(n++));
            }
        }

        return x;
    }

    public List<Message> pickMsg(String id, boolean allowed, boolean all) {
        List<Message> y = new ArrayList<>();
        int q = 0;
        /*try {
            q = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return y;
        }
        if(!msgrepo.getMessages().stream().map(m -> m.getMsgId())
                .collect(Collectors.toList()).contains(q)){
            return y;
        }*/
        try{
            q = Integer.parseInt(id);
            y = msgrepo.pickWithReplies(q);
        }catch(NumberFormatException | NoSuchElementException ex){
           throw new EntityNotFoundException("Message with "+id+" not found!");
        }
        
        if(all){
            y.addAll(y.get(0).getReplies());
        }
        return filterDeleted(allowed, allowed?"no":"", y);
    }
    @Transactional
    public void printComments(String id){
        int q = 0;
        try {
            q = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return;
        }
        Message m = msgrepo.pickWithReplies(q).get(0);
        System.out.println("eredeti - "+m.getText());
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
        }
        Message m2 = msgrepo.pickWithReplies(q).get(0);
        System.out.println("új - "+m2.getText());
    }
    public String searchUrl(Carrier cr) {

        String o = cr.getOrd();
        if (cr.getDesc() != null) {
            try {
                int k = Integer.parseInt(o) + 1;
                o = Integer.toString(k);
            } catch (NumberFormatException e) {
            }
        }
        String ord = cr.getOrd()!=null ? "ord=" + o : "";
        String ct = cr.getCt() != null ? "&ct=h" : "";
        String del = cr.getDel() != null ? "&only=yes" : "";
        String tx = !cr.getTx().equals("") ? "&tx=" + cr.getTx() : "";
        String nm = !cr.getNm().equals("") ? "&nm=" + cr.getNm() : "";
        String tp = !cr.getTp().equals("Bármely topik") ? "&top=" + cr.getTp() : "";
        String start = (cr.getOrd()== null
                && cr.getCt() == null
                && cr.getTx().equals("")
                && cr.getNm().equals("")
                && cr.getTp().equals("Bármely topik")
                && cr.getDel() == null) ? "" : "?";
        return start + ord + ct + nm + tx + tp + del;
    }

    public List<Message> findMsg(String name, String text, List<Message> in) {
        List<Message> x = new ArrayList<>();
        Pattern f1 = Pattern.compile(name);
        Pattern f2 = Pattern.compile(text);
        in.forEach(ms -> {
            Matcher m1 = f1.matcher(ms.getUsername());
            Matcher m2 = f2.matcher(ms.getText());
            if (m1.find() && m2.find()) {
                x.add(ms);
            }
        });
        return x;
    }

    public String hiba(String err) {
        String[] st = {"Ismeretlen hiba",
             "A megadott felhasználónév már foglalt, kérjük válasszon másikat!"};
        int q = 0;
        try {
            q = Integer.parseInt(err);
        } catch (NumberFormatException e) {
        }
        int k = Math.max(0, Math.min(st.length - 1, q));
        return st[k];
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteMsg(String del, boolean restore) {
        int q = 0;
        try {
            q = Integer.parseInt(del);
        } catch (NumberFormatException e) {
            return;
        }

        if(!msgrepo.getMessages().stream().map(m -> m.getMsgId())
                .collect(Collectors.toList()).contains(q)){
            return;
        }
        
        /*for (Message ms : m) {
            if(!restore && ms.getMsgId() == q && !ms.isDeleted()){
                ms.setDeleted(true);
            }
            if(restore && ms.getMsgId() == q && ms.isDeleted()){
                ms.setDeleted(false);
            }
        }*/
        msgrepo.hideOrRestore(q, restore);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeTopic(String top) {
        topicrepo.removeTopic(top);
    }

    public List<Message> filterDeleted(boolean allowed, String only, List<Message> x) {
        if (allowed && only.equals("no")) {
            return x;
        }
        List<Message> y = new ArrayList<>();
        x.forEach(ms -> {
            if (only.equals("") && !ms.isDeleted()) {
                y.add(ms);
            } else if (allowed && only.equals("yes") && ms.isDeleted()) {
                y.add(ms);
            }

        });
        return y;
        //return msgrepo.filterDeletedWithCrit(allowed, only);
    }

    private List<Message> filterTopic(String topic, List<Message> x) {
        if (topic.equals("")) {
            return x;
        }
        List<Message> y = new ArrayList<>();
        x.forEach(ms -> {
            if (ms.getTopic().getTitle().equals(topic)) {
                y.add(ms);
            }
        });
        return y;
    }
    
    public List<Topic> displayTopics(){
        return topicrepo.displayTopics();
    }
    
    @Transactional
    public void newReply(String name, String text, Message replied){
        System.out.println(replied);
        msgrepo.addNewReply(name, text, replied.getTopic(), replied);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void editMessage(boolean allowed, String ident, String text){
        Message m = pickMsg(ident, allowed, false).get(0);
        m.editText(text);
    }
}
