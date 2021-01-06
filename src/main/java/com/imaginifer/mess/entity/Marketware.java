/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import com.imaginifer.mess.enums.TopicAccess;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Marketware implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long marketwareId;
    private String name;
    private String text;
    @Enumerated(EnumType.STRING)
    private TopicAccess restriction;
    private int price;

    public Marketware(String name, String text, TopicAccess restriction, int price) {
        this.name = name;
        this.text = text;
        this.restriction = restriction;
        this.price = price;
    }

    public Marketware() {
    }

    public long getMarketwareId() {
        return marketwareId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TopicAccess getRestriction() {
        return restriction;
    }

    public void setRestriction(TopicAccess restriction) {
        this.restriction = restriction;
    }
    
    
}
