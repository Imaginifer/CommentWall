/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Forum;
import com.imaginifer.mess.entity.Topic;
import java.util.*;

/**
 *
 * @author imaginifer
 */
public interface CustomTopicRepo{
    void removeTopic(long id);
    List<Topic> getAllTopicsWithMessages();
    Topic newTopic(Topic t);
    List<Object[]> displayTopics(long forumId);
    List<Forum> getAllForums();
    void newForum(Forum f);
    Forum getForumById(long forumId);
}
