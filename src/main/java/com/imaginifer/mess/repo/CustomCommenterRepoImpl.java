/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Muting;
import com.imaginifer.mess.entity.Nominee;
import com.imaginifer.mess.entity.Pass;
import com.imaginifer.mess.entity.Permit;
import com.imaginifer.mess.entity.Referendum;
import com.imaginifer.mess.entity.Sanction;
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
    
    public Permit getPermitByName(String name){
        return (Permit)em.createQuery("select p from Permit p where p.authority = :a")
                .setParameter("a", name).getSingleResult();
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
    
    public void newPass(Pass pass){
        em.persist(pass);
    }
    
    public Pass findPassById(long id){
        return (Pass) em.find(Pass.class, id);
    }
    
    public void deletePass(Pass pass){
        em.remove(pass);
    }
    
    public List<Pass> getAllPasses(){
        return em.createQuery("select p from Pass p").getResultList();
    }
    
    public List<Sanction> getAllSanctions(){
        return em.createQuery("select s from Sanction s").getResultList();
    }
    
    public void deleteSanction(Sanction s){
        em.remove(s);
    }
    
    public void newSanction(Sanction s){
        em.persist(s);
    }
    
    public Sanction findSanction(long id){
        return em.find(Sanction.class, id);
    }
    
    public Nominee findNomineeById(long id){
        return em.find(Nominee.class, id);
    }
    
    public Referendum getReferendumWithVotes(long refId){
        EntityGraph eg = em.getEntityGraph("loadWithVoters");
        return (Referendum) em.createQuery("select r from Referendum r where r.referendumId = :id")
                .setParameter("id", refId).setHint(QueryHints.LOADGRAPH, eg).getSingleResult();
    }
    
    public void newReferendum(Referendum r){
        em.persist(r);
    }
    
    public Referendum getReferendum(long refId){
        return em.find(Referendum.class, refId);
    }
    
    public List<Referendum> getAllOpenReferendums(){
        return em.createQuery("select r from Referendum r where r.closed = false order by r.creationDate")
                .getResultList();
    }
    
    public void newMuting(Muting m){
        em.persist(m);
    }
    
    public void removeMuting(Muting m){
        em.remove(m);
    }
    
    public Muting findMuting(long mutingId){
        return em.find(Muting.class, mutingId);
    }
    
    public List<Muting> listCommentersMutings(long commenterId){
        return em.createQuery("select m from Muting m where m.whoMutes.commenterId = :i order by m.mutingId desc")
                .setParameter(0, commenterId).getResultList();
    }
}
