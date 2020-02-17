/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.exceptions.EntityNotFoundException;
import com.imaginifer.mess.repo.TopicRepository;
import com.imaginifer.mess.repo.CustomMsgRepoImpl;
import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
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
    public long addNew(String text, long topicId, String newTopic) {
        Commenter c = getCurrentUser();
        Topic tp = topicHandling(c.getUsername(), topicId, newTopic);
        long nr = newTopic.isEmpty()? getNewNrInTopic(tp.getTopicId()):1;
        LocalDateTime current = LocalDateTime.now();
        msgrepo.addNew(new Message(c, text, current, tp, nr));
        tp.setLastUpdate(current);
        return tp.getTopicId();
    }

    @Transactional
    private Topic topicHandling(String name, long topicId, String newTitle) {
        if (newTitle.isEmpty()) {
            return topicrepo.findTopicByTopicId(topicId);
        }
        Topic t = new Topic(name, newTitle);
        return topicrepo.newTopic(t);
    }

    public List<String> allTopicTitles() {
        List<String> x = topicrepo.findAllTopicTitle();
        return x;
    }
    
    public List<TopicView> allTopicTitlesWithIds(){
        /*TreeSet<Topic> x = new TreeSet<>(new Comparator<Topic>(){
           @Override
            public int compare(Topic o1, Topic o2) {
                if(o1.getLastUpdate().isAfter(o2.getLastUpdate()) 
                        || o1.getLastUpdate().isEqual(o2.getLastUpdate())){
                    return -1;
                }
                return 1;
            }
        });*/
        List<Object[]> x = topicrepo.displayTopics();
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView(0, null, "Bármely topik", 0));
        for (Object[] o : x) {
            Topic tp = (Topic) o[0];
            result.add(new TopicView(tp.getTopicId(), null, tp.getTitle(), 0));
        }
        return result;
    }

    @Transactional
    public List<MessageView> getMsg(int order, int count, String name,
             String text, long topic, boolean allowed, String only) {
        
        int deleted = !only.isEmpty() && isAdmin() ? (only.equals("yes") ? 2 : 1) : 0;
        return ControllerSupport.convertMessage(msgrepo.filterMessages(order, countNr(count), 
                name, text, topic, deleted));
    }
    
    private int countNr(int count){
        int ct = 50;
        switch(count){
            case 3:
                ct *= 2;
            case 2:
                ct *= 3;
            case 1:
                ct *= 3;
            default:
                break;
        }
        return ct;
    }
    
    private long getNewNrInTopic(long topicId){
        return 1+msgrepo.countMessagesInTopic(topicId);
    }
    
    public List<MessageView> pickMsg(long id, boolean all) {
        List<Message> list = new ArrayList<>();
        boolean allowed = isAdmin();
        try{
            list = msgrepo.pickWithReplies(id);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if (!allowed && list.get(0).isDeleted()){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if(all){
            list.addAll(list.get(0).getReplies());
            list.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
        }
        if(all && !allowed){
            list = list.stream().filter(m -> !m.isDeleted()).collect(Collectors.toList());
        }
        return ControllerSupport.convertMessage(list);
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

    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteMsg(long del, boolean restore) {
        try{
            msgrepo.hideOrRestore(del, restore);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Message with "+del+" not found!");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeTopic(long top) {
        topicrepo.removeTopic(top);
    }

    public List<TopicView> displayTopics(){
        /*List<Topic> topics = topicrepo.displayTopics();
        topics.sort((t1, t2) -> {
            List<Message> topic1 = t1.getMessages();
            topic1.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            List<Message> topic2 = t2.getMessages();
            topic2.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            return topic2.get(0).getDate()
                    .compareTo(topic1.get(0).getDate());});*/
        return ControllerSupport.convertTopic(topicrepo.displayTopics());
    }
    
    public String getTopicName(long topicId){
        return topicrepo.findTitleByTopicId(topicId);
    }
    
    public long getTopicId(long msgId){
        return msgrepo.getMessageById(msgId).getTopic().getTopicId();
    }
    
    @Transactional
    public void newReply(String text, long replied){
        Message m = msgrepo.getMessageById(replied);
        long nr = getNewNrInTopic(m.getTopic().getTopicId());
        msgrepo.addNew(new Message(getCurrentUser(), text, LocalDateTime.now(),
                m.getTopic(), nr, m));
    }
    
    
    public boolean isAdmin(){
        if (userHasLoggedIn()){
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
        if(userHasLoggedIn()){
            Commenter c = getCurrentUser();
            return c.getUsername();
        }
        return "null";
    }
    
    private boolean userHasLoggedIn(){
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
    
    private Commenter getCurrentUser(){
        return (Commenter)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
