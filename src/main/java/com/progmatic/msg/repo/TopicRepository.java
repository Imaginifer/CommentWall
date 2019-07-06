/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.msg.repo;

import com.progmatic.msg.entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.*;

/**
 *
 * @author imaginifer
 */
public interface TopicRepository extends JpaRepository<Topic, Integer>, CustomTopicRepo{
    Topic findByTitle(String title);
    @Query("select tp.title from Topic tp")
    List<String> findAllTopicTitle();
}
