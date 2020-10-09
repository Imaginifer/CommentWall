/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;


/**
 *
 * @author imaginifer
 */
public class Carrier {
    private String nm = "", tx = "", ttl = "", res= "msg", desc, del;
    private int ct, ord;
    private long tp;
    
    public String getNm() {
        return nm;
    }

    public String getTx() {
        return tx;
    }

    public int getOrd() {
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

    public void setOrd(int ord) {
        this.ord = ord;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }
    public String getDel(){
        return del;
    }
    public void setDel(String del){
        this.del=del;
    }

    public long getTp() {
        return tp;
    }

    public void setTp(long tp) {
        this.tp = tp;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
    
    
}
