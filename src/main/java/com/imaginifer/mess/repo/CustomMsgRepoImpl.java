/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.entity.Message_;
import com.imaginifer.mess.entity.MsgCounter;
import com.imaginifer.mess.entity.Topic_;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public class CustomMsgRepoImpl {
    
    @PersistenceContext
    EntityManager em;
    
    public void addNew(Message m){
        //System.out.println(text + "repo");
        em.persist(m);
    }
    
    public boolean msgListTooShort(){
        return em.createQuery("select ms from Message ms").getResultList().isEmpty();
    }
    public long countNrOfMessages(){
        return (long) em.createQuery("select count(ms) from Message ms").getSingleResult();
    }
    
    
    public List<Message> getMessages(){
        return em.createQuery("select ms from Message ms").getResultList();
    }
    
    public void hideOrRestore(long q, boolean restore){
        em.find(Message.class, q).setDeleted(!restore);
    }
    
    /*public void addNewReply(String name, String text, Topic topic, Message original){
        em.persist(new Message(name, text, LocalDateTime.now(), topic, original));
    }*/
    
    public List<Message> pickWithReplies(long id){
        EntityGraph eg = em.getEntityGraph("loadWithReplies");
        List<Message> x = em.createQuery("select m from Message m where m.msgId = :i order by m.msgId")
                .setParameter("i", id)
                .setHint(QueryHints.HINT_LOADGRAPH, eg).getResultList();
        return x;
    }
    
    /*public List<Message> filterDeletedWithCrit(boolean allowed, String only){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> ms = cq.from(Message.class);
        if(allowed && only.equals("yes")){
            cq.select(ms).where(cb.equal(ms.get(Message_.deleted), true));
            return em.createQuery(cq).getResultList();
        }else if (allowed && only.equals("no")){
            cq.select(ms);
            return em.createQuery(cq).getResultList();
        }else{
            cq.select(ms).where(cb.equal(ms.get(Message_.deleted), false));
            return em.createQuery(cq).getResultList();
        }
    }*/
    
    public Message getMessageById(long id){
        return em.find(Message.class, id);
    }
    
    public long countMessagesInTopic(long topic){
        /*return ((Number)em.createQuery("select count(m) from Message m where m.topic.topicId := i")
                .setParameter("i", topicId).getSingleResult()).intValue();*/
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Message> ms = cq.from(Message.class);
        cq.select(cb.count(ms)).where(cb.equal(ms.get(Message_.topic)
                .get(Topic_.topicId), topic));
        return em.createQuery(cq).getSingleResult();
    }
    
    public List<Message> displayTopic(long topicId, boolean showDeleted){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> ms = cq.from(Message.class);
        
        List<Predicate> pred = new ArrayList<>();
        pred.add(cb.equal(ms.get(Message_.topic).get(Topic_.topicId), topicId));
        if(!showDeleted){
            pred.add(cb.equal(ms.get(Message_.deleted), false));
        }
        cq.select(ms).distinct(true).where(pred.toArray(new Predicate[pred.size()]))
                .orderBy(cb.asc(ms.get(Message_.msgId)));
        return em.createQuery(cq).getResultList();
    }
    
    public void newMessageCounter(MsgCounter pc){
        em.persist(pc);
    }
    
    public List<MsgCounter> getMessageCounter(long commenterId, long forumId){
        List<MsgCounter> x = em.createQuery("select m from MsgCounter m where m.commenter.commenterId = :c and m.forum.forumId = :f")
                .setParameter("c", commenterId)
                .setParameter("f", forumId).getResultList();
        return x;
    }
    
    
    
}
