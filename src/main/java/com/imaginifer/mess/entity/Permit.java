/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.ManyToMany;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author imaginifer
 */
@Entity
public class Permit implements GrantedAuthority{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String authority;
    @ManyToMany(mappedBy="authorities")
    private List<Commenter> commenters;

    public Permit(String role) {
        this.authority = role;
    }

    public Permit() {
    }

    public List<Commenter> getCommenters() {
        return commenters;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public String getAuthority() {
        return authority;
    }
    
}
