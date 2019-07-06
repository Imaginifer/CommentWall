/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.repo;

import com.progmatic.msg.entity.Topic;
import java.util.List;
import javax.persistence.*;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.*;


/**
 *
 * @author imaginifer
 */
@Repository
public class CustomTopicRepoImpl implements CustomTopicRepo{
    
    @PersistenceContext
    EntityManager em;
        
    @Override
    public void removeTopic(String top) {
        Topic t = (Topic) em.createQuery("select tp from Topic tp where tp.title= :ttl")
                .setParameter("ttl", top).getSingleResult();
        em.remove(t);
    }
    
    @Override
    public List<Topic> displayTopics(){
        EntityGraph eg = em.getEntityGraph("loadWithMessages");
        return em.createQuery("select t from Topic t")
                .setHint(QueryHints.HINT_LOADGRAPH, eg).getResultList();
    }
    
   @Override
    public Topic newTopic(String author, String title){
        Topic t = new Topic(author, title);
        em.persist(t);
        return t;
    }
}
