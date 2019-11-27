/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

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
    private boolean activated;
    private boolean enabled;
    private LocalDateTime resetDate;
    private LocalDateTime joinDate;
    @OneToOne(mappedBy = "comm")
    private Pass pass;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permit> authorities;

    public Commenter() {
    }

    public Commenter(String name, String pwd, String mail, LocalDateTime date, Permit role) {
            
        this.username = name;
        this.pwd = pwd;
        this.mail = mail;
        this.joinDate = date;
        this.resetDate = date;
        authorities = new HashSet<>();
        authorities.add(role);
        activated = true;
        enabled = true;
    }
    
    public Commenter(String name, String pwd, String mail, LocalDateTime date) {
            
        this.username = name;
        this.pwd = pwd;
        this.mail = mail;
        this.joinDate = date;
        this.resetDate = date;
        authorities = new HashSet<>();
        
    }

    
    public String getMail() {
        return mail;
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
        return enabled;
    }
    
    public void grantAuthority (Permit role){
        authorities.add(role);
        activated = true;
    }
    
    public void removeAuthority (Permit role){
        authorities.remove(role);
        
    }

    public int getId() {
        return id;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getResetDate() {
        return resetDate;
    }

    public void setResetDate(LocalDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public Pass getPass() {
        return pass;
    }

    public void setPass(Pass pass) {
        this.pass = pass;
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
