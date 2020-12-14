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
import com.imaginifer.mess.dto.*;
import com.imaginifer.mess.entity.Forum;
import com.imaginifer.mess.entity.MsgCounter;
import com.imaginifer.mess.enums.TopicStatus;
import com.imaginifer.mess.repo.SearchRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author imaginifer
 */
@Service
@Transactional(readOnly = true)
public class MsgServiceImpl {
    
    private final CustomMsgRepoImpl msgrepo;
    private final TopicRepository topicrepo;
    private final SearchRepository sr;
    private final WebUtilService wu;

    
    @Autowired
    public MsgServiceImpl(TopicRepository topicrepo, CustomMsgRepoImpl msgrepo, 
            WebUtilService wu, SearchRepository sr) {
        
        this.topicrepo = topicrepo;
        this.msgrepo = msgrepo;
        this.wu = wu;
        this.sr = sr;
    }

    @Transactional(readOnly = false)
    public long addNew(MessageData ms) {
        Commenter c = wu.getCurrentUser();
        Topic tp = topicHandling(ms.getText(), ms.getChosenTopic(), ms.getNewTopic(), 
                /*ms.getForumId()*/1, ms.isNotUpdating() && ms.getNewTopic() != null || !ms.getNewTopic().isEmpty());
        if(tp.getStatus() == TopicStatus.ARCHIVED){
            return 0;
        }
        long nr = ms.getNewTopic() == null || ms.getNewTopic().isEmpty()? getNewNrInTopic(tp.getTopicId()):1;
        LocalDateTime current = LocalDateTime.now();
        msgrepo.addNew(new Message(c, ms.getText(), current, tp, nr,wu.getRequestIdent()));
        if(tp.getStatus() != TopicStatus.LOCKED && !ms.isNotUpdating()){
            tp.setLastUpdate(current);
        }
        updateCounter(c, tp.getForum());
        return tp.getTopicId();
    }

    private Topic topicHandling(String text, long topicId, String newTitle, long forumId,
            boolean hidden) {
        Forum f = topicrepo.getForumById(forumId);
        if (newTitle == null || newTitle.isEmpty()) {
            f.newMessage();
            return topicrepo.findTopicByTopicId(topicId);
        }
        
        Topic t = new Topic(newTitle, text, f, hidden);
        f.newTopic();
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
        List<Object[]> x = topicrepo.displayTopics(1);
        List<TopicView> result = new ArrayList<>();
        result.add(new TopicView(0, "Bármely topik"));
        for (Object[] o : x) {
            Topic tp = (Topic) o[0];
            result.add(new TopicView(tp.getTopicId(), tp.getTitle()));
        }
        return result;
    }

    public List<MessageView> getMsg(int order, int count, String name,
             String text, String title, long topic, String only) {
        
        int deleted = !only.isEmpty() && wu.isDirector() ? (only.equals("yes") ? 2 : 1) : 0;
        return ControllerSupport.convertMessage(sr.filterMessages(order!=0, countNr(count), 
                name, text, title, topic, deleted));
    }
    
    public List<MessageView> getTopic(long topic){
        return ControllerSupport.convertMessage(msgrepo.displayTopic(topic, wu.isDirector()));
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
        boolean allowed = wu.isDirector();
        try{
            list = msgrepo.pickWithReplies(id);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if (list.isEmpty() || !allowed && list.get(0).isDeleted()){
            throw new EntityNotFoundException("Ilyen üzenet nem létezik!");
        }
        if(all){
            list.addAll(list.get(0).getReplies());
            //list.sort((m1, m2) -> Long.compare(m1.getMsgId(), m2.getMsgId()));
            if(!allowed){
                list = list.stream().filter(m -> !m.isDeleted())
                        .collect(Collectors.toList());
            }
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

    
    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public void deleteMsg(long del, boolean restore) {
        try{
            msgrepo.hideOrRestore(del, restore);
        }catch(NoSuchElementException ex){
            throw new EntityNotFoundException("Message with "+del+" not found!");
        }
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @Transactional(readOnly = false)
    public void removeTopic(long top) {
        topicrepo.removeTopic(top);
    }

    public List<TopicView> displayTopics(long forumId){
        /*List<Topic> topics = topicrepo.displayTopics();
        topics.sort((t1, t2) -> {
            List<Message> topic1 = t1.getMessages();
            topic1.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            List<Message> topic2 = t2.getMessages();
            topic2.sort((m1,m2) -> m2.getDate().compareTo(m1.getDate()));
            return topic2.get(0).getDate()
                    .compareTo(topic1.get(0).getDate());});*/
        return ControllerSupport.convertTopic(topicrepo.displayTopics(forumId));
    }
    
    public String getTopicName(long topicId){
        return topicrepo.findTopicByTopicId(topicId).getTitle();
    }
    
    public long getTopicId(long msgId){
        return msgrepo.getMessageById(msgId).getTopic().getTopicId();
    }
    
    private void updateCounter(Commenter c, Forum f){
        List<MsgCounter> x = msgrepo.getMessageCounter(c.getCommenterId(), f.getForumId());
        if(x.isEmpty()){
            msgrepo.newMessageCounter(new MsgCounter(c, f));
        } else {
            x.get(0).update();
        }
    }
    
    @Transactional(readOnly = false)
    public void newReply(String text, long replied){
        Message m = msgrepo.getMessageById(replied);
        long nr = getNewNrInTopic(m.getTopic().getTopicId());
        Commenter c = wu.getCurrentUser();
        msgrepo.addNew(new Message(c, text, LocalDateTime.now(),
                m.getTopic(), nr, m, wu.getRequestIdent()));
        updateCounter(c, m.getTopic().getForum());
    }
    
}
