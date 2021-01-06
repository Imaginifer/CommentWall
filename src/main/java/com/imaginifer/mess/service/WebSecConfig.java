/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *
 * @author imaginifer
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableCaching
public class WebSecConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .defaultSuccessUrl("/messaging", true)
                .loginPage("/messaging/login")
                .successHandler((HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a) -> {
                    String addr = hsr.getHeader("X-FORWARDED-FOR");
                    addr = addr == null || addr.isEmpty() ? hsr.getRemoteAddr() : addr;
                    System.out.println("Bejelentkezett: "+ a.getName() + ", cím: "+ addr);
        }).failureHandler((HttpServletRequest hsr, HttpServletResponse hsr1, AuthenticationException ae) -> {
            String addr = hsr.getHeader("X-FORWARDED-FOR");
            addr = addr == null || addr.isEmpty() ? hsr.getRemoteAddr() : addr;
            System.out.println("Bejelentkezési hiba: " + ae.getMessage() + ", cím: "+ addr);
        })
                .permitAll()    
                .and().logout()         
                .logoutSuccessUrl("/messaging")
                .logoutSuccessHandler((HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a) -> {
                    System.out.println("Kijelentkezett: "+a.getName());
        })
                .and().authorizeRequests()
                .antMatchers("/messaging","/messaging/register","/messaging/search",
                        "/messaging/res","/messaging/thr/*","/messaging/msg/*",
                        "/messaging/problem", "/messaging/valid").permitAll()  
                .antMatchers("/css/*","/js/*","/fonts/*", "/img/*").permitAll() 
                .antMatchers("/messaging/delete", "/messaging/reports","/messaging/sanction",
                        "/messaging/sanction/*").access("hasRole('DIRECTOR')") 
                .anyRequest().authenticated(); 
                
    }
    
   
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager c = new CaffeineCacheManager("msg", "reg");
        c.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(8, TimeUnit.HOURS));
        return c;
    }
    
}
