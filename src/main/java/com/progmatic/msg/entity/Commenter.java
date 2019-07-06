/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author imaginifer
 */
@Entity
public class Commenter implements UserDetails, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String pwd;
    private String mail;
    private String blood;
    private String mother;
    private String shoe;
    private LocalDateTime joinDate;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permit> authorities;

    public Commenter() {
    }

    public Commenter(String name, String pwd, String mail, String blood
            , String mother, String shoe, LocalDateTime date, Permit role) {
        this.username = name;
        this.pwd = pwd;
        this.mail = mail;
        this.blood = blood;
        this.mother = mother;
        this.shoe = shoe;
        this.joinDate = date;
        authorities = new HashSet<>();
        authorities.add(role);
    }

    
    public String getMail() {
        return mail;
    }

    public String getBlood() {
        return blood;
    }

    public String getMother() {
        return mother;
    }

    public String getShoe() {
        return shoe;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    
    public String getFormattedJoinDate() {
        return joinDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setShoe(String shoe) {
        this.shoe = shoe;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void grantAuthority (Permit role){
        authorities.add(role);
    }
    
    public void removeAuthority (Permit role){
        authorities.remove(role);
        
    }

    public int getId() {
        return id;
    }
    
    public boolean isAdmin(){
        for (Permit a : authorities) {
            if(a.getAuthority().equals("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }
    
    

    
    
    
}
