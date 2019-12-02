/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Permit;
//import com.imaginifer.mess.entity.Commenter_;
//import com.imaginifer.mess.entity.Permit_;
import java.util.*;
import javax.persistence.*;
//import javax.persistence.criteria.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class UserBase implements UserDetailsService{
    
    @PersistenceContext
    EntityManager em;
    
    
    
    public void addNewPermit(Permit p){
        em.persist(p);
    }
    
    public boolean noPermits(){
        return em.createQuery("select p from Permit p").getResultList().isEmpty();
    }
    
    public Permit getPermitByName(String name){
        return (Permit)em.createQuery("select p from Permit p where p.authority = :a")
                .setParameter("a", name).getSingleResult();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
    
    public Commenter findCommenterById(int id){
        return (Commenter) em.find(Commenter.class, id);
    }
    
    
    public boolean noAdmin(){
        
        return em.createQuery("select c from Commenter c join "
                + "c.authorities auth where auth.authority = :a")
                .setParameter("a", "ROLE_ADMIN")
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
        return em.createQuery("select c from Commenter c where c.username != :n")
                .setParameter("n", "admin").getResultList();
    }
    
    
    
    
}
