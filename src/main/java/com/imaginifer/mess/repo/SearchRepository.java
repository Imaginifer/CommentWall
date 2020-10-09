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
        switch (deleted) {
            case 2:
                Predicate p1 = cb.equal(ms.get(Message_.deleted), true);
                pred.add(p1);
                break;
            case 1:
                break;
            default:
                Predicate p = cb.equal(ms.get(Message_.deleted), false);
                pred.add(p);
                break;
        }
        
        cq.select(ms).distinct(true).where(pred.toArray(new Predicate[pred.size()]));
        
        if(descend){
            cq.orderBy(cb.desc(ms.get(Message_.msgId)));
        } else {
            cq.orderBy(cb.asc(ms.get(Message_.msgId)));
        }
        
        return em.createQuery(cq).setMaxResults(count).getResultList();
    }
    //TODO
    public List<Topic> filterTopics (int order, int count, String name, 
            String text, String title, long topic, int deleted){ //rendezés frissítés, ident, cím szerint
        return null;
    }
    //TODO
    public List<Commenter> filterCommenters(int order, int count, String name, 
            String text, String title, long topic, int deleted){ //rendezés ident, név szerint
        return null;
    }
    
}
