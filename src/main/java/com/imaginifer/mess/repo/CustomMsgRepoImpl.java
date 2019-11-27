/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
//import com.imaginifer.mess.entity.Message_;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
//import javax.persistence.criteria.*;
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
    
    public void addNew(String name, String text, Topic topic){
        System.out.println(text + "repo");
        em.persist(new Message(name, text, LocalDateTime.now(), topic));
    }
    
    public boolean ifMsgListTooShort(){
        return em.createQuery("select ms from Message ms").getResultList().size() < 4;
    }
    
    public List<Message> fillerMsg() {
        List<Message> x = new ArrayList<>();
        Topic t = new Topic("admin", "Filler");
        em.persist(t);
        em.persist(new Message("Techno Kolos", "Lorem ipsum dolor sit amet, consectetur "
                + "adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", LocalDateTime.of(2008, 11, 27, 23, 6, 54), t));
        em.persist(new Message("Feles Elek", "Ut enim ad minim veniam, quis nostrud "
                + "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", LocalDateTime.of(2017, 3, 14, 2, 55, 7), t));
        em.persist(new Message("Citad Ella", "Duis aute irure dolor in reprehenderit "
                + "in voluptate velit esse cillum dolore eu fugiat nulla pariatur.", LocalDateTime.of(1998, 6, 20, 9, 15, 9), t));
        em.persist(new Message("Tank Aranka", "Excepteur sint occaecat cupidatat non "
                + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", LocalDateTime.of(2011, 9, 5, 3, 43, 21), t));
        return x;
    }
    
    public List<Message> getMessages(){
        return em.createQuery("select ms from Message ms").getResultList();
    }
    
    public void hideOrRestore(int q, boolean restore){
        em.find(Message.class, q).setDeleted(!restore);
    }
    
    public void addNewReply(String name, String text, Topic topic, Message original){
        em.persist(new Message(name, text, LocalDateTime.now(), topic, original));
    }
    
    public List<Message> pickWithReplies(int id){
        EntityGraph eg = em.getEntityGraph("loadWithReplies");
        List<Message> x = em.createQuery("select m from Message m where m.msgId = :i")
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
    
    public Message getMessageById(int id){
        return em.find(Message.class, id);
    }
    
    
}
