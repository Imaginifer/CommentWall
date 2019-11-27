/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Topic;
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
