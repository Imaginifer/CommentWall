/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.repo.TopicRepository;
import com.imaginifer.mess.repo.CustomMsgRepoImpl;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.dto.Carrier;
import com.imaginifer.mess.dto.TopicView;
import com.imaginifer.mess.dto.MessageView;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author imaginifer
 */
@Service
public class MsgServiceImpl {
    
    private CustomMsgRepoImpl msgrepo;
    private TopicRepository topicrepo;

    
    @Autowired
    public MsgServiceImpl(TopicRepository topicrepo, CustomMsgRepoImpl msgrepo) {
        
        this.topicrepo = topicrepo;
        this.msgrepo = msgrepo;
        
    }

    @Transactional
    public void addNew(String text, String topicTitle, String newTopic) {
        String name = getCurrentUsername();
        msgrepo.addNew(new Message(name, text, LocalDateTime.now(),
                topicHandling(name, topicTitle, newTopic)));
        
    }

    @Transactional
    private Topic topicHandling(String name, String topicTitle, String newTitle) {
        if (!newTitle.isEmpty() && allTopicTitles().contains(newTitle)) {
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
    public List<MessageView> getMsg(String order, String count, String name,
             String text, String topic, boolean allowed, String only) {
        
        int ord = 0;
        try {
            ord = Integer.parseInt(order);
        } catch (NumberFormatException e) {
        }
        return ConvertDTO.convertMessage(countMsg(count, filterDeleted(allowed, only,
                msgrepo.filterMessages(ord, name, text, topic))));

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

    public List<MessageView> pickMsg(String id, boolean allowed, boolean all) {
        List<Message> y = new ArrayList<>();
        int q = 0;
        
        try{
            q = Integer.parseInt(id);
            y = msgrepo.pickWithReplies(q);
        }catch(NumberFormatException | NoSuchElementException ex){
           throw new EntityNotFoundException("Message with "+id+" not found!");
        }
        
        if(all){
            y.addAll(y.get(0).getReplies());
        }
        return ConvertDTO.convertMessage(filterDeleted(allowed, allowed?"no":"", y));
    }
    
    /*@Transactional
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
    }*/
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

    /*public List<Message> findMsg(String name, String text, List<Message> in) {
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
    }*/

    public String hiba(String err) {
        String[] st = {"Ismeretlen hiba",
            "A megadott felhasználónév már foglalt, kérjük válasszon másikat!",
            "A két megadott jelszó nem egyezik!",
            "A hitelesítő hivatkozás elévült."};
        int q = 0;
        try {
            q = Integer.parseInt(err);
        } catch (NumberFormatException e) {
            return st[q];
        }
        int k = q < 0 || q > st.length-1 ? 0 : q;
        //int k = Math.max(0, Math.min(st.length - 1, q));
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

        try{
            msgrepo.hideOrRestore(q, restore);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Message with "+q+" not found!");
        }
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
        if (allowed && only.equals("yes")){
            return x.stream().filter(m -> m.isDeleted()).collect(Collectors.toList());
        }
        
        
        return x.stream().filter(m -> !m.isDeleted()).collect(Collectors.toList());
    }

    public String responseMsg(String num){
        String[] s = {"Ez itt a visszajelző oldal.",
                "A megadott címére hitelesítő üzenetet fog kapni, kattintson az "
                + "ebben található hivatkozásra, és beléphet új fiókjával!",
                "Felhasználói fiókjának hitelesítése sikeres volt, már beléphet jelszavával!"
        };
        int q = 0;
        try {
            q = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return s[q];
        }
        int k = q < 0 || q > s.length-1 ? 0 : q;
        return s[k];
    }
    
    public List<TopicView> displayTopics(){
        return ConvertDTO.convertTopic(topicrepo.displayTopics());
    }
    
    @Transactional
    public void newReply(String text, int replied){
        Message m = msgrepo.getMessageById(replied);
        msgrepo.addNewReply(getCurrentUsername(), text, m.getTopic(), m);
    }
    
    
    
    public boolean isAdmin(){
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            for (GrantedAuthority auth : SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities()) {
                if(auth.getAuthority().equals("ROLE_ADMIN")){
                    return true;
                }
            }
        }
        return false;
    }

    public String getCurrentUsername(){
        Commenter c = (Commenter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return c.getUsername();
        
    }
}
