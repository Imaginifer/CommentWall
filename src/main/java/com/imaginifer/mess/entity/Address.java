/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author imaginifer
 */
@Entity
public class Address implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;
    private String currency;
    private String address;
    private double exchangeRate;

    public Address(String currency, String address, double exchangeRate) {
        this.currency = currency;
        this.address = address;
        this.exchangeRate = exchangeRate;
    }

    public Address() {
    }

    public long getAddressId() {
        return addressId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    
    
}
