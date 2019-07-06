/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.dto;

import java.util.List;

/**
 *
 * @author imaginifer
 */
public class Carrier {
    private String nm, tx, ord, desc, ct, del, tp;
    private List<String> topics;

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
    public String getDel(){
        return del;
    }
    public void setDel(String del){
        this.del=del;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    
    
    
    
}
