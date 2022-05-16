/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.*;
import com.imaginifer.mess.enums.TopicStatus;
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
    public void removeTopic(long id) {
        Topic t = em.find(Topic.class, id);
        em.remove(t);
    }
    
    @Override
    public List<Topic> getAllTopicsWithMessages(){
        EntityGraph eg = em.getEntityGraph("loadWithMessages");
        return em.createQuery("select t from Topic t")
                .setHint(QueryHints.HINT_LOADGRAPH, eg).getResultList();
        //return fullTopics();
    }
    
   @Override
    public Topic newTopic(Topic t){
        em.persist(t);
        return t;
    }

    @Override
    public List<Object[]> displayTopics(long forumId) {
        List<Object[]> resultList = em.createQuery("select m.topic, count(m) from "
                + "Message m where m.topic.forum.forumId = :i and m.topic.topicStatus != :f group by m.topic "
                + "order by m.topic.lastUpdate desc").setParameter("i", forumId)
                .setParameter("f", TopicStatus.ARCHIVED).getResultList();
        return resultList;
    }

    @Override
    public List<Forum> getAllForums() {
        return em.createQuery("select f from Forum f order by f.lastUpdate desc").getResultList();
    }

    @Override
    public void newForum(Forum f) {
        em.persist(f);
    }

    @Override
    public Forum getForumById(long forumId) {
        return em.find(Forum.class, forumId);
    }
    
    
}
