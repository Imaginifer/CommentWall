/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Payment implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;
    private String transactionId;
    private double total;
    private int jetons;
    @ManyToOne (optional = false)
    private Commenter customer;
    private LocalDateTime creation;
    @ManyToOne (optional = false)
    private Address address;
    private boolean resolved;

    public Payment(String transactionId, double total, int jetons, Commenter customer, Address address) {
        this.transactionId = transactionId;
        this.total = total;
        this.jetons = jetons;
        this.customer = customer;
        this.address = address;
        creation = LocalDateTime.now();
        resolved = false;
    }

    public Payment() {
    }

    public long getPaymentId() {
        return paymentId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getTotal() {
        return total;
    }

    public int getJetons() {
        return jetons;
    }

    public Commenter getCustomer() {
        return customer;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isResolved() {
        return resolved;
    }
    
    public void resolve(){
        resolved = true;
    }
}
