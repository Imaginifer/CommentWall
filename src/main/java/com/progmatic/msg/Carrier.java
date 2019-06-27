/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg;

/**
 *
 * @author imaginifer
 */
public class Carrier {
    private String nm, tx, ord, desc, ct;

    public Carrier() {
        nm="";
        tx="";
        ord="";
        desc="";
        ct="";
    }
    
    public String getNm() {
        return nm;
    }

    public String getTx() {
        return tx;
    }

    public String getOrd() {
        return ord;
    }

    public String getDesc() {
        return desc;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public void setOrd(String ord) {
        this.ord = ord;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }
    
    
    
    
}
