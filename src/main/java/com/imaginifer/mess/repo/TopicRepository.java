/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.repo;

import com.imaginifer.mess.entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.*;

/**
 *
 * @author imaginifer
 */
public interface TopicRepository extends JpaRepository<Topic, Long>, CustomTopicRepo{
    Topic findByTitle(String title);
    Topic findTopicByTopicId(long topicId);
    @Query("select tp.title from Topic tp")
    List<String> findAllTopicTitle();
}
