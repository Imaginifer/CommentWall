/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.dto;

import java.util.List;

/**
 *
 * @author imaginifer
 */
public class AccountView {
    private final String address, joinDate, resetDate, lastActive, name, rank, status;
    private final List<TopicView> mutings, duties, bans, activity;
    private final long commenterId;

    public AccountView(String address, String joinDate, String resetDate, 
            String lastActive, String name, String rank, String status, 
            List<TopicView> mutings, List<TopicView> bans, List<TopicView> duties, 
            List<TopicView> activity, long commenterId) {
        this.address = address;
        this.joinDate = joinDate;
        this.resetDate = resetDate;
        this.lastActive = lastActive;
        this.name = name;
        this.rank = rank;
        this.status = status;
        this.mutings = mutings;
        this.bans = bans;
        this.duties = duties;
        this.activity = activity;
        this.commenterId = commenterId;
    }

    public String getAddress() {
        return address;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public String getResetDate() {
        return resetDate;
    }

    public String getLastActive() {
        return lastActive;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public String getStatus() {
        return status;
    }

    public List<TopicView> getMutings() {
        return mutings;
    }

    public List<TopicView> getBans() {
        return bans;
    }

    public List<TopicView> getActivity() {
        return activity;
    }

    public long getCommenterId() {
        return commenterId;
    }

    public List<TopicView> getDuties() {
        return duties;
    }
    
    
}
