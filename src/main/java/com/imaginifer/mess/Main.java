/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 *
 * @author imaginifer
 */
@SpringBootApplication
@ComponentScan
@EntityScan("com.imaginifer.mess.entity")
@EnableScheduling
@EnableCaching
@EnableJpaRepositories("com.imaginifer.mess.repo")
public class Main {
    
    
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}


