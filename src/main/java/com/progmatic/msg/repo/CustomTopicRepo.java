/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.repo;

import com.progmatic.msg.entity.Topic;
import java.util.List;

/**
 *
 * @author imaginifer
 */
public interface CustomTopicRepo{
    void removeTopic(String top);
    List<Topic> displayTopics();
    Topic newTopic(String author, String title);
}
