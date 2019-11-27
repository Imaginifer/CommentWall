/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Topic;
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
        //return fullTopics();
    }
    
   @Override
    public Topic newTopic(String author, String title){
        Topic t = new Topic(author, title);
        em.persist(t);
        return t;
    }
}
