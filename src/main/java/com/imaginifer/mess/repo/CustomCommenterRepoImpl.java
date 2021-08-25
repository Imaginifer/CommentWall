/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Nominee;
import com.imaginifer.mess.entity.Pass;
import com.imaginifer.mess.entity.Permit;
import com.imaginifer.mess.entity.Referendum;
import com.imaginifer.mess.enums.UserRank;
//import com.imaginifer.mess.entity.Commenter_;
//import com.imaginifer.mess.entity.Permit_;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.QueryHints;
//import javax.persistence.criteria.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Repository;

/**
 *
 * @author imaginifer
 */
@Repository
public class CustomCommenterRepoImpl {
    
    @PersistenceContext
    private EntityManager em;
    
    
    
    public void addNewPermit(Permit p){
        em.persist(p);
    }
    
    public boolean noPermits(){
        return em.createQuery("select p from Permit p").getResultList().isEmpty();
    }
    
    public long nrOfAllCommenters(){
        return (long) em.createQuery("select count (c) from Commenter c where c.username != :a")
                .setParameter("a", "Imaginifer").getSingleResult();
    }
    
    public Permit getPermitByName(UserRank name){
        return (Permit)em.createQuery("select p from Permit p where p.authority = :a")
                .setParameter("a", name.toString()).getSingleResult();
    }

    public UserDetails loadUserByUsername(String username){
        return (Commenter)em.createQuery("select c from Commenter c where c.username = :nm")
                .setParameter("nm", username).getSingleResult();
    }
    
    public boolean nameOccupied(String username){
        return !em.createQuery("select c from Commenter c where c.username=:nm")
                .setParameter("nm", username).getResultList().isEmpty();
    }
    
    public void registerNew(Commenter c){
        em.persist(c);
    }
    
    public Commenter findCommenterById(long id){
        return (Commenter) em.find(Commenter.class, id);
    }
    
    public List<Commenter> getUnactivatedCommenters(){
        return em.createQuery("select c from Commenter c where c.activated = false")
                .getResultList();
    }
    
    public void deleteAccount(Commenter c){
        em.remove(c);
    }
    
    public boolean noDirector(){
        
        return em.createQuery("select c from Commenter c join "
                + "c.authorities auth where auth.authority = :a")
                .setParameter("a", "ROLE_DIRECTOR")
                .getResultList().isEmpty();
    }
    
    /*private boolean absentAdmin(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Commenter> cq = cb.createQuery(Commenter.class);
        Root<Commenter> com = cq.from(Commenter.class);
        SetJoin<Commenter, Permit> permits = com.join(Commenter_.authorities);
        cq.select(com).where(cb.equal(permits.get(Permit_.authority), "ROLE_ADMIN"));
        return em.createQuery(cq).getResultList().isEmpty();
    }*/
    
    
    
    public List<Commenter> listCommenters(){
        return em.createQuery("select c from Commenter c where c.username != :n "
                + "and c.activated = true")
                .setParameter("n", "Imaginifer").getResultList();
    }
    
    
}
