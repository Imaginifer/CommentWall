/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 *
 * @author imaginifer
 */
@SpringBootApplication
@ComponentScan
@EnableScheduling
@EnableCaching
public class Main {
    
    
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}


