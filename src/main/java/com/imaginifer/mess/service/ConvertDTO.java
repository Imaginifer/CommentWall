/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.imaginifer.mess.entity.Commenter;
import com.imaginifer.mess.entity.Topic;
import com.imaginifer.mess.entity.Message;
import com.imaginifer.mess.dto.TopicView;
import com.imaginifer.mess.dto.CommenterView;
import com.imaginifer.mess.dto.MessageView;
import java.util.*;

/**
 *
 * @author imaginifer
 */
public class ConvertDTO {
    
    public static List<MessageView> convertMessage (List<Message> x){
        List<MessageView> view = new ArrayList<>();
        for (Message m : x) {
            view.add(new MessageView(m.getUsername(), m.getText(), m.getDate(),
                    m.getMsgId(), m.isDeleted(), m.getTopic().getTitle()
                    , m.isReply()?m.getReplyTo().getMsgId():0));
        }
        return view;
    }
    
    public static List<TopicView> convertTopic (List<Topic> x){
        List<TopicView> view = new ArrayList<>();
        for (Topic t : x) {
            view.add(new TopicView(t.getTopicId(), t.getAuthor(), t.getTitle()
                    , convertMessage(t.getMessages())));
        }
        return view;
    }
    
    public static List<CommenterView> convertCommenter(List<Commenter> x){
        List<CommenterView> view = new ArrayList<>();
        for (Commenter c : x) {
            view.add(new CommenterView(c.getUsername(), c.getId(), c.isAdmin()
                    , c.getFormattedJoinDate()));
        }
        return view;
    }
}
