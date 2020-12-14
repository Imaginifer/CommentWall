/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.SanctionType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.persistence.*;
import static javax.persistence.CascadeType.REMOVE;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author imaginifer
 */
@Entity
@NamedEntityGraph(name = "loadWithComments", 
        attributeNodes = @NamedAttributeNode(value ="messages"))
@NamedEntityGraph(name = "loadWithVotes", 
        attributeNodes = @NamedAttributeNode(value ="votedIn"))
public class Commenter implements UserDetails, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commenterId;
    private String username;
    private String pwd;
    private String mail;
    private boolean activated;
    private boolean enabled;
    private LocalDateTime resetDate;
    private LocalDateTime joinDate;
    @OneToOne(mappedBy = "commenter")
    private Pass pass;
    @OneToMany(mappedBy= "commenter")
    private List<Message> messages;
    @OneToMany(mappedBy = "commenter", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Sanction> sanctions;
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Permit> authorities;
    @ManyToMany(mappedBy = "voted")
    private Set<Referendum> votedIn;
    @OneToMany(cascade = REMOVE, mappedBy = "commenter")
    private Set<MsgCounter> msgCount;

    public Commenter() {
    }

    public Commenter(String name, String pwd, String mail, LocalDateTime date, Permit role) {
            
        this.username = name;
        this.pwd = pwd;
        this.mail = mail;
        this.joinDate = date;
        this.resetDate = date;
        authorities = new HashSet<>();
        messages = new ArrayList<>();
        sanctions = new HashSet<>();
        votedIn = new HashSet<>();
        msgCount = new HashSet<>();
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
        messages = new ArrayList<>();
        sanctions = new HashSet<>();
        votedIn = new HashSet<>();
        msgCount = new HashSet<>();
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
        return !sanctions.stream().anyMatch(s -> (s.isValid() && s.getType() == SanctionType.EXILE 
                && s.getSanctionScope() == null));
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
        enabled = true;
    }
    
    public void removeAuthority (Permit role){
        authorities.remove(role);
        
    }

    public long getCommenterId() {
        return commenterId;
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
    
    public boolean isDirector(){
        for (Permit a : authorities) {
            if(a.getAuthority().equals("ROLE_DIRECTOR")){
                return true;
            }
        }
        return false;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Set<Sanction> getSanctions() {
        return sanctions;
    }

    public Set<Referendum> getVotedIn() {
        return votedIn;
    }
    
    public void castVote(Referendum r){
        votedIn.add(r);
    }

    
    
    
    
    
    
    

    
    
    
}
