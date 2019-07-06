/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.service;

import com.progmatic.msg.entity.*;
import com.progmatic.msg.dto.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author imaginifer
 */
@Component
public class UserBase implements UserDetailsService{
    
    @PersistenceContext
    EntityManager em;
    
    private PasswordEncoder pwd;
    
    @Autowired
    public UserBase(PasswordEncoder pwd) {
        this.pwd = pwd;
    }
    
    @Transactional
    private Permit getPermit(String name){
        /*if(em.createQuery("select p from Permit p").getResultList().isEmpty()){
            em.persist(new Permit("ROLE_ADMIN"));
            em.persist(new Permit("ROLE_USER"));
        }*/
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
    
    @Transactional
    public void registerNew(RegData reg){
        em.persist(new Commenter(reg.getName(), pwd.encode(reg.getPwd()), reg.getMail()
                , reg.getBlood(), reg.getMother(), reg.getShoe()
                , LocalDateTime.now(),getPermit("ROLE_USER")));
        
    }
    
    
    public boolean noAdmin(){
        
        return em.createQuery("select c from Commenter c join fetch "
                + "c.authorities where c.authorities.authority = :a")
                .setParameter("a", "ROLE_ADMIN")
                .getResultList().isEmpty();
    }
    
    @Transactional
    public void addAdmin(boolean must){
        if(must){
            em.persist(new Commenter("admin",pwd.encode("pa ss wo rd 12 34")," "," "," "," "
                    ,LocalDateTime.of(1980, 1, 1, 9, 15),getPermit("ROLE_ADMIN")));
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public List<Commenter> listCommenters(){
        return em.createQuery("select c from Commenter c where c.username != :n")
                .setParameter("n", "admin").getResultList();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void promoteOrDemote(String id){
        int q=0;
        try {
            q=Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return;
        }
        Commenter c=em.find(Commenter.class, q);
        if(c.isAdmin()){
            c.removeAuthority(getPermit("ROLE_ADMIN"));
        }else{
            c.grantAuthority(getPermit("ROLE_ADMIN"));
        }
        
    }
    
}
