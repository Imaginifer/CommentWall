/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public class SearchRepository {
    
    @PersistenceContext
    EntityManager em;

    public List<Message> filterMessages(boolean descend, int count, String name, 
            String text, String title, long topic, int deleted) {
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> ms = cq.from(Message.class);
        
        List<Predicate> pred = new ArrayList<>();
        
        if (!name.isEmpty()) {
            Predicate p = cb.like(ms.get(Message_.commenter).get(Commenter_.username), "%" + name + "%");
            pred.add(p);
        }
        if (!text.isEmpty()) {
            Predicate p = cb.like(ms.get(Message_.text), "%" + text + "%");
            pred.add(p);
        }
        if (topic != 0) {
            Predicate p = cb.equal(ms.get(Message_.topic).get(Topic_.topicId), topic);
            pred.add(p);
        }
        if (!title.isEmpty()) {
            Predicate p = cb.like(ms.get(Message_.topic).get(Topic_.title), "%" + title + "%");
            pred.add(p);
        }
        if(deleted != 1){
            pred.add(cb.equal(ms.get(Message_.deleted), deleted == 2));
        }
        
        cq.select(ms).distinct(true).where(pred.toArray(new Predicate[pred.size()]));
        
        if(descend){
            cq.orderBy(cb.desc(ms.get(Message_.msgId)));
        } else {
            cq.orderBy(cb.asc(ms.get(Message_.msgId)));
        }
        
        return em.createQuery(cq).setMaxResults(count).getResultList();
    }

    public List<Topic> filterTopics (int order, int count, String userName, 
            String msgText, String topicTitle, String topicText, long forum, int deleted){ 
    //rendezés frissítés, ident, cím szerint, keresés felh.név, üzenet szöveg, cím szerint
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Topic> cq = cb.createQuery(Topic.class);
        Root<Topic> tp = cq.from(Topic.class);
        ListJoin<Topic, Message> ms = tp.join(Topic_.messages);
        
        List<Predicate> pr = new ArrayList<>();
        
        if(!userName.isEmpty()){
            pr.add(cb.like(ms.get(Message_.commenter).get(Commenter_.username), "%" + userName + "%"));
        }
        
        if(!msgText.isEmpty()){
            Subquery<Message> sub = cq.subquery(Message.class);
            Root<Message> subMsg = sub.from(Message.class);
            if(deleted!=1){
                pr.add(cb.exists(sub.select(subMsg).where(cb.like(subMsg.get(Message_.text), 
                       "%" + msgText + "%"),
                        cb.and(cb.equal(tp.get(Topic_.topicId), subMsg.get(Message_.topic))),
                        cb.and(cb.equal(subMsg.get(Message_.deleted), deleted == 2)))));
            } else {
                pr.add(cb.exists(sub.select(subMsg).where(cb.like(subMsg.get(Message_.text),
                        "%" + msgText + "%"),
                        cb.and(cb.equal(tp.get(Topic_.topicId), subMsg.get(Message_.topic))))));
            }
            
        }
        if(!topicTitle.isEmpty()){
           pr.add(cb.like(tp.get(Topic_.title), "%" + topicTitle + "%"));
        }    
        if(!topicText.isEmpty()){
           pr.add(cb.like(tp.get(Topic_.text), "%" + topicText + "%"));
        }
        if(forum != 0){
           pr.add(cb.equal(tp.get(Topic_.forum).get(Forum_.forumId), forum));
        }
        
        cq.select(tp).distinct(true).where(pr.toArray(new Predicate[pr.size()]));
        
        switch(order){
            case 1:
                cq.orderBy(cb.desc(tp.get(Topic_.lastUpdate)));
                break;
            case 2:
                cq.orderBy(cb.asc(tp.get(Topic_.lastUpdate)));
                break;
            case 3:
                cq.orderBy(cb.desc(tp.get(Topic_.title)));
                break;
            case 4:
                cq.orderBy(cb.asc(tp.get(Topic_.title)));
                break;
            case 5:
                cq.orderBy(cb.asc(tp.get(Topic_.topicId)));
                break;
            default:
                cq.orderBy(cb.desc(tp.get(Topic_.topicId)));
        }        
        return em.createQuery(cq).setMaxResults(count).getResultList(); 
    }
    //TODO
    public List<Commenter> filterCommenters(int order, int count, String name, 
            String msgText, String topicTitle, long forum, int sanctioned){ 
    //rendezés ident, név szerint, keresés üzenet, topik, fórum ill. szankciók szerint
        return null;
    }
    
}
