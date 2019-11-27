/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 *
 * @author imaginifer
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().formLogin()
                .defaultSuccessUrl("/messaging", true)
                .loginPage("/messaging/login")
                .permitAll()    
                .and().logout()         
                .logoutSuccessUrl("/messaging")
                .and().authorizeRequests()
                .antMatchers("/messaging").permitAll()  
                .antMatchers("/messaging/register").permitAll()
                .antMatchers("/messaging/search").permitAll()
                .antMatchers("/messaging/topics").permitAll()
                .antMatchers("/css/*","/js/*").permitAll() 
                .antMatchers("/messaging/delete").access("hasRole('ADMIN')") 
                .anyRequest().authenticated(); 
                
    }
    
   
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
